
package net.mirrorloong.chineseweapons.network;

import net.mirrorloong.chineseweapons.procedures.ArmorcastingtableguipreventgivingawayitemsforfreeProcedure;
import net.mirrorloong.chineseweapons.procedures.ArmorcastingtableguioutputsynthesisjudgmentProcedure;
import net.mirrorloong.chineseweapons.spi.ModPacket;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;

import java.util.HashMap;

public class ArmorcastingtableguiSlotMessage implements ModPacket {
	private final int slotID, x, y, z, changeType, meta;

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
		Player entity = sender;
		handleSlotAction(entity, slotID, changeType, meta, x, y, z);
	}

	public static void handleSlotAction(Player entity, int slot, int changeType, int meta, int x, int y, int z) {
		Level world = entity.level;
		// security measure to prevent arbitrary chunk generation
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
