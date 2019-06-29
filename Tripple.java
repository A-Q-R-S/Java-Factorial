import java.math.BigInteger;

public class Tripple {
	private boolean isSearchable	= false;
	private BigInteger number 		= BigInteger.valueOf(-1);
	private int calculationsCount	= -1;
	
	public Tripple () {
		this.isSearchable 		= false;
		this.number 			= BigInteger.valueOf(-1);
		this.calculationsCount 	= -1;
	}
	
	public Tripple (boolean isSearchable, final BigInteger num, int calculationsCount) {
		this.isSearchable 		= isSearchable;
		this.number 			= num;
		this.calculationsCount 	= calculationsCount;
	}
	
	public void fillTripple (final BigInteger num, int calculationsCount) {
		this.isSearchable 		= true;
		this.number 			= num;
		this.calculationsCount 	= calculationsCount;
	}
	
	public boolean getIsSearchable () {
		return isSearchable;
		}
	
	public void setIsSearchable (boolean isSearchable) {
		this.isSearchable = isSearchable;
	}
	
	public BigInteger getNumber () {
		return number;
	}
	
	public void setNumber (final BigInteger number) {
		this.number = number;
	}
	
	public int getCalculationsCount () {
		return calculationsCount;
	}
	
	public void setCalculationsCount (int calculationsCount) {
		this.calculationsCount = calculationsCount;
	}
}
