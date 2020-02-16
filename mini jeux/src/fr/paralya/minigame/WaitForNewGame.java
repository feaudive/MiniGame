package fr.paralya.minigame;

import org.bukkit.Bukkit;

public class WaitForNewGame extends MiniGame {

	@Override
	protected void onLoad() {
		Bukkit.broadcastMessage("On change de jeux :");
		Bukkit.broadcastMessage("Voter ici");
		//TODO voter entre reedo et deux jeux randoms sans passez par GameTimer
		MiniGamePlugin.changeCurrentGame(MiniGamePlugin.getRandomGame());
		
	}

	@Override
	protected void onUnload() {
	}

}
