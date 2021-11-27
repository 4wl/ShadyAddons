/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.world.World
 *  net.minecraftforge.event.world.WorldEvent$Load
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.InputEvent$KeyInputEvent
 */
package cheaters.get.banned.features;

import cheaters.get.banned.Shady;
import cheaters.get.banned.config.Config;
import cheaters.get.banned.events.ClickEvent;
import cheaters.get.banned.utils.KeybindUtils;
import cheaters.get.banned.utils.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class ItemMacro {
    private static boolean sentMissingSoulWhipMessage = false;

    public ItemMacro() {
        KeybindUtils.register("Use Ice Spray", 0);
        KeybindUtils.register("Use Power Orb", 0);
        KeybindUtils.register("Use Weird Tuba", 0);
        KeybindUtils.register("Use Gyrokinetic Wand", 0);
        KeybindUtils.register("Use Pigman Sword", 0);
        KeybindUtils.register("Use Healing Wand", 0);
        KeybindUtils.register("Use Rogue Sword", 0);
        KeybindUtils.register("Use Fishing Rod", 0);
    }

    @SubscribeEvent
    public void onLeftCLick(ClickEvent.Left event) {
        if (Config.soulWhipWithAnything && !ItemMacro.useSkyBlockItem("SOUL_WHIP", true) && !sentMissingSoulWhipMessage) {
            ItemMacro.sendMissingItemMessage("Soul Whip");
            sentMissingSoulWhipMessage = true;
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        sentMissingSoulWhipMessage = false;
    }

    @SubscribeEvent
    public void onInput(InputEvent.KeyInputEvent event) {
        if (Config.iceSprayHotkey && KeybindUtils.isPressed("Use Ice Spray") && !ItemMacro.useSkyBlockItem("ICE_SPRAY_WAND", true)) {
            ItemMacro.sendMissingItemMessage("Ice Spray Wand");
        }
        if (Config.powerOrbHotkey && KeybindUtils.isPressed("Use Power Orb") && !ItemMacro.useSkyBlockItem("PLASMAFLUX_POWER_ORB", true) && !ItemMacro.useSkyBlockItem("OVERFLUX_POWER_ORB", true) && !ItemMacro.useSkyBlockItem("MANA_FLUX_POWER_ORB", true) && !ItemMacro.useSkyBlockItem("RADIANT_POWER_ORB", true)) {
            ItemMacro.sendMissingItemMessage("Power Orb");
        }
        if (Config.weirdTubaHotkey && KeybindUtils.isPressed("Use Weird Tuba") && !ItemMacro.useSkyBlockItem("WEIRD_TUBA", true)) {
            ItemMacro.sendMissingItemMessage("Weird Tuba");
        }
        if (Config.gyrokineticWandHotkey && KeybindUtils.isPressed("Use Gyrokinetic Wand") && !ItemMacro.useSkyBlockItem("GYROKINETIC_WAND", false)) {
            ItemMacro.sendMissingItemMessage("Gyrokinetic Wand");
        }
        if (Config.pigmanSwordHotkey && KeybindUtils.isPressed("Use Pigman Sword") && !ItemMacro.useSkyBlockItem("PIGMAN_SWORD", true)) {
            ItemMacro.sendMissingItemMessage("Pigman Sword");
        }
        if (Config.healingWandHotkey && KeybindUtils.isPressed("Use Healing Wand") && !ItemMacro.useSkyBlockItem("WAND_OF_ATONEMENT", true) && !ItemMacro.useSkyBlockItem("WAND_OF_RESTORATION", true) && !ItemMacro.useSkyBlockItem("WAND_OF_MENDING", true) && !ItemMacro.useSkyBlockItem("WAND_OF_HEALING", true)) {
            ItemMacro.sendMissingItemMessage("Healing Wand");
        }
        if (Config.rogueSwordHotkey && KeybindUtils.isPressed("Use Rogue Sword") && !ItemMacro.useRogueSword()) {
            ItemMacro.sendMissingItemMessage("Rogue Sword");
        }
        if (Config.fishingRodHotkey && KeybindUtils.isPressed("Use Fishing Rod") && !ItemMacro.useVanillaItem((Item)Items.FISHING_ROD)) {
            ItemMacro.sendMissingItemMessage("Fishing Rod");
        }
    }

    public static boolean useSkyBlockItem(String itemId, boolean rightClick) {
        for (int i = 0; i < 9; ++i) {
            ItemStack item = Shady.mc.player.inventory.getStackInSlot(i);
            if (!itemId.equals(Utils.getSkyBlockID(item))) continue;
            int previousItem = Shady.mc.player.inventory.currentItem;
            Shady.mc.player.inventory.currentItem = i;
            if (rightClick) {
                Shady.mc.playerController.func_78769_a((EntityPlayer)Shady.mc.player, (World)Shady.mc.world, item);
            } else {
                KeybindUtils.leftClick();
            }
            Shady.mc.player.inventory.currentItem = previousItem;
            return true;
        }
        return false;
    }

    public static boolean useRogueSword() {
        for (int i = 0; i < 9; ++i) {
            ItemStack item = Shady.mc.player.inventory.getStackInSlot(i);
            if (!"ROGUE_SWORD".equals(Utils.getSkyBlockID(item))) continue;
            int previousItem = Shady.mc.player.inventory.currentItem;
            Shady.mc.player.inventory.currentItem = i;
            for (int j = 0; j < 40; ++j) {
                Shady.mc.playerController.func_78769_a((EntityPlayer)Shady.mc.player, (World)Shady.mc.world, item);
            }
            Shady.mc.player.inventory.currentItem = previousItem;
            return true;
        }
        return false;
    }

    public static boolean useVanillaItem(Item item) {
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = Shady.mc.player.inventory.getStackInSlot(i);
            if (itemStack == null || itemStack.getItem() != item) continue;
            int previousItem = Shady.mc.player.inventory.currentItem;
            Shady.mc.player.inventory.currentItem = i;
            Shady.mc.playerController.func_78769_a((EntityPlayer)Shady.mc.player, (World)Shady.mc.world, itemStack);
            Shady.mc.player.inventory.currentItem = previousItem;
            return true;
        }
        return false;
    }

    private static void sendMissingItemMessage(String itemName) {
        Utils.sendModMessage("No &9" + itemName + "&r found in your hotbar");
    }
}

