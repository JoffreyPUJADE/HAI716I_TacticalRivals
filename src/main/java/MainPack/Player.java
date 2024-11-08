package MainPack;

import Tiles.Tile;
import Tiles.Urban;
import Tiles.Factory;

import Units.Unit;
import Units.Infantry;

public abstract class Player
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
		int u1Damage = (int) (u2.getPower() - (u1.getArmor() * 0.5));
		int u2Damage = (int) (u1.getPower() - (u2.getArmor() * 0.2));
		
		u1.setHealth(u1.getHealth() - u1Damage);
		u2.setHealth(u1.getHealth() - u2Damage);
	}
	
	public void move(Unit u, Tile t)
	{
		t.takeTile(u);
	}
	
	public void capture(Infantry i, Urban u)
	{
		u.takeTile(i);
	}

	public void generateUnit(Unit u, Factory f)
	{
		if(f.getOccupiedBy() == null && u.getCost() <= m_gold) // Rajouter le fait que le factory nous appartienne
		{
			f.takeTile(u); // L'unité est créée sur la factory.
			m_gold -= u.getCost();
		}
	}

	public void addGold(int gold) {
		m_gold += gold;
	}
	
	public abstract int play();

	public int getGold() {
		return m_gold;
	}

	public String getColor() {
		return m_color;
	}
}
