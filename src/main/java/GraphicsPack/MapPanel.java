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
import java.util.Map;

public class MapPanel extends JPanel {

    private ArrayList<ArrayList<Tile>> m_map;
    private ArrayList<ArrayList<Unit>> m_units;

    public MapPanel() {
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
                    if (tile instanceof City) {
                        String color = tile.getColor();
                        ArrayList<Player> players = Game.getPlayers();

                        for (Player player : players){
                            if (player.getColor().equals(color)){
                                row.set(j, new Infantry(player));
                            }
                        }
                    }
                }
                m_units.add(row);
            }
        }
        setVisible(true);
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int tileSize = 32;

        // Calculer les décalages pour centrer la carte
        int xOffset = (getWidth() - (m_map.get(0).size() * tileSize)) / 2;
        int yOffset = (getHeight() - (m_map.size() * tileSize)) / 2;

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
                        g.drawImage(img, xOffset + col * tileSize, yOffset + row * tileSize, width * 2, height * 2, this);
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
                        g.drawImage(img, xOffset + col * tileSize, yOffset + (row * tileSize) - (height), width * 2, height * 2, this);
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
                        g.drawImage(img, xOffset + col * tileSize, yOffset + (row * tileSize), width * 2, height * 2, this);
                    }
                }
            }
        }
    }
    
    public ArrayList<ArrayList<Unit>> getUnits(Player p)
    {
    	ArrayList<ArrayList<Unit>> arrayRes = new ArrayList<>();
    	
    	for(int i=0;i<m_units.size();++i)
    	{
    		ArrayList<Unit> arrayTemp = new ArrayList<Unit>();
    		
    		for(int j=0;j<m_units.get(i).size();++j)
    		{
    			if(m_units.get(i).get(j).getPlayer() == p)
    			{
    				arrayTemp.add(m_units.get(i).get(j));
    			}
    		}
    		
    		arrayRes.add(arrayTemp);
    	}
    	
    	return arrayRes;
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


    public Map<Urban, int[]> getCoordOfAllCapturableTiles(String playerColor) {
        Map<Urban, int[]> tiles = new HashMap<>();
        for (int i = 0; i < m_map.size(); i++) {
            for (int j = 0; j < m_map.get(i).size(); j++) {
                Urban tile = m_map.get(i).get(j) instanceof Urban ? (Urban) m_map.get(i).get(j) : null;
                if (tile != null && !tile.getColor().equals(playerColor)) {
                    tiles.put(tile, new int[]{i, j});
                }
            }
        }
        return tiles;
    }

    public ArrayList<Base> getBases() {
        ArrayList<Base> bases = new ArrayList<>();
        for (int i = 0; i < m_map.size(); i++) {
            for (int j = 0; j < m_map.get(i).size(); j++) {
                if (m_map.get(i).get(j) instanceof Base) {
                    bases.add((Base) m_map.get(i).get(j));
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
