package MainPack;

import GraphicsPack.MapPanel;
import GraphicsPack.Window;

import java.util.ArrayList;
import java.util.Arrays;

public class Game {
	private MapPanel m_map;
	private static ArrayList<Player> m_players = new ArrayList<>(Arrays.asList(new PlayerAttack("blue"), new PlayerAttack("red")));

	public Game()
	{
		this.m_map = new MapPanel();
		
		int[] map_size = m_map.getMapSize();
		
		Window win = new Window("Tactical Rivals", map_size[0]*50, map_size[1]*50);
		
		win.add(m_map);
	}
	
	public int start()
	{
		return 0;
	}
	
	public void turn()
	{
		
	}
	
	public static ArrayList<Player> getPlayers()
	{
		return m_players;
	}
}
