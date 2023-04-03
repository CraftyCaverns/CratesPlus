package plus.crates.Handlers.Holograms;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Location;
import plus.crates.Crates.Crate;

import java.util.ArrayList;

public class DecentHologramsHologram implements CPHologram {
    @Override
    public void create(Location location, Crate crate, ArrayList<String> lines) {
        DHAPI.createHologram(crate.getName(), location.clone().add(0, 1.25, 0), lines);
    }

    @Override
    public void remove(Location location, Crate crate) {
        final Hologram hologram = DHAPI.getHologram(crate.getName());

        if (hologram == null) {
            return;
        }

        hologram.delete();
    }
}
