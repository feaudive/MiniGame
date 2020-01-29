package fr.feaudive.packetmanager;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketListener;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

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
	
}
