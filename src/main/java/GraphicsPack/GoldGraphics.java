package GraphicsPack;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.JLabel;

public class GoldGraphics extends JPanel {
	private int m_x;
	private int m_y;
	private int m_width;
	private int m_height;
	private Color m_playerColor;
	private int m_gold;
	private int m_xTxt;
	private int m_yTxt;
	private String m_strGold;
	private JLabel m_labelGold;
	
	public GoldGraphics(int x, int y, int width, int height, Color playerColor, int gold) {
		super();
		
		m_x = x;
		m_y = y;
		m_width = width;
		m_height = height;
		
		m_playerColor = playerColor;
		m_gold = gold;
		
		m_xTxt = 5;
		m_yTxt = 5;
		
		setLayout(null);
		setBounds(m_x, m_y, m_width, m_height);
		
		m_labelGold = new JLabel();
		m_labelGold.setBounds(m_xTxt, m_yTxt, m_width, m_height);
		
		m_strGold = String.format("G.     %d", m_gold);
		m_labelGold.setText(m_strGold);
		
		add(m_labelGold);
		
		setVisible(true);
	}
	
	public int getPosX() {
		return m_x;
	}
	
	public int getPosY() {
		return m_y;
	}
	
	public int getWidth() {
		return m_width;
	}
	
	public int getHeight() {
		return m_height;
	}
	
	public int getGold() {
		return m_gold;
	}
	
	public void setPosX(int x) {
		m_x = x;
	}
	
	public void setPosY(int y) {
		m_y = y;
	}
	
	public void setWidth(int width) {
		m_width = width;
	}
	
	public void setHeight(int height) {
		m_height = height;
	}
	
	public void updateGold(int gold) {
		m_gold = gold;
		
		redraw();
	}
	
	public void redraw() {
		m_strGold = String.format("G.     %d", m_gold);
		m_labelGold.setText(m_strGold);
		
		setVisible(true);
		revalidate();
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(m_playerColor);
		g.drawRect(0, 0, m_width - 1, m_height - 1);
		g.fillRect(0, 0, m_width - 1, m_height - 1);
		g.setColor(Color.WHITE);
	}
}
