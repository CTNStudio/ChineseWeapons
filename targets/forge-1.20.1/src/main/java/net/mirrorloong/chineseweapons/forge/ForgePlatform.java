package net.mirrorloong.chineseweapons.forge;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkHooks;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.gui.recipe.DeferredGui;
import net.mirrorloong.chineseweapons.gui.recipe.WeaponsCastingTableTypeGuiMenu;
import net.mirrorloong.chineseweapons.recipes.RecipeJsonSupport;
import net.mirrorloong.chineseweapons.spi.ContentFactory;
import net.mirrorloong.chineseweapons.spi.GameplayCompat;
import net.mirrorloong.chineseweapons.spi.Network;
import net.mirrorloong.chineseweapons.spi.Platform;
import net.mirrorloong.chineseweapons.spi.RecipeSupport;
import net.mirrorloong.chineseweapons.spi.Registration;

public final class ForgePlatform implements Platform {
    private final IEventBus modBus;
    private final ForgeRegistration registration = new ForgeRegistration();
    private final ForgeNetwork network = new ForgeNetwork();
    private final ForgeContentFactory content = new ForgeContentFactory();
    private final ForgeGameplayCompat gameplay = new ForgeGameplayCompat();

    public ForgePlatform(IEventBus modBus) {
        this.modBus = modBus;
    }

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
        return FMLEnvironment.dist.isClient();
    }

    @Override
    public MenuType<WeaponsCastingTableTypeGuiMenu> castingTableMenu() {
        return DeferredGui.REG_MENU.get();
    }

    @Override
    public void openCastingTable(ServerPlayer player, BlockPos pos) {
        NetworkHooks.openScreen(player, new SimpleMenuProvider(
                (id, inventory, ignored) -> new WeaponsCastingTableTypeGuiMenu(id, inventory, pos),
                Component.translatable("block.chineseweapons.armor_casting_table")), pos);
    }

    @Override
    public void attach() {
        DeferredGui.init();
        registration.attach(modBus);
    }
}
