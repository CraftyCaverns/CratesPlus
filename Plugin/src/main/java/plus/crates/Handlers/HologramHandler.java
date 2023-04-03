package plus.crates.Handlers;

import org.bukkit.Bukkit;
import plus.crates.CratesPlus;
import plus.crates.Handlers.Holograms.CPHologram;
import plus.crates.Handlers.Holograms.DecentHologramsHologram;
import plus.crates.Handlers.Holograms.FallbackHologram;
import plus.crates.Handlers.Holograms.HolographicDisplaysHologram;

public class HologramHandler {
    private HologramPlugin hologramPlugin = HologramPlugin.NONE;

    public HologramHandler(CratesPlus cratesPlus) {
        if (Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
            this.hologramPlugin = HologramPlugin.HOLOGRAPHIC_DISPLAYS;
        } else if (Bukkit.getPluginManager().isPluginEnabled("DecentHolograms")) {
            this.hologramPlugin = HologramPlugin.DECENT_HOLOGRAMS;
        }
        getHologramPlugin().init(cratesPlus);
    }

    public enum HologramPlugin {
        NONE,
        DECENT_HOLOGRAMS,
        HOLOGRAPHIC_DISPLAYS;

        private CPHologram hologram;

        public void init(CratesPlus cratesPlus) {
            switch (this) {
                default:
                case NONE:
                    this.hologram = new FallbackHologram();
                    break;
                case HOLOGRAPHIC_DISPLAYS:
                    this.hologram = new HolographicDisplaysHologram(cratesPlus);
                    break;
                case DECENT_HOLOGRAMS:
                    this.hologram = new DecentHologramsHologram();
                    break;
            }
        }

        public CPHologram getHologram() {
            return hologram;
        }
    }

    public HologramPlugin getHologramPlugin() {
        return hologramPlugin;
    }

}
