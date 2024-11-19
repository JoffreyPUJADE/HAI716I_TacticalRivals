package MainPack;

import GraphicsPack.MapPanel;
import GraphicsPack.Window;
import GraphicsPack.ActionHistory;
import Tiles.City;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Game {
	private MapPanel m_map;
	private ActionHistory m_history;
	private static ArrayList<Player> m_players = new ArrayList<>(Arrays.asList(new Capturer("blue"), new Killer("red")));
	private static Game instance;

	public Game()
	{
		this.m_map = new MapPanel();
		instance = this;
		int[] map_size = m_map.getMapSize();

		Window window = new Window("Tactical Rivals", map_size[0], map_size[1]);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		m_history = new ActionHistory(screenSize.width - screenSize.width / 3, 0, screenSize.width / 4, screenSize.height - 96);
		m_history.addAction(true, "Le jeu a été lancé.");
		m_history.addAction("Initialisation.");
		
		m_map.setBounds(25, 25, map_size[0] * 67, map_size[1] * 67);

		window.setBounds(0, 0, screenSize.width, screenSize.height);
		window.add(m_map);
		window.add(m_history);
		
		m_history.addAction("Fin de l'initialisation.");
		m_history.repaint();
	}
	
	public void run()
	{
		try {
			boolean anyWinner = false;
			int turn = 1;
			while (!anyWinner){
				for (Player player : m_players) {
					String turnAnnouncement = String.format("------------ Turn %d of player %s ------------", turn, player.getColor());
					System.out.println("\n\n" + turnAnnouncement);
					m_history.addAction(true, turnAnnouncement);
					m_history.repaint();
					
					TimeUnit.MILLISECONDS.sleep(500);
					addGold(player);
					TimeUnit.MILLISECONDS.sleep(500);
					boolean isWinner = player.play();
					
					if (isWinner) {
						anyWinner = true;
						String winAnnouncement = String.format("------------Player %s win ! ------------", player.getColor());
						System.out.println("\n\n" + winAnnouncement);
						m_history.addAction(true, winAnnouncement);
						m_history.repaint();
						break;
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
		player.addGold(gold);
		
		String addGoldAction = String.format("%d gold added to player %s", gold, player.getColor());
		System.out.println(addGoldAction);
		m_history.addAction(addGoldAction);
		m_history.repaint();
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
	
	public ActionHistory getHistory()
	{
		return m_history;
	}
}
