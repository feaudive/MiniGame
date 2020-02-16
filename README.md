# MiniGame
API des minijeux pour Paralya

Le minijeu doit etre fait dans le package fr.<votre psoedo>.<nom du minijeu>
  
Pour tester votre minijeux, il suffit d'ajouter directement une instance de votre classe héritant de MiniGame dans la liste games dans la classe MiniGamePlugin (ligne 26).

Tout les listeners et les runnables composant votre minijeux doivent etre des respectivement des implémentations de l'interface MiniGameListener ou des instance de la classe MiniGameTimedTask. Ils s'enregistres directement via votre classe héritant de MiniGame avec les methodes addListener et addTimedTask et peuvent etre "désenregistés" via les methodes removeListener et removeTimedTask.

La fin du minijeux doit etre annoncer avec l'appel de la methode finish de MiniGame, depuis n'importe quelle classe.

A savoir : il est inutile (voir dangereux si fait depuis un runnable) de "désenregistés" les listeners et runnables avant la fin du jeu. Celà est fait automatiquement.
