package pkgCore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.common.collect.MinMaxPriorityQueue;

import pkgEnum.eCardDestination;
import pkgEnum.eDrawCount;
import pkgEnum.eRank;
import pkgEnum.eSubstituteDeck;
import pkgEnum.eSuit;
import pkgException.DeckException;
import pkgException.HandException;

public class GamePlay {

	private Rule Rle;
	private ArrayList<Player> GamePlayers = new ArrayList<Player>();
	private HashMap<UUID, HandPoker> GameHand = new HashMap<UUID, HandPoker>();
	private ArrayList<Card> CommonCards = new ArrayList<Card>();
	private Deck GameDeck;

	/**
	 * GamePlay - Create an instance of GamePlay. For every player in the table, add
	 * them to the game Set the GameDeck.
	 * 
	 * @param t
	 * @param rle
	 */
	public GamePlay(Table t, Rule rle) {
		this.Rle = rle;
		if (t.getTablePlayers() != null)
			GamePlayers.addAll(t.getTablePlayers());
		GameDeck = new Deck();
	}

	public void Draw(Player p, CardDraw CD) throws DeckException, HandException {

		for (int crdCnt = 0; crdCnt < CD.getCardCount().getCardCount(); crdCnt++) {
			if (CD.getCardDestination() == eCardDestination.COMMON) {
				CommonCards.add(GameDeck.Draw());
			} else {
				GameHand.get(p.getPlayerID()).Draw(GameDeck);
			}
		}
	}

	/**
	 * @author BRG
	 * @version Lab #4
	 * @since Lab #4
	 * 
	 *        EvaluateGameHands - Find every hand in the GameHand map and evaluate
	 *        it.
	 * 
	 * @throws HandException
	 */
	public void EvaluateGameHands() throws HandException {
		ArrayList<HandPoker> pokerHands = new ArrayList<HandPoker>();
		Iterator<Map.Entry<UUID, HandPoker>> itr = GameHand.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry<UUID, HandPoker> entry = itr.next();

			HandPoker hp = entry.getValue();
			pokerHands.addAll(hp.EvaluateHand(hp));
		}
	}
	
	/**
	 * getBestMadeHands - get the best made hands by Player.
	 * 
	 * @version Lab #5
	 * @since Lab #5
	 * 
	 * @param player
	 * @return
	 * @throws HandException
	 */
	public ArrayList<HandPoker> getBestMadeHands(Player player) throws HandException {
		return this.getBestHands(player, true);
	}

	/**
	 * getBestPossibleHands - get the best possible hands. These are hands that are
	 * made, but are made using a wild card.
	 * 
	 * @version Lab #5
	 * @since Lab #5
	 * @param player
	 * @return
	 * @throws HandException
	 */
	public ArrayList<HandPoker> getBestPossibleHands(Player player) throws HandException {

		MinMaxPriorityQueue<HandPoker> queue = MinMaxPriorityQueue
				.orderedBy(HandPoker.hpComparator)
				.maximumSize(20)
				.create();
		for (HandPoker hp : this.getBestHands(player, false)) {
			queue.add(hp);
		}

		return new ArrayList<HandPoker>(queue);

	}

	/**
	 * getBestHands - will pass back an array list of the best hands. If bMadeHand
	 * is true, it's looking for a hand that doesn't have jokers... which means the
	 * hand is an actual hand
	 * 
	 * @version Lab #5
	 * @since Lab #5
	 * @param player    - given player
	 * @param bMadeHand - if true, ensure the found hand has no jokers.
	 * @return
	 * @throws HandException
	 */
	private ArrayList<HandPoker> getBestHands(Player player, boolean bMadeHand) throws HandException {

		ArrayList<HandPoker> pokerHands = new ArrayList<HandPoker>();
		Iterator<Map.Entry<UUID, HandPoker>> itr = GameHand.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry<UUID, HandPoker> entry = itr.next();

			if (player.getPlayerID().equals(entry.getKey())) {
				HandPoker hp = entry.getValue();
				try {
					pokerHands.addAll(hp.EvaluateHand(hp));
				} catch (HandException e) {
					e.printStackTrace();
					throw e;
				}
			}
		}
		pokerHands = (ArrayList<HandPoker>) pokerHands.stream()
				.filter(x -> x.getHandScorePoker().isNatural() == bMadeHand).collect(Collectors.toList());
		pokerHands.sort(HandPoker.hpComparator);
		return pokerHands;

	}

	/**
	 * @version Lab #5
	 * @since Lab #5
	 * getCommonCards - return an ArrayList of the game's common cards
	 * If there aren't five cards, return jokers for the missing cards
	 * @return
	 */
	public ArrayList<Card> getCommonCards() {
		int iSize = CommonCards.size();
		ArrayList<Card> commonCards = (ArrayList<Card>) CommonCards.clone();
		for (int i = iSize; i < this.getRle().getCommunityCardsMax(); i++) {
			commonCards.add(new Card(eSuit.JOKER, eRank.JOKER, eSubstituteDeck.SUBSTITUTE));
		}
		return commonCards;
	}

	/**
	 * @author BRG
	 * @version Lab #4
	 * @since Lab #4 GetGamePlayer - return the Player object for a given PlayerID
	 * @param PlayerID - ID for the Player
	 * @return - Player object
	 */
	private Player GetGamePlayer(UUID PlayerID) {
		for (Player p : GamePlayers) {
			if (p.getPlayerID() == PlayerID)
				return p;
		}
		return null;
	}

	/**
	 * @author BRG
	 * @version Lab #4
	 * @since Lab #4
	 * 
	 *        GetPlayersHand - return the Hand in the GameHand hashmap for a given
	 *        player
	 * @param player
	 * @return
	 */
	public HandPoker GetPlayersHand(Player player) {
		return GameHand.get(player.getPlayerID());
	}

	/**
	 * @author BRG
	 * @version Lab #4
	 * @since Lab #4
	 * 
	 *        getRle - Get the rule for the game. It's set in the constructor
	 * @return
	 */
	public Rule getRle() {
		return Rle;
	}

	/**
	 * @author BRG
	 * @version Lab #4
	 * @since Lab #4
	 * 
	 *        isMadeHandBestPossibleHand - return 'true' if the BestMadeHand is one
	 *        of the BestPossibleHands
	 * @param player
	 * @return
	 * @throws HandException
	 */
	public boolean isMadeHandBestPossibleHand(Player player) throws HandException {

		if (getBestMadeHands(player).get(0).getHS().equals(this.getBestPossibleHands(player).get(0).getHS())) {
			return true;
		}
		return false;
	}

	/**
	 * @author BRG
	 * @version Lab #4
	 * @since Lab #4
	 * 
	 *        StartGame - Create a new HandPoker for each player, put it in the
	 *        GameHand map, execute the first Draw
	 * 
	 * @throws DeckException
	 * @throws HandException
	 */
	public void StartGame() throws DeckException, HandException {
		for (Player p : GamePlayers) {
			HandPoker hp = new HandPoker(p, this);
			GameHand.put(p.getPlayerID(), hp);
			Draw(p, this.Rle.getCardDraw(eDrawCount.FIRST));
		}
	}

	/**
	 * @author BRG
	 * @version Lab #4
	 * @since Lab #4
	 * 
	 *        PutGameHand - puts a hand to the GameHand map
	 * @return
	 */

	private void PutGameHand(UUID PlayerID, HandPoker hp) {
		GameHand.put(PlayerID, hp);
	}

	/**
	 * @author BRG
	 * @version Lab #4
	 * @since Lab #4
	 * 
	 *        setCommonCards - set the common cards.
	 * @param cards
	 */
	private void setCommonCards(ArrayList<Card> cards) {
		this.CommonCards.clear();
		this.CommonCards.addAll(cards);
	}

	int getGameDeckCount() {
		return this.GameDeck.getiDeckCount();
	}
	
	/**
	 * @version Lab #6
	 * @since Lab #6
	 * getBestMadeHands - Get the best made hand for all players
	 * 
	 * @return
	 * @throws HandException
	 */
	private ArrayList<HandPoker> getBestMadeHands() throws HandException
	{
		//TODO: Return the best made hands in the game
		ArrayList<HandPoker> BestGameHands = new ArrayList<HandPoker>();
		return BestGameHands;
	}
	
	/**
	 * @version Lab #6
	 * @since Lab #6
	 * getWinningScore - Get the winning score, looking at all Player's hands
	 * 
	 * @return
	 * @throws HandException
	 */
	public HandScorePoker getWinningScore() throws HandException
	{
		//TODO: return the best made hand's hand score
		return null;
	}
	
	/**
	 * @author BRG
	 * @version Lab #6
	 * @since Lab #6
	 * 
	 *        GetGameWinners - Return an ArrayList of players with the winning hand.
	 *        Could be a tie...
	 * @return
	 * @throws HandException 
	 */
	public ArrayList<Player> GetGameWinners() throws HandException {
		
		ArrayList<Player> WinningPlayers = new ArrayList<Player>();
		//TODO: Return an array list of the winning players in the game
		return WinningPlayers;
	}

}
