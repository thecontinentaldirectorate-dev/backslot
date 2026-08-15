package com.backslot;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * One-slot view over a player's back slot. Nothing is stored here — the stack lives in the
 * player's synched data so it reaches every client that can see them, not just the owner.
 */
public class BackContainer implements Container {
	private final Player owner;

	public BackContainer(Player owner) {
		this.owner = owner;
	}

	private BackSlotHolder holder() {
		return (BackSlotHolder) owner;
	}

	@Override
	public int getContainerSize() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return holder().backslot$getItem().isEmpty();
	}

	@Override
	public ItemStack getItem(int slot) {
		return slot == 0 ? holder().backslot$getItem() : ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeItem(int slot, int count) {
		ItemStack current = getItem(slot);
		if (current.isEmpty()) {
			return ItemStack.EMPTY;
		}

		ItemStack taken = current.split(count);
		holder().backslot$setItem(current);
		return taken;
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		if (slot != 0) {
			return ItemStack.EMPTY;
		}

		ItemStack current = holder().backslot$getItem();
		holder().backslot$setItem(ItemStack.EMPTY);
		return current;
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		if (slot == 0) {
			holder().backslot$setItem(stack);
		}
	}

	@Override
	public void setChanged() {
		// getItem() hands out the live stack and callers edit it in place, which the data
		// tracker can't see. Push it back through to force the sync.
		holder().backslot$setItem(holder().backslot$getItem());
	}

	@Override
	public boolean stillValid(Player player) {
		return player == owner && !owner.isRemoved();
	}

	@Override
	public void clearContent() {
		holder().backslot$setItem(ItemStack.EMPTY);
	}
}
