package net.mirrorloong.chineseweapons.fabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.mirrorloong.chineseweapons.gui.recipe.DeferredGui;
import net.mirrorloong.chineseweapons.gui.recipe.WeaponsCastingTableTypeGuiMenu;
import net.mirrorloong.chineseweapons.recipes.RecipeJsonSupport;
import net.mirrorloong.chineseweapons.spi.ContentFactory;
import net.mirrorloong.chineseweapons.spi.GameplayCompat;
import net.mirrorloong.chineseweapons.spi.Network;
import net.mirrorloong.chineseweapons.spi.Platform;
import net.mirrorloong.chineseweapons.spi.RecipeSupport;
import net.mirrorloong.chineseweapons.spi.Registration;

public final class FabricPlatform implements Platform {
    private final FabricRegistration registration = new FabricRegistration();
    private final FabricNetwork network = new FabricNetwork();
    private final FabricContentFactory content = new FabricContentFactory();
    private final FabricGameplayCompat gameplay = new FabricGameplayCompat();

    @Override
    public Registration registration() {
        return registration;
    }

    @Override
    public Network network() {
        return network;
    }

    @Override
    public ContentFactory content() {
        return content;
    }

    @Override
    public GameplayCompat gameplay() {
        return gameplay;
    }

    @Override
    public RecipeSupport recipes() {
        return new RecipeJsonSupport();
    }

    @Override
    public boolean isClient() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    @Override
    public MenuType<WeaponsCastingTableTypeGuiMenu> castingTableMenu() {
        return DeferredGui.REG_MENU.get();
    }

    @Override
    public void openCastingTable(ServerPlayer player, BlockPos pos) {
        player.openMenu(new ExtendedScreenHandlerFactory() {
            @Override
            public void writeScreenOpeningData(ServerPlayer serverPlayer, FriendlyByteBuf buffer) {
                buffer.writeBlockPos(pos);
            }

            @Override
            public Component getDisplayName() {
                return Component.translatable("block.chineseweapons.armor_casting_table");
            }

            @Override
            public AbstractContainerMenu createMenu(int id, Inventory inventory, Player ignored) {
                return new WeaponsCastingTableTypeGuiMenu(id, inventory, pos);
            }
        });
    }

    @Override
    public void attach() {
        DeferredGui.init();
    }
}
