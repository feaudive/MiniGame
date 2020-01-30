package fr.feaudive.packetmanager;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import fr.feaudive.packetmanager.PacketOutputEvent;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PlayerConnection;

@SuppressWarnings("rawtypes")
public class PacketOutputManager extends PlayerConnection {

	private static final HashMap<UUID, PacketOutputManager> managers = new HashMap<UUID, PacketOutputManager>();
	
	private final PlayerConnection oldPlayerConnection;
	
	public static PacketOutputManager getPacketManager(Player player) {
		return managers.get(player.getUniqueId());
	}
	
	public static void removePacketManager(Player player) {
		managers.remove(player.getUniqueId());
	}

	public PlayerConnection getOldPlayerConnection() {
		return oldPlayerConnection;
	}
	
	public PacketOutputManager(Player player) {
		super(((CraftPlayer) player).getHandle().playerConnection.player.server, ((CraftPlayer) player).getHandle().playerConnection.networkManager, ((CraftPlayer) player).getHandle().playerConnection.player);
		this.oldPlayerConnection = ((CraftPlayer) player).getHandle().playerConnection;
		managers.put(player.getUniqueId(), this);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void sendPacket(Packet packet) {
		PacketOutputEvent event = new PacketOutputEvent(super.player.getBukkitEntity(), packet);
		Bukkit.getServer().getPluginManager().callEvent(event);
		if(event.isCancelled()) return;
		super.sendPacket(event.getPacket());
	}
	
	public void createInputPacket(Packet packet, boolean bypassListeners) throws Exception {
		if(bypassListeners) super.sendPacket(packet);
		else this.sendPacket(packet);
	}
	
}

