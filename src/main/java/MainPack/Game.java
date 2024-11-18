package MainPack;

import GraphicsPack.MapPanel;
import GraphicsPack.Window;
import Tiles.City;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Game {
	private MapPanel m_map;
	private static ArrayList<Player> m_players = new ArrayList<>(Arrays.asList(new Capturer("blue"), new Killer("red")));
	private static Game instance;

	public Game()
	{
		this.m_map = new MapPanel();
		instance = this;
		int[] map_size = m_map.getMapSize();

		Window window = new Window("Tactical Rivals", map_size[0], map_size[1]);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		window.setBounds(0, 0, screenSize.width, screenSize.height);
		window.add(m_map);
	}
	
	public void run()
	{
		try {
			boolean anyWinner = false;
			int turn = 1;
			while (!anyWinner){
				for (Player player : m_players) {
					System.out.println("\n\n------------ Turn " + turn + " of player " + player.getColor() + " ------------");
					TimeUnit.MILLISECONDS.sleep(500);
					addGold(player);
					TimeUnit.MILLISECONDS.sleep(500);
					boolean isWinner = player.play();
					if (isWinner) {
						anyWinner = true;
						System.out.println("\n\n------------Player " + player.getColor() + " win ! ------------");
					}
				}
				turn++;
			}
		} catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

	public void addGold(Player player) {
		ArrayList<City> cities = m_map.getCities(player.getColor());
		int gold = cities.size() * 100;
		System.out.println(gold + " gold added to player " + player.getColor());
		player.addGold(gold);
	}
	
	public static ArrayList<Player> getPlayers()
	{
		return m_players;
	}

	public static Game getInstance() {
		return instance;
	}

	public MapPanel getMap() {
		return m_map;
	}
}
