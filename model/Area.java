package ankhmorpork.model;

import ankhmorpork.collection.Party;

import java.util.ArrayList;
import java.util.List;

import ankhmorpork.model.landmark.Building;
import ankhmorpork.model.landmark.Demon;
import ankhmorpork.model.landmark.Minion;
import ankhmorpork.model.landmark.Troll;

/**
 * Area on the board that has landmarks and neighbors other areas
 * @see Board
 * @author Team 2
 * @since Build 1
 */
public class Area extends Piece
{
	private final int number;
	private String name = "";
	private int cost = 0;
	private Card areaCard;
	private Party<Minion> minionList = new Party<Minion>();
	private Party<Troll> trollList = new Party<Troll>();
	private Party<Demon> demonList = new Party<Demon>();
	private Building building = null;
	private boolean hasTrouble = false;
	private List<Area> listNeighbor = new ArrayList<Area>();
	private boolean isNearRiver = false;
	
	/**
	 * Create an empty area with a number
	 * @param number the number of the area
	 */
	public Area(int number)
	{
		this.number = number;
	}

	/**
	 * Retrieve the building on the area
	 * @return the building on the area
	 */
	public Building getBuilding() 
	{
		return building;
	}
	
	/**
	 * Whether the area as a building
	 * @return if the area has a building on it
	 */
	public boolean hasBuilding()
	{
		return building != null;
	}

	/**
	 * Whether the area as a building owned by player
	 * @param player owner of building
	 * @return if the area has a building owned by player on it
	 */
	public boolean hasBuilding(Player player)
	{
		if (player != null && building != null)
		{
			return building.isOwnedBy(player);
		}
		return false;
	}
	
	/**
	 * Assign a building to the area (without transfer nor validation)
	 * @param building the building to set
	 */
	public void setBuilding(Building building) 
	{
		this.building = building;
	}

	/**
	 * Remove the building on the area
	 * @return the building on the area
	 */
	public Building removeBuilding() 
	{
		Building tempBuilding = building;
		building = null;
		return tempBuilding;
	}
	
	/**
	 * List of minions of each players on this area
	 * @return the list of all minions on the area
	 */
	public List<Minion> getMinionList()
	{
		return minionList;
	}

	/**
	 * Whether the area as a minion
	 * @return if the area has a minion on it
	 */
	public boolean hasMinion()
	{
		return minionList != null && !minionList.isEmpty();
	}
	
	/**
	 * Whether the area has a minion owned by player
	 * @param player owner of minion
	 * @return if the area has a minion owned by player on it
	 */
	public boolean hasMinion(Player player)
	{
		if (player != null && minionList != null)
		{
			for(Minion minion : minionList)
			{
				if (minion.isOwnedBy(player))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Whether the area has a minion other than the player in parameter
	 * @param player that is an exception
	 * @return if the area has a minion from another player then the specified player
	 */
	public boolean hasMinionExceptPlayer(Player player)
	{
		if (player != null && minionList != null)
		{
			for(Minion minion : minionList)
			{
				if (!minion.isOwnedBy(player))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Whether the area's neighbor has a minion owned by player
	 * @param player owner of minion
	 * @return if the area's neighbor has a minion owned by player on it
	 */
	public boolean hasNeighborMinion(Player player)
	{
		if (player != null && listNeighbor != null)
		{
			for(Area neighbor : listNeighbor)
			{
				if (neighbor.hasMinion(player))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Retrieve the minion on the area belonging to the player
	 * @param player the player of whom we want the list of minions
	 * @return the list of all minions belonging to the player on the area
	 */
	public List<Minion> getMinionList(Player player)
	{
		List<Minion> playerMinionList = new ArrayList<Minion>();
		if (minionList != null && player != null)
		{
			for(Minion minion : minionList)
			{
				if (minion.isOwnedBy(player))
				{
					playerMinionList.add(minion);
				}
			}
		}
		return playerMinionList;
	}
	
	/**
	 * Add a minion to the area
	 * @param minion the minion to add
	 */
	public void addMinion(Minion minion)
	{
		if (minion != null)
		{
			if (minionList == null)
			{
				minionList = new Party<Minion>();
			}
			if(minionList.size() + this.trollList.size() + this.demonList.size() > 0)
			{
				hasTrouble = true;
			}
			minionList.add(minion);
		}
	}
	
	/**
	 * Remove a minion from the area (any owner)
	 * @return 
	 */
	public Minion removeMinion()
	{
		if (minionList != null && !minionList.isEmpty())
		{
			Minion minion = minionList.get(0);
			minionList.remove(0);
			hasTrouble = false;
			return minion;
		}
		return null;
	}
			
	/**
	 * Remove a minion of given player from area
	 * @param player the player from whom we want to remove the minion
	 * @return the removed minion
	 */
	public Minion removeMinion(Player player)
	{
		if (minionList != null && !minionList.isEmpty() && player != null)
		{
			Minion minion = null;
			for(Minion m : minionList)
			{
				if (m.isOwnedBy(player))
				{
					minion = m;
					break;
				}
			}
			if (minion != null)
			{
				minionList.remove(minion);
				hasTrouble = false;
				return minion;
			}
		}
		return null;
	}
	
	/**
	 * Amount of minion on this area
	 * @return number of minions on this area
	 */
	public int getNbMinion()
	{
		if (minionList == null)
		{
			return 0;
		}
		return minionList.size();
	}
	
	/**
	 * Amount of demon on this area
	 * @return number of demons on this area
	 */
	public int getNbDemon()
	{
		if (demonList == null)
		{
			return 0;
		}
		return demonList.size();
	}

	/**
	 * Add a demon to the area
	 * @param demon the demon to add
	 */
	public void addDemon(Demon demon)
	{
		if (demonList == null && demon != null)
		{
			demonList = new Party<Demon>();
		}			
		hasTrouble = true;
		demonList.add(demon);
	}
	
	/**
	 * Remove a demon from area
	 * @return the removed demon
	 */
	public Demon removeDemon()
	{
		if (demonList == null || demonList.isEmpty())
		{
			return null;
		}
		
		hasTrouble = false;
		return demonList.remove(0);
	}
	
	/**
	 * Get the list of demons on the area
	 * @return the demonList
	 */
	public List<Demon> getDemonList() 
	{
		return demonList;
	}
	
	/**
	 * Amount of troll on this area
	 * @return number of trolls on this area
	 */
	public int getNbTroll()
	{
		if (trollList == null)
		{
			return 0;
		}
		return trollList.size();
	}

	/**
	 * Add a troll to the area
	 * @param troll the troll to add
	 */
	public void addTroll(Troll troll)
	{
		if (trollList == null && troll != null)
		{
			trollList = new Party<Troll>();
		}
		if(minionList.size() + this.trollList.size() + this.demonList.size() > 0)
		{
			hasTrouble = true;
		}
		trollList.add(troll);
	}
	
	/**
	 * Remove a troll from area
	 * @return the removed troll
	 */
	public Troll removeTroll()
	{
		if (trollList != null && !trollList.isEmpty())
		{
			Troll troll = trollList.get(0);
			trollList.remove(troll);
			hasTrouble = false;
			return troll;
		}
		return null;
	}
	
	/**
	 * Get the list of trolls on the area
	 * @return the trollList
	 */
	public List<Troll> getTrollList() 
	{
		return trollList;
	}

	/**
	 * Amount of characters on this area
	 * @return number of characters on this area
	 */
	public int getNbCharacter()
	{
		return getNbDemon() + getNbTroll() + getNbMinion();
	}
	
	/**
	 * The number of this area
	 * @return the ID number of the area
	 */
	public int getNumber()
	{
		return this.number;
	}

	/**
	 * The name of the area
	 * @return the name of the area
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * @param name Set the name of the area
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * The cost to build on the area
	 * @return the cost of the area
	 */
	public int getCost()
	{
		return this.cost;
	}

	/**
	 * @param cost Set the cost of the area
	 */
	public void setCost(int cost)
	{
		this.cost = cost;
	}

	/**
	 * The area card associated to the area
	 * @return the area card or null if the card has been taken
	 */
	public Card getAreaCard()
	{
		return this.areaCard;
	}

	/**
	 * Associate the area card to this area
	 * @param areaCard the area card to be assigned to the current territory
	 */
	public void setAreaCard(Card areaCard)
	{
		this.areaCard = areaCard;
	}

	/**
	 * Check whether the area has trouble
	 * @return if this area has trouble
	 */
	public boolean hasTrouble()
	{
		return this.hasTrouble;
	}

	/**
	 * Set whether this area now has trouble
	 * @param hasTrouble whether true or false this area has trouble
	 */
	public void setTroubleMarker(boolean hasTrouble)
	{
		this.hasTrouble = hasTrouble;
	}

	/**
	 * Add neighbor area to this area
	 * @param area add an area as a neighbor of this area
	 */
	public void addNeighbor(Area area)
	{
		listNeighbor.add(area);
	}

	/**
	 * Get all neighbors
	 * @return area plus neighbors
	 */
	public List<Area> getNeighborOnly()
	{
		return listNeighbor;
	}

	/**
	 * Get area and all neighbors (copy of list)
	 * @return area plus neighbors
	 */
	public List<Area> getAreaAndNeighborhood()
	{
		ArrayList<Area> neighborhood = new ArrayList<Area>();
		neighborhood.add(this);
		neighborhood.addAll(listNeighbor);
		return neighborhood;
	}

	/**
	 * Is neighbor
	 * @param area whether area is neighbor
	 */
	public boolean isNeighbor(Area area)
	{
		return listNeighbor != null && listNeighbor.contains(area);
	}

	/**
	 * Check whether the area is adjacent to river
	 * @return whether the area is adjacent to river
	 */
	public boolean isNearRiver()
	{
		return this.isNearRiver;
	}

	/**
	 * Set whether the area is adjacent to river
	 * @param nearRiver whether the area is adjacent to river
	 */
	public void setRiverAdjacency(boolean nearRiver)
	{
		this.isNearRiver = nearRiver;
	}

	/**
	 * Override the toString to show the area and the cost
	 */
	@Override
	public String toString()
	{
		return this.name + " (#" + this.number + ", " + this.cost + "$)";
	}

	/**
	 * @return if there Demon
	 */
	public boolean hasDemon() 
	{
		return this.getNbDemon() > 0;
	}

	/**
	 * @return if there Troll
	 */
	public boolean hasTroll() 
	{
		return this.getNbTroll() > 0;
	}
}
