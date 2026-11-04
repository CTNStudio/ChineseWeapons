package net.mirrorloong.chineseweapons.neoforge;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import net.mirrorloong.chineseweapons.gui.recipe.DeferredGui;
import net.mirrorloong.chineseweapons.gui.recipe.WeaponsCastingTableTypeGuiMenu;
import net.mirrorloong.chineseweapons.spi.ContentFactory;
import net.mirrorloong.chineseweapons.spi.Network;
import net.mirrorloong.chineseweapons.spi.Platform;
import net.mirrorloong.chineseweapons.spi.Registration;

public final class NeoForgePlatform implements Platform {
    private final IEventBus modBus;
    private final NeoForgeRegistration registration = new NeoForgeRegistration();
    private final NeoForgeNetwork network = new NeoForgeNetwork();
    private final NeoForgeContentFactory content = new NeoForgeContentFactory();

    public NeoForgePlatform(IEventBus modBus) {
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

    public NeoForgeNetwork networkImpl() {
        return network;
    }

    @Override
    public ContentFactory content() {
        return content;
    }

    @Override
    public boolean isClient() {
        return FMLEnvironment.getDist().isClient();
    }

    @Override
    public MenuType<WeaponsCastingTableTypeGuiMenu> castingTableMenu() {
        return DeferredGui.REG_MENU.get();
    }

    @Override
    public void openCastingTable(ServerPlayer player, BlockPos pos) {
        player.openMenu(new SimpleMenuProvider(
                (id, inventory, ignored) -> new WeaponsCastingTableTypeGuiMenu(id, inventory, pos),
                Component.translatable("block.chineseweapons.armor_casting_table")), buffer -> buffer.writeBlockPos(pos));
    }

    @Override
    public void attach() {
        DeferredGui.init();
        registration.attach(modBus);
    }
}
