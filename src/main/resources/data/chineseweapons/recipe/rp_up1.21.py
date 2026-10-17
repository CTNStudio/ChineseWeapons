import os
import json
import shutil
from pathlib import Path
from typing import List, Tuple, Dict, Any
from datetime import datetime

class RecipeConverter:
    def __init__(self, root_dir: str):
        self.root_dir = Path(root_dir)
        self.recipes_found = []
        self.changes_made = []
        self.backup_dir = None
        self.backup_files = []
        
    def create_backup_dir(self) -> Path:
        if self.backup_dir is None:
            timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
            self.backup_dir = self.root_dir / f"backup_{timestamp}"
            self.backup_dir.mkdir(parents=True, exist_ok=True)
            print(f"创建备份目录: {self.backup_dir}")
        return self.backup_dir
    
    def backup_file(self, file_path: Path) -> Path:
        backup_dir = self.create_backup_dir()
        
        rel_path = file_path.relative_to(self.root_dir)
        backup_path = backup_dir / rel_path
        backup_path.parent.mkdir(parents=True, exist_ok=True)
        
        shutil.copy2(file_path, backup_path)
        
        self.backup_files.append({
            'original': file_path,
            'backup': backup_path,
            'relative': rel_path
        })
        
        return backup_path
    
    def find_recipe_files(self) -> List[Path]:
        recipe_files = []
        for ext in ['*.json']:
            recipe_files.extend(self.root_dir.glob(f'**/{ext}'))
        return recipe_files
    
    def is_recipe_file(self, file_path: Path) -> bool:
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                data = json.load(f)
                return 'type' in data and 'result' in data
        except:
            return False
    
    def is_target_recipe_type(self, data: Dict) -> bool:
        recipe_type = data.get('type', '')
        return recipe_type in [
            'minecraft:crafting_shaped', 
            'minecraft:crafting_shapeless',
            'minecraft:smithing_transform'
        ]
    
    def check_recipe_format(self, file_path: Path) -> Tuple[bool, Dict, str]:
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                data = json.load(f)
            
            if 'result' not in data:
                return False, data, "无result字段"
            
            result = data['result']
            
            if isinstance(result, dict):
                if 'item' in result and 'id' not in result:
                    return True, data, "需要替换"
                elif 'id' in result:
                    return False, data, "已经是新格式"
                else:
                    return False, data, "未知格式"
            else:
                return False, data, "result不是对象"
                
        except json.JSONDecodeError as e:
            return False, {}, f"JSON解析错误: {e}"
        except Exception as e:
            return False, {}, f"读取错误: {e}"
    
    def scan_recipes(self) -> List[Dict]:
        results = []
        files = self.find_recipe_files()
        
        print(f"找到 {len(files)} 个JSON文件，正在扫描...")
        print(f"配方类型: crafting_shaped, crafting_shapeless, smithing_transform")
        
        total_checked = 0
        type_stats = {
            'crafting_shaped': 0,
            'crafting_shapeless': 0,
            'smithing_transform': 0
        }
        
        for file_path in files:
            if not self.is_recipe_file(file_path):
                continue
            
            try:
                with open(file_path, 'r', encoding='utf-8') as f:
                    data = json.load(f)
                
                if not self.is_target_recipe_type(data):
                    continue
                
                recipe_type = data.get('type', '')
                if 'smithing_transform' in recipe_type:
                    type_stats['smithing_transform'] += 1
                elif 'crafting_shaped' in recipe_type:
                    type_stats['crafting_shaped'] += 1
                elif 'crafting_shapeless' in recipe_type:
                    type_stats['crafting_shapeless'] += 1
                
                total_checked += 1
                needs_fix, _, reason = self.check_recipe_format(file_path)
                
                if needs_fix:
                    current_item = data['result'].get('item', '')
                    count = data['result'].get('count', 1)
                    results.append({
                        'path': file_path,
                        'current_item': current_item,
                        'count': count,
                        'data': data,
                        'modified': False,
                        'backup_path': None,
                        'type': recipe_type
                    })
                    
            except Exception as e:
                print(f"读取文件失败 {file_path}: {e}")
                continue
        
        self.recipes_found = results
        
        print(f"\n配方统计:")
        print(f"  - crafting_shaped: {type_stats['crafting_shaped']} 个")
        print(f"  - crafting_shapeless: {type_stats['crafting_shapeless']} 个")
        print(f"  - smithing_transform: {type_stats['smithing_transform']} 个")
        print(f"  总计: {total_checked} 个合成配方")
        print(f"  需要修改: {len(results)} 个")
        
        return results
    
    def display_results(self):
        if not self.recipes_found:
            print("\n没有发现需要修改的配方文件！")
            return False
        
        print(f"\n发现 {len(self.recipes_found)} 个需要修改的配方文件：")
        print("=" * 80)
        
        for idx, recipe in enumerate(self.recipes_found, 1):
            rel_path = recipe['path'].relative_to(self.root_dir)
            count_info = f", count: {recipe['count']}" if recipe['count'] != 1 else ""
            
            type_short = recipe['type'].replace('minecraft:', '')
            
            print(f"\n{idx}. 文件: {rel_path}")
            print(f"   类型: {type_short}")
            print(f"   当前格式:")
            print(f"   \"result\": {{")
            print(f"     \"item\": \"{recipe['current_item']}\"{count_info}")
            print(f"   }}")
            
            if recipe['count'] != 1:
                print(f"   将改为:")
                print(f"   \"result\": {{")
                print(f"     \"id\": \"{recipe['current_item']}\",")
                print(f"     \"count\": {recipe['count']}")
                print(f"   }}")
            else:
                print(f"   将改为:")
                print(f"   \"result\": {{")
                print(f"     \"id\": \"{recipe['current_item']}\"")
                print(f"   }}")
        
        print("\n" + "=" * 80)
        return True
    
    def convert_single_recipe(self, recipe: Dict) -> bool:
        file_path = recipe['path']
        data = recipe['data']
        
        try:
            backup_path = self.backup_file(file_path)
            recipe['backup_path'] = backup_path
            
            item_value = data['result'].pop('item')
            count_value = data['result'].get('count', 1)
            
            new_result = {'id': item_value}
            if count_value != 1:
                new_result['count'] = count_value
            
            data['result'] = new_result
            
            with open(file_path, 'w', encoding='utf-8') as f:
                json.dump(data, f, indent=2, ensure_ascii=False)
            
            recipe['modified'] = True
            return True
            
        except Exception as e:
            print(f"❌ 转换失败 {file_path}: {e}")
            return False
    
    def confirm_and_convert(self):
        if not self.recipes_found:
            print("没有需要转换的配方。")
            return
        
        print("\n即将进行以下转换：")
        print("-" * 80)
        
        for recipe in self.recipes_found:
            rel_path = recipe['path'].relative_to(self.root_dir)
            count_info = f" (x{recipe['count']})" if recipe['count'] != 1 else ""
            type_short = recipe['type'].replace('minecraft:', '')
            print(f"  {rel_path} [{type_short}]")
            print(f"     item -> id: {recipe['current_item']}{count_info}")
        
        print("-" * 80)
        
        while True:
            response = input("\n是否确认执行转换？(y/n): ").strip().lower()
            if response in ['y', 'yes', '是']:
                self.perform_conversion()
                break
            elif response in ['n', 'no', '否']:
                print("已取消转换操作。")
                break
            else:
                print("请输入 y 或 n")
    
    def perform_conversion(self):
        print("\n开始转换...")
        success_count = 0
        
        for idx, recipe in enumerate(self.recipes_found, 1):
            rel_path = recipe['path'].relative_to(self.root_dir)
            type_short = recipe['type'].replace('minecraft:', '')
            print(f"  [{idx}/{len(self.recipes_found)}] 处理: {rel_path} [{type_short}]")
            
            if self.convert_single_recipe(recipe):
                success_count += 1
                print(f"    转换成功 (已备份)")
            else:
                print(f"    转换失败")
        
        print(f"\n转换完成！成功: {success_count}/{len(self.recipes_found)}")
        
        if success_count > 0:
            print(f"备份文件保存在: {self.backup_dir}")

    
    def undo_changes(self):
        if not self.backup_files:
            print("没有可撤回的更改。")
            return
        
        print(f"\n准备撤回 {len(self.backup_files)} 个文件的更改...")
        
        response = input("确认撤回所有更改？(y/n): ").strip().lower()
        if response not in ['y', 'yes', '是']:
            print("已取消撤回操作")
            return
        
        success_count = 0
        for backup_info in self.backup_files:
            original = backup_info['original']
            backup = backup_info['backup']
            
            try:
                if backup.exists():
                    shutil.copy2(backup, original)
                    print(f"  已撤回: {backup_info['relative']}")
                    success_count += 1
                else:
                    print(f"  备份文件不存在: {backup}")
            except Exception as e:
                print(f"  撤回失败 {backup_info['relative']}: {e}")
        
        print(f"\n撤回完成！成功: {success_count}/{len(self.backup_files)}")
    
    def run(self):
        print("处理类型: crafting_shaped, crafting_shapeless, smithing_transform")
        print(f"扫描目录: {self.root_dir}")
        print("=" * 80)
        
        self.scan_recipes()
        
        if not self.display_results():
            return
        
        self.confirm_and_convert()


def main():
    script_dir = Path(__file__).parent
    
    print("请输入配方的根目录:")
    user_input = input("路径: ").strip()
    
    if user_input:
        root_dir = Path(user_input)
    else:
        root_dir = script_dir
    
    if not root_dir.exists():
        print(f"目录不存在: {root_dir}")
        return
    
    converter = RecipeConverter(root_dir)
    
    print("\n是否执行撤回操作？(从之前的备份恢复)")
    response = input("撤回 (y/n): ").strip().lower()
    if response in ['y', 'yes', '是']:
        backup_dirs = sorted([d for d in root_dir.glob('backup_*') if d.is_dir()], reverse=True)
        if backup_dirs:
            print(f"找到最近的备份: {backup_dirs[0]}")
            converter.backup_files = []
            for backup_file in backup_dirs[0].rglob('*.json'):
                rel_path = backup_file.relative_to(backup_dirs[0])
                original_path = root_dir / rel_path
                converter.backup_files.append({
                    'original': original_path,
                    'backup': backup_file,
                    'relative': rel_path
                })
            converter.undo_changes()
            return
        else:
            print("没有找到备份目录")
    
    converter.run()


if __name__ == "__main__":
    main()