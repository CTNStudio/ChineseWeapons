package net.mirrorloong.chineseweapons.gui.recipe;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.mirrorloong.chineseweapons.ChineseweaponsMod;
import net.mirrorloong.chineseweapons.recipes.DeferredRecipe;
import net.mirrorloong.chineseweapons.recipes.WCTRecipe;

import java.util.List;
@Mod.EventBusSubscriber(modid = ChineseweaponsMod.MODID,value = Dist.CLIENT,bus= Mod.EventBusSubscriber.Bus.MOD)
public class WeaponsCastingTableTypeGuiMenu extends AbstractContainerMenu {
    public static RecipeManager recipeManager;
    private final Player player;
    private final SimpleContainer container2 = new SimpleContainer(10);
    private final SimpleContainer outputContainer=new SimpleContainer(1);
    private final Slot outputSlot = new Slot(outputContainer,0,115,35){
        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public boolean mayPickup(Player player) {
            return true;
        }

        @Override
        public ItemStack remove(int amount) {
            ItemStack current = getItem();
            if (current.isEmpty()) {
                return ItemStack.EMPTY;
            }

            int removeAmount = Math.min(amount, current.getCount());
            ItemStack result = current.copyWithCount(removeAmount);
            current.shrink(removeAmount);

            set(current);

            return result;
        }
        @Override
        public void onTake(Player player, ItemStack stack) {
            super.onTake(player, stack);
            for(int j=0;j<10;++j){
                container2.removeItem(j,1);
            }
            if (!player.level().isClientSide)
                this.setChanged();
        }
    };

    public WeaponsCastingTableTypeGuiMenu(int id, Inventory inventoryPlayer) {
        super(DeferredGui.RegMenu.get(), id);
        this.player = inventoryPlayer.player;
        //初始化 hyw
        if (inventoryPlayer.player.level().isClientSide()) {
            this.recipeManager = Minecraft.getInstance().level.getRecipeManager();
        } else {
            this.recipeManager = inventoryPlayer.player.level().getServer().getRecipeManager();
        }

        container2.addListener(container -> {
            try {
                if (recipeManager == null) {
                    if (player.level().isClientSide()) {
                        recipeManager = Minecraft.getInstance().level.getRecipeManager();
                    } else {
                        recipeManager = player.level().getServer().getRecipeManager();
                    }
                }

                if (recipeManager != null) {
                    List<WCTRecipe> AllWCTRecipe = recipeManager.getAllRecipesFor(DeferredRecipe.WeaponCastingShapedType.get());
                    for (WCTRecipe wctRecipe : AllWCTRecipe) {
                        if (wctRecipe.matches(container2, null)) {
                            outputContainer.setItem(0, wctRecipe.getResultItem2());
                            break;
                        } else if (!outputContainer.getItem(0).is(Items.AIR)) {
                            outputContainer.setItem(0, new ItemStack(Items.AIR));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        int i = 0;
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(inventoryPlayer, col + row * 9 + 9, col * 18 - 19, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(inventoryPlayer, col, col * 18 - 19, 142));
        }
        for (int x = 0; x <= 36; x += 18) {
            for (int y = 0; y <= 36; y += 18) {
                this.addSlot(new Slot(container2, i, y - 2, x + 17));
                ++i;
            }
        }
        this.addSlot(new Slot(container2, 9, 69, 53));
        this.addSlot(outputSlot);
    }
    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        if (!player.level().isClientSide && player instanceof ServerPlayer serverPlayer) {
            for (int i = 0; i < this.slots.size(); i++) {
                Slot slot = this.slots.get(i);
                serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(
                        this.containerId,
                        this.incrementStateId(),
                        i,
                        slot.getItem()
                ));
            }
        }
    }
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }
    @Override
    public void removed(Player player) {

        if (!player.level().isClientSide) {
            returnItemsToPlayer(player);
        }
        super.removed(player);
    }

    private void returnItemsToPlayer(Player player) {
        Inventory inventory = player.getInventory();

        for (int i = 36; i < 46; i++) {
            Slot slot = this.slots.get(i);
            if (slot.hasItem()) {
                ItemStack stack = slot.getItem().copy();

                boolean added = false;
                if (inventory.add(stack)) added = inventory.add(stack);

                if (!added && !stack.isEmpty()) player.drop(stack, false);


                slot.set(ItemStack.EMPTY);
            }
        }
    }
    @Override
    public boolean stillValid(Player player) {
        return true;
    }
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event){
        event.enqueueWork(()->{
            MenuScreens.register(DeferredGui.RegMenu.get(),WeaponsCastingTableScreen::new);
        });
    }
}
