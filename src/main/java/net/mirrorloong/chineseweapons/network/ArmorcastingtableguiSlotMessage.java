package net.mirrorloong.chineseweapons.network;

import net.mirrorloong.chineseweapons.procedures.ArmorcastingtableguipreventgivingawayitemsforfreeProcedure;
import net.mirrorloong.chineseweapons.procedures.ArmorcastingtableguioutputsynthesisjudgmentProcedure;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ArmorcastingtableguiSlotMessage(int slotID, int x, int y, int z, int changeType, int meta) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ArmorcastingtableguiSlotMessage> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("chineseweapons", "armorcastingtablegui_slot_message"));

    public static final StreamCodec<FriendlyByteBuf, ArmorcastingtableguiSlotMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ArmorcastingtableguiSlotMessage::slotID,
            ByteBufCodecs.INT,
            ArmorcastingtableguiSlotMessage::x,
            ByteBufCodecs.INT,
            ArmorcastingtableguiSlotMessage::y,
            ByteBufCodecs.INT,
            ArmorcastingtableguiSlotMessage::z,
            ByteBufCodecs.INT,
            ArmorcastingtableguiSlotMessage::changeType,
            ByteBufCodecs.INT,
            ArmorcastingtableguiSlotMessage::meta,
            ArmorcastingtableguiSlotMessage::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ArmorcastingtableguiSlotMessage packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player entity = context.player();
            handleSlotAction(entity, packet.slotID(), packet.changeType(), packet.meta(), packet.x(), packet.y(), packet.z());
        });
    }

    public static void handleSlotAction(Player entity, int slot, int changeType, int meta, int x, int y, int z) {
        Level world = entity.level();
        if (!world.hasChunkAt(new BlockPos(x, y, z)))
            return;
        if (slot == 1 && changeType == 1) {

            ArmorcastingtableguipreventgivingawayitemsforfreeProcedure.execute(entity);
        }
        if (slot == 1 && changeType == 2) {
            int amount = meta;

            ArmorcastingtableguipreventgivingawayitemsforfreeProcedure.execute(entity);
        }
        if (slot == 2 && changeType == 1) {

            ArmorcastingtableguipreventgivingawayitemsforfreeProcedure.execute(entity);
        }
        if (slot == 2 && changeType == 2) {
            int amount = meta;

            ArmorcastingtableguipreventgivingawayitemsforfreeProcedure.execute(entity);
        }
        if (slot == 3 && changeType == 1) {

            ArmorcastingtableguipreventgivingawayitemsforfreeProcedure.execute(entity);
        }
        if (slot == 3 && changeType == 2) {
            int amount = meta;

            ArmorcastingtableguipreventgivingawayitemsforfreeProcedure.execute(entity);
        }
        if (slot == 4 && changeType == 1) {

            ArmorcastingtableguipreventgivingawayitemsforfreeProcedure.execute(entity);
        }
        if (slot == 4 && changeType == 2) {
            int amount = meta;

            ArmorcastingtableguipreventgivingawayitemsforfreeProcedure.execute(entity);
        }
        if (slot == 5 && changeType == 1) {

            ArmorcastingtableguipreventgivingawayitemsforfreeProcedure.execute(entity);
        }
        if (slot == 5 && changeType == 2) {
            int amount = meta;

            ArmorcastingtableguipreventgivingawayitemsforfreeProcedure.execute(entity);
        }
        if (slot == 6 && changeType == 1) {

            ArmorcastingtableguipreventgivingawayitemsforfreeProcedure.execute(entity);
        }
        if (slot == 6 && changeType == 2) {
            int amount = meta;

            ArmorcastingtableguipreventgivingawayitemsforfreeProcedure.execute(entity);
        }
        if (slot == 7 && changeType == 1) {

            ArmorcastingtableguipreventgivingawayitemsforfreeProcedure.execute(entity);
        }
        if (slot == 7 && changeType == 2) {
            int amount = meta;

            ArmorcastingtableguipreventgivingawayitemsforfreeProcedure.execute(entity);
        }
        if (slot == 8 && changeType == 1) {

            ArmorcastingtableguipreventgivingawayitemsforfreeProcedure.execute(entity);
        }
        if (slot == 8 && changeType == 2) {
            int amount = meta;

            ArmorcastingtableguipreventgivingawayitemsforfreeProcedure.execute(entity);
        }
        if (slot == 9 && changeType == 1) {

            ArmorcastingtableguipreventgivingawayitemsforfreeProcedure.execute(entity);
        }
        if (slot == 9 && changeType == 2) {
            int amount = meta;

            ArmorcastingtableguipreventgivingawayitemsforfreeProcedure.execute(entity);
        }
        if (slot == 10 && changeType == 1) {

            ArmorcastingtableguipreventgivingawayitemsforfreeProcedure.execute(entity);
        }
        if (slot == 10 && changeType == 2) {
            int amount = meta;

            ArmorcastingtableguipreventgivingawayitemsforfreeProcedure.execute(entity);
        }
        if (slot == 11 && changeType == 1) {

            ArmorcastingtableguioutputsynthesisjudgmentProcedure.execute(entity);
        }
        if (slot == 11 && changeType == 2) {
            int amount = meta;

            ArmorcastingtableguioutputsynthesisjudgmentProcedure.execute(entity);
        }
    }
}
