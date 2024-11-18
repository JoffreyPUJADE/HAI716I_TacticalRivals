package Units;

import MainPack.Player;

public class Artillery extends Unit
{
	public Artillery(Player player)
	{
		super(10, 6, 2, 3, 3, player, "data/sprite/"+ player.getColor() +"/artillerie.png", 30);
	}
}
