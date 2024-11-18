package Units;

import MainPack.Player;

public class Infantry extends Unit
{
	public Infantry(Player player)
	{
		super(10, 3, 3, 4, 1, player, "data/sprite/"+ player.getColor() +"/soldat.png", 10);
	}
}
