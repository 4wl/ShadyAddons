/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraftforge.client.event.GuiOpenEvent
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$ElementType
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$Post
 *  net.minecraftforge.event.world.WorldEvent$Load
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.InputEvent$KeyInputEvent
 */
package cheaters.get.banned.features;

import cheaters.get.banned.Shady;
import cheaters.get.banned.config.Config;
import cheaters.get.banned.config.components.ConfigInput;
import cheaters.get.banned.utils.KeybindUtils;
import cheaters.get.banned.utils.ThreadUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class AutoClicker {
    private static boolean toggled = false;
    private static boolean burstActive = false;

    public AutoClicker() {
        KeybindUtils.register("Auto Clicker", 21);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (KeybindUtils.get("Auto Clicker").isPressed()) {
            if (Config.autoClickerMode == 0 && !burstActive) {
                burstActive = true;
                new Thread(() -> {
                    for (int i = 0; i < 25 && burstActive; ++i) {
                        KeybindUtils.rightClick();
                        ThreadUtils.sleep(1000 / Config.autoClickerCps);
                    }
                    burstActive = false;
                }, "ShadyAddons-Autoclicker").start();
            } else if (Config.autoClickerMode == 1) {
                boolean bl = toggled = !toggled;
                if (toggled) {
                    new Thread(() -> {
                        while (toggled) {
                            KeybindUtils.rightClick();
                            ThreadUtils.sleep(1000 / Config.autoClickerCps);
                        }
                    }, "ShadyAddons-Autoclicker").start();
                }
            }
        }
    }

    @SubscribeEvent
    public void onOpenGui(GuiOpenEvent event) {
        if (Config.stopAutoClickerInGui) {
            toggled = false;
            burstActive = false;
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (toggled && Config.autoClickerIndicator && event.type == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            int x = new ScaledResolution(Shady.mc).getScaledWidth() / 2 + 10;
            int y = new ScaledResolution(Shady.mc).getScaledHeight() / 2 - 2;
            Gui.drawRect((int)x, (int)y, (int)(x + 5), (int)(y + 5), (int)ConfigInput.red.getRGB());
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        toggled = false;
        burstActive = false;
    }
}

