package com.backslot;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;

public class BackItemSlot extends Slot {
	private final Player owner;

	public BackItemSlot(Container container, Player owner, int x, int y) {
		super(container, 0, x, y);
		this.owner = owner;
	}

	// isActive gates drawing, the hover test and therefore clicking, so this is enough to
	// take the slot out of play on a server that doesn't know about it. The server's own
	// copy stays active either way.
	@Override
	public boolean isActive() {
		return !owner.level().isClientSide() || BackSlotSync.serverHasMod();
	}
}
