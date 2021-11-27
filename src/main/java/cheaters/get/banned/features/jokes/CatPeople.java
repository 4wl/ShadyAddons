/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.resources.ResourcePackRepository$Entry
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$ElementType
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$Pre
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  org.apache.commons.io.FileUtils
 */
package cheaters.get.banned.features.jokes;

import cheaters.get.banned.Shady;
import cheaters.get.banned.config.Config;
import cheaters.get.banned.events.ResourcePackRefreshEvent;
import cheaters.get.banned.events.TickEndEvent;
import cheaters.get.banned.remote.Analytics;
import cheaters.get.banned.utils.ArrayUtils;
import cheaters.get.banned.utils.MathUtils;
import cheaters.get.banned.utils.ReflectionUtils;
import cheaters.get.banned.utils.RenderUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.io.FileUtils;

public class CatPeople {
    public static ArrayList<CatPerson> catPeople = new ArrayList();
    private static ArrayList<ResourceLocation> images = new ArrayList();
    public static boolean usingPack = false;
    private int counter = 0;

    public static void load() {
        usingPack = false;
        catPeople.clear();
        for (ResourcePackRepository.Entry pack : Shady.mc.getResourcePackRepository().getRepositoryEntries()) {
            Set domains = pack.getResourcePack().getResourceDomains();
            if (domains == null || !domains.contains("shadyaddons")) continue;
            System.out.println("RESOURCE PACK FOUND");
            File directory = (File)ReflectionUtils.field((Object)pack, "resourcePackFile");
            if (directory == null) continue;
            System.out.println("DIRECTORY FOUND " + directory.getAbsolutePath());
            Collection images = FileUtils.listFiles((File)directory, (String[])new String[]{"png"}, (boolean)true);
            images.removeIf(image -> image.getName().equals("pack.png"));
            CatPeople.images.clear();
            for (File image2 : images) {
                CatPeople.images.add(new ResourceLocation("shadyaddons", image2.getParentFile().getName() + "/" + image2.getName()));
            }
            System.out.println("IMAGES " + images);
            usingPack = true;
            return;
        }
        Config.catGirls = false;
    }

    public static void addRandomCatPerson(int type) {
        CatPerson catPerson = new CatPerson();
        catPerson.percentage = (float)MathUtils.random(10, 80) / 100.0f;
        Collections.shuffle(images);
        switch (type) {
            case 0: {
                catPerson.image = ArrayUtils.getFirstMatch(images, image -> image.getPath().contains("catgirl"));
                break;
            }
            case 1: {
                catPerson.image = ArrayUtils.getFirstMatch(images, image -> image.getPath().contains("catboy"));
                break;
            }
            case 2: {
                CatPeople.addRandomCatPerson(MathUtils.random(0, 1));
                return;
            }
            case 3: {
                catPerson.image = ArrayUtils.getFirstMatch(images, image -> image.getPath().contains("realcat"));
            }
        }
        catPerson.side = CatPerson.Side.values()[MathUtils.random(0, 3)];
        catPerson.size = MathUtils.random(75, 200);
        catPeople.add(catPerson);
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (Config.catGirls && usingPack) {
            if (this.counter % 20 == 0) {
                if (MathUtils.random(1, 5) == 5) {
                    CatPeople.addRandomCatPerson(Config.catGirlsMode);
                }
                this.counter = 0;
            }
            ++this.counter;
        } else {
            catPeople.clear();
        }
    }

    @SubscribeEvent
    public void onResourcePackRefresh(ResourcePackRefreshEvent.Post event) {
        for (ResourcePackRepository.Entry pack : Shady.mc.getResourcePackRepository().getRepositoryEntries()) {
            Set resourceDomains = pack.getResourcePack().getResourceDomains();
            if (resourceDomains == null || !resourceDomains.contains("shadyaddons")) continue;
            CatPeople.load();
            Analytics.collect("using_pack", usingPack ? "1" : "0");
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.type == RenderGameOverlayEvent.ElementType.HOTBAR && Config.catGirls) {
            ScaledResolution scaledResolution = new ScaledResolution(Shady.mc);
            for (CatPerson catPerson : catPeople) {
                int x = 0;
                int y = 0;
                int angle = 0;
                switch (catPerson.side) {
                    case BOTTOM: {
                        x = (int)((float)scaledResolution.getScaledWidth() * catPerson.percentage);
                        y = scaledResolution.getScaledHeight() - catPerson.size;
                        break;
                    }
                    case TOP: {
                        x = (int)((float)scaledResolution.getScaledWidth() * catPerson.percentage);
                        y = 0;
                        angle = 180;
                        break;
                    }
                    case RIGHT: {
                        x = scaledResolution.getScaledWidth() - catPerson.size;
                        y = (int)((float)scaledResolution.getScaledHeight() * catPerson.percentage);
                        angle = -90;
                        break;
                    }
                    case LEFT: {
                        x = 0;
                        y = (int)((float)scaledResolution.getScaledHeight() * catPerson.percentage);
                        angle = 90;
                    }
                }
                RenderUtils.drawRotatedTexture(catPerson.image, x, y, catPerson.size, catPerson.size, angle);
            }
        }
    }

    private static class CatPerson {
        float percentage;
        Side side;
        ResourceLocation image;
        int size;

        static enum Side {
            TOP,
            BOTTOM,
            LEFT,
            RIGHT;

        }
    }
}

