package net.mirrorloong.chineseweapons.network;

import net.mirrorloong.chineseweapons.procedures.ArmorcastingtableguipreventgivingawayitemsforfreeProcedure;
import net.mirrorloong.chineseweapons.procedures.ArmorcastingtableguioutputsynthesisjudgmentProcedure;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;

public class ArmorcastingtableguiSlotMessage implements ModPacket {

	public static final int ID = 2;

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
	public int id() {
		return ID;
	}

	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.slotID);
		buffer.writeInt(this.x);
		buffer.writeInt(this.y);
		buffer.writeInt(this.z);
		buffer.writeInt(this.changeType);
		buffer.writeInt(this.meta);
	}

		public void handle(Player entity) {
		int slotID = this.slotID;
		int changeType = this.changeType;
		int meta = this.meta;
		int x = this.x;
		int y = this.y;
		int z = this.z;
		handleSlotAction(entity, slotID, changeType, meta, x, y, z);
	}

	public static void handleSlotAction(Player entity, int slot, int changeType, int meta, int x, int y, int z) {
		Level world = entity.level();
				if (!world.hasChunkAt(new BlockPos(x, y, z)))
			return;
		if (slot >= 1 && slot <= 10 && changeType >= 1 && changeType <= 2) {
			ArmorcastingtableguipreventgivingawayitemsforfreeProcedure.execute(entity);
		}
		if (slot == 11 && changeType >= 1 && changeType <= 2) {
			ArmorcastingtableguioutputsynthesisjudgmentProcedure.execute(entity);
		}
	}
}
