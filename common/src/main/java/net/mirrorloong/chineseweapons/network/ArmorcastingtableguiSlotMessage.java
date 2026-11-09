package net.mirrorloong.chineseweapons.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.mirrorloong.chineseweapons.rules.CastingRules;
import net.mirrorloong.chineseweapons.spi.ModPacket;

/**
 * Server-bound slot click from the armour casting table GUI. Slots 1-10 warn
 * the player that the inputs are not free; slot 11 consumes the inputs when
 * the output is taken.
 */
public final class ArmorcastingtableguiSlotMessage implements ModPacket {
    private final int slotID;
    private final int x;
    private final int y;
    private final int z;
    private final int changeType;
    private final int meta;

    public ArmorcastingtableguiSlotMessage(int slotID, int x, int y, int z, int changeType, int meta) {
        this.slotID = slotID;
        this.x = x;
        this.y = y;
        this.z = z;
        this.changeType = changeType;
        this.meta = meta;
    }

    public ArmorcastingtableguiSlotMessage(FriendlyByteBuf buffer) {
        this.slotID = buffer.readInt();
        this.x = buffer.readInt();
        this.y = buffer.readInt();
        this.z = buffer.readInt();
        this.changeType = buffer.readInt();
        this.meta = buffer.readInt();
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(slotID);
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        buffer.writeInt(changeType);
        buffer.writeInt(meta);
    }

    @Override
    public void handle(ServerPlayer sender) {
        handleSlotAction(sender, slotID, changeType, meta, x, y, z);
    }

    public static void handleSlotAction(Player entity, int slot, int changeType, int meta, int x, int y, int z) {
        Level world = net.mirrorloong.chineseweapons.ChineseWeapons.gameplay().level(entity);
        // security measure to prevent arbitrary chunk generation
        if (!world.hasChunkAt(new BlockPos(x, y, z))) {
            return;
        }

        if (slot >= 1 && slot <= 10 && (changeType == 1 || changeType == 2)) {
            CastingRules.clearOutput(entity);
        }
        if (slot == 11 && (changeType == 1 || changeType == 2)) {
            CastingRules.consumeInputs(entity);
        }
    }
}
