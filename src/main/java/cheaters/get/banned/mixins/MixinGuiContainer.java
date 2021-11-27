/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.inventory.Container
 *  net.minecraft.inventory.Slot
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package cheaters.get.banned.mixins;

import cheaters.get.banned.events.DrawSlotEvent;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiContainer.class})
public abstract class MixinGuiContainer {
    @Shadow
    public Container inventorySlots;

    @Inject(method={"drawSlot"}, at={@At(value="HEAD")}, cancellable=true)
    private void beforeDrawSlot(Slot slot, CallbackInfo callbackInfo) {
        if (MinecraftForge.EVENT_BUS.post((Event)new DrawSlotEvent(this.inventorySlots, slot))) {
            callbackInfo.cancel();
        }
    }
}

