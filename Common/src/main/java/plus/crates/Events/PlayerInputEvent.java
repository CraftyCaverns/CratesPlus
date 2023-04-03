package plus.crates.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

public class PlayerInputEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    public List<String> lines;

    public PlayerInputEvent(Player player, List<String> lines) {
        this.player = player;
        this.lines = lines;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public List<String> getLines() {
        return lines;
    }

}