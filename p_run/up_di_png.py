import os
import tkinter as tk
from tkinter import filedialog
from PIL import Image
import numpy as np
import colorsys
import time
import json

MATERIAL_COLORS = {
    'diamond':   (77, 219, 216),
    'wooden':    (156, 127, 78),
    'stone':     (114, 114, 114),
    'golden':    (217, 148, 19),
    'iron':      (216, 216, 216),
    'netherite': (68, 60, 60),
}

MATERIAL_SPAN = {
    'diamond':   0.75,
    'wooden':    0.75,
    'stone':     0.35,
    'golden':    0.85,
    'iron':      0.70,
    'netherite': 0.65,
}

PREFIXES = ('diamond_', 'wooden_', 'stone_', 'golden_', 'iron_', 'netherite_')

DEFAULT_PREFIX = 'mini_'

def select_directory():
    root = tk.Tk()
    root.withdraw()
    root.attributes('-topmost', True)
    folder = filedialog.askdirectory(title='选择包含PNG的目录')
    root.destroy()
    return folder

def scan_source_files(folder):
    png_files = [f for f in os.listdir(folder) if f.lower().endswith('.png')]
    bare_files = []
    diamond_files = []
    for f in png_files:
        name = f.lower()
        if '_diamond_' in name or name.startswith('diamond_'):
            diamond_files.append(f)
        elif any(f'_{p}' in name or name.startswith(p) for p in PREFIXES):
            continue
        else:
            bare_files.append(f)
    return bare_files, diamond_files


# 特征提取
def rgb_to_hsv_deg(r, g, b):
    h, s, v = colorsys.rgb_to_hsv(r / 255.0, g / 255.0, b / 255.0)
    return h * 360.0, s, v


def classify_color(r, g, b, a):
    if a < 10:
        return 'transparent'
    h, s, v = rgb_to_hsv_deg(r, g, b)

    if s < 0.10 and v > 0.85:
        return 'highlight'
    if 160 <= h <= 235 and s > 0.10:
        return 'diamond_blue'
    if 20 <= h <= 55 and s > 0.20:
        return 'wood'
    if (h < 20 or h > 330) and s > 0.30:
        return 'red'
    if v < 0.15:
        return 'dark'
    return 'other'


def extract_features(folder, png_files):
    global_colors = {}
    file_stats = {}
    diamond_vs = []

    for f in png_files:
        path = os.path.join(folder, f)
        try:
            img = Image.open(path).convert('RGBA')
        except Exception:
            continue
        arr = np.array(img)
        total = 0
        cats = {}

        flat = arr.reshape(-1, 4)
        opaque = flat[flat[:, 3] > 10]
        if len(opaque) == 0:
            continue

        unique, counts = np.unique(opaque[:, :3], axis=0, return_counts=True)
        for i in range(len(unique)):
            r, g, b = int(unique[i][0]), int(unique[i][1]), int(unique[i][2])
            cnt = int(counts[i])
            total += cnt
            cat = classify_color(r, g, b, 255)
            cats[cat] = cats.get(cat, 0) + cnt

            key = (r, g, b)
            if key not in global_colors:
                global_colors[key] = {'count': 0, 'category': cat, 'files': set()}
            global_colors[key]['count'] += cnt
            global_colors[key]['files'].add(f)

            if cat == 'diamond_blue':
                _, _, v = rgb_to_hsv_deg(r, g, b)
                diamond_vs.extend([v] * cnt)

        file_stats[f] = {'total': total, 'categories': cats}

    v_range = (min(diamond_vs), max(diamond_vs)) if diamond_vs else (0.0, 1.0)
    return global_colors, file_stats, v_range


def print_feature_report(global_colors, file_stats, v_range):
    print('\n' + '=' * 60)
    print('特征提取报告')
    print('=' * 60)

    cat_totals = {}
    for (r, g, b), info in global_colors.items():
        cat = info['category']
        cat_totals[cat] = cat_totals.get(cat, 0) + info['count']

    grand_total = sum(cat_totals.values())
    print(f'\n总不透明像素: {grand_total}\n')
    print(f'{"类别":<16} {"像素数":>8} {"占比":>8}')
    print('-' * 40)
    for cat, cnt in sorted(cat_totals.items(), key=lambda x: -x[1]):
        print(f'{cat:<16} {cnt:>8} {cnt/grand_total*100:>7.1f}%')

    print(f'\n钻石青蓝 V 范围: {v_range[0]:.3f} ~ {v_range[1]:.3f}')

    print('\n' + '-' * 60)
    print('各类别主要颜色 (按像素数排序)')
    print('-' * 60)
    by_cat = {}
    for (r, g, b), info in global_colors.items():
        by_cat.setdefault(info['category'], []).append((r, g, b, info['count']))

    for cat in ['diamond_blue', 'highlight', 'wood', 'red', 'dark', 'other']:
        if cat not in by_cat:
            continue
        items = sorted(by_cat[cat], key=lambda x: -x[3])
        print(f'\n[{cat}]  共 {len(items)} 种颜色')
        for r, g, b, cnt in items[:12]:
            h, s, v = rgb_to_hsv_deg(r, g, b)
            print(f'  #{r:02X}{g:02X}{b:02X}  RGB({r:>3},{g:>3},{b:>3})  '
                  f'H={h:>6.1f}° S={s:.3f} V={v:.3f}  数量={cnt}')

    print('\n' + '-' * 60)
    print('各文件分类统计')
    print('-' * 60)
    for f, st in file_stats.items():
        print(f'\n{f}  总像素={st["total"]}')
        for cat, cnt in sorted(st['categories'].items(), key=lambda x: -x[1]):
            print(f'  {cat:<16} {cnt:>6}  {cnt/st["total"]*100:>5.1f}%')

    return cat_totals


def save_features_json(folder, global_colors, file_stats, v_range, cat_totals):
    out = {
        'diamond_v_range': list(v_range),
        'category_totals': cat_totals,
        'colors': [],
        'files': file_stats,
    }
    for (r, g, b), info in sorted(global_colors.items(), key=lambda x: -x[1]['count']):
        h, s, v = rgb_to_hsv_deg(r, g, b)
        out['colors'].append({
            'hex': f'#{r:02X}{g:02X}{b:02X}',
            'rgb': [r, g, b],
            'hsv': [round(h, 2), round(s, 4), round(v, 4)],
            'count': info['count'],
            'category': info['category'],
            'files': sorted(info['files']),
        })

    path = os.path.join(folder, 'feature_report.json')
    with open(path, 'w', encoding='utf-8') as fp:
        json.dump(out, fp, ensure_ascii=False, indent=2)
    print(f'\n特征已导出: {path}')
    return path


# 材质识别
def identify_material(img_array):
    arr = img_array.astype(np.float32)
    h, w = arr.shape[:2]
    r = arr[:, :, 0] / 255.0
    g = arr[:, :, 1] / 255.0
    b = arr[:, :, 2] / 255.0
    a = arr[:, :, 3] if arr.shape[2] == 4 else np.full((h, w), 255.0)

    maxc = np.maximum(np.maximum(r, g), b)
    minc = np.minimum(np.minimum(r, g), b)
    s = np.where(maxc > 0, (maxc - minc) / np.maximum(maxc, 1e-6), 0)
    v = maxc

    rc = np.where(maxc > 0, (maxc - r) / np.maximum(maxc - minc, 1e-6), 0)
    gc = np.where(maxc > 0, (maxc - g) / np.maximum(maxc - minc, 1e-6), 0)
    bc = np.where(maxc > 0, (maxc - b) / np.maximum(maxc - minc, 1e-6), 0)
    hh = np.zeros_like(maxc)
    hh = np.where(maxc == r, bc - gc, hh)
    hh = np.where(maxc == g, 2.0 + rc - bc, hh)
    hh = np.where(maxc == b, 4.0 + gc - rc, hh)
    hh = (hh / 6.0) % 1.0
    h_deg = hh * 360.0

    valid = (a > 10) & (v >= 0.10) & ~((s < 0.10) & (v > 0.90))
    if valid.sum() == 0:
        return 'unknown', {}, 0.0

    H = h_deg[valid]
    S = s[valid]
    V = v[valid]
    total = len(H)

    scores = {}

    diamond_mask   = (H >= 160) & (H <= 205) & (S > 0.30)
    netherite_mask = (H >= 290) & (H <= 340) & (S > 0.15)
    golden_mask    = (H >= 20) & (H <= 57) & (S > 0.70) & (V > 0.70)
    iron_mask      = (S < 0.10) & (V > 0.65)
    stone_mask     = (S < 0.10) & (V > 0.25) & (V <= 0.65)
    wooden_mask    = (H >= 20) & (H <= 57) & (S > 0.40) & (V < 0.65)

    scores['diamond']   = int(diamond_mask.sum())
    scores['netherite'] = int(netherite_mask.sum())
    scores['golden']    = int(golden_mask.sum())
    scores['iron']      = int(iron_mask.sum())
    scores['stone']     = int(stone_mask.sum())
    scores['wooden']    = int(wooden_mask.sum())

    ratios = {k: v / total for k, v in scores.items()}

    THRESHOLD = 0.08
    for mat in ['diamond', 'netherite', 'golden', 'iron', 'stone', 'wooden']:
        if ratios[mat] >= THRESHOLD:
            return mat, scores, min(ratios[mat], 1.0)

    best = max(scores, key=scores.get)
    if scores[best] == 0:
        return 'unknown', scores, 0.0
    return best, scores, scores[best] / total


def do_identify(folder):
    print('\n' + '=' * 60)
    print('材质识别')
    print('=' * 60)

    prefix = input(f'输入前缀（默认 {DEFAULT_PREFIX}，直接回车使用默认）: ').strip()
    if not prefix:
        prefix = DEFAULT_PREFIX
    if not prefix.endswith('_'):
        prefix += '_'
    while True:
        suffix = input('输入物品名后缀（必填）: ').strip()
        if suffix:
            break
        print('后缀不能为空，请重新输入')

    png_files = [f for f in os.listdir(folder) if f.lower().endswith('.png')]
    candidates = [f for f in png_files if not f.startswith(prefix)]

    if not candidates:
        print('未找到待识别的 PNG 文件')
        return

    print(f'\n前缀: {prefix}')
    print(f'后缀: {suffix}')
    print(f'待识别文件: {len(candidates)} 个\n')

    output_dir = os.path.join(folder, 'output')
    os.makedirs(output_dir, exist_ok=True)

    results = []
    for f in candidates:
        path = os.path.join(folder, f)
        try:
            img = Image.open(path).convert('RGBA')
        except Exception as e:
            print(f'  [跳过] {f}: {e}')
            continue

        arr = np.array(img)
        material, scores, conf = identify_material(arr)
        results.append((f, material, conf, scores))

        print(f'{f}')
        print(f'  → 材质: {material}  (权重置信度 {conf*100:.1f}%)')
        print(f'    权重得分: {scores}')

    if not results:
        print('\n没有可识别的文件')
        return

    print('\n' + '-' * 60)
    print('即将重命名并保存到 output/:')
    print('-' * 60)
    for f, material, conf, _ in results:
        ext = os.path.splitext(f)[1]
        new_name = f'{prefix}{material}_{suffix}{ext}'
        print(f'  {f}  →  {new_name}')

    confirm = input('\n确认执行？(y/n): ').strip().lower()
    if confirm != 'y':
        print('已取消')
        return

    for f, material, conf, _ in results:
        src = os.path.join(folder, f)
        ext = os.path.splitext(f)[1]
        new_name = f'{prefix}{material}_{suffix}{ext}'
        dst = os.path.join(output_dir, new_name)
        try:
            img = Image.open(src).convert('RGBA')
            img.save(dst, 'PNG')
            print(f'  [保存] output/{new_name}')
        except Exception as e:
            print(f'  [失败] {f}: {e}')

    print(f'\n完成！共处理 {len(results)} 个文件，输出到 {output_dir}')


# 生成
def build_diamond_mask(img_array):
    arr = img_array.astype(np.float32)
    h, w = arr.shape[:2]
    r = arr[:, :, 0] / 255.0
    g = arr[:, :, 1] / 255.0
    b = arr[:, :, 2] / 255.0
    a = arr[:, :, 3] if arr.shape[2] == 4 else np.full((h, w), 255.0)

    maxc = np.maximum(np.maximum(r, g), b)
    minc = np.minimum(np.minimum(r, g), b)
    s = np.where(maxc > 0, (maxc - minc) / np.maximum(maxc, 1e-6), 0)

    rc = np.where(maxc > 0, (maxc - r) / np.maximum(maxc - minc, 1e-6), 0)
    gc = np.where(maxc > 0, (maxc - g) / np.maximum(maxc - minc, 1e-6), 0)
    bc = np.where(maxc > 0, (maxc - b) / np.maximum(maxc - minc, 1e-6), 0)
    hh = np.zeros_like(maxc)
    hh = np.where(maxc == r, bc - gc, hh)
    hh = np.where(maxc == g, 2.0 + rc - bc, hh)
    hh = np.where(maxc == b, 4.0 + gc - rc, hh)
    hh = (hh / 6.0) % 1.0
    h_deg = hh * 360.0

    mask = (a > 10) & (h_deg >= 160.0) & (h_deg <= 235.0) & (s > 0.10)
    return mask


def derive_ramp(material):
    R, G, B = [c / 255.0 for c in MATERIAL_COLORS[material]]
    h_mid, s_mid, v_mid = colorsys.rgb_to_hsv(R, G, B)
    span = MATERIAL_SPAN.get(material, 0.7)

    v_dark = max(0.02, v_mid * (1.0 - span * 0.60))
    s_dark = min(1.0, s_mid + 0.05 * span)
    h_dark = h_mid

    v_light = min(1.0, v_mid + (1.0 - v_mid) * span * 0.80 + 0.05)
    s_light = max(0.0, s_mid * (1.0 - span * 0.50))
    h_light = h_mid

    if material == 'golden':
        h_dark = h_mid * 0.60
        h_light = min(1.0, h_mid * 1.20)
    elif material == 'wooden':
        h_dark = h_mid * 0.88
        h_light = min(1.0, h_mid * 1.08)
    elif material == 'netherite':
        h_dark = h_mid * 0.92
        h_light = h_mid * 1.04

    return (h_dark, s_dark, v_dark), (h_mid, s_mid, v_mid), (h_light, s_light, v_light)


def hsv_to_rgb_array(H, S, V):
    i = np.floor(H * 6.0)
    f = H * 6.0 - i
    p = V * (1.0 - S)
    q = V * (1.0 - f * S)
    t = V * (1.0 - (1.0 - f) * S)
    i = i.astype(np.int32) % 6
    r = np.choose(i, [V, q, p, p, t, V])
    g = np.choose(i, [t, V, V, q, p, p])
    b = np.choose(i, [p, p, t, V, V, q])
    return r, g, b


def replace_material_color(img_array, material, v_range_hint=None):
    arr = img_array.astype(np.float32).copy()
    r = arr[:, :, 0] / 255.0
    g = arr[:, :, 1] / 255.0
    b = arr[:, :, 2] / 255.0
    maxc = np.maximum(np.maximum(r, g), b)
    v = maxc

    mask = build_diamond_mask(img_array)
    if not mask.any():
        return img_array

    (h_dark, s_dark, v_dark), (h_mid, s_mid, v_mid), (h_light, s_light, v_light) = derive_ramp(material)

    v_masked = v[mask]
    if v_range_hint is not None:
        v_min, v_max = v_range_hint
    else:
        v_min, v_max = v_masked.min(), v_masked.max()

    norm = (v_masked - v_min) / max(v_max - v_min, 1e-6)
    norm = np.clip(norm, 0.0, 1.0)
    norm = norm ** 0.85

    t1 = np.clip(norm / 0.5, 0.0, 1.0)
    t2 = np.clip((norm - 0.5) / 0.5, 0.0, 1.0)

    H_dm = h_dark + t1 * (h_mid - h_dark)
    S_dm = s_dark + t1 * (s_mid - s_dark)
    V_dm = v_dark + t1 * (v_mid - v_dark)
    H_ml = h_mid + t2 * (h_light - h_mid)
    S_ml = s_mid + t2 * (s_light - s_mid)
    V_ml = v_mid + t2 * (v_light - v_mid)

    use_second = norm > 0.5
    H_new = np.where(use_second, H_ml, H_dm)
    S_new = np.where(use_second, S_ml, S_dm)
    V_new = np.where(use_second, V_ml, V_dm)
    V_new = np.clip(V_new, 0.0, 1.0)
    S_new = np.clip(S_new, 0.0, 1.0)

    nr, ng, nb = hsv_to_rgb_array(H_new, S_new, V_new)
    arr[:, :, 0][mask] = nr * 255.0
    arr[:, :, 1][mask] = ng * 255.0
    arr[:, :, 2][mask] = nb * 255.0
    return np.clip(arr, 0, 255).astype(np.uint8)


def generate_variants(src_path, output_dir, base_name, v_range_hint=None):
    try:
        img = Image.open(src_path).convert('RGBA')
    except Exception as e:
        print(f'  [跳过] 无法打开: {e}')
        return
    img_array = np.array(img)

    for mat in ['wooden', 'stone', 'golden', 'iron', 'netherite']:
        out_name = f'{mat}_{base_name}'
        out_path = os.path.join(output_dir, out_name)
        new_array = replace_material_color(img_array, mat, v_range_hint)
        Image.fromarray(new_array, 'RGBA').save(out_path, 'PNG')
        print(f'  [生成] output/{out_name}')


def do_generate(folder):
    output_dir = os.path.join(folder, 'output')
    os.makedirs(output_dir, exist_ok=True)
    print(f'输出目录: {output_dir}\n')

    bare_files, diamond_files = scan_source_files(folder)
    if not bare_files and not diamond_files:
        print('未找到可处理的PNG文件')
        return

    print(f'裸文件: {len(bare_files)} 个，diamond_ 源图: {len(diamond_files)} 个\n')

    source_files = bare_files + diamond_files
    print('预扫描钻石青蓝 V 范围...')
    _, _, v_range = extract_features(folder, source_files)
    print(f'V 范围: {v_range[0]:.3f} ~ {v_range[1]:.3f}\n')
    v_range_hint = v_range

    start_time = time.time()

    for f in bare_files:
        print(f'处理原始文件: {f}')
        src_path = os.path.join(folder, f)
        diamond_name = f'diamond_{f}'
        diamond_path = os.path.join(output_dir, diamond_name)
        if not os.path.exists(diamond_path):
            Image.open(src_path).convert('RGBA').save(diamond_path, 'PNG')
            print(f'  [生成] output/{diamond_name}')
        else:
            print(f'  [已存在] output/{diamond_name}')
        generate_variants(src_path, output_dir, f, v_range_hint)
        print()

    for f in diamond_files:
        print(f'处理钻石文件: {f}')
        base_name = f[len('diamond_'):]
        src_path = os.path.join(folder, f)
        out_diamond = os.path.join(output_dir, f)
        if not os.path.exists(out_diamond):
            Image.open(src_path).convert('RGBA').save(out_diamond, 'PNG')
            print(f'  [生成] output/{f}')
        generate_variants(src_path, output_dir, base_name, v_range_hint)
        print()

    print('=' * 60)
    print('全部完成！')
    print(f'输出文件夹: {output_dir}')
    print(f'耗时: {time.time() - start_time:.2f} 秒')


def do_extract(folder):
    bare_files, diamond_files = scan_source_files(folder)
    source_files = bare_files + diamond_files
    if not source_files:
        print('未找到可处理的PNG文件')
        return

    print(f'将分析 {len(source_files)} 个源文件 '
          f'(裸文件 {len(bare_files)}，diamond_ {len(diamond_files)})\n')

    t0 = time.time()
    global_colors, file_stats, v_range = extract_features(folder, source_files)
    cat_totals = print_feature_report(global_colors, file_stats, v_range)
    save_features_json(folder, global_colors, file_stats, v_range, cat_totals)
    print(f'\n特征提取耗时: {time.time() - t0:.2f} 秒')


def main_menu(folder):
    while True:
        print('\n' + '=' * 60)
        print(f'工作目录: {folder}')
        print('=' * 60)
        print('请选择功能:')
        print('  1 - 批量生成材质变体（实验 仅限钻石材质生成其他材质）')
        print('  2 - 提取所有颜色并分析特征（算法分析）')
        print('  3 - 材质识别 批量命名')
        print('  0 - 退出')
        print('=' * 60)

        choice = input('输入选项 [1/2/3/0]: ').strip()

        if choice == '1':
            do_generate(folder)
            print('\n功能执行完毕，自动退出')
            break
        elif choice == '2':
            do_extract(folder)
            print('\n功能执行完毕，自动退出')
            break
        elif choice == '3':
            do_identify(folder)
            print('\n功能执行完毕，自动退出')
            break
        elif choice == '0':
            print('已退出')
            break
        else:
            print('无效选项，请重新输入')


def main():
    print('=' * 60)
    print('MC 物品材质批量生成工具')
    print('GitHub: https://github.com/CTNStudio/ChineseWeapons')
    print('请勿用于售卖或商业用途 BSD-3')
    print('=' * 60)

    folder = select_directory()
    if not folder:
        print('未选择目录，退出')
        return

    main_menu(folder)


if __name__ == '__main__':
    main()