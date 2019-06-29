import java.math.BigInteger;

public class FactorialRunnable implements Runnable {

	private Factorial 	fact 			= null;
	private int 		thread 			= -1, 
						numberToSolve 	= -1, 
						threadCount 	= -1,
						workSplitCount	= -1,
						firstCell 		= -1,
						secondCell 		= -1;
	
	public FactorialRunnable (final Factorial fact, int thread) {
		this.fact 			= fact;
		this.thread 		= thread;
		this.numberToSolve 	= fact.getNumberToSolve();
		this.threadCount 	= fact.getThreadCount();
		this.workSplitCount	= Factorial.workSplit.length;
	}
	
	public int getFirst () {
		return firstCell;
	}
	
	public int getSecond () {
		return secondCell;
	}
	
	public void setPair (int first, int second) {
		this.firstCell = first;
		this.secondCell = second;
	}
	
	public int getThread () {
		return thread;
	}

	public void fillTrippleArray(int cell, final BigInteger threadResult, int score) {
		Factorial.trippleArray[cell].fillTripple(threadResult, score);
	}

	private boolean findPair () throws Exception{
		
		if (Factorial.trippleArray.length == 0) {
			throw new Exception("trippleArray out of bounds!");
		}
		
		boolean success	= false;
		int		i 		= 0,
				k 		= 0;

		for (; i < threadCount * Factorial.workSplit.length && !success; i++)
		{
			assert (i < Factorial.trippleArray.length) : "Accessing uninitialized memory!";
			if (Factorial.trippleArray[i].getIsSearchable() == true)
			{
				for (k=i+1; k < threadCount * Factorial.workSplit.length && !success; k++) {
					assert (k < Factorial.trippleArray.length) : "Accessing uninitialized memory!";
					if (Factorial.trippleArray[k].getIsSearchable() == true)
					{
						success = true;
						Factorial.trippleArray[i].setIsSearchable(false);
						Factorial.trippleArray[k].setIsSearchable(false);
						this.setPair(i, k);
					}
				}
			}
		}
		
		return success;
	}
	
	private boolean calculator () throws Exception{
		
		if (Factorial.trippleArray.length == 0) {
			throw new Exception("trippleArray out of bounds!");
		}
		
		boolean	success	= false;
		
		if (this.firstCell >= 0 && this.secondCell >= 0) {
			assert (this.firstCell < Factorial.trippleArray.length && this.secondCell < Factorial.trippleArray.length):
				"Accessing uninitialized memory!";
			
			if (!Factorial.trippleArray[this.firstCell].getIsSearchable() && 
				!Factorial.trippleArray[this.secondCell].getIsSearchable())
			{
				int 		scoreFirst 		= Factorial.trippleArray[this.firstCell].getCalculationsCount(),
							scoreSecond		= Factorial.trippleArray[this.secondCell].getCalculationsCount();
				BigInteger 	num1 			= Factorial.trippleArray[this.firstCell].getNumber(),
							num2 			= Factorial.trippleArray[this.secondCell].getNumber();

				Factorial.trippleArray[this.firstCell] = 
						new Tripple(true, num1.multiply(num2), scoreFirst + scoreSecond);
				Factorial.trippleArray[this.secondCell].setCalculationsCount(0);
				success = true;
				
				if (scoreFirst + scoreSecond == numberToSolve) {
					Factorial.setResult(Factorial.trippleArray[this.firstCell].getNumber());
				}
			} else {
				throw new Exception("Array cells must be closed for searching!");
			}
		} else {
			throw new Exception("Not initialized IDs passed! Both values must be valid!");
		}
		return success;
	}
	
	public void run()
	{
		boolean 	killThread		= false;
		int 		cycle 			= 0;
		int			currentNumber	= thread + 1; // Each thread (staring from 0) must have a different currentNumber
		int			division		= 0;
		Timer 		timer 			= new Timer();
		
		for (; Factorial.result == BigInteger.valueOf(-1) && !killThread; cycle++)
		{
			if (currentNumber <= numberToSolve || division <= numberToSolve)
			{
				timer.start();/***************************************************************************************/
				division += Math.ceil(numberToSolve * Factorial.workSplit[cycle] / 100) + 1;
				
				BigInteger threadResult = BigInteger.ONE;
				boolean foundMultNumber	= false;
				int score = 0;
				
				for (; currentNumber <= division && currentNumber <= numberToSolve; currentNumber += threadCount) {
					threadResult = threadResult.multiply(BigInteger.valueOf(currentNumber));
					foundMultNumber = true;
					score ++;
				}
				if (foundMultNumber == true) {
					this.fillTrippleArray(cycle * threadCount + thread, threadResult, score);
				}
				timer.stop();/****************************************************************************************/

				
				
				//Non-parallel part: keep it fast and simple
				boolean found2Cells = false;
				synchronized (fact) {
					try {
						found2Cells = this.findPair();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				
				
				
				timer.start();/***************************************************************************************/
				if (found2Cells)
				{
					try {
						if (this.calculator())
						{	
							workSplitCount --;
							
							if (workSplitCount <= 0) {
								killThread = true;
							} 
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				timer.stop();/****************************************************************************************/
			} 
			/*else
			{
				killThread = true;
			}/**/
		}
		
		System.out.println(">>> Finished " + thread + ": " + timer.getCurrent() + " milliseconds");
	}
}
