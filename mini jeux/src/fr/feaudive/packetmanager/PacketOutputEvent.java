package fr.feaudive.packetmanager;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketListenerPlayOut;

import org.bukkit.entity.Player;

public class PacketOutputEvent extends PacketEvent {

	public PacketOutputEvent(Player player, Packet<PacketListenerPlayOut> packet) {
		super(player, packet);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Packet<PacketListenerPlayOut> getPacket() {
		return (Packet<PacketListenerPlayOut>) super.getPacket();
	}
	
}
