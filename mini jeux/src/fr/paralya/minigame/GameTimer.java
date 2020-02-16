package fr.paralya.minigame;

import org.bukkit.scheduler.BukkitRunnable;

public class GameTimer extends BukkitRunnable {
	
	private MiniGame game;
	
	@Override
	public void run() {
		if(game != null) if(!game.runTick()) MiniGamePlugin.changeCurrentGame(new WaitForNewGame());
	}
	
	public void setGame(MiniGame game) {
		this.game = game;
	}
	
}
