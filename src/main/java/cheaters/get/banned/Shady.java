/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiMainMenu
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.command.ICommand
 *  net.minecraftforge.client.ClientCommandHandler
 *  net.minecraftforge.client.event.ClientChatReceivedEvent
 *  net.minecraftforge.client.event.GuiOpenEvent
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.client.registry.ClientRegistry
 *  net.minecraftforge.fml.common.Loader
 *  net.minecraftforge.fml.common.Mod
 *  net.minecraftforge.fml.common.Mod$EventHandler
 *  net.minecraftforge.fml.common.event.FMLInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLPostInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLPreInitializationEvent
 *  net.minecraftforge.fml.common.eventhandler.EventPriority
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cheaters.get.banned;

import cheaters.get.banned.config.Config;
import cheaters.get.banned.config.ConfigLogic;
import cheaters.get.banned.config.MainCommand;
import cheaters.get.banned.config.settings.BooleanSetting;
import cheaters.get.banned.config.settings.SelectSetting;
import cheaters.get.banned.config.settings.Setting;
import cheaters.get.banned.events.TickEndEvent;
import cheaters.get.banned.features.AbilityKeybind;
import cheaters.get.banned.features.AutoClicker;
import cheaters.get.banned.features.AutoCloseChest;
import cheaters.get.banned.features.AutoGG;
import cheaters.get.banned.features.AutoMelody;
import cheaters.get.banned.features.AutoReadyUp;
import cheaters.get.banned.features.AutoRenewCrystalHollows;
import cheaters.get.banned.features.AutoSalvage;
import cheaters.get.banned.features.AutoSell;
import cheaters.get.banned.features.AutoSimonSays;
import cheaters.get.banned.features.AutoTerminals;
import cheaters.get.banned.features.BlockAbilities;
import cheaters.get.banned.features.CrystalReach;
import cheaters.get.banned.features.DisableSwordAnimation;
import cheaters.get.banned.features.GemstoneESP;
import cheaters.get.banned.features.GhostBlocks;
import cheaters.get.banned.features.HideSummons;
import cheaters.get.banned.features.ItemMacro;
import cheaters.get.banned.features.MobESP;
import cheaters.get.banned.features.RoyalPigeonMacro;
import cheaters.get.banned.features.ShowHiddenEntities;
import cheaters.get.banned.features.SocialCommandSolver;
import cheaters.get.banned.features.StonklessStonk;
import cheaters.get.banned.features.TeleportWithAnything;
import cheaters.get.banned.features.connectfoursolver.ConnectFourSolver;
import cheaters.get.banned.features.dungeonmap.DungeonMap;
import cheaters.get.banned.features.dungeonmap.DungeonScanner;
import cheaters.get.banned.features.dungeonmap.RoomLoader;
import cheaters.get.banned.features.jokes.ByeEntities;
import cheaters.get.banned.features.jokes.CatPeople;
import cheaters.get.banned.features.jokes.FakeBan;
import cheaters.get.banned.features.jokes.MissingItem;
import cheaters.get.banned.remote.Analytics;
import cheaters.get.banned.remote.CrashReporter;
import cheaters.get.banned.remote.MayorAPI;
import cheaters.get.banned.remote.UpdateGui;
import cheaters.get.banned.remote.Updater;
import cheaters.get.banned.utils.DungeonUtils;
import cheaters.get.banned.utils.KeybindUtils;
import cheaters.get.banned.utils.LocationUtils;
import cheaters.get.banned.utils.Utils;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.ICommand;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid="autogg", name="ShadyAddons", version="4.1.3", clientSideOnly=true, acceptedMinecraftVersions="[1.8.9]")
public class Shady {
    public static final String MOD_NAME = "ShadyAddons";
    public static final String MOD_ID = "autogg";
    public static final String VERSION = "2.2.3";
    public static final boolean BETA = "2.2.3".contains("-pre") || "2.2.3".equals("@VERSION@");
    public static final Minecraft mc = Minecraft.getMinecraft();
    public static boolean usingSkyBlockAddons = false;
    public static boolean usingPatcher = false;
    public static boolean usingSkytils = false;
    public static GuiScreen guiToOpen = null;
    public static boolean enabled = true;
    private static boolean sentPlayTimeCommand = false;
    private static boolean sentPlayTimeData = false;
    private static Pattern playTimePattern = Pattern.compile("You have (\\d*) hours and \\d* minutes playtime!");
    public static ArrayList<Setting> settings = ConfigLogic.collect(Config.class);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand((ICommand)new MainCommand());
        ConfigLogic.load();
        RoomLoader.load();
        Updater.check();
        MayorAPI.fetch();
        Analytics.collect("version", VERSION);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register((Object)new TickEndEvent());
        MinecraftForge.EVENT_BUS.register((Object)this);
        MinecraftForge.EVENT_BUS.register((Object)new Utils());
        MinecraftForge.EVENT_BUS.register((Object)new LocationUtils());
        MinecraftForge.EVENT_BUS.register((Object)new DungeonUtils());
        MinecraftForge.EVENT_BUS.register((Object)new DungeonScanner());
        MinecraftForge.EVENT_BUS.register((Object)new BlockAbilities());
        MinecraftForge.EVENT_BUS.register((Object)new StonklessStonk());
        MinecraftForge.EVENT_BUS.register((Object)new GhostBlocks());
        MinecraftForge.EVENT_BUS.register((Object)new AutoCloseChest());
        MinecraftForge.EVENT_BUS.register((Object)new RoyalPigeonMacro());
        MinecraftForge.EVENT_BUS.register((Object)new AutoGG());
        MinecraftForge.EVENT_BUS.register((Object)new AutoSimonSays());
        MinecraftForge.EVENT_BUS.register((Object)new AbilityKeybind());
        MinecraftForge.EVENT_BUS.register((Object)new AutoClicker());
        MinecraftForge.EVENT_BUS.register((Object)new AutoRenewCrystalHollows());
        MinecraftForge.EVENT_BUS.register((Object)new DisableSwordAnimation());
        MinecraftForge.EVENT_BUS.register((Object)new ShowHiddenEntities());
        MinecraftForge.EVENT_BUS.register((Object)new HideSummons());
        MinecraftForge.EVENT_BUS.register((Object)new TeleportWithAnything());
        MinecraftForge.EVENT_BUS.register((Object)new ItemMacro());
        MinecraftForge.EVENT_BUS.register((Object)new MobESP());
        MinecraftForge.EVENT_BUS.register((Object)new GemstoneESP());
        MinecraftForge.EVENT_BUS.register((Object)new AutoTerminals());
        MinecraftForge.EVENT_BUS.register((Object)new AutoMelody());
        MinecraftForge.EVENT_BUS.register((Object)new DungeonMap());
        MinecraftForge.EVENT_BUS.register((Object)new CatPeople());
        MinecraftForge.EVENT_BUS.register((Object)new AutoReadyUp());
        MinecraftForge.EVENT_BUS.register((Object)new CrystalReach());
        MinecraftForge.EVENT_BUS.register((Object)new AutoSalvage());
        MinecraftForge.EVENT_BUS.register((Object)new AutoSell());
        MinecraftForge.EVENT_BUS.register((Object)new SocialCommandSolver());
        MinecraftForge.EVENT_BUS.register((Object)new ConnectFourSolver());
        MinecraftForge.EVENT_BUS.register((Object)new FakeBan());
        MinecraftForge.EVENT_BUS.register((Object)new ByeEntities());
        MinecraftForge.EVENT_BUS.register((Object)new MissingItem());
        for (KeyBinding keyBinding : KeybindUtils.keyBindings.values()) {
            ClientRegistry.registerKeyBinding((KeyBinding)keyBinding);
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        usingSkyBlockAddons = Loader.isModLoaded((String)"skyblockaddons");
        usingPatcher = Loader.isModLoaded((String)"patcher");
        usingSkytils = Loader.isModLoaded((String)"skytils");
        Analytics.collect("hash", CrashReporter.hashMod());
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (guiToOpen != null) {
            mc.displayGuiScreen(guiToOpen);
            guiToOpen = null;
        }
        if (Utils.inSkyBlock && !sentPlayTimeCommand) {
            Utils.sendMessageAsPlayer("/playtime");
            sentPlayTimeCommand = true;
        }
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void onChat(ClientChatReceivedEvent event) {
        Matcher matcher;
        if (Utils.inSkyBlock && sentPlayTimeCommand && !sentPlayTimeData && event.message.getUnformattedText().contains("minutes playtime!") && (matcher = playTimePattern.matcher(event.message.getUnformattedText())).matches()) {
            Analytics.collect("playtime", matcher.group(1));
            event.setCanceled(true);
            sentPlayTimeData = true;
        }
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (Updater.shouldUpdate && event.gui instanceof GuiMainMenu) {
            guiToOpen = new UpdateGui();
            Updater.shouldUpdate = false;
        }
    }

    public static void disable() {
        enabled = false;
        for (Setting setting : settings) {
            if (setting instanceof BooleanSetting) {
                setting.set(false);
            }
            if (!(setting instanceof SelectSetting)) continue;
            setting.set(0);
        }
    }
}

