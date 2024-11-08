package MainPack;

import GraphicsPack.MapPanel;
import GraphicsPack.Window;
import Tiles.City;
import Tiles.Urban;

import java.util.ArrayList;
import java.util.Arrays;

public class Game {
	private MapPanel m_map;
	private static ArrayList<Player> m_players = new ArrayList<>(Arrays.asList(new Capturer("blue"), new Rusher("red")));

	public Game()
	{
		this.m_map = new MapPanel();
		
		int[] map_size = m_map.getMapSize();
		
		Window win = new Window("Tactical Rivals", map_size[0]*100, map_size[1]*100);
		
		win.add(m_map);
	}
	
	public void run()
	{
		boolean anyWinner = false;
		while (!anyWinner){
			for (Player player : m_players){

				int isWinner = player.play();
				if (isWinner == 0) {
					anyWinner = true;
					System.out.println("Player " + player.getColor() + "  win !");
				}
			}
		}
	}

	public void addGold(Player player) {
		ArrayList<City> cities = m_map.getCities(player.getColor());
		player.addGold(cities.size() * 100);
	}
	
	public static ArrayList<Player> getPlayers()
	{
		return m_players;
	}
}
