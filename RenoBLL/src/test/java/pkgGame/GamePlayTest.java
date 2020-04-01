package pkgGame;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import pkgCore.Card;
import pkgCore.GamePlay;
import pkgCore.HandPoker;
import pkgCore.HandScorePoker;
import pkgCore.Player;
import pkgCore.Rule;
import pkgCore.Table;
import pkgEnum.eGame;
import pkgEnum.eRank;
import pkgEnum.eSuit;
import pkgException.DeckException;
import pkgException.HandException;
import pkgHelper.GamePlayHelper;
import pkgHelper.HandPokerHelper;

public class GamePlayTest {
 
	@Test
	public void GamePlay_Test1() {
		
		//	Create new table
		Table t = new Table("Table 1");
		
		//	Create two new players 
		Player p1 = new Player("Bert");
		Player p2 = new Player("Joe");
		//	Add players to the table
		t.AddPlayerToTable(p1);
		t.AddPlayerToTable(p2);

		//	Create a new GamePlay with Rule for Texas Hold 'em
		Rule rle = new Rule(eGame.TexasHoldEm);
		GamePlay gp = new GamePlay(t, rle);
		
		try {
			gp.StartGame();
		} catch (DeckException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (HandException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//	Get the Player hand (should be empty at this point) from GamePlay.
		HandPoker hp1 = gp.GetPlayersHand(p1);		
		HandPoker hp2 = gp.GetPlayersHand(p2);
		
		//	Create P1 cards
		ArrayList<Card> p1Cards = new ArrayList<Card>();
		p1Cards.add(new Card(eSuit.HEARTS, eRank.ACE));
		p1Cards.add(new Card(eSuit.HEARTS, eRank.ACE));
		
		//	Create p2 cards	
		ArrayList<Card> p2Cards = new ArrayList<Card>();
		p2Cards.add(new Card(eSuit.CLUBS, eRank.ACE));
		p2Cards.add(new Card(eSuit.CLUBS, eRank.ACE));
		
		//	Create common cards
		ArrayList<Card> commonCards = new ArrayList<Card>();
		commonCards.add(new Card(eSuit.HEARTS, eRank.THREE));
		commonCards.add(new Card(eSuit.HEARTS, eRank.FOUR));
		commonCards.add(new Card(eSuit.HEARTS, eRank.FIVE));
		commonCards.add(new Card(eSuit.CLUBS, eRank.TEN));	
		commonCards.add(new Card(eSuit.CLUBS, eRank.QUEEN));	
		gp = GamePlayHelper.setCommonCards(gp,  commonCards);
		

		//	Set the HandPoker with known cards
		hp1 = HandPokerHelper.SetHand(p1Cards, hp1);
		hp2 = HandPokerHelper.SetHand(p2Cards, hp2);
		
		//	Set the Hands in GamePlay
		gp = GamePlayHelper.PutGamePlay(gp, p1.getPlayerID(), hp1);		
		gp = GamePlayHelper.PutGamePlay(gp, p2.getPlayerID(), hp2);
		
		try {
			gp.EvaluateGameHands();
		} catch (HandException e) {
			fail("Evaluate hands failed");
		}

		ArrayList<Player> pWinner = null;
		try {
			pWinner = gp.GetGameWinners();
		} catch (HandException e) {
			fail("GetGameWinner Failed");
		}
		
		try {
			PrintHandScore(gp.getWinningScore());
		} catch (HandException e) {
			fail("Get winning score failed");
		}
		
		//Player 1 had a flush, they should win
		assertTrue(pWinner.contains(p1));
		
	}

	@Test
	public void GamePlay_Test2() {
		
		//	Create new table
		Table t = new Table("Table 1");
		
		//	Create two new players 
		Player p1 = new Player("Bert");
		Player p2 = new Player("Joe");
		//	Add players to the table
		t.AddPlayerToTable(p1);
		t.AddPlayerToTable(p2);

		//	Create a new GamePlay with Rule for Texas Hold 'em
		Rule rle = new Rule(eGame.TexasHoldEm);
		GamePlay gp = new GamePlay(t, rle);
		
		try {
			gp.StartGame();
		} catch (DeckException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (HandException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//	Get the Player hand (should be empty at this point) from GamePlay.
		HandPoker hp1 = gp.GetPlayersHand(p1);		
		HandPoker hp2 = gp.GetPlayersHand(p2);
		
		//	Create P1 cards
		ArrayList<Card> p1Cards = new ArrayList<Card>();
		p1Cards.add(new Card(eSuit.DIAMONDS, eRank.ACE));
		p1Cards.add(new Card(eSuit.DIAMONDS, eRank.TWO));
		
		//	Create p2 cards	
		ArrayList<Card> p2Cards = new ArrayList<Card>();
		p2Cards.add(new Card(eSuit.CLUBS, eRank.ACE));
		p2Cards.add(new Card(eSuit.CLUBS, eRank.TWO));
		
		//	Create common cards
		ArrayList<Card> commonCards = new ArrayList<Card>();
		commonCards.add(new Card(eSuit.HEARTS, eRank.THREE));
		commonCards.add(new Card(eSuit.HEARTS, eRank.FOUR));
		commonCards.add(new Card(eSuit.HEARTS, eRank.FIVE));
		commonCards.add(new Card(eSuit.CLUBS, eRank.TEN));	
		commonCards.add(new Card(eSuit.CLUBS, eRank.QUEEN));	
		gp = GamePlayHelper.setCommonCards(gp,  commonCards);
		

		//	Set the HandPoker with known cards
		hp1 = HandPokerHelper.SetHand(p1Cards, hp1);
		hp2 = HandPokerHelper.SetHand(p2Cards, hp2);
		
		//	Set the Hands in GamePlay
		gp = GamePlayHelper.PutGamePlay(gp, p1.getPlayerID(), hp1);		
		gp = GamePlayHelper.PutGamePlay(gp, p2.getPlayerID(), hp2);
		
		try {
			gp.EvaluateGameHands();
		} catch (HandException e) {
			fail("Evaluate hands failed");
		}

		ArrayList<Player> pWinner = null;
		try {
			pWinner = gp.GetGameWinners();
		} catch (HandException e) {
			fail("GetGameWinner Failed");
		}
		
		try {
			PrintHandScore(gp.getWinningScore());
		} catch (HandException e) {
			fail("Get winning score failed");
		}
		
		//Tie Game...  same suit...  this should be scored a tie
		assertTrue(pWinner.contains(p1));
		assertTrue(pWinner.contains(p2));
		
	}
	private void PrintHandScore(HandScorePoker hsp)
	{
		System.out.println("Hand Strength: " + hsp.geteHandStrength() );
	}
}
