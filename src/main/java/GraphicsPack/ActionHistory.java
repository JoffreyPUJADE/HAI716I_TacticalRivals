package GraphicsPack;

import java.util.ArrayList;

import javax.swing.*;
import java.awt.*;

public class ActionHistory extends JPanel
{
	private int m_x;
	private int m_y;
	private int m_width;
	private int m_height;
	private int m_widthActions;
	private int m_heightActions;
	private ArrayList<ActionG> m_history;
	private JPanel m_contentPanel; // Panel qui contient les actions
	private JScrollPane m_scrollPane; // ScrollPane pour gérer le défilement
	
	public ActionHistory(int x, int y, int width, int height)
	{
		super();
		
		m_x = x;
		m_y = y;
		m_width = width;
		m_height = height;
		
		m_widthActions = m_width;
		m_heightActions = 50;
		
		System.out.println("x = " + m_x + ", y = " + m_y);
		
		m_history = new ArrayList<>();
		
		redraw();
	}
	
	// Action en noir.
	public void addAction(String action)
	{
		m_history.add(new ActionG(false, action, 0, calculateYOfNewAction(), calculateWidthOfNewAction(action), m_heightActions));
		
		//redraw();
		adding();
	}
	
	// Action dont on définit la couleur (true : rouge, false : noir).
	public void addAction(boolean urgence, String action)
	{
		m_history.add(new ActionG(urgence, action, 0, calculateYOfNewAction(), calculateWidthOfNewAction(action), m_heightActions));
		
		//redraw();
		adding();
	}
	
	// Action dont on... Ajoute l'action.
	public void addAction(ActionG action)
	{
		m_history.add(new ActionG(action.isUrgent(), action.getMessage(), action.getPosX(), action.getPosY(), action.getWidth(), action.getHeight()));
		
		//redraw();
		adding();
	}
	
	public int getPosX()
	{
		return m_x;
	}
	
	public int getPosY()
	{
		return m_y;
	}
	
	public int getWidth()
	{
		return m_width;
	}
	
	public int getHeight()
	{
		return m_height;
	}
	
	public void setPosX(int x)
	{
		m_x = x;
	}
	
	public void setPosY(int y)
	{
		m_y = y;
	}
	
	public void setWidth(int width)
	{
		m_width = width;
	}
	
	public void setHeight(int height)
	{
		m_height = height;
	}
	
	// Méthode jamais appelée ; ne provoque pas de lags sur l'ajout d'actions à l'affichage mais reset la scroll bar verticale vers le haut.
	public void redraw()
	{
		// Suppression de tous les composants de la classe.
		removeAll();
		
		// Réinitialisation de l'affichage de la classe.
		setLayout(null);
		setBounds(m_x, m_y, m_width, m_height);
		
		// Création panel contenu.
		m_contentPanel = new JPanel();
		m_contentPanel.setLayout(null);
		
		int totalHeight = calculateCurrentItemsHeight();
		
		m_contentPanel.setPreferredSize(new Dimension(maxWidth(), totalHeight));
		
		// Ajout des actions.
		for(int i=0;i<m_history.size();++i)
		{
			m_contentPanel.add(m_history.get(i));
		}
		
		// Création JScrollPane.
		m_scrollPane = new JScrollPane(m_contentPanel);
		m_scrollPane.setBounds(0, 0, m_width, m_height);
		m_scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		m_scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		m_scrollPane.setPreferredSize(new Dimension(m_width, m_height));
		
		add(m_scrollPane);
		setVisible(true);
		
		// On force le réaffichage.
		revalidate();
		repaint();
	}
	
	private void adding()
	{
		int totalHeight = calculateCurrentItemsHeight();
		
		m_contentPanel.setPreferredSize(new Dimension(maxWidth(), totalHeight));
		m_contentPanel.add(m_history.get(m_history.size() - 1));
	}
	
	private int calculateCurrentItemsHeight()
	{
		return m_history.size() * m_heightActions;
	}
	
	private int calculateYOfNewAction()
	{
		return m_history.size() == 0 ? 0 : calculateCurrentItemsHeight();
	}
	
	private FontMetrics getDefaultFontMetrics()
	{
		Font defaultFont = new Font("Dialog", Font.PLAIN, 12);
		Canvas cv = new Canvas();
		return cv.getFontMetrics(defaultFont);
	}
	
	private int defaultStringWidth(String str)
	{
		FontMetrics fm = getDefaultFontMetrics();
		return fm.stringWidth(str);
	}
	
	private int calculateWidthOfNewAction(String msgAction)
	{
		int width = defaultStringWidth(msgAction);
		
		return width > m_widthActions ? width + width / 4 : m_widthActions;
	}
	
	private int maxWidth()
	{
		int max = -1;
		
		for(int i=0;i<m_history.size();++i)
		{
			if(max < m_history.get(i).getWidth())
			{
				max = m_history.get(i).getWidth();
			}
		}
		
		return max;
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		g.drawRect(0, 0, m_width - 1, m_height - 1);
	}
}
