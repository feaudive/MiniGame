package fr.paralya.minigame;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public abstract class MiniGame {
	
	private boolean enable = false;
	private final Set<MiniGameListener> listeners = new HashSet<MiniGameListener>();
	private final Set<MiniGameTimedTask> runnables = new HashSet<MiniGameTimedTask>();
	private final String name;
	
	public MiniGame(String name) {
		this.name = name;
	}

	public final MiniGame addListeners(MiniGameListener listener) {
		this.listeners.add(listener);
		MiniGamePlugin.registerEvents(listener, MiniGamePlugin.getInstance());
		return this;
	}
	
	public final MiniGame addTimedTask(MiniGameTimedTask timedTask) {
		if(timedTask.getGame() != this) throw new IllegalArgumentException();
		this.runnables.add(timedTask);
		return this;
	}
	
	public final MiniGame removeListeners(MiniGameListener listener) {
		if(this.listeners.remove(listener))
			MiniGamePlugin.unregisterEvents(listener, MiniGamePlugin.getInstance());
		return this;
	}

	public final MiniGame removeTimedTask(MiniGameTimedTask timedTask) {
		if(timedTask.getGame() != this) throw new IllegalArgumentException();
		this.runnables.remove(timedTask);
		return this;
	}
	
	public String getName() {
		return name;
	}
	
	public final void load() {
		onLoad();
		enable = true;
	}
	
	public final void unload() {
		listeners.forEach(l -> removeListeners(l));
		listeners.clear();
		runnables.clear();
		onUnload();
		enable = false;
	}
	
	public final void finish() {
		enable = false;
	}
	
	//TODO return true et change le jeu si fini
	public final boolean runTick() {
		if(enable) {
			Iterator<MiniGameTimedTask> it = runnables.iterator();
			while (it.hasNext()) {
				MiniGameTimedTask task = (MiniGameTimedTask) it.next();
				if(task.step())
					it.remove();
			}
		}
		return enable;
	}

	/**
	 * Methode appelé au lancement du MiniJeu
	 * Doit contenir les appels à
	 * @see #addListeners(MiniGameListener)
	 * et à
	 * @see #addTimedTask(MiniGameTimedTask)
	 * (des appels à ces methodes marcherons meme après l'initialisation)
	 */
	protected abstract void onLoad();

	/**
	 * Methode appelé a la fin du MiniJeu
	 * A l'appel, les listeners sont déchargés
	 */
	protected abstract void onUnload();
	
	
}