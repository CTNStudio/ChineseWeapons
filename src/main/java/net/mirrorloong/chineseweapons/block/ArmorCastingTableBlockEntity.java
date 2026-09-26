package net.mirrorloong.chineseweapons.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.mirrorloong.chineseweapons.init.ChineseweaponsModBlockEntities;
import net.mirrorloong.chineseweapons.network.ArmorCastingTableBlockProgressPacket;
import net.mirrorloong.chineseweapons.recipes.DeferredRecipe;
import net.mirrorloong.chineseweapons.recipes.WCTRecipe;

public class ArmorCastingTableBlockEntity extends BlockEntity {

    public static final int MIN_TICKS_PER_STEP = 20;   // 最少等待
    public static final int MAX_TICKS_PER_STEP = 40;   // 最多等待
    public static final int MIN_PROGRESS_PER_STEP = 15; //最少增长
    public static final int MAX_PROGRESS_PER_STEP = 35; //最多增长
    public static final int MAX_PROGRESS = 100;

    private final SimpleContainer input = new SimpleContainer(10);
    private final SimpleContainer output = new SimpleContainer(1);

    private int progress = 0;
    private int stepTimer = 0;
    private int nextStepTicks = 0;

    private boolean lastHasRecipe = false;
    private int lastProgress = -1;

    public ArmorCastingTableBlockEntity(BlockPos pos, BlockState state) {
        super(ChineseweaponsModBlockEntities.ARMOR_CASTING_TABLE.get(), pos, state);
    }

    public SimpleContainer getInput() { return input; }
    public SimpleContainer getOutput() { return output; }
    public int getProgress() { return progress; }

    public void setProgress(int p) {
        this.progress = Math.max(0, Math.min(MAX_PROGRESS, p));
    }

    public void resetProgress() {
        this.progress = 0;
        this.stepTimer = 0;
        this.nextStepTicks = 0;
    }

    public boolean isFinished() {
        return progress >= MAX_PROGRESS;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ArmorCastingTableBlockEntity be) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        WCTRecipe recipe = be.findMatchingRecipe(serverLevel);
        boolean hasRecipe = recipe != null;

        if (hasRecipe != be.lastHasRecipe || be.progress != be.lastProgress) {
            be.lastHasRecipe = hasRecipe;
            be.lastProgress = be.progress;
            be.syncProgress(hasRecipe);
        }

        if (be.isFinished()) {
            if (be.output.getItem(0).isEmpty() && recipe != null) {
                be.output.setItem(0, recipe.getResultItem2());
            }
            return;
        }

        if (recipe == null) {
            if (be.progress != 0 || be.stepTimer != 0) {
                be.resetProgress();
                be.setChanged();
            }
            be.output.setItem(0, ItemStack.EMPTY);
            return;
        }

        if (be.output.getItem(0).isEmpty()) {
            be.output.setItem(0, recipe.getResultItem2());
        }

        be.stepTimer++;
        if (be.stepTimer >= be.getNextStepTicks(serverLevel)) {
            be.stepTimer = 0;
            be.nextStepTicks = 0;

            int delta = MIN_PROGRESS_PER_STEP + serverLevel.random.nextInt(
                    MAX_PROGRESS_PER_STEP - MIN_PROGRESS_PER_STEP + 1);
            be.progress = Math.min(MAX_PROGRESS, be.progress + delta);

            if (be.progress < MAX_PROGRESS && MAX_PROGRESS - be.progress <= MIN_PROGRESS_PER_STEP) {
                be.progress = MAX_PROGRESS;
            }

            be.setChanged();

            if (be.progress >= MAX_PROGRESS) {
                serverLevel.playSound(null, pos, SoundEvents.ANVIL_PLACE, SoundSource.BLOCKS,
                        1F, 0.6F);
                be.consumeInputs();
            } else {
                serverLevel.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS,
                        0.8F, 0.9F + serverLevel.random.nextFloat() * 0.2F);
            }

            spawnParticles(serverLevel, pos);
        }
    }

    private int getNextStepTicks(ServerLevel level) {
        if (nextStepTicks <= 0) {
            nextStepTicks = MIN_TICKS_PER_STEP + level.random.nextInt(
                    MAX_TICKS_PER_STEP - MIN_TICKS_PER_STEP + 1);
        }
        return nextStepTicks;
    }

    private static void spawnParticles(ServerLevel level, BlockPos pos) {
        double cx = pos.getX() + 0.5;
        double cy = pos.getY() + 1.0;
        double cz = pos.getZ() + 0.5;

        int points = 12;
        double radius = 0.85;
        for (int i = 0; i < points; i++) {
            double angle = (Math.PI * 2 / points) * i;
            level.sendParticles(
                    ParticleTypes.CRIT,
                    cx + Math.cos(angle) * radius, cy, cz + Math.sin(angle) * radius,
                    1, 0, 0, 0, 0
            );
        }

        level.sendParticles(ParticleTypes.ENCHANT, cx, cy + 0.5, cz,
                6, 0.3, 0.3, 0.3, 0.02);
    }

    private void consumeInputs() {
        for (int i = 0; i < input.getContainerSize(); i++) {
            input.removeItem(i, 1);
        }
    }

    private WCTRecipe findMatchingRecipe(ServerLevel level) {
        RecipeManager rm = level.getRecipeManager();
        for (WCTRecipe r : rm.getAllRecipesFor(DeferredRecipe.WeaponCastingShapedType.get())) {
            if (r.matches(input, level)) return r;
        }
        return null;
    }

    private void syncProgress(boolean hasRecipe) {
        if (level instanceof ServerLevel sl) {
            ArmorCastingTableBlockProgressPacket.send(sl, worldPosition, progress, hasRecipe);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Progress", progress);
        tag.putInt("StepTimer", stepTimer);
        tag.putInt("NextStepTicks", nextStepTicks);

        CompoundTag inTag = new CompoundTag();
        for (int i = 0; i < input.getContainerSize(); i++) {
            ItemStack s = input.getItem(i);
            if (!s.isEmpty()) {
                CompoundTag t = new CompoundTag();
                t.putInt("Slot", i);
                s.save(t);
                inTag.put("S" + i, t);
            }
        }
        tag.put("Input", inTag);

        ItemStack out = output.getItem(0);
        if (!out.isEmpty()) {
            CompoundTag t = new CompoundTag();
            out.save(t);
            tag.put("Output", t);
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        progress = tag.getInt("Progress");
        stepTimer = tag.getInt("StepTimer");
        nextStepTicks = tag.getInt("NextStepTicks");

        for (int i = 0; i < input.getContainerSize(); i++) {
            input.setItem(i, ItemStack.EMPTY);
        }
        CompoundTag inTag = tag.getCompound("Input");
        for (String key : inTag.getAllKeys()) {
            CompoundTag t = inTag.getCompound(key);
            int slot = t.getInt("Slot");
            if (slot >= 0 && slot < input.getContainerSize()) {
                input.setItem(slot, ItemStack.of(t));
            }
        }

        output.setItem(0, tag.contains("Output") ? ItemStack.of(tag.getCompound("Output")) : ItemStack.EMPTY);
    }

    public void dropContents(ServerLevel level, BlockPos pos) {
        for (int i = 0; i < input.getContainerSize(); i++) {
            ItemStack s = input.getItem(i);
            if (!s.isEmpty()) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), s);
                input.setItem(i, ItemStack.EMPTY);
            }
        }

        if (isFinished()) {
            ItemStack out = output.getItem(0);
            if (!out.isEmpty()) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), out);
                output.setItem(0, ItemStack.EMPTY);
            }
        } else {
            output.setItem(0, ItemStack.EMPTY);
        }
    }

    public boolean tryConsumeInputs() {
        boolean hasAny = false;
        for (int i = 0; i < input.getContainerSize(); i++) {
            if (!input.getItem(i).isEmpty()) {
                hasAny = true;
                break;
            }
        }
        if (!hasAny) return false;

        for (int i = 0; i < input.getContainerSize(); i++) {
            input.removeItem(i, 1);
        }
        setChanged();
        return true;
    }

    public void onRecipeTaken() {
        this.progress = 0;
        this.stepTimer = 0;
        this.nextStepTicks = 0;

        this.lastHasRecipe = false;
        this.lastProgress = -1;

        this.setChanged();

        if (level instanceof ServerLevel sl) {
            ArmorCastingTableBlockProgressPacket.send(sl, worldPosition, 0, false);
        }
    }
}