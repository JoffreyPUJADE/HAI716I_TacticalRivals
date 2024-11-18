package MainPack;

import GraphicsPack.MapPanel;
import Tiles.Base;
import Tiles.Tile;

import Units.Unit;

import java.util.*;
import java.util.concurrent.TimeUnit;

public abstract class Player
{
	private int m_gold;
	private String m_color;

	public Player(String color)
	{
		m_color = color;
		m_gold = 500;
	}

	// Assume that U1 is unit of current player.
	public void attack(Unit u1, Unit u2)
	{
		int u1Damage = (int) (u2.getPower() - (u1.getArmor() * 0.5));
		int u2Damage = (int) (u1.getPower() - (u2.getArmor() * 0.2));

		u1.setHealth(u1.getHealth() - u1Damage);
		u2.setHealth(u1.getHealth() - u2Damage);
	}

	public void move(Unit u, Tile t)
	{
		t.takeTile(u);
	}

	public void capture(Unit u1, int[] coords)
	{
		Game game = Game.getInstance();
		MapPanel mapPanel = game.getMap();
		ArrayList<ArrayList<Tile>> map = mapPanel.getMap();

		Tile tile = map.get(coords[0]).get(coords[1]);
		System.out.println(tile.getSprite() + " " + tile.getColor());
		tile.takeTile(u1);
		tile.setColor(u1.getPlayer().getColor());
		tile.setSprite(u1.getPlayer().getColor());

		System.out.println(tile.getSprite() + " " + tile.getColor());
		mapPanel.repaint();
	}

	public void generateUnit(Unit u, int[] coords)
	{
		Game game = Game.getInstance();
		MapPanel mapPanel = game.getMap();
		ArrayList<ArrayList<Unit>> units = mapPanel.getUnits();
		ArrayList<ArrayList<Tile>> map = mapPanel.getMap();

		Tile tile = map.get(coords[0]).get(coords[1]);
		tile.takeTile(u);

		units.get(coords[0]).set(coords[1], u);
		m_gold -= u.getCost();
		mapPanel.repaint();
	}

	public void addGold(int gold) {
		m_gold += gold;
	}

	public boolean isWinner() {
		Game game = Game.getInstance();
		MapPanel map = game.getMap();
		Map<Base, int[]> base = map.getOpponentBase(getColor());
        return base.isEmpty();
	}

	public abstract boolean play();

	public int getGold() {
		return m_gold;
	}

	public String getColor() {
		return m_color;
	}


	public boolean moveDistanceManhattan(Map<Unit, int[]> u, Map<Tile, int[]> t) {
		Game game = Game.getInstance();
		MapPanel mapPanel = game.getMap();
		ArrayList<ArrayList<Tile>> m_map = mapPanel.getMap();
		ArrayList<ArrayList<Unit>> m_units = mapPanel.getUnits();
		boolean objectifReached = false;

		Unit unit = u.keySet().iterator().next();
		int[] unitPosition = u.get(unit);
		int speed = unit.getSpeed();  // Récupérer la vitesse de l'unité

		Tile targetTile = t.keySet().iterator().next();
		int[] targetPosition = t.get(targetTile);  // C'est la destination

		// Exécution de l'algorithme A*
		List<Node> openList = new ArrayList<>();
		Set<Node> closedList = new HashSet<>();
		Map<Node, Node> cameFrom = new HashMap<>();  // Pour reconstruire le chemin

		Node startNode = new Node(unitPosition[0], unitPosition[1], null, 0, calculateHCost(unitPosition, targetPosition));
		openList.add(startNode);

		while (!openList.isEmpty()) {
			// Tri de la liste pour obtenir le nœud avec le coût total le plus bas (F-cost)
			Node currentNode = openList.stream()
					.min(Comparator.comparingInt(node -> node.fCost))
					.orElseThrow(() -> new NoSuchElementException("Aucun nœud trouvé dans la liste ouverte"));
			openList.remove(currentNode);
			closedList.add(currentNode);

			// Si la cible est atteinte, on arrête
			if (currentNode.x == targetPosition[0] && currentNode.y == targetPosition[1]) {
				// Reconstruct the path
				List<Node> path = reconstructPath(cameFrom, currentNode);
				objectifReached = moveAlongPath(path, u, m_map, m_units, unit, speed);  // Déplacer l'unité selon le chemin trouvé
				break;
			}

			// Recherche des voisins
			List<Node> neighbors = getNeighbors(currentNode, m_map, m_units, targetPosition, speed);

			for (Node neighbor : neighbors) {
				if (closedList.contains(neighbor)) continue;

				int tentativeGCost = currentNode.gCost + 1;  // Supposons que le coût pour chaque mouvement est de 1

				// Si ce nœud est dans la liste ouverte mais avec un coût plus élevé, on ignore ce voisin
				if (!openList.contains(neighbor) || tentativeGCost < neighbor.gCost) {
					cameFrom.put(neighbor, currentNode);  // Enregistrer le parent du voisin
					neighbor.gCost = tentativeGCost;
					neighbor.fCost = neighbor.gCost + neighbor.hCost;

					if (!openList.contains(neighbor)) {
						openList.add(neighbor);
					}
				}
			}
		}

		return objectifReached;
	}

	private List<Node> getNeighbors(Node node, ArrayList<ArrayList<Tile>> map, ArrayList<ArrayList<Unit>> units, int[] targetPosition, int speed) {
		List<Node> neighbors = new ArrayList<>();

		// Déplacements possibles sur les axes X et Y (sans diagonale)
		int[] directions = {-1, 1};  // -1 : mouvement vers la gauche ou vers le haut, 1 : mouvement vers la droite ou vers le bas

		// Parcours des voisins uniquement sur les axes X et Y (pas de diagonale)
		for (int dx : directions) {
			int newX = node.x + dx;
			if (isInBounds(newX, node.y, map) && !isObstacle(map, newX, node.y)) {
				// Si l'unité peut se déplacer jusqu'à ce voisin, l'ajouter à la liste des voisins
				if (Math.abs(node.x - newX) + Math.abs(node.y - node.y) <= speed) {
					int hCost = calculateHCost(new int[]{newX, node.y}, targetPosition);
					neighbors.add(new Node(newX, node.y, node, node.gCost + 1, hCost));  // Créer un nœud voisin
				}
			}
		}

		for (int dy : directions) {
			int newY = node.y + dy;
			if (isInBounds(node.x, newY, map) && !isObstacle(map, node.x, newY)) {
				// Si l'unité peut se déplacer jusqu'à ce voisin, l'ajouter à la liste des voisins
				if (Math.abs(node.x - node.x) + Math.abs(node.y - newY) <= speed) {
					int hCost = calculateHCost(new int[]{node.x, newY}, targetPosition);
					neighbors.add(new Node(node.x, newY, node, node.gCost + 1, hCost));  // Créer un nœud voisin
				}
			}
		}

		return neighbors;
	}


	private boolean isObstacle(ArrayList<ArrayList<Tile>> map, int x, int y) {
		return map.get(x).get(y).isObstacle();  // Vérifier si la tuile est un obstacle
	}

	private boolean isInBounds(int x, int y, ArrayList<ArrayList<Tile>> map) {
		return x >= 0 && x < map.size() && y >= 0 && y < map.get(x).size();
	}

	private boolean moveAlongPath(List<Node> path, Map<Unit, int[]> u, ArrayList<ArrayList<Tile>> m_map, ArrayList<ArrayList<Unit>> m_units, Unit unit, int speed) {
		Game game = Game.getInstance();
		MapPanel mapPanel = game.getMap();

		int distanceTravelled = 0;  // Compteur pour suivre le nombre de cases parcourues

		for (Node node : path) {
			// Si l'unité a atteint ou dépassé sa vitesse maximale, elle doit s'arrêter
			if (distanceTravelled >= speed+1) {
				break;  // On arrête le mouvement si la distance maximale est atteinte
			}

			Tile tile = m_map.get(node.x).get(node.y);
			if (!tile.isObstacle()) {
				// Déplacer l'unité sur la tuile
				Tile currentTile = m_map.get(u.get(unit)[0]).get(u.get(unit)[1]);
				currentTile.takeTile(null); // Libère la tuile actuelle
				m_units.get(u.get(unit)[0]).set(u.get(unit)[1], null);

				u.put(unit, new int[]{node.x, node.y});  // Mise à jour de la position de l'unité
				tile.takeTile(unit);  // Occupe la nouvelle tuile
				m_units.get(node.x).set(node.y, unit);  // Mise à jour de la position de l'unité dans m_units

				mapPanel.repaint();  // Repaint pour afficher le mouvement

				// Attendre 1 seconde avant de déplacer l'unité à la suivante
				try {
					TimeUnit.SECONDS.sleep(1);  // Affichage à chaque déplacement
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}

				// Augmenter le compteur de distance parcourue
				distanceTravelled++;
			}
		}

		return distanceTravelled - 1 <= speed;
	}

	private List<Node> reconstructPath(Map<Node, Node> cameFrom, Node currentNode) {
		List<Node> path = new ArrayList<>();
		while (currentNode != null) {
			path.add(currentNode);
			currentNode = cameFrom.get(currentNode);
		}
		Collections.reverse(path);  // Reverser le chemin pour avoir l'ordre de départ à destination
		return path;
	}


	private void moveAlongPath(List<Node> path, Map<Unit, int[]> u, ArrayList<ArrayList<Tile>> m_map, ArrayList<ArrayList<Unit>> m_units, Unit unit) {
		Game game = Game.getInstance();
		MapPanel mapPanel = game.getMap();

		for (Node node : path) {
			Tile tile = m_map.get(node.x).get(node.y);
			if (!tile.isObstacle()) {
				// Déplacer l'unité sur la tuile
				Tile currentTile = m_map.get(u.get(unit)[0]).get(u.get(unit)[1]);
				currentTile.takeTile(null); // Libère la tuile actuelle
				m_units.get(u.get(unit)[0]).set(u.get(unit)[1], null);

				u.put(unit, new int[]{node.x, node.y});  // Mise à jour de la position de l'unité
				tile.takeTile(unit);  // Occupe la nouvelle tuile
				m_units.get(node.x).set(node.y, unit);  // Mise à jour de la position de l'unité dans m_units

				mapPanel.repaint();  // Repaint pour afficher le mouvement

				// Attendre 1 seconde avant de déplacer l'unité à la suivante
				try {
					TimeUnit.SECONDS.sleep(1);  // Affichage à chaque déplacement
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	private int calculateHCost(int[] currentPosition, int[] targetPosition) {
		// Calcul de la distance de Manhattan entre la position actuelle et la destination
		return Math.abs(currentPosition[0] - targetPosition[0]) + Math.abs(currentPosition[1] - targetPosition[1]);
	}

	private List<Node> getNeighbors(Node node, ArrayList<ArrayList<Tile>> map, ArrayList<ArrayList<Unit>> units, int[] targetPosition) {
		List<Node> neighbors = new ArrayList<>();
		int[] directions = {-1, 0, 1};  // Déplacements possibles sur les axes X et Y

		// Parcours des voisins adjacents
		for (int dx : directions) {
			for (int dy : directions) {
				if (dx == 0 && dy == 0) continue; // Ignorer le nœud actuel, pas de déplacement
				int newX = node.x + dx;
				int newY = node.y + dy;

				// Vérifier si le voisin est dans les limites de la carte
				if (isInBounds(newX, newY, map)) {
					// Calculer la H-cost pour l'heuristique (distance de Manhattan à la cible)
					int hCost = calculateHCost(new int[]{newX, newY}, targetPosition);
					neighbors.add(new Node(newX, newY, node, node.gCost + 1, hCost)); // Créer un nœud voisin
				}
			}
		}
		return neighbors;
	}

	private boolean isObstacle(ArrayList<ArrayList<Tile>> map, Node node) {
		return map.get(node.x).get(node.y).isObstacle();
	}

	// Classe Node pour A*
	private static class Node {
		int x, y;        // Coordonnées du nœud
		Node parent;     // Référence au nœud parent (pour reconstruire le chemin)
		int gCost;       // Coût du chemin depuis le nœud de départ
		int hCost;       // Coût heuristique (distance de Manhattan dans ce cas)
		int fCost;       // Coût total (gCost + hCost)

		// Constructeur pour créer un nœud
		Node(int x, int y, Node parent, int gCost, int hCost) {
			this.x = x;
			this.y = y;
			this.parent = parent;
			this.gCost = gCost;
			this.hCost = hCost;
			this.fCost = gCost + hCost; // fCost est la somme de gCost et hCost
		}

		// equals et hashCode pour Node afin de les comparer correctement dans les structures de données
		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null || getClass() != obj.getClass()) return false;
			Node node = (Node) obj;
			return x == node.x && y == node.y;
		}

		@Override
		public int hashCode() {
			return Objects.hash(x, y);
		}
	}

}
