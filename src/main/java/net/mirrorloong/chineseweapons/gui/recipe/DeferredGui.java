package net.mirrorloong.chineseweapons.gui.recipe;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.mirrorloong.chineseweapons.compat.registry.DeferredRegister;
import net.mirrorloong.chineseweapons.compat.registry.RegistryObject;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;

public class DeferredGui{
    //old api DeferredRegister.create(ForgeRegistries.MENU_TYPES, MODID) -> new api DeferredRegister.create(BuiltInRegistries.MENU, MODID)
    public static final DeferredRegister<MenuType<?>> Deferred = DeferredRegister.create(BuiltInRegistries.MENU, ChineseweaponsMod.MODID);
    public static final RegistryObject<MenuType<WeaponsCastingTableTypeGuiMenu>> RegMenu =
            Deferred.register("gui.chineseweapons.weapons_casting_table",
                    () -> new ExtendedScreenHandlerType<>((windowId, inv, data) ->
                            new WeaponsCastingTableTypeGuiMenu(windowId, inv, data.readBlockPos())));
        public static void bootstrap() {
    }
 }
