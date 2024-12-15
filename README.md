
# Jeu HOP!

Un jeu de plateforme vertical où le joueur doit sauter de plateforme en plateforme pour survivre.

## Classes Principales

### Hop
- Classe principale qui gère le jeu
- Contrôle le flux du jeu, les scores et les transitions entre les écrans
- Gère deux thèmes différents: Star Wars et Japon

### Axel
- Représente le personnage jouable
- Gère les mouvements (sauts, déplacements latéraux)
- Implémente la physique du jeu (gravité, collisions)
- Gère les power-ups (double saut, boost de vitesse)

### Field (Terrain)
- Représente le terrain de jeu
- Génère et gère les plateformes
- Contrôle le défilement vertical
- Gère l'apparition des power-ups

### Block
- Représente une plateforme simple
- Contient les coordonnées et dimensions

### GamePanel
- Gère l'affichage du jeu
- Contrôle les images et les sons
- Affiche le score et les power-ups actifs
- Gère les entrées clavier

### MainMenu
- Interface du menu principal
- Permet de choisir le thème
- Affiche le tableau des scores
- Gère la musique du menu

### GameOverScreen
- Écran de fin de partie
- Affiche le score final
- Permet de rejouer
- Animations et sons spécifiques selon le thème

### Leaderboard
- Affiche les meilleurs scores
- Utilise une base de données SQLite

### PowerUp
- Représente les bonus du jeu
- Deux types: Double saut et Boost de vitesse
- Effets temporaires (10 secondes)

## Thèmes
Le jeu propose deux thèmes:
- Star Wars (Baby Yoda comme personnage)
- Japon (Ninja comme personnage)

Chaque thème a ses propres:
- Images
- Sons
- Musiques
- Effets visuels