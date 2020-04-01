package pkgEnum;

public enum eSuit {
	HEARTS((char)'\u2764'), CLUBS((char)'\u2663'), 
	DIAMONDS((char)'\u2666'), SPADES((char)'\u2660'), JOKER((char)'\uDCCF');
	
	private char iSuitChar;
	
	private eSuit(char SuitChar)
	{
		this.iSuitChar = SuitChar;
	}

	public char getiSuitChar() {
		return iSuitChar;
	}

	
	
}