/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.Cancelable
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package cheaters.get.banned.events;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ResourcePackRefreshEvent
extends Event {

    public static class Post
    extends ResourcePackRefreshEvent {
    }

    @Cancelable
    public static class Pre
    extends ResourcePackRefreshEvent {
    }
}

