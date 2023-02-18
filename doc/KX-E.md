# Rapport de projet PS5.2 Equipe E
## Nos principaux choix de conceptions
### Un main simple et efficace !
Le [Main](../src/main/java/citadelles/Main.java) se veut être le plus simple possible. 
Pour cela, une classe intermédiaire gère le jeu : 
[Citadelles.java](../src/main/java/citadelles/Citadelles.java)<br/>
Cette classe lance le jeu avec un certain nombre de bots et affiche les résultats.<br/>
Citadelles prend trois arguments, un pour chaque type de bot :
- Un bot intelligent <a href="../src/main/java/citadelles/bots/SmartBot.java">SmartBot</a>
- Un bot intermédiaire <a href="../src/main/java/citadelles/bots/MediumBot.java">MediumBot</a>
- Un bot stupide <a href="../src/main/java/citadelles/bots/StupidBot.java">StupidBot</a>

### Les bots
Les bots représentent une partie fondamentale du jeu. </br>
Les classes dédiées aux bots ont été regroupées dans un package bots.</br>

Dans ce répertoire bots vous trouverez :
#### La classe Bot
<a href="../src/main/java/citadelles/bots/Bot.java">Bot.java</a> est la super classe permettant la création de bots.</br>
En détails :
- Permet à un joueur de jouer s'il est vivant
- Piocher une ou plusieurs cartes de quartier
- Choisir une carte
- Déterminer si un quartier n'a pas déjà été posé
- Gérer la trésorerie, les quartiers posés, les quartiers en main de chaque bot
- Ajouter ou supprimer la couronne à un bot
- Choisir un rôle

Cette classe abstraite sert de moule aux différents bots.

#### Le BotManager
<a href="../src/main/java/citadelles/bots/BotManager.java">BotManager</a> agit sur la gestion des bots. Il a ainsi accès à toutes les informations contenues dans les bots.</br>
BotManager crée des objets bots : stupides, intelligents ou moyens.</br>

#### L'arbitre ou Referee
<a href="../src/main/java/citadelles/bots/Referee.java">L'arbitre</a> a une fonction utilitaire. </br>
Il s'occupe de tuer un bot, de détruire des quartiers, de voler un bot et d'échanger des cartes ou des pièces. Il est donc en étroite relation avec le <a href="../src/main/java/citadelles/bots/BotManager.java">BotManager</a></br>

#### Le bot stupide
<a href="../src/main/java/citadelles/bots/BotStupide.java">BotStupide</a> est basé sur du code pseudo-aléatoire ou des stratégies autodestructrices, il ne possède pas de stratégie particulière.</br>
Lorsqu'il pioche, il essaie de prendre des quartiers qu'il a déjà placés...</br>
L'assassin tue aléatoirement, le condottiere détruit son propre quartier, le voleur vole aléatoirement (mais pas lui-même) ...</br>

#### Le bot moyen
<a href="../src/main/java/citadelles/bots/MediumBot.java">BotMoyen</a> possède des prémices de stratégie.</br>
Il cherche à gagner de l'argent pour poser plus de bâtiments. S'il a beaucoup d'or ou qu'il n'a plus de carte, il pioche, sinon il prend de l'or.</br>
Assassine et vole aléatoirement, mais pas lui-même, ni l'assassin. Le magicien choisit la personne avec le plus de cartes.</br>Le condottiere détruit un bâtiment à celui qui en a le plus posé, etc.</br>

#### Le bot avancé
<a href="../src/main/java/citadelles/bots/SmartBot.java">SmartBot</a> applique des stratégies plus développées.</br>
Si le magicien a moins de 3 cartes en main, il échange ses cartes avec la personne qui a le plus de cartes si possible. Sinon échange toutes ses cartes avec la pioche. Le condottiere détruit le bâtiment le moins cher du joueur qui a le plus de bâtiments posés...</br>
Le choix du rôle est influencé par l'état de la partie. Ainsi, si la partie touche à sa fin pour un des joueurs, il aura tendance à prendre le rôle assassin pour l'en empêcher.
S'il possède plus de 2 quartiers d'une catégorie alors il essayera de sélectionner le personnage de la catégorie.

### Les stratégies
_La liste ultra détaillée pour chaque bot est disponible dans l'_<a href="https://github.com/pns-si3-projects/projet2-ps5-21-22-ps5-21-22-projet2-e/issues/59">issue 59</a>.
Suite à ces stratégies les classements des bots lors de 1000 parties donne le résultat suivant :

![Diagramme](https://github.com/pns-si3-projects/projet2-ps5-21-22-ps5-21-22-projet2-e/blob/master/doc/Result1000games.png)

### Les quartiers
Les quartiers ont été référencés dans le fichier <a href="../src/main/data/districtsData.csv">districtsData.csv</a> permettant ainsi de rajouter ou de modifier une valeur simplement sans devoir modifier le code source du programme.</br>
Les merveilles, quant à elles, été implémentées par des classes pour une manipulation plus simple de leurs actions spécifiques.

Les catégories de quartiers *districts* ont été implémentées en "dur" sous la forme d'une énumération.</br>
<a href="../src/main/java/citadelles/districts/CategoryEnum.java">CategoryEnum.java</a> <- Cette enumération contient les quartiers religieux, nobles, d'échanges, militaires ou uniques.</br>

### Les rôles
Les rôles sont représentés par une énumération 
<a href="../src/main/java/citadelles/characters/CharacterEnum.java">CharacterEnum.java</a> comprenant un id et un nom.</br>
Nous avons suivi les mêmes choix que lors de l'implémentation des bots avec l'utilisation d'une classe 
<a href="../src/main/java/citadelles/characters/CharacterManager.java">CharacterManager</a> qui coordonne nos rôles.</br>
Nous avons aussi choisi de créer une classe par rôle qui hérite d'une classe abstraite <a href="../src/main/java/citadelles/characters/Character.java">Character.</a></br>
Chaque classe correspond à un rôle qui contient des actions spécifiques. Ainsi, il est facile d'ajouter de nouveaux rôles.

## Où trouver la documentation ?
La documentation est trouvable dans la page <a href="https://antoinebqt.github.io/html-citadelle-documentation/">Javadoc</a> du projet directement. L'ensemble des classes et la quasi-totalité des méthodes sont commentées au-dessus de leur déclaration.</br>
Les stratégies des bots sont écrites de manière détaillée dans l'<a href="https://github.com/pns-si3-projects/projet2-ps5-21-22-ps5-21-22-projet2-e/issues/59">issue 59</a>, sinon ce rapport et le ReadMe contiennent également des données utiles à la compréhension du projet.

## La répartition des tâches au sein de l'équipe
Les choix de conceptions ont été réalisés ensemble.</br>
<hr>
<b>Benoit</b> <a href="https://github.com/BenoitGAUDET38">@BenoitGAUDET38</a></br>
<ul><li>Implémentation des bots, de leur logique et implémentation</li><li>Implémentation des rôles/characters</li>
  <li>Implémentation des districts, quelques districts spéciaux et de l'import des données des districts depuis un fichier csv externe</li></ul>
<hr>
<b>Thomas</b> <a href="https://github.com/Thobil">@Thobil</a></br>
<ul><li>Initialisation du projet</li>
<li>Implémentation d'une Vue du jeu qui est donné aux bots pour qu'ils ne puissent pas modifier le jeu (GameView)</li>
<li>Implémentation des rôles thief, magician et architect et de leurs logiques</li>
<li>Amélioration de la logique des bots</li></ul>
<hr>
<b>Tobias</b> <a href="https://github.com/TobiasBonifay">@TobiasBonifay</a></br>
<ul><li>Initialisation du projet avec Thomas</li><li>Rédaction du rapport #60</li><li>Réalisation des logs avec Antoine #26 #45</li>
  <li>"Refactor" et ajout de l'énumération pour les personnages #43</li><li>Réalisation de quelques tests</li><li>"Refactor" des personnages #9 #10 #11 #12</li></ul>
<hr>
<b>Antoine</b> <a href="https://github.com/antoinebqt">@antoinebqt</a></br>
<ul><li>Classe LogManager et l'affichage des logs</li>
<li>Calculateur du score final avec les différents bonus et affichage des résultats triés</li>
<li>Gestion de la couronne</li>
<li>Implémentation du code du Roi, de l'Assassin et de l'Architecte</li>
<li>Implémentation des quartiers spéciaux</li>
<li>Implémentation des stratégies des bots pour l'architecte et les quartiers spéciaux</li>
<li>Documentation du code</li></ul>

## Process de l'équipe

```java
class PS5(){
    Date date = new Date("10 Novembre 2021","Fr");
    Date deadLine = new Date("18 Decembre 2021","Fr");
    
    Work citadelle = new Project();
    Git git = new Github(citadelle);
    Discord discord = new DiscordServer(citadelle);
    
    void planning(){
        while (date <= deadLine)
        {
            if(date.day().equals("Mercredi")){
                git.closeMilestone();

                citadelle.exhibition();
                citadelle.meeting();

                git.openMilestone();
            }else{
                if(discord.agreement()&&git.issueCreated()){
                    citadelle.tasksDistribution();
                    citadelle.enhancement();
                }else{
                    discord.meeting();
                }
            }

            date.plusOneDay();
        }
    }
}  
```
Ainsi le processus de l'équipe, comme résumé avec le code ci-dessus, est de fonctionner en sprint d'une semaine avec création d'une milestone dans laquelle différentes issues étaient créées correspondant aux objectifs de la semaine. Ensuite, il y avait une réunion et une présentation du projet tous les mercredis pour discuter de la suite et des objectifs du nouveau sprint.

Lors de la réalisation de ce projet, nous avons énormément utilisé l'outil Git. Nous avons aussi appris à utiliser Maven, d'ailleurs, nous avons eu l'occasion d'ajouter <a href="https://github.com/dialex/JColor">Jcolor</a> dans les dépendances du fichier pom.xml.

## Avancement sur les fonctionnalités
### Nos fonctionnalités
Nous avons des bots, jouables de 4 à 7, possédant trois niveaux de difficultés.</br>
Tous les quartiers (districts) (y compris les spéciaux) ont été implémenté.</br>
Tous les rôles (characters) ont été implémentés.</br>
On peut donc en conclure que tout le jeu a été implémentés.</br>
Le jeu possède aussi un affichage fonctionnel et détaillé ! Voir les issues associées aux logs 
<a href="https://github.com/pns-si3-projects/projet2-ps5-21-22-ps5-21-22-projet2-e/issues/14">14</a>
<a href="https://github.com/pns-si3-projects/projet2-ps5-21-22-ps5-21-22-projet2-e/issues/26">26</a>
<a href="https://github.com/pns-si3-projects/projet2-ps5-21-22-ps5-21-22-projet2-e/issues/32">32</a>
<a href="https://github.com/pns-si3-projects/projet2-ps5-21-22-ps5-21-22-projet2-e/issues/45">56</a>
</br>
### De nouveaux bots pourraient être implémentés.
L'implémentation de nouveaux bots permettrait de tester si la logique du botSmart est réellement "smart". Par exemple, un bot Agressif qui tente de finir le jeu le plus vite possible, un bot Merveille qui ne placera que des merveilles...</br>
### "Refactor" le code
Certaines parties comportent des "statics", et les classes "discutent" trop entre-elles. </br>
Se référer au principe "High cohesion, low coupling étudié en cours de programmation orienté objet".</br>

## Qualité du code
Au fur et à mesure des semaines, nous avons été contraints de *refactor* notre code. Par exemple, un de nos premiers changement a été d'intégrer des énumérations à la place de multiples constantes.</br>

Dès les premiers jours avec l'<a href="https://github.com/pns-si3-projects/projet2-ps5-21-22-ps5-21-22-projet2-e/issues/12">issue 12</a>, 
nous avons été contraint de revoir la structure des bots.</br>

Notre principal problème était de corriger la conception de nos classes qui comportaient un constructeur de tous les objets.</br> Dans l'ensemble, nous avons diminué ses erreurs de conception par l'intermédiaire de nombreux *reformats*.
</br>

Les *deadlines* nous ont obligé à prendre des décisions rapides. Ainsi plutôt que de passer toute la semaine à refactor le projet certains compromis ont été réalisé et donc nous avons utilisé des méthodes static.
</br>

Les tests unitaires du package citadelles couvrent 97% de nos classes, 86% des méthodes et 80% de nos lignes pour un total de 225 tests.

## Rétrospective

Ce projet a été très constructif, nous avons pu appliquer de nombreux concepts vus en cours de programmation orientée objet comme les énumérations, les classes abstraites et la notion d'héritage, les tests unitaires, etc.</br>
De plus, nous avons découvert les bases de Maven, et nous nous sommes habitués à l'utilisation de Git.</br>
Nous avons également utilisé pour la première fois le système de gestion des issues/tickets de GitHub. Nos commits ont été taggés avec le numéro des issues.</br>
Ce projet nous a permis de déveloper de nombreuses compétences de travail en équipe :
<ul>
<li>Le respect des deadlines</li>
<li>La coopération</li>
<li>L'importance des réunions de groupe</li>
</ul>

![Diagramme](https://github.com/pns-si3-projects/projet2-ps5-21-22-ps5-21-22-projet2-e/blob/master/doc/Diagram.png)


