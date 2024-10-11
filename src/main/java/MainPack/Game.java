package MainPack;

import GraphicsPack.MapPanel;
import GraphicsPack.Window;

import java.util.ArrayList;

public class Game {

    private MapPanel m_map;

    public Game() {

        this.m_map = new MapPanel();

        int[] map_size = m_map.getMapSize();

        Window win = new Window("Tactical Rivals", map_size[0]*32, map_size[1]*32);
        
        win.add(m_map);
    }

    public int start() {
        return 0;
    }
}
