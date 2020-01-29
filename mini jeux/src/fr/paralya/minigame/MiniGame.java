package fr.paralya.minigame;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public abstract class MiniGame {

	private final Set<MiniGameListener> listeners = new HashSet<MiniGameListener>();
	private final Set<MiniGameTimedTask> runnables = new HashSet<MiniGameTimedTask>();

	public final MiniGame addListeners(MiniGameListener listener) {
		this.listeners.add(listener);
		MiniGamePlugin.registerEvents(listener, MiniGamePlugin.getInstance());
		return this;
	}
	
	public final MiniGame addTimedTask(MiniGameTimedTask timedTask) {
		if(timedTask.getGame() != this) throw new IllegalArgumentException();
		this.runnables.add(timedTask);
		//TODO
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
	
	protected final void load() {
		onLoad();
	}
	
	protected final void unload() {
		listeners.forEach(l -> removeListeners(l));
		listeners.clear();
		runnables.clear();
		onUnload();
	}
	
	//TODO return true et change le jeu si fini
	public final void runTick() {
		Iterator<MiniGameTimedTask> it = runnables.iterator();
		while (it.hasNext()) {
			MiniGameTimedTask task = (MiniGameTimedTask) it.next();
			if(task.step())
				it.remove();
		}
	}

	/**
	 * Methode appel� au lancement du MiniJeu
	 * Doit contenir les appels �
	 * @see #addListeners(MiniGameListener)
	 * et �
	 * @see #addTimedTask(MiniGameTimedTask)
	 * (des appels � ces methodes marcherons meme apr�s l'initialisation)
	 */
	protected abstract void onLoad();

	/**
	 * Methode appel� a la fin du MiniJeu
	 * A l'appel, les listeners sont d�charg�s
	 */
	protected abstract void onUnload();
	
	
}