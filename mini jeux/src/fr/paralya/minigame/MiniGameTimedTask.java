package fr.paralya.minigame;

import java.util.TimerTask;

public abstract class MiniGameTimedTask extends TimerTask {
	
	private final int timeLoop;
	private int time;
	private int count;
	protected final MiniGame game;
	
	
	/**
	 * @param timeBetweenLoop temps entre deux execution de @see #run()
	 * @param firstDelay temps entre l'enregistrement et la premiere execution de @see #run()
	 * @param countExecution nombre de fois que @see #run() doit etre executer (négatif = infini)
	 * @param game instance vers le jeu qui contient l'object courant 
	 */
	public MiniGameTimedTask(int timeBetweenLoop, int firstDelay, int countExecution, MiniGame game) {
		super();
		if(timeBetweenLoop <= 0 || firstDelay < 0 || countExecution == 0) throw new IllegalArgumentException();
		timeLoop = timeBetweenLoop;
		time = firstDelay;
		count = countExecution;
		this.game = game;
	}
	
	public final boolean step() {
		time--;
		if(time <= 0) {
			time = timeLoop;
			if(count > 0) count --;
			run();
			if(count == 0) return true;
		}
		return false;
	}
	
	public final MiniGame getGame() {
		return game;
	}
	
}