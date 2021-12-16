/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  net.minecraft.block.Block
 *  net.minecraft.util.ResourceLocation
 */
package cheaters.get.banned.features.dungeonmap;

import cheaters.get.banned.Shady;
import cheaters.get.banned.features.dungeonmap.Room;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

public class RoomLoader {
    public static ArrayList<Room> rooms = new ArrayList();
    public static HashSet<Integer> yLevels = new HashSet();

    public static void load() {
        try {
            ResourceLocation roomsResource = new ResourceLocation("shadyaddons:dungeonscanner/rooms.json");
            InputStream inputStream = Shady.mc.getResourceManager().getResource(roomsResource).getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            JsonParser parser = new JsonParser();
            JsonObject fullJson = parser.parse((Reader)reader).getAsJsonObject();
            JsonArray roomsArray = fullJson.getAsJsonArray("dungeonRooms");
            for (JsonElement roomElement : roomsArray) {
                Room room = new Room();
                JsonObject roomObject = roomElement.getAsJsonObject();
                room.name = roomObject.get("roomName").getAsString();
                room.type = RoomLoader.getTypeFromString(roomObject.get("roomType").getAsString());
                room.secrets = roomObject.get("secrets").getAsInt();
                room.yLevel = roomObject.get("yLevel").getAsInt();
                room.crypts = null;
                try {
                    room.crypts = Integer.parseInt(roomObject.get("crypts").getAsString());
                }
                catch (NumberFormatException numberFormatException) {
                    // empty catch block
                }
                JsonObject blocksObject = roomObject.getAsJsonObject("blocks");
                HashMap stringBlocksMap = (HashMap)new Gson().fromJson((JsonElement)blocksObject, HashMap.class);
                HashMap<Block, String> blockConditions = new HashMap<Block, String>();
                /*for (Map.Entry block : stringBlocksMap.entrySet()) {
                    blockConditions.put(Block.getBlockFromName((String)("minecraft:" + (String)block.getKey())), (String)block.getValue());
                }*/
                room.blockConditions = blockConditions;
                yLevels.add(room.yLevel);
                rooms.add(room);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static Room.Type getTypeFromString(String string) {
        switch (string) {
            case "normal": {
                return Room.Type.NORMAL;
            }
            case "blood": {
                return Room.Type.BLOOD;
            }
            case "fairy": {
                return Room.Type.FAIRY;
            }
            case "green": {
                return Room.Type.ENTRANCE;
            }
            case "puzzle": {
                return Room.Type.PUZZLE;
            }
            case "trap": {
                return Room.Type.TRAP;
            }
            case "yellow": {
                return Room.Type.MINIBOSS;
            }
        }
        return Room.Type.UNKNOWN;
    }
}

