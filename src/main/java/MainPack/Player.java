package MainPack;

import Tiles.Tile;
import Tiles.Urban;
import Tiles.Factory;

import Units.Unit;
import Units.Infantry;

public class Player
{
	private int m_gold;
	private String m_color;
	
	public Player(String color)
	{
		m_color = color;
		m_gold = 500;
	}
	
	// Assume that U1 is unit of current player.
	public void attack(Unit u1, Unit u2)
	{
		int u1Damage = u2.getPower() - (u1.getArmor() % 10);
		int u2Damage = u1.getPower() - (u2.getArmor() % 10);
		
		u1.setHealth(u1.getHealth() - u1Damage);
		u2.setHealth(u1.getHealth() - u2Damage);
	}
	
	public int move(Unit u, Tile t)
	{
		return 0;
	}
	
	public void capture(Infantry i, Urban u)
	{
		//
	}
	
	public void generateUnit(Unit u, Factory f)
	{
		//
	}

	public int getGold() {
		return m_gold;
	}

	public String getColor() {
		return m_color;
	}
}
