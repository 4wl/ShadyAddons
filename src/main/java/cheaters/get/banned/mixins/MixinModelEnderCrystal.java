/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelEnderCrystal
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 */
package cheaters.get.banned.mixins;

import cheaters.get.banned.Shady;
import cheaters.get.banned.features.CrystalReach;
import cheaters.get.banned.utils.RenderUtils;
import java.awt.Color;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ModelEnderCrystal.class})
public abstract class MixinModelEnderCrystal {
    @Inject(method={"render"}, at={@At(value="HEAD")})
    public void preRender(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale, CallbackInfo ci) {
        if (CrystalReach.isEnabled()) {
            GlStateManager.disableTexture2D();
            GlStateManager.disableDepth();
            GlStateManager.disableCull();
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
            RenderUtils.bindColor(entityIn.equals((Object)CrystalReach.crystal) && Shady.mc.thePlayer.isSneaking() ? Color.MAGENTA : Color.WHITE);
        }
    }

    @Inject(method={"render"}, at={@At(value="RETURN")})
    public void postRender(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale, CallbackInfo ci) {
        if (CrystalReach.isEnabled()) {
            GlStateManager.enableTexture2D();
            GlStateManager.enableDepth();
            GlStateManager.enableCull();
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
        }
    }
}

