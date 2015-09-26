package ankhmorpork.util;

import ankhmorpork.collection.Deck;
import ankhmorpork.manager.GameManager;
import ankhmorpork.manager.db.XMLFile;
import ankhmorpork.model.Area;
import ankhmorpork.model.Card;
import ankhmorpork.model.Color;
import ankhmorpork.model.Player;
import ankhmorpork.model.landmark.Building;
import ankhmorpork.model.landmark.Minion;

public class StartingConditionsBuild3 
{
	public static void main(String[] args) throws Exception
	{
		GameManager gameManager = GameManager.createGameManager(Environment.CONFIGURATION_PATH);
		gameManager.getAvailableColorList().clear();
		
		Player player1_r = new Player(1, "Player1R", Color.getColor("red"), false);
		Player player2_y = new Player(2, "Player2Y", Color.getColor("yellow"), false);
		Player player3_g = new Player(3, "Player3G", Color.getColor("green"), false);
		Player player4_b = new Player(4, "Player4B", Color.getColor("blue"), false);
		
		gameManager.addPlayer(player1_r);
		gameManager.addPlayer(player2_y);
		gameManager.addPlayer(player3_g);
		gameManager.addPlayer(player4_b);
		
		for(Player player : gameManager.getPlayerList())
		{
			for(int i = 0; i < 6; i++)
			{
				player.addBuilding(new Building());
				player.addMinion(new Minion());
				player.addMinion(new Minion());
			}
		}
	
		gameManager.setCurrentPlayerID(player1_r.getId());

		player1_r.setPersonalityCard(gameManager.getBoard().getPersonalityCardDeck().pickCardByID(15));
		player2_y.setPersonalityCard(gameManager.getBoard().getPersonalityCardDeck().pickCardByID(19));
		player3_g.setPersonalityCard(gameManager.getBoard().getPersonalityCardDeck().pickCardByID(18));
		player4_b.setPersonalityCard(gameManager.getBoard().getPersonalityCardDeck().pickCardByID(13));
		
		Area area = gameManager.getBoard().getArea(1); // Dolly Sisters
		area.addMinion(player1_r.removeMinion());
		area.addMinion(player2_y.removeMinion());
		area.addMinion(player3_g.removeMinion());
		area.addMinion(player4_b.removeMinion());
		area.setTroubleMarker(true);

		area = gameManager.getBoard().getArea(2); // Unreal Estate
		area.addMinion(player1_r.removeMinion());
		area.setTroubleMarker(false);
		
		area = gameManager.getBoard().getArea(3); // Dragon's Landing
		area.addMinion(player1_r.removeMinion());
		area.addMinion(player1_r.removeMinion());
		area.setTroubleMarker(true);
		
		area = gameManager.getBoard().getArea(4); // Small Gods
		area.setTroubleMarker(false);
		
		area = gameManager.getBoard().getArea(5); // The Scours
		area.addMinion(player2_y.removeMinion());
		area.addMinion(player4_b.removeMinion());
		area.addMinion(player4_b.removeMinion());
		area.addMinion(player4_b.removeMinion());
		area.setBuilding(player2_y.removeBuilding());
		player2_y.getCityAreaCardDeck().addCard(area.getAreaCard());
		area.addDemon(gameManager.getBoard().removeDemon());
		area.setTroubleMarker(true);		
		
		area = gameManager.getBoard().getArea(6); // The Hippo
		area.addMinion(player4_b.removeMinion());
		area.setBuilding(player4_b.removeBuilding());
		player4_b.getCityAreaCardDeck().addCard(area.getAreaCard());
		area.addTroll(gameManager.getBoard().removeTroll());
		area.setTroubleMarker(false);
		
		area = gameManager.getBoard().getArea(7); // The Shades
		area.addMinion(player2_y.removeMinion());
		area.addMinion(player4_b.removeMinion());
		area.setBuilding(player2_y.removeBuilding());
		player2_y.getCityAreaCardDeck().addCard(area.getAreaCard());
		area.setTroubleMarker(true);
		
		area = gameManager.getBoard().getArea(8); // Dimwell
		area.addMinion(player1_r.removeMinion());
		area.addMinion(player3_g.removeMinion());
		area.setBuilding(player1_r.removeBuilding());
		player1_r.getCityAreaCardDeck().addCard(area.getAreaCard());
		area.setTroubleMarker(false);
		
		area = gameManager.getBoard().getArea(9); // Longwall
		area.addMinion(player3_g.removeMinion());
		area.addMinion(player4_b.removeMinion());
		area.addMinion(player4_b.removeMinion());
		area.setTroubleMarker(true);
		
		area = gameManager.getBoard().getArea(10); // Isle of Gods
		area.addMinion(player1_r.removeMinion());
		area.setTroubleMarker(false);
		
		area = gameManager.getBoard().getArea(11); // Seven Sleepers
		area.addMinion(player1_r.removeMinion());
		area.addMinion(player2_y.removeMinion());
		area.addMinion(player3_g.removeMinion());
		area.setBuilding(player2_y.removeBuilding());
		player2_y.getCityAreaCardDeck().addCard(area.getAreaCard());
		area.setTroubleMarker(true);
		
		area = gameManager.getBoard().getArea(12); // Nap Hill
		area.addMinion(player1_r.removeMinion());
		area.addMinion(player4_b.removeMinion());
		area.addTroll(gameManager.getBoard().removeTroll());
		area.setTroubleMarker(false);
		
		gameManager.getBoard().getBankAccount().transfertAmountTo(player1_r, 15, true);
		player1_r.getPlayerCardDeck().addCard(gameManager.getBoard().getPlayerCardDeck().pickCardByID(44));
		player1_r.getPlayerCardDeck().addCard(gameManager.getBoard().getPlayerCardDeck().pickCardByID(49));
		player1_r.getPlayerCardDeck().addCard(gameManager.getBoard().getPlayerCardDeck().pickCardByID(78));
		player1_r.getPlayerCardDeck().addCard(gameManager.getBoard().getPlayerCardDeck().pickCardByID(67));
		player1_r.getPlayerCardDeck().addCard(gameManager.getBoard().getPlayerCardDeck().pickCardByID(60));
		player1_r.getPlayerCardDeck().addCard(gameManager.getBoard().getPlayerCardDeck().pickCardByID(79));

		gameManager.getBoard().getBankAccount().transfertAmountTo(player2_y, 22, true);
		player2_y.getPlayerCardDeck().addCard(gameManager.getBoard().getPlayerCardDeck().pickCardByID(53));
		player2_y.getPlayerCardDeck().addCard(gameManager.getBoard().getPlayerCardDeck().pickCardByID(63));
		player2_y.getPlayerCardDeck().addCard(gameManager.getBoard().getPlayerCardDeck().pickCardByID(72));
		player2_y.getPlayerCardDeck().addCard(gameManager.getBoard().getPlayerCardDeck().pickCardByID(40));
		player2_y.getPlayerCardDeck().addCard(gameManager.getBoard().getPlayerCardDeck().pickCardByID(39));

		gameManager.getBoard().getBankAccount().transfertAmountTo(player3_g, 17, true);
		player3_g.getPlayerCardDeck().addCard(gameManager.getBoard().getPlayerCardDeck().pickCardByID(43));
		player3_g.getPlayerCardDeck().addCard(gameManager.getBoard().getPlayerCardDeck().pickCardByID(50));
		player3_g.getPlayerCardDeck().addCard(gameManager.getBoard().getPlayerCardDeck().pickCardByID(32));
		player3_g.getPlayerCardDeck().addCard(gameManager.getBoard().getPlayerCardDeck().pickCardByID(45));
		player3_g.getPlayerCardDeck().addCard(gameManager.getBoard().getPlayerCardDeck().pickCardByID(37));

		gameManager.getBoard().getBankAccount().transfertAmountTo(player4_b, 18, true);
		player4_b.getPlayerCardDeck().addCard(gameManager.getBoard().getPlayerCardDeck().pickCardByID(48));
		player4_b.getPlayerCardDeck().addCard(gameManager.getBoard().getPlayerCardDeck().pickCardByID(59));
		player4_b.getPlayerCardDeck().addCard(gameManager.getBoard().getPlayerCardDeck().pickCardByID(70));
		player4_b.getPlayerCardDeck().addCard(gameManager.getBoard().getPlayerCardDeck().pickCardByID(68));
		player4_b.getPlayerCardDeck().addCard(gameManager.getBoard().getPlayerCardDeck().pickCardByID(61));
		player4_b.getPlayerCardDeck().addCard(gameManager.getBoard().getPlayerCardDeck().pickCardByID(74));
		
		// Shuffle left over cards, we won't discard any card as we don't know what to discard.
		gameManager.getBoard().getRandomEventCardDeck().shuffleDeck();
		gameManager.getBoard().getPersonalityCardDeck().shuffleDeck();
		Deck<Card> brownCards = new Deck<Card>();
		Deck<Card> greenCards = new Deck<Card>();
		for(Card card : gameManager.getBoard().getPlayerCardDeck())
		{
			if(card.getType() == Card.CardType.GreenPlayerCard)
			{
				greenCards.addCard(card);
			}
			else
			{
				brownCards.addCard(card);
			}
		}
		brownCards.shuffleDeck();
		greenCards.shuffleDeck();
		gameManager.getBoard().getPlayerCardDeck().clear();
		gameManager.getBoard().getPlayerCardDeck().addAll(brownCards);
		gameManager.getBoard().getPlayerCardDeck().addAll(greenCards);
		
		XMLFile db = new XMLFile("teacher.dat");
		db.save(gameManager);
	}
}
