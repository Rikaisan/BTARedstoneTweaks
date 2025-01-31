package com.rikaisan.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.container.ScreenActivator;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.block.entity.TileEntityActivator;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.sound.SoundCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(value = ScreenActivator.class, remap = false)
public abstract class ScreenActivatorMixin extends ScreenContainerAbstract {

	@Final
	@Shadow
	protected List<ButtonElement> slotButtons;

	@Final
	@Shadow
	protected TileEntityActivator tileEntityActivator;

	public ScreenActivatorMixin(MenuAbstract container) {
		super(container);
	}

	/**
	 * @author Rikai
	 * @reason Allow left-clicking to lock slots
	 */
	@Overwrite
	public void clickInventory(int x, int y, int mouseButton) {
		if ((mouseButton == 0 || mouseButton == 1) && this.mc.thePlayer.inventory.getHeldItemStack() == null) {
			for (ButtonElement button : this.slotButtons) {
				if (this.tileEntityActivator.getItem(button.id) == null && button.mouseClicked(this.mc, x, y)) {
					this.mc.playerController.handleInventoryMouseClick(this.inventorySlots.containerId, InventoryAction.LOCK, new int[]{button.id, 0}, this.mc.thePlayer);
					if (button.playSound) {
						this.mc.sndManager.playSound("random.click", SoundCategory.GUI_SOUNDS, 1.0F, 1.0F);
					}
					break;
				}
			}
		}

		super.clickInventory(x, y, mouseButton);
	}
}
