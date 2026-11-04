package net.mirrorloong.chineseweapons.forge;

import java.util.Random;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mirrorloong.chineseweapons.ChineseWeapons;
import net.mirrorloong.chineseweapons.compat.WeaponScriptSettings;
import net.mirrorloong.chineseweapons.spi.GlaiveItem;

@Mod.EventBusSubscriber(modid = ChineseWeapons.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ForgeEvents {
    private static final Random RANDOM = new Random();

    private ForgeEvents() {
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity().getVehicle() == null) {
            return;
        }
        DamageSource source = event.getSource();
        if (!(source.getEntity() instanceof Player player)) {
            return;
        }
        Item item = player.getMainHandItem().getItem();
        if (!(item instanceof GlaiveItem)) {
            return;
        }
        float chance = WeaponScriptSettings.getChance(item, WeaponScriptSettings.Skill.GLAIVE_DISMOUNT, 0.60F);
        if (RANDOM.nextFloat() < chance) {
            event.getEntity().stopRiding();
        }
    }
}
