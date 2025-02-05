package com.rikaisan.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
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
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
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

	@Definition(id = "mouseButton", local = @Local(type = int.class, ordinal = 2, argsOnly = true))
	@Expression("mouseButton == 1")
	@ModifyExpressionValue(method = "clickInventory", at = @At("MIXINEXTRAS:EXPRESSION"))
	public boolean a(boolean original) {
		return false;
	}

	@Inject(method = "clickInventory(III)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/container/ScreenContainerAbstract;clickInventory(III)V"), cancellable = true)
	public void clickInventory(int x, int y, int mouseButton, CallbackInfo ci) {
		if (mouseButton == 2) {
			List<ButtonElement> lockedSlots = new ArrayList<>();
			List<ButtonElement> unlockedSlots = new ArrayList<>();
			for (ButtonElement button : this.slotButtons) {
				if (this.tileEntityActivator.getItem(button.id) != null) continue;
				if (this.tileEntityActivator.locked(button.id)) {
					lockedSlots.add(button);
				} else {
					unlockedSlots.add(button);
				}
			}

			if (unlockedSlots.isEmpty()) {
				for (ButtonElement button : lockedSlots) {
					this.tileEntityActivator.lockSlot(button.id, false);
				}
			} else {
				for (ButtonElement button : unlockedSlots) {
					this.tileEntityActivator.lockSlot(button.id, true);
				}
			}

			this.mc.sndManager.playSound("random.click", SoundCategory.GUI_SOUNDS, 1.0F, 1.0F);
			ci.cancel();
		}

		if (mouseButton == 0 || mouseButton == 1) {
			for (ButtonElement button : this.slotButtons) {
				if (button.mouseClicked(this.mc, x, y) && (this.tileEntityActivator.locked(button.id) || (this.tileEntityActivator.getItem(button.id) == null && this.mc.thePlayer.inventory.getHeldItemStack() == null))) {
					this.mc.playerController.handleInventoryMouseClick(this.inventorySlots.containerId, InventoryAction.LOCK, new int[]{button.id, 0}, this.mc.thePlayer);
					if (button.playSound) {
						this.mc.sndManager.playSound("random.click", SoundCategory.GUI_SOUNDS, 1.0F, 1.0F);
					}
					ci.cancel();
				}
			}
		}
	}
}
