package net.mirrorloong.chineseweapons.gui.recipe;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;

public class DeferredGui{
    public static final DeferredRegister<MenuType<?>> Deferred = DeferredRegister.create(BuiltInRegistries.MENU, ChineseweaponsMod.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<WeaponsCastingTableTypeGuiMenu>> RegMenu = Deferred.register("gui.chineseweapons.weapons_casting_table",()-> IMenuTypeExtension.create((windowId, inv, data) -> new WeaponsCastingTableTypeGuiMenu(windowId, inv)));

 }