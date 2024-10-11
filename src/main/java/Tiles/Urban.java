package Tiles;

public abstract class Urban extends Tile
{
	public Urban()
	{
		m_defense = 3;
		m_obstacle = false;
		m_occupiedBy = null;
	}
}
