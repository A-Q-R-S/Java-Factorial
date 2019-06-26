import java.math.BigInteger;

public class FactorialRunnable implements Runnable {

	private Factorial 	fact 			= null;
	private int 		thread 			= 0, 
						numberToSolve 	= 0, 
						threadCount 	= 0,
						first			= -1,
						second			= -1;

	public FactorialRunnable (Factorial fact, int thread, int numberToSolve) {
		this.thread 		= thread;
		this.numberToSolve 	= numberToSolve;
		this.threadCount 	= fact.getThreadCount();
		this.fact 			= fact;
		this.first 			= -1;
		this.second 		= -1;
	}
	
	public int getFirst () {
		return first;
	}
	public void setFirst (int num) {
		this.first = num;
	}
	public int getSecond () {
		return second;
	}
	public void setSecond (int num) {
		this.second = num;
	}
	public int getThread () {
		return thread;
	}

	public void run() {
		BigInteger 	threadResult 	= BigInteger.ONE;
		boolean 	found2Cells		= false,
					killThread 		= false;
		
		/**/long startTime = System.currentTimeMillis();
		
		for (int i = thread+1; i <= numberToSolve; i += threadCount) {
			threadResult = threadResult.multiply(BigInteger.valueOf(i));
		}
		
		fact.fillTrippleArray(thread, threadResult);
		
		/**/long endTime = System.currentTimeMillis();
		/**/System.out.println("Thread " + thread + ": " + (endTime - startTime) + " milliseconds");

		while (fact.getResult() == BigInteger.ONE && killThread == false)
		{
			synchronized (fact)
			{
				found2Cells = fact.searcher(this);
				
				if (!found2Cells) {
					try {
						fact.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				else {
					fact.notifyAll();
				}
			}
			
			if (found2Cells && fact.calculator(first, second)) {
				killThread = true;
			}
		}
	}

}
