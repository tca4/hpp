# Auteurs du projet : Julien TISSIER (tca4), Nader BEN ABDELJELIL (nad05)

## Benchmarks

Les benchmarks du projet sont au nombre de 3, et ont été réalisés sur le fichier sorted_data.csv de 2 milions de DebsRecord du site DEBS Challenge 2015.

Les benchmarks sont :
- BenchmarkQuery1 : benchmark de la query 1
- BenchmarkQuery2 : benchmark de la query 2
- BenchmarkQueryProjet : benchmark des deux query exécutées simultanément

Ces fichiers se trouvent dans le package src/main/java/fr/tse/fi2/hpp/benchmarks.

Nous avons réalisé les benchmarks sur les machines de l'école. Voici nos résultats :

- BenchmarkQuery1 : 19,5 secondes
- BenchmarkQuery2 : 1 min 06 secondes
- BenchmarkQueryProjet : 1 min 09 secondes

## Explication du fonctionnement des queries

Les queries se trouvent dans le package src/main/java/projet. Les résultats de traitement sont formatés comme il est stipulé sur le site DEBS Challenge 2015. Notre delay est calculé en nanosecondes.

### Query 1 : MostCommonRoutes

A chaque nouvelle entrée de DebsRecord, on calcule sa route (concaténation de sa case de départ et de sa case d'arrivéé) et on incrémente le compteur de cette route. Cette route est stockée dans une HashMap. La clé est la route, le valeur est une instance de la classe FrequenceRoute, qui contient un compteur d'occurences. Nous ajoutons cette route à une arrayList de debsRecord, qui représente la fenêtre de 30 min sur laquelle les courses sont considérées. Nous décrementons ensuite les occurences des routes ne faisant plus partie de cette fenêtre. Nous trions ensuite les routes selon leur occurences mais nous ne faisons le tri que sur les routes dont l'occurence a été modifiée, car seules celles là sont suceptibles de changer de place dans le tableau des 10 routes les plus fréquentes.
Notre algorithme ne prend pas en compte les courses dont une des cases (soit de depart, soit d'arrivée) ne se trouvent pas dans la grille.

### Query 2 : MostProfitableAreas

A chaque nouvelle entrée de DebsRecord, on ajoute le gain de la course (fare_amount + tip_amount) dans la case de départ. Les cases sont stockées dans une hashmap, dont la clé est une case, et la valeur est une instance de la classe Profitability, qui contient une liste de tous les gains de la case. On ajoute aussi ce record à une arrayList qui représente une fenêtre de 15 min, et une autre de 30 min. Cette course étant terminée, un taxi vide se trouve dans la case d'arrivée. On ajoute le médaillon du taxi à la liste des taxis vides se trouvant dans la case d'arrivée. On regarde si ce taxi était vide, c'est-à-dire s'il est dans la liste des taxis vide de sa derniere position (grâce à la hashmap lastPositionofTaxi). Si c'est le cas, cela veut dire que ce taxi a trouvé une nouvelle course, donc il ne fait plus partie des taxis vides de cette case, et donc on l'enlève. On soustrait les montants des courses qui ne font plus partie de la fenêtre des 15 min, et on enlève les taxis des liste de taxis vides s'ils ne font plus partie de la fenêtre des 30 min. Ensuite nous trions les cases selon leur profitabilité, mais nous trions seulement les cases dont soit le nombre de taxis a été modifié, soit les gains ont été modifiés, car seules ces cases là sont susceptibles de changer de places dans le tableau des 10 cases les plus rentables.
Notre algorithme ne prend pas en compte :
- les cases ne faisat pas partie de la grille
- les gains dont soit le fare_amount est négatif, soit le tip_amount est négatif
- les cases dont le nombre de taxis vides est 0 (car sinon division par 0 dans le calcul de la profitabilité)


