package Tiles;

import Units.Unit;

public abstract class Tile
{
	protected int m_defense;
	protected boolean m_obstacle;
	protected Unit m_occupiedBy;
	protected String m_sprite;
	
	public Tile(int defense, boolean obstacle, Unit occupiedBy, String sprite)
	{
		m_defense = defense;
		m_obstacle = obstacle;
		m_occupiedBy = occupiedBy;
		m_sprite = sprite;
		
	}

	public String getSprite() {
		return m_sprite;
	}

	public Unit getOccupiedBy() {
		return m_occupiedBy;
	}

	public boolean isObstacle() {
		return m_obstacle;
	}

	public int getDefense() {
		return m_defense;
	}
}
