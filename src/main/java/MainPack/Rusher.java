package MainPack;

import GraphicsPack.MapPanel;
import Tiles.Tile;
import Units.Unit;

import java.util.ArrayList;
import java.util.Map;

public class Rusher extends Player
{
	public Rusher(String color)
	{
		super(color);
	}
	
	@Override
	public boolean play()
	{
		Game game = Game.getInstance();
		MapPanel map = game.getMap();
		Map<Unit, int[]> units = map.getCoordOfAllUnits();

		for (Unit unit : units.keySet()){
			if (unit.getPlayer() == this){
				int[] coords = units.get(unit);
				Map<Tile, int[]> tiles = map.scanTilesAroundUnit(unit.getSpeed());
			}
			map.repaint();
		}

		return isWinner();
	}
}
