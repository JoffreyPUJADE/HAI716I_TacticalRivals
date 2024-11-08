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
		int u1Damage = u2.getPower() - (u1.getArmor() % 10);
		int u2Damage = u1.getPower() - (u2.getArmor() % 10);
		
		u1.setHealth(u1.getHealth() - u1Damage);
		u2.setHealth(u1.getHealth() - u2Damage);
	}
	
	public int move(Unit u, Tile t)
	{
		/*
			Basique :
				- Tile occupée ?
					- On ne bouge pas.
				- Tile inoccupée ?
					- On la prend.
			/!\ : Ne pas oublier de "libérer" la tile précédemment occupée par l'unité. ie : trouver sur la map la tile courante de l'unité pour mettre "m_occupiedBy" à null.
		*/
		
		if(t.getOccupiedBy() != null)
		{
			return -1; // Tile occupée.
		}
		
		t.takeTile(u);
		
		return 1; // Tile inoccupée et prise.
	}
	
	public void capture(Infantry i, Urban u)
	{
		/*
			Basique :
			- Tile occupée ?
				- On attaque l'unité adverse
				- Unité adverse vivante ?
					- Attente du prochain tour
				- Unité adverse morte ?
					- La tile appartient à notre unité
			- Tile inoccupée ?
				- La tile appartient à notre unité
		*/
		
		Unit occupied = u.getOccupiedBy();
		
		if(occupied != null)
		{
			attack(i, occupied);
			
			/*if(!occupied.isDead())
			{
				return -1; // Tile occupée et unité adverse toujours vivante.
			}
			else
			{
				u.takeTile(i);
				
				return 0; // Tile occupée, unité adverse morte et tile prise.
			}*/
			
			if(occupied.isDead())
			{
				u.takeTile(i);
			}
		}
		
		// Arrivé ici : Tile inoccupée.
		
		u.takeTile(i);
		
		//return 1; // Tile inoccupée et prise.
	}
	
	public void generateUnit(Unit u, Factory f)
	{
		if(f.getOccupiedBy() == null && u.getCost() <= m_gold) // Rajouter le fait que le factory nous appartienne
		{
			f.takeTile(u); // L'unité est créée sur la factory.
		}
	}
	
	public abstract int play();

	public int getGold() {
		return m_gold;
	}

	public String getColor() {
		return m_color;
	}
}
