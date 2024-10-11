package GraphicsPack;

import Tiles.*;
import Units.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
        }
        setVisible(true);
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Taille d'une tile (par exemple 32x32 pixels)
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
}
