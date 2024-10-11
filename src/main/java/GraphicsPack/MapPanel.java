package GraphicsPack;

import Tiles.Plain;
import Tiles.Tile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MapPanel extends JPanel {

    private ArrayList<ArrayList<Tile>> m_map;

    public MapPanel() {
        m_map = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            ArrayList<Tile> tiles = new ArrayList<>();
            for (int j = 0; j < 30; j++) {
                tiles.add(new Plain());
            }
            m_map.add(tiles);
        }
        
        setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int row = 0; row < m_map.size(); row++) {
            for (int col = 0; col < m_map.get(row).size(); col++) {
                Tile tile = m_map.get(row).get(col);
                Image img = loadImage(tile.getSprite());

                if (img != null) {
                    g.drawImage(img, col * 32, row * 32, 32, 32, this);
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
}
