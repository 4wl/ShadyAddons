/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.texture.TextureManager
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package cheaters.get.banned.mixins;

import cheaters.get.banned.events.ClickEvent;
import cheaters.get.banned.events.ResourcePackRefreshEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Minecraft.class})
public abstract class MixinMinecraft {
    @Mutable
    @Shadow
    @Final
    private static ResourceLocation locationMojangPng;

    @Inject(method={"drawSplashScreen"}, at={@At(value="HEAD")})
    public void modifyMojangLogo(TextureManager textureManagerInstance, CallbackInfo ci) {
        locationMojangPng = new ResourceLocation("shadyaddons:splash.png");
    }

    @Inject(method={"refreshResources"}, at={@At(value="HEAD")}, cancellable=true)
    public void refreshResourcesPre(CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post((Event)new ResourcePackRefreshEvent.Pre())) {
            ci.cancel();
        }
    }

    @Inject(method={"refreshResources"}, at={@At(value="RETURN")})
    public void refreshResourcesPost(CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post((Event)new ResourcePackRefreshEvent.Post());
    }

    @Inject(method={"rightClickMouse"}, at={@At(value="HEAD")}, cancellable=true)
    public void rightClickEvent(CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post((Event)new ClickEvent.Right())) {
            ci.cancel();
        }
    }

    @Inject(method={"clickMouse"}, at={@At(value="HEAD")}, cancellable=true)
    public void leftClickEvent(CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post((Event)new ClickEvent.Left())) {
            ci.cancel();
        }
    }

    @Inject(method={"middleClickMouse"}, at={@At(value="HEAD")}, cancellable=true)
    public void middleClickEvent(CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post((Event)new ClickEvent.Middle())) {
            ci.cancel();
        }
    }
}

