import java.math.BigInteger;

public class Tripple {
	private boolean flag 		= false;
	private BigInteger number 	= BigInteger.ONE;
	private int score 			= 1;
	
	public Tripple () {
		this.flag 	= false;	// false = closed for searching
		this.number = BigInteger.ONE;
		this.score 	= 1;
	}
	
	public Tripple (boolean flag, BigInteger num) {
		this.flag 	= flag;
		this.number = num;
		this.score 	= 1;
	}
	
	public void fillTripple (BigInteger num) {
		this.flag 	= true;	// true = open for searching
		this.number = num;
		this.score 	= 1;
	}
	
	public boolean getFlag () {
		return flag;
		}
	
	public void setFlag (boolean flag) {
		this.flag = flag;
	}
	
	public BigInteger getNumber () {
		return number;
	}
	
	public void setNumber (BigInteger number) {
		this.number = number;
	}
	
	public int getScore () {
		return score;
	}
	
	public void setScore (int score) {
		this.score = score;
	}
}
