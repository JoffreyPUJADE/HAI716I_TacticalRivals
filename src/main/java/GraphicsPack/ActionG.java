package GraphicsPack;

import javax.swing.*;
import java.awt.*;

public class ActionG extends JPanel
{
	private boolean m_urgent;
	private String m_message;
	private int m_x;
	private int m_y;
	private int m_width;
	private int m_height;
	private JLabel m_actionLabel;
	
	public ActionG(boolean urgence, String message, int x, int y, int width, int height) {
		super();
		
		m_urgent = urgence;
		m_message = message;
		m_x = x;
		m_y = y;
		m_width = width;
		m_height = height;
		
		redraw();
		setVisible(true);
	}
	
	public String getMessage() {
		return m_message;
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
	
	public void setUrgence(boolean urgence) {
		m_urgent = urgence;
	}
	
	public void setMessage(String message) {
		m_message = message;
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
	
	public boolean isUrgent() {
		return m_urgent;
	}
	
	@Override
	public String toString() {
		return m_message;
	}
	
	public void redraw() {
		removeAll();
		
		setLayout(null);
		setBounds(m_x, m_y, m_width, m_height);
		
		m_actionLabel = new JLabel(m_message);
		m_actionLabel.setBounds(5, 5, m_width, m_height);
		m_actionLabel.setForeground(m_urgent ? Color.RED : Color.BLACK);
		add(m_actionLabel);
		
		setVisible(true);
		
		revalidate();
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawRect(0, 0, m_width - 1, m_height - 1);
	}
}
