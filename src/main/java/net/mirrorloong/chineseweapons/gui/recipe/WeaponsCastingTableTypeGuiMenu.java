package net.mirrorloong.chineseweapons.gui.recipe;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.block.ArmorCastingTableBlockEntity;
import net.mirrorloong.chineseweapons.network.ArmorCastingTableBlockProgressPacket;

@Mod.EventBusSubscriber(modid = ChineseweaponsMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WeaponsCastingTableTypeGuiMenu extends AbstractContainerMenu {

    private final Player player;
    private final BlockPos tablePos;
    private ArmorCastingTableBlockEntity table;

    public WeaponsCastingTableTypeGuiMenu(int id, Inventory inventoryPlayer, BlockPos pos) {
        super(DeferredGui.RegMenu.get(), id);
        this.player = inventoryPlayer.player;
        this.tablePos = pos;

        if (player.level().getBlockEntity(pos) instanceof ArmorCastingTableBlockEntity be) {
            this.table = be;
        }

        if (table != null) {
            SimpleContainer input = table.getInput();
            int i = 0;
            for (int x = 0; x <= 36; x += 18) {
                for (int y = 0; y <= 36; y += 18) {
                    this.addSlot(new Slot(input, i, y - 2, x + 17));
                    ++i;
                }
            }
            this.addSlot(new Slot(input, 9, 69, 53));

            this.addSlot(new Slot(table.getOutput(), 0, 115, 35) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return false;
                }

                @Override
                public boolean mayPickup(Player p) {
                    return table != null && table.isFinished();
                }

                @Override
                public void onTake(Player p, ItemStack stack) {
                    super.onTake(p, stack);
                    if (table != null) {
                        table.getOutput().setItem(0, ItemStack.EMPTY);
                        table.onRecipeTaken(); //刷新
                    }
                }
            });
        }

        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(inventoryPlayer, col + row * 9 + 9, col * 18 - 19, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(inventoryPlayer, col, col * 18 - 19, 142));
        }
    }

    public BlockPos getTablePos() {
        return tablePos;
    }

    public ArmorCastingTableBlockEntity getTable() {
        return table;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot == null || !slot.hasItem()) return ItemStack.EMPTY;

        ItemStack stack = slot.getItem();
        result = stack.copy();

        // onTake
        if (index == 11) {
            if (table == null || !table.isFinished()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, stack);
            table.getOutput().setItem(0, ItemStack.EMPTY);
            table.onRecipeTaken();

            if (!player.getInventory().add(stack)) {
                player.drop(stack, false);
            }
            slot.set(ItemStack.EMPTY);
            return result;
        }

        // 输入槽 0-10
        if (index >= 0 && index <= 10) {
            if (table != null && !table.getOutput().getItem(0).isEmpty()) {
                return ItemStack.EMPTY;
            }
            if (!this.moveItemStackTo(stack, 12, 47, true)) {
                return ItemStack.EMPTY;
            }
        }
        // 背包 12-46
        else if (index >= 12 && index <= 46) {
            if (!this.moveItemStackTo(stack, 0, 11, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        return result;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (table != null) {
            table.setChanged();
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> MenuScreens.register(DeferredGui.RegMenu.get(), WeaponsCastingTableScreen::new));
    }
}