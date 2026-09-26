import os
import re
import json
from pathlib import Path
from datetime import datetime

# 武器名称列表
weapon_names = [
    "diamond_dagger_axe", "diamond_glaive", "diamond_sky_piercing_halberd",
    "diamond_song_javelin", "diamond_tang_dynasty_heng_saber",
    "golden_dagger_axe", "golden_glaive", "golden_sky_piercing_halberd",
    "golden_song_javelin", "golden_tang_dynasty_heng_saber",
    "green_loong_glaive", "green_loong_glaive_head",
    "iron_dagger_axe", "iron_glaive", "iron_sky_piercing_halberd",
    "iron_song_javelin", "iron_tang_dynasty_heng_saber",
    "netherite_dagger_axe", "netherite_glaive", "netherite_sky_piercing_halberd",
    "netherite_song_javelin", "netherite_tang_dynasty_heng_saber",
    "standard_glaive_head",
    "stone_dagger_axe", "stone_glaive", "stone_sky_piercing_halberd",
    "stone_song_javelin", "stone_tang_dynasty_heng_saber",
    "wooden_dagger_axe", "wooden_glaive", "wooden_sky_piercing_halberd",
    "wooden_song_javelin", "wooden_tang_dynasty_heng_saber"
]

# 构建正则表达式 - 匹配 block/武器名称（不包含.png）
pattern = re.compile(
    r'block/(' + '|'.join(re.escape(name) for name in weapon_names) + r')(?=["\s,])'
)
replacement = r'block/weapon/\1'

def process_file(file_path, dry_run=True):
    """处理单个文件"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        original_content = content
        
        # 执行替换
        new_content = pattern.sub(replacement, content)
        
        # 计算修改次数
        matches = pattern.findall(content)
        
        if new_content != original_content:
            if not dry_run:
                # 写入新内容
                with open(file_path, 'w', encoding='utf-8') as f:
                    f.write(new_content)
                print(f"✓ 已处理: {file_path.name} ({len(matches)}处修改)")
            else:
                print(f"[预览] 将修改: {file_path.name} ({len(matches)}处修改)")
                # 显示修改示例
                if matches:
                    for match in matches[:3]:  # 显示前3个修改
                        print(f"  - block/{match} -> block/weapon/{match}")
            
            return True, len(matches)
        else:
            print(f"→ 无变化: {file_path.name}")
            return False, 0
            
    except Exception as e:
        print(f"✗ 处理失败 {file_path}: {e}")
        return False, 0

def process_directory(directory_path, dry_run=True):
    """处理目录下的所有JSON文件"""
    directory = Path(directory_path)
    
    if not directory.exists():
        print(f"错误: 目录不存在 - {directory_path}")
        return
    
    total_files = 0
    changed_files = 0
    total_changes = 0
    
    print(f"\n开始{'预览' if dry_run else '处理'}...")
    print(f"目录: {directory_path}")
    print(f"{'='*60}\n")
    
    # 递归处理所有.json文件
    for file_path in sorted(directory.rglob('*.json')):
        total_files += 1
        changed, change_count = process_file(file_path, dry_run)
        if changed:
            changed_files += 1
            total_changes += change_count
    
    print(f"\n{'='*60}")
    print(f"处理完成!")
    print(f"总文件数: {total_files}")
    print(f"修改文件数: {changed_files}")
    print(f"总修改处数: {total_changes}")
    if dry_run:
        print(f"\n⚠️  这是预览模式，未实际修改文件。")

def create_backup(file_path):
    """为文件创建备份"""
    backup_dir = Path("backup_" + datetime.now().strftime("%Y%m%d_%H%M%S"))
    backup_dir.mkdir(exist_ok=True)
    
    # 保持目录结构
    relative_path = file_path.relative_to(file_path.anchor) if file_path.is_absolute() else file_path
    backup_path = backup_dir / relative_path.name
    
    import shutil
    shutil.copy2(file_path, backup_path)
    return backup_path

def process_directory_with_backup(directory_path):
    """带备份的目录处理"""
    directory = Path(directory_path)
    
    if not directory.exists():
        print(f"错误: 目录不存在 - {directory_path}")
        return
    
    total_files = 0
    changed_files = 0
    total_changes = 0
    
    print(f"\n开始处理（带备份）...")
    print(f"目录: {directory_path}")
    print(f"{'='*60}\n")
    
    for file_path in sorted(directory.rglob('*.json')):
        total_files += 1
        
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()
            
            original_content = content
            new_content = pattern.sub(replacement, content)
            matches = pattern.findall(content)
            
            if new_content != original_content:
                # 创建备份
                backup_path = create_backup(file_path)
                
                # 写入新内容
                with open(file_path, 'w', encoding='utf-8') as f:
                    f.write(new_content)
                
                print(f"✓ 已处理: {file_path.name} ({len(matches)}处修改)")
                print(f"  备份: {backup_path}")
                
                changed_files += 1
                total_changes += len(matches)
            
        except Exception as e:
            print(f"✗ 处理失败 {file_path}: {e}")
    
    print(f"\n{'='*60}")
    print(f"处理完成!")
    print(f"总文件数: {total_files}")
    print(f"修改文件数: {changed_files}")
    print(f"总修改处数: {total_changes}")

# 主程序
if __name__ == "__main__":
    import sys
    
    # 获取目标路径
    if len(sys.argv) > 1:
        target_path = sys.argv[1]
    else:
        target_path = input("请输入要处理的目录路径: ").strip()
    
    # 先预览
    print("\n第一步：预览修改")
    process_directory(target_path, dry_run=True)
    
    # 询问是否继续
    confirm = input(f"\n是否确认修改? (yes/no): ").strip().lower()
    
    if confirm in ['yes', 'y', '是']:
        # 询问是否备份
        backup_confirm = input("是否需要备份原文件? (yes/no): ").strip().lower()
        
        if backup_confirm in ['yes', 'y', '是']:
            print("\n第二步：执行修改（带备份）")
            process_directory_with_backup(target_path)
        else:
            print("\n第二步：执行修改（不备份）")
            process_directory(target_path, dry_run=False)
    else:
        print("已取消修改。")