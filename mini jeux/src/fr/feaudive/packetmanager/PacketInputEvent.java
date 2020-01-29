package fr.feaudive.packetmanager;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketListener;
import net.minecraft.server.v1_8_R3.PacketListenerPlayIn;

import org.bukkit.entity.Player;

public class PacketInputEvent extends PacketEvent {
	
	private ChannelHandlerContext context;
	
	public PacketInputEvent(Player player, Packet<PacketListenerPlayIn> packet, ChannelHandlerContext context) {
		super(player, packet);
		this.context = context;
	}
	
	public ChannelHandlerContext getContext() {
		return context;
	}
	
	public void setContext(ChannelHandlerContext context) {
		this.context = context;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Packet<PacketListenerPlayIn> getPacket() {
		return (Packet<PacketListenerPlayIn>) super.getPacket();
	}
	
	@Override
	public void setPacket(Packet<PacketListener> packet) {
		super.setPacket(packet);
	}
	
}