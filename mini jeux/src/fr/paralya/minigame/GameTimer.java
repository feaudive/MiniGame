package fr.paralya.minigame;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTimer extends BukkitRunnable {
	
	private MiniGame game;
	private int timeVoteLeft = 0;
	private MiniGame game1 = null;
	private MiniGame game2 = null;
	private MiniGame game3 = null;
	
	@Override
	public void run() {
		if(game != null) {
			if(!game.runTick()) {
				game.unload();
				game1 = game;
				game2 = MiniGamePlugin.getRandomGame();
				game3 = MiniGamePlugin.getRandomGame();
				timeVoteLeft = 200;
				game = null;
				Bukkit.broadcastMessage("on vote"); //TODO vote
			}
		} else {
			timeVoteLeft--;
			if(timeVoteLeft == 0) {
				Bukkit.broadcastMessage("Le peuple à choisi");
				game = game1; //TODO chois selon le vote
				game.load();
			}
		}
	}
	
	void setGame(MiniGame game) {
		this.game = game;
	}
	
}