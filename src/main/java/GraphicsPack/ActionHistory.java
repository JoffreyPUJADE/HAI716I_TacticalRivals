package GraphicsPack;

import java.util.ArrayList;

import javax.swing.*;
import java.awt.*;

public class ActionHistory extends JPanel {
	private int m_x;
	private int m_y;
	private int m_width;
	private int m_height;
	private int m_widthActions;
	private int m_heightActions;
	private ArrayList<ActionG> m_history;
	private JPanel m_contentPanel; // Panel qui contient les actions
	private JScrollPane m_scrollPane; // ScrollPane pour gérer le défilement
	
	public ActionHistory(int x, int y, int width, int height) {
		super();
		
		m_x = x;
		m_y = y;
		m_width = width;
		m_height = height;
		
		m_widthActions = m_width;
		m_heightActions = 50;
		
		System.out.println("x = " + m_x + ", y = " + m_y);
		
		m_history = new ArrayList<>();
		
		setLayout(null);
		setBounds(m_x, m_y, m_width, m_height);
		
		m_contentPanel = new JPanel();
		m_contentPanel.setLayout(null);
		
		// Création JScrollPane.
		m_scrollPane = new JScrollPane(m_contentPanel);
		m_scrollPane.setBounds(0, 0, m_width, m_height);
		m_scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		m_scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		m_scrollPane.setPreferredSize(new Dimension(m_width, m_height));
		
		add(m_scrollPane);
	}
	
	// Action en noir.
	public void addAction(String action) {
		addAction(new ActionG(false, action, 0, calculateYOfNewAction(), calculateWidthOfNewAction(action), m_heightActions));
	}
	
	// Action dont on définit la couleur (true : rouge, false : noir).
	public void addAction(boolean urgence, String action) {
		addAction(new ActionG(urgence, action, 0, calculateYOfNewAction(), calculateWidthOfNewAction(action), m_heightActions));
	}
	
	// Action dont on... Ajoute l'action.
	public void addAction(ActionG action) {
		m_history.add(action);
		
		redrawAfterAddingAction();
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
	
	@Override
	public String toString() {
		String strHistory = "";
		
		for(int i=0;i<m_history.size();++i) {
			strHistory = String.format("%s %s", strHistory, m_history.get(i).toString());
			
			if(i < m_history.size() - 1) {
				strHistory = String.format("%s\n\n", strHistory);
			}
		}
		
		return strHistory;
	}
	
	private int calculateCurrentItemsHeight() {
		return m_history.size() * m_heightActions;
	}
	
	private int calculateYOfNewAction() {
		return m_history.size() == 0 ? 0 : calculateCurrentItemsHeight();
	}
	
	private FontMetrics getDefaultFontMetrics() {
		Font defaultFont = new Font("Dialog", Font.PLAIN, 12);
		Canvas cv = new Canvas();
		return cv.getFontMetrics(defaultFont);
	}
	
	private int defaultStringWidth(String str) {
		FontMetrics fm = getDefaultFontMetrics();
		return fm.stringWidth(str);
	}
	
	private int calculateWidthOfNewAction(String msgAction) {
		int width = defaultStringWidth(msgAction);
		
		return width > m_widthActions ? width + width / 4 : m_widthActions;
	}
	
	private int maxWidth() {
		int max = -1;
		
		for(int i=0;i<m_history.size();++i) {
			if(max < m_history.get(i).getWidth()) {
				max = m_history.get(i).getWidth();
			}
		}
		
		return max;
	}
	
	private void redrawAfterAddingAction() {
		m_contentPanel.add(m_history.get(m_history.size() - 1));
		m_contentPanel.setPreferredSize(new Dimension(maxWidth(), calculateCurrentItemsHeight()));
		m_contentPanel.revalidate();
		m_contentPanel.repaint();
		JScrollBar verticalScrollBar = m_scrollPane.getVerticalScrollBar();
		verticalScrollBar.setValue(verticalScrollBar.getMaximum());
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawRect(0, 0, m_width - 1, m_height - 1);
	}
}
