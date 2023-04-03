package plus.crates.Handlers.Holograms;

import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import org.bukkit.Location;
import plus.crates.Crates.Crate;
import plus.crates.CratesPlus;

import java.util.ArrayList;
import java.util.HashMap;

public class HolographicDisplaysHologram implements CPHologram {
    private HashMap<String, Hologram> holograms = new HashMap<>();

    private final HolographicDisplaysAPI api;

    public HolographicDisplaysHologram(CratesPlus cratesPlus) {
        this.api = HolographicDisplaysAPI.get(cratesPlus);
    }

    public void create(Location location, Crate crate, ArrayList<String> lines) {
        final Hologram hologram = api.createHologram(location.clone().add(0, 1.25, 0));
        for (String line : lines) {
            hologram.getLines().appendText(line);
        }
        holograms.put("" + location.getWorld().getName() + "|" + location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ(), hologram);
    }

    public void remove(Location location, Crate crate) {
        if (holograms.containsKey("" + location.getWorld().getName() + "|" + location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ())) {
            holograms.get("" + location.getWorld().getName() + "|" + location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ()).delete();
            holograms.remove("" + location.getWorld().getName() + "|" + location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ());
        }
    }

}
