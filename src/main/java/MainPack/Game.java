package MainPack;

import GraphicsPack.MapPanel;
import GraphicsPack.Window;

import java.util.ArrayList;

public class Game {

    private MapPanel m_map;

    public Game() {

        this.m_map = new MapPanel();

        int[] map_size = m_map.getMapSize();

        Window win = new Window("Tactical Rivals", map_size[0]*50, map_size[1]*50);
        
        win.add(m_map);
    }

    public int start() {
        return 0;
    }
}
