/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.inventory.GuiChest
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.Container
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.Item
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.client.event.GuiScreenEvent$DrawScreenEvent$Pre
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package cheaters.get.banned.features.connectfoursolver;

import cheaters.get.banned.Shady;
import cheaters.get.banned.config.Config;
import cheaters.get.banned.events.DrawSlotEvent;
import cheaters.get.banned.events.TickEndEvent;
import cheaters.get.banned.features.connectfoursolver.ConnectFourAlgorithm;
import cheaters.get.banned.utils.RenderUtils;
import cheaters.get.banned.utils.Utils;
import java.awt.Color;
import net.minecraft.block.Block;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ConnectFourSolver {
    private boolean calculating = false;
    private int solutionSlot = -1;
    private boolean isOurTurn = false;
    private boolean inGame = false;
    private Thread thread = null;
    private static final int EMPTY = 0;
    private static final int COMPUTER = 1;
    private static final int OPPONENT = 2;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (Config.connectFourAI && Utils.inSkyBlock) {
            boolean bl = this.inGame = Shady.mc.currentScreen instanceof GuiChest && Utils.getInventoryName().startsWith("Four in a Row - ");
            if (this.inGame && Shady.mc.player.openContainer.inventorySlots.get(1) != null && ((Slot)Shady.mc.player.openContainer.inventorySlots.get(1)).getStack() != null) {
                boolean bl2 = this.isOurTurn = ((Slot)Shady.mc.player.openContainer.inventorySlots.get(1)).getStack().getItem() == Items.ITEM_FRAME;
            }
            if (!this.isOurTurn || !this.inGame) {
                this.solutionSlot = -1;
            }
            if (!this.inGame && this.thread != null && this.thread.isAlive()) {
                this.thread.stop();
            }
            if (this.isOurTurn && !this.calculating && this.inGame && this.solutionSlot == -1) {
                this.calculating = true;
                this.thread = new Thread(() -> {
                    ConnectFourAlgorithm.Board board = this.serializeInventory(Shady.mc.player.openContainer);
                    ConnectFourAlgorithm algorithm = new ConnectFourAlgorithm(board);
                    int column = algorithm.getAIMove();
                    algorithm.board.placeMove(column, 1);
                    for (int row = 5; row >= 0; --row) {
                        if (board.board[row][column] != 0) continue;
                        this.solutionSlot = row * 9 + column + 1 + 9;
                        break;
                    }
                    this.calculating = false;
                }, "ShadyAddons-ConnectFour");
                this.thread.start();
            }
        }
    }

    private ConnectFourAlgorithm.Board serializeInventory(Container chest) {
        ConnectFourAlgorithm.Board board = new ConnectFourAlgorithm.Board();
        for (Slot slot : chest.inventorySlots) {
            if (slot.slotNumber > 53 || slot.getStack() == null || slot.slotNumber % 9 == 0 || (slot.slotNumber + 1) % 9 == 0) continue;
            int row = slot.slotNumber / 9;
            int column = (slot.slotNumber - 1) % 9;
            Item item = slot.getStack().getItem();
            if (item == Items.ITEM_FRAME || item == Items.PAINTING) {
                board.board[row][column] = 0;
            }
            if (item != Item.getItemFromBlock((Block)Blocks.STAINED_HARDENED_CLAY)) continue;
            if (slot.getStack().getItemDamage() == 1) {
                board.board[row][column] = 2;
                continue;
            }
            board.board[row][column] = 1;
        }
        return board;
    }

    @SubscribeEvent
    public void onBackgroundRender(GuiScreenEvent.DrawScreenEvent.Pre event) {
        if (Config.connectFourAI && Utils.inSkyBlock && this.inGame && this.calculating) {
            int y = event.gui.height / 2 - 105;
            int x = event.gui.width / 2 + 73;
            int textureX = (int)(System.currentTimeMillis() / 10L % 20L * 8L);
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)0.0f, (float)0.0f, (float)1000.0f);
            GlStateManager.color((float)64.0f, (float)64.0f, (float)64.0f);
            RenderUtils.drawTexture(new ResourceLocation("shadyaddons:loader.png"), x, y, 8, 8, 160, 8, textureX, 0);
            GlStateManager.popMatrix();
        }
    }

    @SubscribeEvent
    public void onDrawSlot(DrawSlotEvent event) {
        if (Config.connectFourAI && Utils.inSkyBlock && this.inGame && this.solutionSlot == event.slot.slotNumber) {
            int x = event.slot.xPos;
            int y = event.slot.yPos;
            Gui.drawRect((int)x, (int)y, (int)(x + 16), (int)(y + 16), (int)Color.GREEN.getRGB());
        }
    }
}

