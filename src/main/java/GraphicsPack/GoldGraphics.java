package GraphicsPack;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.Font;
import java.awt.FontMetrics;

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
		
		m_strGold = String.format("G.     %d", m_gold);
		
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
		
		setVisible(true);
		revalidate();
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int arcWidth = 50;
		int arcHeight = 50;
		
		Graphics2D g2d = (Graphics2D)g;
		GradientPaint gradient = new GradientPaint(0, 0, m_playerColor.darker(), m_width, m_height, m_playerColor.brighter());
		
		g2d.setPaint(gradient);
		g2d.fillRoundRect(0, 0, m_width, m_height, arcWidth, arcHeight);
		
		g2d.setColor(m_playerColor.darker());
		g2d.drawRoundRect(0, 0, m_width - 1, m_height - 1, arcWidth, arcHeight);
		
		g.setColor(Color.WHITE);
		
		Font font = g.getFont().deriveFont(Font.BOLD, 14);
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics(font);
		
		int textWidth = metrics.stringWidth(m_strGold);
		int textHeight = metrics.getHeight();
		
		int x = (m_width - textWidth) / 2;
		int y = (m_height - textHeight) / 2 + metrics.getAscent();
		
		g.drawString(m_strGold, x, y);
	}
}
