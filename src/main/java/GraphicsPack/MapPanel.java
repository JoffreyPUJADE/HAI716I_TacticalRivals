package GraphicsPack;

import MainPack.Game;
import Tiles.*;
import Units.*;
import MainPack.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapPanel extends JPanel {

    private ArrayList<ArrayList<Tile>> m_map;
    private ArrayList<ArrayList<Unit>> m_units;

    public MapPanel() {
    	super();
    	setLayout(null);
    	
        MapData mapData = MapLoader.loadMap("data/map/map.json");
        if (mapData == null) {
            JOptionPane.showMessageDialog(null, "Map not found");
        } else {
            m_map = new ArrayList<>();

            ArrayList<ArrayList<TileData>> mapType = mapData.getMap();

            for (ArrayList<TileData> rowType : mapType) {
                ArrayList<Tile> row = new ArrayList<>();
                for (TileData tileType : rowType) {

                    String color = tileType.getColor(); // blue or red

                    switch (tileType.getType()) {
                        case "plains":
                            row.add(new Plain());
                            break;
                        case "city":
                            row.add(new City(color));
                            break;
                        case "water":
                            row.add(new Water());
                            break;
                        case "factory":
                            row.add(new Factory(color));
                            break;
                        case "base":
                            row.add(new Base(color));
                            break;
                        default:
                             System.out.println(tileType.getType());
                    }
                }
                m_map.add(row);
            }

            m_units = new ArrayList<>(m_map.size());
            for (int i = 0; i < m_map.size(); i++) {
                ArrayList<Unit> row = new ArrayList<>();
                for (int j = 0; j < m_map.get(i).size(); j++) {
                    Tile tile = m_map.get(i).get(j);
                    row.add(null);
                    if (tile instanceof Factory) {
                        String color = tile.getColor();
                        ArrayList<Player> players = Game.getPlayers();

                        for (Player player : players){
                            if (player.getColor().equals(color)){
                                Infantry soldat = new Infantry(player);
                                tile.takeTile(soldat);
                                row.set(j, soldat);
                            }
                        }
                    }
                }
                m_units.add(row);
            }
        }
        
        setVisible(true);
        revalidate();
        repaint();
    }

    public Map<Tile, int[]> scanTilesAroundUnit(int[] coords, int range) {
        int x = coords[0];
        int y = coords[1];
        Map<Tile, int[]> surroundingTiles = new HashMap<>();

        // Collecter toutes les tuiles dans le rayon
        for (int i = Math.max(0, x - range); i <= Math.min(m_map.size() - 1, x + range); i++) {
            for (int j = Math.max(0, y - range); j <= Math.min(m_map.get(i).size() - 1, y + range); j++) {
                int distanceSquared = (i - x) * (i - x) + (j - y) * (j - y);
                if (distanceSquared <= range * range) {
                    surroundingTiles.put(m_map.get(i).get(j), new int[]{i, j});
                }
            }
        }

        // Convertir le HashMap en une liste triée
        List<Map.Entry<Tile, int[]>> tileList = new ArrayList<>(surroundingTiles.entrySet());
        tileList.sort((e1, e2) -> {
            int[] pos1 = e1.getValue();
            int[] pos2 = e2.getValue();
            int dist1 = (pos1[0] - x) * (pos1[0] - x) + (pos1[1] - y) * (pos1[1] - y);
            int dist2 = (pos2[0] - x) * (pos2[0] - x) + (pos2[1] - y) * (pos2[1] - y);
            return Integer.compare(dist1, dist2);
        });

        // Recréer un HashMap dans l'ordre trié
        Map<Tile, int[]> sortedTiles = new HashMap<>();
        for (Map.Entry<Tile, int[]> entry : tileList) {
            sortedTiles.put(entry.getKey(), entry.getValue());
        }

        return sortedTiles;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int tileSize = 48;

        // Calculer les décalages pour centrer la carte
        int xOffset = (getWidth() - (m_map.get(0).size() * tileSize)) / 4;
        int yOffset = (getHeight() - (m_map.size() * tileSize)) / 2;
        
        // La position "globale" de la map étant définie et forcée dans Game, on ajuste la position "locale" de la map par rapport à la globale.
        xOffset += getX();
        yOffset += getY() * 2;

        // Première passe: Dessiner les tuiles normales (Plain, Water)
        for (int row = 0; row < m_map.size(); row++) {
            for (int col = 0; col < m_map.get(row).size(); col++) {
                Tile tile = m_map.get(row).get(col);
                if (tile instanceof Plain || tile instanceof Water) {
                    Image img = loadImage(tile.getSprite());

                    if (img != null) {
                        int width = img.getWidth(null);
                        int height = img.getHeight(null);
                        // Appliquer les décalages pour centrer les tuiles
                        g.drawImage(img, xOffset + col * tileSize, yOffset + row * tileSize, width * 3, height * 3, this);
                    }
                }
            }
        }

        // Deuxième passe: Dessiner les bâtiments (City, Factory, Base)
        for (int row = 0; row < m_map.size(); row++) {
            for (int col = 0; col < m_map.get(row).size(); col++) {
                Tile tile = m_map.get(row).get(col);
                if (tile instanceof City || tile instanceof Factory || tile instanceof Base) {
                    Image img = loadImage(tile.getSprite());

                    if (img != null) {
                        int width = img.getWidth(null);
                        int height = img.getHeight(null);
                        // Appliquer les décalages et ajuster la position verticale pour simuler le dépassement
                        g.drawImage(img, xOffset + col * tileSize, yOffset + (row * tileSize) - (int) (height*1.5), width * 3, height * 3, this);
                    }
                }
            }
        }

        // Troisième passe: Dessiner les unités (Soldat, Tank, Artillerie)
        for (int row = 0; row < m_units.size(); row++) {
            for (int col = 0; col < m_units.get(row).size(); col++) {
                Unit unit = m_units.get(row).get(col);
                if (unit != null) {
                    Image img = loadImage(unit.getSprite());

                    if (img != null) {
                        int width = img.getWidth(null);
                        int height = img.getHeight(null);
                        // Appliquer les décalages et ajuster la position verticale pour simuler le dépassement
                        g.drawImage(img, xOffset + col * tileSize, yOffset + (row * tileSize), width * 3, height * 3, this);
                    }
                }
            }
        }
    }

    public ArrayList<City> getCities(String color) {
        ArrayList<City> cities = new ArrayList<>();
        for (int i = 0; i < m_map.size(); i++) {
            for (int j = 0; j < m_map.get(i).size(); j++){
                City city = m_map.get(i).get(j) instanceof City ? (City) m_map.get(i).get(j) : null;
                if (city != null && city.getColor().equals(color)){
                    cities.add(city);
                }
            }
        }
        return cities;
    }

    public Map<Unit, int[]> getCoordOfAllUnits() {
        Map<Unit, int[]> units = new HashMap<>();
        for (int i = 0; i < m_units.size(); i++) {
            for (int j = 0; j < m_units.get(i).size(); j++) {
                Unit unit = m_units.get(i).get(j);
                if (unit != null) {
                    units.put(unit, new int[]{i, j});
                }
            }
        }
        return units;
    }

    public Map<Factory, int[]> getCoordOfAllFactories() {
        Map<Factory, int[]> factories = new HashMap<>();
        for (int i = 0; i < m_map.size(); i++) {
            for (int j = 0; j < m_map.get(i).size(); j++) {
                Tile tile = m_map.get(i).get(j);
                if (tile instanceof Factory) {
                    factories.put((Factory) tile, new int[]{i, j});
                }
            }
        }
        return factories;
    }

    public Map<Base, int[]> getOpponentBase(String color) {
        Map<Base, int[]> bases = new HashMap<>();
        for (int i = 0; i < m_map.size(); i++) {
            for (int j = 0; j < m_map.get(i).size(); j++) {
                if (m_map.get(i).get(j) instanceof Base){
                    if (!m_map.get(i).get(j).getColor().equals(color)) {
                        bases.put((Base) m_map.get(i).get(j), new int[]{i, j});
                    }
                }
            }
        }
        return bases;
    }

    // Méthode pour charger l'image depuis le chemin donné
    private Image loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int[] getMapSize(){
        int[] size = new int[2];
        size[1] = m_map.size();
        size[0] = m_map.get(0).size();
        return size;
    }

    public ArrayList<ArrayList<Tile>> getMap() {
        return m_map;
    }

    public ArrayList<ArrayList<Unit>> getUnits() {
        return m_units;
    }
}
