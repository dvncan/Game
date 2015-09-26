package ankhmorpork.ui;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.*;

import ankhmorpork.manager.GameManager;
import ankhmorpork.model.Area;
import ankhmorpork.model.Card;
import ankhmorpork.model.Player;
import ankhmorpork.model.landmark.Minion;
import ankhmorpork.ui.model.AreaUI;
import ankhmorpork.ui.model.PieceUI;
import ankhmorpork.util.Environment;


/**
 * @author Pascal
 * UI with AWT which we are loading a single image and using RayTracing for detecting if the cursor
 * is over an polygon delimited by a series of Points
 * 
 * We used the DoubleBuffer from codeproject.com to fix the drawing issues
 * It simply draw in a side buffer every modification and when it done,
 * it update the UI with the result.
 */
@SuppressWarnings("serial")
public class AnkhMorporkUI extends DoubleBuffer implements MouseListener
{
	private GameManager gameManager;
	private int currentMapSelect = 0;
	private Point mousePosition = new Point(0,0);

	// The background image acting as buffer
	private Image bufferImage;
	
	// For testing purpose, to drag and move pieces
	private PieceUI dragPiece = null;
	private ankhmorpork.model.Color dragColor = null;
	private Point dragPosition = null;
	private boolean dragMove = false;
	
	private PieceUI minionUI = null;
	private PieceUI buildingUI = null;
	private PieceUI demonUI = null;
	private PieceUI trollUI = null;
	private PieceUI troubleUI = null;
	
	HashMap<Integer, AreaUI> areaUIMap = new HashMap<Integer, AreaUI>();
	
	/**
	 * Constructor
	 * @param gameManager the game to load into the UI
	 * @throws Exception The UI failed to initialize
	 */
	public AnkhMorporkUI(final GameManager gameManager) throws Exception
	{
		super();
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		MediaTracker tracker = new MediaTracker(this);
		this.gameManager = gameManager;
		this.setBackground(java.awt.Color.black);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		bufferImage = toolkit.getImage(Environment.GAME_BOARD_IMAGE_PATH);
		if(bufferImage == null)
		{
			throw new Exception("Picture: '" + Environment.GAME_BOARD_IMAGE_PATH + "' not found.");
		}
		tracker.addImage(bufferImage, 0);
		try
		{
			tracker.waitForAll();
		} catch (final InterruptedException ex)
		{

		}
		int width = bufferImage.getWidth(this);
		int height = bufferImage.getHeight(this);
		this.setSize(width, height);

		this.addMouseMotionListener(new MouseMotionAdapter()
		{
			public void mouseMoved(final MouseEvent e)
			{
				setMousePosition(new Point(e.getX(), e.getY()));
				setCurrentMapSelect(Environment.FindZone(areaUIMap.entrySet().iterator(), e.getX(), e.getY()));
				if(dragMove)
				{
					dragPosition = getMousePosition();
				}
			}
		});
		addMouseListener(this);
		
	  int delay = 250; // force refresh each 250ms
	  AnkhMorporkUI currentUI = this;
	  ActionListener taskPerformer = new ActionListener() 
	  {
	      public void actionPerformed(ActionEvent evt) 
	      {
	      	currentUI.repaint();
	      }
	  };
	  new Timer(delay, taskPerformer).start();
	}
	
	/**
	 * Paint the picture on the frame
	 */
	public void paintBuffer(final Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(bufferImage, 0, 0, this.getWidth(), this.getHeight(), this);
		g2.setStroke(new BasicStroke(3));
	
		if (getCurrentMapSelect() > 0)
		{
			g2.setColor(java.awt.Color.red);
			AreaUI area = areaUIMap.get(getCurrentMapSelect());
			List<Point> lstPoint = area.getListPointDelimiter();
			for (int i = 0; i < lstPoint.size() - 1; i++)
			{
				g2.drawLine(lstPoint.get(i).x, lstPoint.get(i).y,
						lstPoint.get(i + 1).x, lstPoint.get(i + 1).y);
			}
			drawCard(gameManager.getBoard().getAreaList().get(getCurrentMapSelect() - 1).getAreaCard().getId(), new Point(800,600), g2);
		}
		if(gameManager.getBoard().getLastPlayedCard() != null)
		{
			drawCard(gameManager.getBoard().getLastPlayedCard().getId(), new Point(1050,600), g2);
		}

		for(Area area : gameManager.getBoard().getAreaList())
		{
			AreaUI areaUI = areaUIMap.get(area.getNumber());
			int position = 0;
			for(Minion minion : area.getMinionList())
			{
				Point pt = areaUI.getPosition(position++);
				minionUI.drawPicture(minion.getColor(), g2, new Point(pt.x, pt.y + makeWinningMinionJump(minion)));
			}
			for(int i = 0; i < area.getTrollList().size(); i++)
			{
				trollUI.drawPicture(null, g2, areaUI.getPosition(position++));
			}
			for(int i = 0; i < area.getDemonList().size(); i++)
			{
				demonUI.drawPicture(null, g2, areaUI.getPosition(position++));
			}
			if(area.hasBuilding())
			{
				buildingUI.drawPicture(area.getBuilding().getColor(), g2, areaUI.getPosition(position++));
			}
			if(area.hasTrouble())
			{
				Point point = areaUI.getPosition(position++);
				troubleUI.drawPicture(null, g2, point);
			}
		}
		
		List<Card> cardListShow = new ArrayList<Card>();
		for(Player player : gameManager.getPlayerList())
		{
			int pos = player.getId();
			g2.setColor(new java.awt.Color(player.getColor().getHexColor()));
			if(pos == gameManager.getCurrentPlayerID())
			{
				g2.drawRect(850-4, 50 + (pos - 1) * 100-4, 450+8, 90+8);
			}

			g2.drawRect(850, 50 + (pos - 1) * 100, 300, 90);
			g2.drawRect(1150, 50 + (pos - 1) * 100, 50, 90);
			g2.drawRect(1200, 50 + (pos - 1) * 100, 50, 90);
			g2.drawRect(1250, 50 + (pos - 1) * 100, 50, 90);

			g2.drawString(player.getName() + " ["+player.getPersonalityCard().getName()+"]", 860, 70 + (pos - 1) * 100);
			g2.drawString("Building: " + player.getBuildingCount(), 860, 85 + (pos - 1) * 100);
			g2.drawString("Minion: " + player.getMinionCount(), 860, 100 + (pos - 1) * 100);
			g2.drawString("Money: " + player.getMoneyAmount() + "$", 860, 115 + (pos - 1) * 100);
			g2.drawString("Cards -> City:" + player.getCityAreaCardDeck().size() + " Player:" + player.getPlayerCardDeck().size() + " Front:" + player.getInFrontOfHimDeck().size(), 860, 130 + (pos - 1) * 100);
			
			g2.drawString("P", 1170, 70 + (pos - 1) * 100);
			g2.drawString("A", 1220, 70 + (pos - 1) * 100);
			g2.drawString("W", 1270, 70 + (pos - 1) * 100);
			
			if(getMousePosition().x > 1150 && getMousePosition().x < 1300)
			{
				if(getMousePosition().y > 50 + (pos - 1) * 100 && getMousePosition().y < 140 + (pos - 1) * 100)
				{
					if(getMousePosition().x < 1200)
					{
						cardListShow.addAll(player.getPlayerCardDeck());
					}
					else if(getMousePosition().x < 1250)
					{
						cardListShow.addAll(player.getCityAreaCardDeck());
					}
					else
					{
						cardListShow.add(player.getPersonalityCard());
					}
				}
			}
		}		
		for(int i = 0; i < cardListShow.size(); i++)
		{
			drawCard(cardListShow.get(i).getId(), new Point(50 + (i % 5) * 250, 50 + (i / 5) * 450), g2);
		}
		
		if(dragPiece != null)
		{
			dragPiece.drawPicture(dragColor, g2, dragPosition);
		}
	}
	
	/**
	 * When someone win, make their minion jump
	 * @param minion
	 * @return
	 */
	private int makeWinningMinionJump(Minion minion) 
	{
		int heightJump = 0;
		if(gameManager.getWinnerList().contains(minion.getOwner()))
		{
			int second = (int)System.currentTimeMillis() / 300;
			if(second % 2 == 0)
			{
				heightJump = 5;
			}
		}
		return heightJump;
	}

	/**
	 * @return the current map that is selected
	 */
	int getCurrentMapSelect()
	{
		return currentMapSelect;
	}
	
	/**
	 * @param currentMapSelect Set the current map value and update the View
	 */
	void setCurrentMapSelect(final int currentMapSelect)
	{
		if (this.currentMapSelect != currentMapSelect)
		{
			this.currentMapSelect = currentMapSelect;
			this.repaint();
		}
	}

	/**
	 * @return the mousePosition
	 */
	public Point getMousePosition() 
	{
		return mousePosition;
	}

	/**
	 * @param mousePosition the mousePosition to set
	 */
	public void setMousePosition(Point mousePosition) 
	{
		if (this.mousePosition != mousePosition)
		{
			this.mousePosition = mousePosition;
			this.repaint();
		}
	}

	/**
	 * Mouse Click Event
	 */
	public void mouseClicked(final MouseEvent event)
	{
	}

	/**
	 * Mouse Entered Event
	 */
	public void mouseEntered(final MouseEvent event)
	{
	}

	/**
	 * Mouse Released Event
	 */
	public void mouseReleased(final MouseEvent event)
	{
		//dragMove = false;
	}

	/**
	 * Mouse Exited Event
	 */
	public void mouseExited(final MouseEvent event)
	{
	}

	/**
	 * Mouse Pressed Event
	 */
	public void mousePressed(final MouseEvent event)
	{
		/*
		try 
		{ 
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("pos3.txt", true)));
			out.println("<pos x=\""+ event.getX()+"\" y=\""+event.getY()+"\"/>");
			out.close(); 
		} catch (Exception e) { }
		*/

		if(!dragMove)
		{
			// testing purpose and enable dragdrop later
			dragColor = gameManager.getCurrentPlayer().getColor();
			dragColor = null;
			dragPiece = troubleUI;
			dragPiece = null;
			dragPosition = getMousePosition();
		}
		dragMove = !dragMove;
	}
	
	/**
	 * Draw a card at a specific spot on the screen
	 * @param id
	 * @param point
	 */
	public void drawCard(int id, Point point, Graphics2D g2)
	{
		String cardImagePath = String.format(Environment.CARDS_IMAGE_PATH, id);
		Image image = Toolkit.getDefaultToolkit().getImage(cardImagePath);
		g2.drawImage(image, point.x, point.y, point.x + 250, point.y + 500, 0, 0, 250, 500, null);
	}

	public AreaUI getArea(int id) 
	{
		return areaUIMap.get(id);
	}

	public void addArea(AreaUI areaUI) 
	{
		areaUIMap.put(areaUI.getAreaID(), areaUI);
	}

	public void setMinionUI(PieceUI minionUI) 
	{
		this.minionUI = minionUI;
	}

	public void setBuildingUI(PieceUI buildingUI) 
	{
		this.buildingUI = buildingUI;
	}

	public void setDemonUI(PieceUI demonUI) 
	{
		this.demonUI = demonUI;
	}

	public void setTrollUI(PieceUI trollUI) 
	{
		this.trollUI = trollUI;
	}

	public void setTroubleUI(PieceUI troubleUI) 
	{
		this.troubleUI = troubleUI;
	}
}