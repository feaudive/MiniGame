package fr.feaudive.packetmanager;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketListener;
import net.minecraft.server.v1_8_R3.PlayerConnection;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PacketEvent extends Event implements Cancellable {
	
public static final HandlerList handlers = new HandlerList();
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	private boolean cancel = false;
	private Player player;
	private Packet<? extends PacketListener> packet;
	
	public PacketEvent(Player player, Packet<? extends PacketListener> packet) {
		this.player = player;
		this.packet = packet;
	}
	
	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Packet<? extends PacketListener> getPacket() {
		return packet;
	}
	
	public void setPacket(Packet<PacketListener> packet) {
		this.packet = packet;
	}
	
	public static void registerAllPlayerAutomatically(JavaPlugin plugin) {
		Bukkit.getPluginManager().registerEvents(new Listener() {
			@EventHandler
			public void onJoin(PlayerJoinEvent event) {
				registerPacketListener(event.getPlayer());
			}

			@EventHandler
			public void onLeave(PlayerQuitEvent event) {
				unregisterPacketListener(event.getPlayer());
			}
			
			@EventHandler
			public void onKicked(PlayerKickEvent event) {
				unregisterPacketListener(event.getPlayer());
			}
			
		}, plugin);
		Bukkit.getOnlinePlayers().forEach((p) -> registerPacketListener(p)); 
	}
	
	public static void registerPacketListener(Player player) {
		if(((CraftPlayer) player).getHandle().playerConnection instanceof PacketOutputManager) return;
		PlayerConnection playerConnection  = new PacketOutputManager(player);
		try{((CraftPlayer) player).getHandle().playerConnection = playerConnection;}catch(Exception e) {}
		try{playerConnection.networkManager.channel.pipeline().addBefore("packet_handler", "packet_listener", new PacketInputManager(player));}catch(Exception e) {}
	}
	
	public static void unregisterPacketListener(Player player) {
		try {
			PacketOutputManager pom = PacketOutputManager.getPacketManager(player);
			((CraftPlayer) player).getHandle().playerConnection = pom.getOldPlayerConnection();
			PacketInputManager.removePacketManager(player);
			PacketOutputManager.removePacketManager(player);
		} catch(Exception e) {}
	}
	
}
