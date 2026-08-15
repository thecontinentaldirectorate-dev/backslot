package com.backslot;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

/** Mixed into {@link net.minecraft.world.entity.player.Player}; cast a player to this. */
public interface BackSlotHolder {
	ItemStack backslot$getItem();

	void backslot$setItem(ItemStack stack);

	Container backslot$container();
}
