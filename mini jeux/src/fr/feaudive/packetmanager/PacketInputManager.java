package fr.feaudive.packetmanager;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.feaudive.packetmanager.PacketInputEvent;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketListenerPlayIn;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class PacketInputManager extends ChannelInboundHandlerAdapter {
	
	private final UUID uuid;
	private static final HashMap<UUID, PacketInputManager> managers = new HashMap<UUID, PacketInputManager>();
	
	public static PacketInputManager getPacketManager(Player player) {
		return managers.get(player.getUniqueId());
	}
	
	public static void removePacketManager(Player player) {
		managers.remove(player.getUniqueId());
	}
	
	public PacketInputManager(Player player) {
		this.uuid = player.getUniqueId();
		managers.put(player.getUniqueId(), this);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void channelRead(ChannelHandlerContext context, Object packet) throws Exception {
		if(packet instanceof Packet) {
			PacketInputEvent event = new PacketInputEvent(Bukkit.getPlayer(uuid), (Packet<PacketListenerPlayIn>) packet, context);
			Bukkit.getServer().getPluginManager().callEvent(event);
			if(event.isCancelled()) return;
			super.channelRead(event.getContext(), event.getPacket());
		} else {
			super.channelRead(context, packet);
		}
	}
	
	public void createInputPacket(ChannelHandlerContext context, Packet<PacketListenerPlayIn> packet, boolean bypassListeners) throws Exception {
		if(bypassListeners) super.channelRead(context, packet);
		else this.channelRead(context, packet);
	}
	
}
