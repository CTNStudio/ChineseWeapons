package net.mirrorloong.chineseweapons.gui.recipe;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;

public class DeferredGui{
    public static final DeferredRegister<MenuType<?>> Deferred = DeferredRegister.create(ForgeRegistries.MENU_TYPES, ChineseweaponsMod.MODID);

    public static final RegistryObject<MenuType<WeaponsCastingTableTypeGuiMenu>> RegMenu = Deferred.register("gui.chineseweapons.weapons_casting_table",()-> IForgeMenuType.create((windowId, inv, data) -> new WeaponsCastingTableTypeGuiMenu(windowId, inv)));

 }