package Tiles;

public abstract class Urban extends Tile
{
	protected Player m_captured;
	
	public Urban(String sprite)
	{
		super(3, false, null, sprite);
	}
}
