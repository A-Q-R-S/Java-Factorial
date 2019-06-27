import java.math.BigInteger;

public class FactorialRunnable implements Runnable {

	private Factorial 	fact 			= null;
	private int 		thread 			= 0, 
						numberToSolve 	= 0, 
						threadCount 	= 0,
						firstCell		= -1,
						secondCell		= -1,
						workSplitCount	= 1;
	
	public FactorialRunnable (Factorial fact, int thread) {
		this.thread 		= thread;
		this.numberToSolve 	= fact.getNumberToSolve();
		this.threadCount 	= fact.getThreadCount();
		this.fact 			= fact;
		this.firstCell 		= -1;
		this.secondCell 	= -1;
		this.workSplitCount	= Factorial.workSplit.length;
	}	
	
	public int getFirst () {
		return firstCell;
	}
	public void setFirst (int num) {
		this.firstCell = num;
	}
	public int getSecond () {
		return secondCell;
	}
	public void setSecond (int num) {
		this.secondCell = num;
	}
	public int getThread () {
		return thread;
	}
	
	public void run() {
		 
		BigInteger 	threadResult 	= BigInteger.ONE;
		boolean 	killThread		= false,
					found2Cells		= false,
					foundMultNumber	= false;
		long 		startTime		= 0,
					endTime			= 0;
		int 		cycle 			= 0;
		int			currentNumber	= thread + 1; // threads start from 0
		int			division		= numberToSolve * Factorial.workSplit[0] / 100 + 1;
			
		
		while (fact.getResult() == BigInteger.ONE && !killThread)
		{				
			/**/startTime = System.currentTimeMillis();
			int score = 0;
			
			for (; currentNumber <= division && currentNumber <= numberToSolve; currentNumber += threadCount) {
				threadResult = threadResult.multiply(BigInteger.valueOf(currentNumber));
				foundMultNumber = true;
				score ++;
			}
			if (foundMultNumber == true) {
				fact.fillTrippleArray(cycle * threadCount + thread, threadResult, score);
			}
			/**/endTime += (System.currentTimeMillis() - startTime);

			
			synchronized (fact) {
				found2Cells = fact.searcher(this);
			}
			
			/**/startTime = System.currentTimeMillis();
			if (found2Cells)
			{
				if (fact.calculator(firstCell, secondCell))
				{
					workSplitCount --;
					
					if (workSplitCount <= 0)
					{
						killThread = true;
					} 
				}
			}
			/**/endTime += (System.currentTimeMillis() - startTime);
			
			// Now prepare for the new cycle:
			if (currentNumber <= numberToSolve) {
				cycle ++;
				threadResult 	= BigInteger.ONE;
				foundMultNumber	= false;
				firstCell 		= -1;
				secondCell 		= -1;
				division 		+= numberToSolve * (Factorial.workSplit[cycle]) / 100 + 1;
			} else {
				killThread		= true;
			}
		}
		
		/**/System.out.println(">>> Finished " + thread + ": " + endTime + " milliseconds");
	}
}
