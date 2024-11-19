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
		Game game = Game.getInstance();
		MapPanel mapPanel = game.getMap();
		ArrayList<ArrayList<Unit>> m_units = mapPanel.getUnits();
		ArrayList<ArrayList<Tile>> m_map = mapPanel.getMap();


		int u1Damage = (int) (u2.getPower() - (u1.getArmor() * 0.5));
		int u2Damage = (int) (u1.getPower() - (u2.getArmor() * 0.2));

		u1.setHealth(u1.getHealth() - u1Damage);
		u2.setHealth(u2.getHealth() - u2Damage);

		for (int i = 0; i < m_units.size(); i++) {
			for (int j = 0; j < m_units.get(i).size(); j++) {
				Unit currentUnit = m_units.get(i).get(j);

				if (currentUnit == u1 && u1.isDead()) {
					m_units.get(i).set(j, null);
					m_map.get(i).get(j).takeTile(null);
					System.out.println("The " + u1.getPlayer().getColor()  + " " + u1.getClass().getSimpleName().toLowerCase() + " in " + Arrays.toString(new int[]{i, j}) + " has been destroyed !" );
				}

				if (currentUnit == u2 && u2.isDead()) {
					m_units.get(i).set(j, null);
					m_map.get(i).get(j).takeTile(null);
					System.out.println("The " + u2.getPlayer().getColor()  + " " + u2.getClass().getSimpleName().toLowerCase() + " in " + Arrays.toString(new int[]{i, j}) + " has been destroyed !" );
				}
			}
		}

		mapPanel.repaint();
	}

	public void capture(Unit u1, int[] coords)
	{
		Game game = Game.getInstance();
		MapPanel mapPanel = game.getMap();
		ArrayList<ArrayList<Tile>> map = mapPanel.getMap();

		Tile tile = map.get(coords[0]).get(coords[1]);

		if (tile.getColor().equals(u1.getPlayer().getColor())) {
			return;
		}

		tile.takeTile(u1);
		tile.setColor(u1.getPlayer().getColor());
		tile.setSprite(u1.getPlayer().getColor());

		System.out.println("The "+ tile.getClass().getSimpleName().toLowerCase() + " in " + Arrays.toString(new int[]{coords[0], coords[1]}) + " has been captured by the " + u1.getPlayer().getColor() + " player !" );
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

		System.out.println("The factory in " + Arrays.toString(new int[]{coords[0], coords[1]}) + " produced a " + u.getClass().getSimpleName().toLowerCase() + " !" );
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
		int speed = unit.getSpeed();

		Tile targetTile = t.keySet().iterator().next();
		int[] targetPosition = t.get(targetTile);

		List<Node> openList = new ArrayList<>();
		Set<Node> closedList = new HashSet<>();
		Map<Node, Node> cameFrom = new HashMap<>();

		Node startNode = new Node(unitPosition[0], unitPosition[1], null, 0, calculateHCost(unitPosition, targetPosition));
		openList.add(startNode);

		while (!openList.isEmpty()) {
			Node currentNode = openList.stream()
					.min(Comparator.comparingInt(node -> node.fCost))
					.orElseThrow(() -> new NoSuchElementException("Aucun noeud trouvé dans la liste ouverte"));
			openList.remove(currentNode);
			closedList.add(currentNode);

			if (currentNode.x == targetPosition[0] && currentNode.y == targetPosition[1]) {
				List<Node> path = reconstructPath(cameFrom, currentNode);
				objectifReached = moveAlongPath(path, u, m_map, m_units, unit, speed);
				break;
			}

			List<Node> neighbors = getNeighbors(currentNode, m_map, m_units, targetPosition, speed, unit);

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

	private List<Node> getNeighbors(Node node, ArrayList<ArrayList<Tile>> map, ArrayList<ArrayList<Unit>> units, int[] targetPosition, int speed, Unit currentUnit) {
		List<Node> neighbors = new ArrayList<>();

		// Déplacements possibles (haut, bas, gauche, droite)
		int[] dx = {-1, 1, 0, 0};
		int[] dy = {0, 0, -1, 1};

		for (int i = 0; i < 4; i++) {
			int newX = node.x + dx[i];
			int newY = node.y + dy[i];


			if (isInBounds(newX, newY, map) && !isObstacle(map, units, newX, newY, currentUnit)) {
				int hCost = calculateHCost(new int[]{newX, newY}, targetPosition);
				neighbors.add(new Node(newX, newY, node, node.gCost + 1, hCost));
			}
		}
		return neighbors;
	}


	private boolean isObstacle(ArrayList<ArrayList<Tile>> map, ArrayList<ArrayList<Unit>> units, int x, int y, Unit currentUnit) {
		return map.get(x).get(y).isObstacle() || (units.get(x).get(y) != null && units.get(x).get(y) != currentUnit);
	}

	private boolean isInBounds(int x, int y, ArrayList<ArrayList<Tile>> map) {
		return x >= 0 && x < map.size() && y >= 0 && y < map.get(x).size();
	}

	private boolean moveAlongPath(List<Node> path, Map<Unit, int[]> u, ArrayList<ArrayList<Tile>> m_map, ArrayList<ArrayList<Unit>> m_units, Unit unit, int speed) {
		Game game = Game.getInstance();
		MapPanel mapPanel = game.getMap();

		int distanceTravelled = 0;

		for (Node node : path) {
			if (distanceTravelled >= speed) {
				break;
			}

			Tile tile = m_map.get(node.x).get(node.y);
			if (!tile.isObstacle() && m_units.get(node.x).get(node.y) == null) {
				Tile currentTile = m_map.get(u.get(unit)[0]).get(u.get(unit)[1]);
				currentTile.takeTile(null);
				m_units.get(u.get(unit)[0]).set(u.get(unit)[1], null);


				u.put(unit, new int[]{node.x, node.y});
				tile.takeTile(unit);
				m_units.get(node.x).set(node.y, unit);

				mapPanel.repaint();

				try {
					TimeUnit.MILLISECONDS.sleep(500);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}

				distanceTravelled++;

				if (node.x == path.get(path.size() - 1).x && node.y == path.get(path.size() - 1).y) {
					return true;
				}
			}
		}

		return false;
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

	private int calculateHCost(int[] currentPosition, int[] targetPosition) {

		return Math.abs(currentPosition[0] - targetPosition[0]) + Math.abs(currentPosition[1] - targetPosition[1]);
	}


	// Classe Node pour A*
	private static class Node {
		int x, y;        // Coordonnées du nœud
		Node parent;     // Référence au nœud parent (pour reconstruire le chemin)
		int gCost;       // Coût du chemin depuis le nœud de départ
		int hCost;       // Coût heuristique (distance de Manhattan dans ce cas)
		int fCost;       // Coût total (gCost + hCost)


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
