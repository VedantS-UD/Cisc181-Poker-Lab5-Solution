package pkgCore;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import pkgEnum.eCardCount;
import pkgEnum.eCardDestination;
import pkgEnum.eCardVisibility;
import pkgEnum.eGame;
import pkgEnum.eRank;
import pkgEnum.eSuit;
import pkgException.DeckException;
import pkgException.HandException;
import pkgHelper.GamePlayHelper;
import pkgHelper.HandPokerHelper;

public class TexasHoldemTest {

 
	@Test
	public void GenerateHands() throws DeckException, HandException {
		Table t = new Table("my table");
		Player p1 = new Player("Bert");
		Player p2 = new Player("Joe");
		Player p3 = new Player("Jim");
		
		t.AddPlayerToTable(p1);
		t.AddPlayerToTable(p2);
		t.AddPlayerToTable(p3);
		
		Rule rle = new Rule(eGame.TexasHoldEm);
		GamePlay gp = new GamePlay(t, rle);
		gp.StartGame();
		
		//	P1 Cards
		ArrayList<Card> p1Cards = new ArrayList<Card>();
		p1Cards.add(new Card(eSuit.CLUBS, eRank.TWO));
		p1Cards.add(new Card(eSuit.DIAMONDS, eRank.THREE));
		
		HandPoker hp1 = HandPokerHelper.SetHand(p1Cards, gp.GetPlayersHand(p1));
		GamePlayHelper.PutGamePlay(gp, p1.getPlayerID(), hp1);
		
		//	Common Cards
		ArrayList<Card> commonCards = new ArrayList<Card>();
		commonCards.add(new Card(eSuit.SPADES, eRank.FOUR));
		commonCards.add(new Card(eSuit.SPADES, eRank.FIVE));
		commonCards.add(new Card(eSuit.SPADES, eRank.SIX));
		//commonCards.add(new Card(eSuit.DIAMONDS, eRank.ACE));
		//commonCards.add(new Card(eSuit.SPADES, eRank.QUEEN));
		GamePlayHelper.setCommonCards(gp,  commonCards);
		
		//	Evaluate
		gp.EvaluateGameHands();
		
		
		//	Get P1 Hand
		hp1 = gp.GetPlayersHand(p1);
		
		PrintHand(gp.GetPlayersHand(p1).getCards(),"Player 1 Cards");
		PrintHand(gp.getCommonCards(),"Common Cards");
		
		ArrayList<HandPoker> BestPossibleHands = hp1.getGP().getBestPossibleHands(p1);
		Collections.sort(BestPossibleHands, HandPoker.hpComparator);
		System.out.println("Best Possible Hand Count " + BestPossibleHands.size());
		
		ArrayList<HandPoker> BestMadeHands = hp1.getGP().getBestMadeHands(p1);
		System.out.println("Best Made Hand Count: " + BestMadeHands.size());

		
		for (HandPoker hp: BestPossibleHands)
		{
			this.PrintHand(hp.getCards(), hp.getHandScorePoker().geteHandStrength().toString());
			System.out.println("---------------");
			for (Card c: hp.getHandScorePoker().getSubtitutedCards())
			{
				System.out.println(c.geteRank() + " " + c.geteSuit().getiSuitChar());
			}
			System.out.println("---------------");
			
		}
	}

	
	public void PrintHand(ArrayList<Card> cards, String strHandName)
	{
		System.out.println("************" + strHandName + "************" );
		for (Card handPC : cards)
		{
			System.out.print(handPC.geteRank() + " " + handPC.geteSuit().getiSuitChar() + " " + handPC.geteSubstituteDeck() + " " );
		}
		System.out.println(" ");
	}
	
}
