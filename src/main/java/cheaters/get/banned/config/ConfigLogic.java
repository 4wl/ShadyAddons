/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.reflect.TypeToken
 */
package cheaters.get.banned.config;

import cheaters.get.banned.Shady;
import cheaters.get.banned.config.Config;
import cheaters.get.banned.config.Property;
import cheaters.get.banned.config.settings.BooleanSetting;
import cheaters.get.banned.config.settings.FolderSetting;
import cheaters.get.banned.config.settings.NumberSetting;
import cheaters.get.banned.config.settings.ParentSetting;
import cheaters.get.banned.config.settings.SelectSetting;
import cheaters.get.banned.config.settings.Setting;
import cheaters.get.banned.remote.DisableFeatures;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigLogic {
    private static String fileName = "config/ShadyAddons.cfg";

    public static ArrayList<Setting> collect(Class<Config> instance) {
        Field[] fields = instance.getDeclaredFields();
        ArrayList<Setting> settings = new ArrayList<Setting>();
        block6: for (Field field : fields) {
            Property annotation = field.getAnnotation(Property.class);
            if (annotation == null) continue;
            switch (annotation.type()) {
                case BOOLEAN: 
                case CHECKBOX: {
                    settings.add(new BooleanSetting(annotation, field, annotation.type()));
                    continue block6;
                }
                case NUMBER: {
                    settings.add(new NumberSetting(annotation, field));
                    continue block6;
                }
                case SELECT: {
                    settings.add(new SelectSetting(annotation, field));
                    continue block6;
                }
                case FOLDER: {
                    settings.add(new FolderSetting(annotation, field));
                }
            }
        }
        List<String> disabledFeatures = DisableFeatures.load();
        ArrayList<Setting> settingsToRemove = new ArrayList<Setting>();
        for (Setting setting : settings) {
            if (disabledFeatures.contains(setting.name)) {
                settingsToRemove.add(setting);
                continue;
            }
            if (settingsToRemove.contains(setting.parent)) {
                settingsToRemove.add(setting);
                continue;
            }
            if (setting.annotation.parent().equals("")) continue;
            setting.parent = (ParentSetting)ConfigLogic.getSettingByName(setting.annotation.parent(), settings);
            if (setting.parent == null) continue;
            setting.parent.children.add(setting);
        }
        settings.removeAll(settingsToRemove);
        return settings;
    }

    public static Setting getSettingByName(String name, ArrayList<Setting> settings) {
        for (Setting setting : settings) {
            if (!setting.name.equals(name)) continue;
            return setting;
        }
        return null;
    }

    public static Setting getSettingByFieldName(String fieldName, ArrayList<Setting> settings) {
        for (Setting setting : settings) {
            if (!setting.field.getName().equals(fieldName)) continue;
            return setting;
        }
        return null;
    }

    public static void legacySave() {
        try {
            HashMap<String, Object> convertedSettings = new HashMap<String, Object>();
            for (Setting setting : Shady.settings) {
                if (setting instanceof FolderSetting) continue;
                convertedSettings.put(setting.name, setting.get(Object.class));
            }
            String json = new Gson().toJson(convertedSettings);
            Files.write(Paths.get(fileName, new String[0]), json.getBytes(StandardCharsets.UTF_8), new OpenOption[0]);
        }
        catch (Exception error) {
            System.out.println("Error saving config file");
            error.printStackTrace();
        }
    }

    public static void save() {
        try {
            HashMap<String, Object> settingsToSave = new HashMap<String, Object>();
            for (Setting setting : Shady.settings) {
                if (setting instanceof FolderSetting) continue;
                settingsToSave.put(setting.field.getName(), setting.get(Object.class));
            }
            String json = new Gson().toJson(settingsToSave);
            Files.write(Paths.get(fileName, new String[0]), json.getBytes(StandardCharsets.UTF_8), new OpenOption[0]);
        }
        catch (Exception error) {
            System.out.println("Error saving config file");
            error.printStackTrace();
        }
    }

    public static void load() {
        try {
            File file = new File(fileName);
            if (file.exists()) {
                BufferedReader reader = Files.newBufferedReader(Paths.get(fileName, new String[0]));
                Type type = new TypeToken<HashMap<String, Object>>(){}.getType();
                HashMap settingsFromConfig = (HashMap)new Gson().fromJson((Reader)reader, type);
                for (Map.Entry fromConfig : settingsFromConfig.entrySet()) {
                    Setting beingUpdated = ConfigLogic.getSettingByFieldName((String)fromConfig.getKey(), Shady.settings);
                    if (beingUpdated != null) {
                        if (beingUpdated instanceof NumberSetting || beingUpdated instanceof SelectSetting) {
                            beingUpdated.set(((Double)fromConfig.getValue()).intValue());
                            continue;
                        }
                        beingUpdated.forceSet(fromConfig.getValue());
                        continue;
                    }
                    beingUpdated = ConfigLogic.getSettingByName((String)fromConfig.getKey(), Shady.settings);
                    if (beingUpdated == null) continue;
                    if (beingUpdated instanceof NumberSetting || beingUpdated instanceof SelectSetting) {
                        beingUpdated.set(((Double)fromConfig.getValue()).intValue());
                        continue;
                    }
                    beingUpdated.forceSet(fromConfig.getValue());
                }
            }
        }
        catch (Exception error) {
            System.out.println("Error while loading config file");
            error.printStackTrace();
        }
    }

    public static void legacyLoad() {
        try {
            File file = new File(fileName);
            if (file.exists()) {
                BufferedReader reader = Files.newBufferedReader(Paths.get(fileName, new String[0]));
                Type type = new TypeToken<HashMap<String, Object>>(){}.getType();
                HashMap settingsFromConfig = (HashMap)new Gson().fromJson((Reader)reader, type);
                for (Map.Entry fromConfig : settingsFromConfig.entrySet()) {
                    Setting beingUpdated = ConfigLogic.getSettingByName((String)fromConfig.getKey(), Shady.settings);
                    if (beingUpdated == null) continue;
                    if (beingUpdated instanceof NumberSetting || beingUpdated instanceof SelectSetting) {
                        beingUpdated.set(((Double)fromConfig.getValue()).intValue());
                        continue;
                    }
                    beingUpdated.forceSet(fromConfig.getValue());
                }
            }
        }
        catch (Exception error) {
            System.out.println("Error while loading config file");
            error.printStackTrace();
        }
    }
}

