package Units;

import MainPack.Player;

public class Tank extends Unit
{
	public Tank(Player player)
	{
		super(10, 7, 7, 3, 1, player, "data/sprite/"+ player.getColor() +"/tank.png");
	}
}
