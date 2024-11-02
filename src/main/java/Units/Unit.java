package Units;

import MainPack.Player;

public abstract class Unit
{
	protected int m_health;
	protected int m_power;
	protected int m_armor;
	protected int m_speed;
	protected int m_range;
	protected Player m_player;
	protected String m_sprite;
	
	public Unit(int health, int power, int armor, int speed, int range, Player player, String sprite)
	{
		m_health = health;
		m_power = power;
		m_armor = armor;
		m_speed = speed;
		m_range = range;
		m_player = player;
		m_sprite = sprite;
	}

	public int getHealth() {
		return m_health;
	}

	public int getPower() {
		return m_power;
	}

	public int getArmor() {
		return m_armor;
	}

	public int getSpeed() {
		return m_speed;
	}

	public int getRange() {
		return m_range;
	}
	
	public Player getPlayer()
	{
		return m_player;
	}

	public String getSprite() {
		return m_sprite;
	}

	public void setHealth(int health)
	{
		m_health = health;
	}
	
	public boolean isDead()
	{
		return m_health <= 0;
	}
}
