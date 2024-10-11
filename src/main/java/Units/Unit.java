package Units;

public abstract class Unit
{
	protected int m_health;
	protected int m_power;
	protected int m_armor;
	protected int m_speed;
	protected int m_range;
	
	public Unit(int health, int power, int armor, int speed, int range)
	{
		m_health = health;
		m_power = power;
		m_armor = armor;
		m_speed = speed;
		m_range = range;
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
}
