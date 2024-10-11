package GraphicsPack;

import Tiles.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MapPanel extends JPanel {

    private ArrayList<ArrayList<Tile>> m_map;

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
                    switch (tileType.getType()) {
                        case "plains":
                            row.add(new Plain());
                            break;
                        case "city":
                            row.add(new City());
                            break;
                        case "water":
                            row.add(new Water());
                            break;
                        case "factory":
                            row.add(new Factory());
                            break;
                        case "base":
                            row.add(new Base());
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

        // Première passe: Dessiner toutes les tuiles normales
        for (int row = 0; row < m_map.size(); row++) {
            for (int col = 0; col < m_map.get(row).size(); col++) {
                Tile tile = m_map.get(row).get(col);
                if (tile instanceof Plain || tile instanceof Water) {
                    Image img = loadImage(tile.getSprite());

                    if (img != null) {
                        int width = img.getWidth(null);
                        int height = img.getHeight(null);
                        g.drawImage(img, col * 32, row * 32, width * 2, height * 2, this);
                    }
                }
            }
        }

        // Deuxième passe: Dessiner les bâtiments ou autres objets qui peuvent dépasser
        for (int row = 0; row < m_map.size(); row++) {
            for (int col = 0; col < m_map.get(row).size(); col++) {
                Tile tile = m_map.get(row).get(col);
                if (tile instanceof City || tile instanceof Factory || tile instanceof Base) {
                    Image img = loadImage(tile.getSprite());

                    if (img != null) {
                        int width = img.getWidth(null);
                        int height = img.getHeight(null);
                        // Dessiner à une position légèrement décalée pour simuler le dépassement
                        g.drawImage(img, col * 32, (row * 32) - (height), width * 2, height * 2, this);
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
