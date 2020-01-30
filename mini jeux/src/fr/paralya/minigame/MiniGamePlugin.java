package fr.paralya.minigame;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;

import fr.feaudive.packetmanager.PacketInputManager;
import fr.feaudive.packetmanager.PacketOutputManager;
import net.minecraft.server.v1_8_R3.PlayerConnection;

public class MiniGamePlugin extends JavaPlugin {
	
	private static MiniGamePlugin INSTANCE;
	private static List<MiniGame> games = Arrays.asList(); //mettre ici tout les Minigames (ou le seul en cour de test)
	private static MiniGame currentGame;
	private static GameTimer taskManager = new GameTimer();
	private static Random random = new Random();
	
	public MiniGamePlugin() {
		super();
		INSTANCE = this;
		taskManager.runTaskTimer(INSTANCE, 0, 1);
	}
	
	@Override
	public void onEnable() {
		registerEvents(new Listener() {
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
		
	}, this);
		Bukkit.getOnlinePlayers().forEach((p) -> registerPacketListener(p)); 
		currentGame = games.get(random.nextInt(games.size()));
	}
	
	@Override
	public void onDisable() {
		games.clear();
		for(Player player : Bukkit.getOnlinePlayers()) unregisterPacketListener(player);
	}
	
	public static void changeCurrentGame() { //TODO voter entre reedo et deux jeux randoms
		changeCurrentGame(games.get(random.nextInt(games.size())));
	}
	
	private static void changeCurrentGame(MiniGame game) {
		currentGame.unload();
		currentGame = game;
		currentGame.load();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void registerEvents(Listener listener, Plugin plugin) {
	if (!plugin.isEnabled()) {
		throw new IllegalPluginAccessException("Plugin attempted to register " + listener + " while not enabled");
	}
		for (Entry<Class<? extends Event>, Set<RegisteredListener>> entry : plugin.getPluginLoader().createRegisteredListeners(listener, plugin).entrySet()) {
			getEventListeners(getRegistrationClass( (Class) entry.getKey())).registerAll( (Collection) entry.getValue());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "static-access" })
	public static void unregisterEvents(Listener listener, Plugin plugin) {
		if (!plugin.isEnabled()) {
			throw new IllegalPluginAccessException("Plugin attempted to unregister " + listener + " while not enabled");
		}
		for (Map.Entry<Class<? extends Event>, Set<RegisteredListener>> entry : plugin.getPluginLoader().createRegisteredListeners(listener, plugin).entrySet()) {
			getEventListeners(getRegistrationClass( (Class) entry.getKey())).unregisterAll(listener);
		}
	}

	private static Class<? extends Event> getRegistrationClass(Class<? extends Event> clazz) {
		try {
			clazz.getDeclaredMethod("getHandlerList", new Class[0]);
			return clazz;
		} catch (NoSuchMethodException localNoSuchMethodException) {
			if ((clazz.getSuperclass() != null) && (!clazz.getSuperclass().equals(Event.class)) && (Event.class.isAssignableFrom(clazz.getSuperclass()))) {
				return getRegistrationClass(clazz.getSuperclass().asSubclass(Event.class));
			}
			throw new IllegalPluginAccessException("Unable to find handler list for event " + clazz.getName() + ". Static getHandlerList method required!");
		}
	}

	private static HandlerList getEventListeners(Class<? extends Event> type) {
		try {
			Method method = getRegistrationClass(type).getDeclaredMethod("getHandlerList", new Class[0]);
			method.setAccessible(true);
			return (HandlerList) method.invoke(null, new Object[0]);
	    } catch (Exception e) {
			throw new IllegalPluginAccessException(e.toString());
		}
	}
	
	private static void registerPacketListener(Player player) {
		PlayerConnection playerConnection  = new PacketOutputManager(player);
		try{((CraftPlayer) player).getHandle().playerConnection = playerConnection;}catch(Exception e) {}
		try{playerConnection.networkManager.channel.pipeline().addBefore("packet_handler", "packet_listener", new PacketInputManager(player));}catch(Exception e) {}
	}
	
	private static void unregisterPacketListener(Player player) {
		try {
			PacketOutputManager pom = PacketOutputManager.getPacketManager(player);
			((CraftPlayer) player).getHandle().playerConnection = pom.getOldPlayerConnection();
			PacketInputManager.removePacketManager(player);
			PacketOutputManager.removePacketManager(player);
		} catch(Exception e) {}
	}
	
	public static void callEvent(Event event) {
		Bukkit.getServer().getPluginManager().callEvent(event);
	}

	public static MiniGamePlugin getInstance() {
		return INSTANCE;
	}
	
}
