package MainPack;

import GraphicsPack.MapPanel;
import Tiles.Base;
import Tiles.Factory;
import Tiles.Tile;
import Tiles.Urban;
import Units.Artillery;
import Units.Infantry;
import Units.Tank;
import Units.Unit;

import java.util.*;

public class Killer extends Player
{
	public Killer(String color)
	{
		super(color);
	}
	
	@Override
	public boolean play()
	{
		Game game = Game.getInstance();
		MapPanel map = game.getMap();
		Map<Unit, int[]> units = map.getCoordOfAllUnits();
		Map<Factory, int[]> factories = map.getCoordOfAllFactories();

		// Action sur toutes les unités du joueur
		for (Unit unit : units.keySet()){
			boolean actionDone = false;
			if (unit.getPlayer() == this){
				int[] coords = units.get(unit);
				Map<Tile, int[]> tiles = map.scanTilesAroundUnit(coords, unit.getSpeed()-1);

				Map<Unit, int[]> infoUnit = new HashMap<>();
				Map<Tile, int[]> infoTile = new HashMap<>();

				infoUnit.put(unit, coords);
				// Stratégie : Chercher à attaquer les ennemis en premier, sinon capturer un objectif
				// sinon se diriger vers la base
				for (Tile tile : tiles.keySet()){

                    if (tile.getOccupiedBy() != null && !Objects.equals(tile.getOccupiedBy().getPlayer().getColor(), getColor())){
						infoUnit.put(unit, coords);
						infoTile.put(tile, tiles.get(tile));
						moveDistanceManhattan(infoUnit, infoTile);
                        actionDone = true;
                        break;
                    }
                }


				if (!actionDone){

					for (Tile tile : tiles.keySet()){
						if (tile instanceof Urban && !Objects.equals(tile.getColor(), getColor())){

							infoUnit.put(unit, coords);
							infoTile.put(tile, tiles.get(tile));
							boolean objectifReached = moveDistanceManhattan(infoUnit, infoTile);

							if (objectifReached){
								capture(unit, tiles.get(tile));
							}
							actionDone = true;
							break;
						}
					}

					if (!actionDone){
						Map<Base, int[]> base = map.getOpponentBase(getColor());
						if (base.isEmpty()) return true;
						infoUnit.put(unit, coords);

						Map.Entry<Base, int[]> entry = base.entrySet().iterator().next();
						infoTile.put(entry.getKey(), entry.getValue());
						moveDistanceManhattan(infoUnit, infoTile);
					}
				}

			}
		}

		for (Factory factory : factories.keySet()){
			if (Objects.equals(factory.getColor(), getColor()) && factory.getOccupiedBy() == null){
				int gold = getGold();
				int possibilies = 0;

				Artillery artillery = new Artillery(this);
				Tank tank = new Tank(this);
				Infantry infantry = new Infantry(this);

				if (artillery.getCost() <= gold){
					possibilies++;
				}
				if (infantry.getCost() <= gold){
					possibilies++;
				}
				if (tank.getCost() <= gold){
					possibilies++;
				}

				Random random = new Random();
				switch (random.nextInt(possibilies) + 1){
					case 1:
						generateUnit(infantry, factories.get(factory));
						break;
					case 2:
						generateUnit(tank, factories.get(factory));
						break;
					case 3:
						generateUnit(artillery, factories.get(factory));
						break;
				}
			}
		}

		return isWinner();
	}
}