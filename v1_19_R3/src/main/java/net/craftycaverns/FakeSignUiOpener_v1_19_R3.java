package net.craftycaverns;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.protocol.game.PacketPlayInUpdateSign;
import net.minecraft.network.protocol.game.PacketPlayOutOpenSignEditor;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import plus.crates.Events.PlayerInputEvent;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class FakeSignUiOpener_v1_19_R3 implements FakeSignUiOpener {
    private final JavaPlugin plugin;

    public FakeSignUiOpener_v1_19_R3(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void openFakeSignUi(Player player) {
        player.sendBlockChange(player.getLocation(), Material.OAK_SIGN.createBlockData());

        final int x = player.getLocation().getBlockX(),
                y = player.getLocation().getBlockY(),
                z = player.getLocation().getBlockZ();
        PacketPlayOutOpenSignEditor packet = new PacketPlayOutOpenSignEditor(new BlockPosition(x, y, z));

        ((CraftPlayer) player).getHandle().b.a(packet);
        this.injectNetty(player);

        player.sendBlockChange(player.getLocation(), player.getLocation().getBlock().getBlockData());
    }

    private void injectNetty(Player player) {
        final Channel channel = this.getNetworkManager(((CraftPlayer) player).getHandle().b).m;

        if (channel == null) {
            return;
        }

        channel.pipeline().addAfter("decoder", "update_sign", new MessageToMessageDecoder<>() {
            @Override
            protected void decode(ChannelHandlerContext ctx, Object msg, List<Object> out) {
                if (!(msg instanceof PacketPlayInUpdateSign)) {
                    return;
                }

                final PacketPlayInUpdateSign packet = (PacketPlayInUpdateSign) msg;
                final List<String> lines = Arrays.asList(packet.c());

                if (lines.isEmpty()) {
                    player.sendMessage(ChatColor.RED + "Unable to handle input");
                } else {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        Bukkit.getPluginManager().callEvent(new PlayerInputEvent(player, lines));
                        ejectNetty(player);
                    });
                }

                out.add(msg);
            }
        });
    }

    private void ejectNetty(Player player) {
        final Channel channel = this.getNetworkManager(((CraftPlayer) player).getHandle().b).m;

        if (channel == null) {
            return;
        }

        if (channel.pipeline().get("update_sign") != null) {
            channel.pipeline().remove("update_sign");
        }
    }

    private NetworkManager getNetworkManager(PlayerConnection connection) {
        try {
            final Field networkManager = connection.getClass().getDeclaredField("h");
            networkManager.setAccessible(true);
            return (NetworkManager) networkManager.get(connection);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
