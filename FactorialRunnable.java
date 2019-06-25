

import java.math.BigInteger;

public class FactorialRunnable implements Runnable {

	private Factorial fact = null;
	private int thread 		= 0, 
			numberToSolve 	= 0, 
			threadCount 	= 0;

	public FactorialRunnable (Factorial fact, int thread, int numberToSolve) {
		this.thread 		= thread;
		this.numberToSolve 	= numberToSolve;
		this.threadCount 	= fact.getThreadCount();
		this.fact 			= fact;
	}

	public void run() {
		BigInteger temp = BigInteger.ONE;
		
		/**/long startTime = System.currentTimeMillis();
		for (int i = thread+1; i <= numberToSolve; i += threadCount) {
			temp = temp.multiply(BigInteger.valueOf(i));
		}

		synchronized (fact) {
			BigInteger currentResult = fact.getResult();
			fact.setResult(currentResult.multiply(temp));
		}
		/**/long endTime = System.currentTimeMillis();
		/**/System.out.println("Thread " + thread + ": " + (endTime - startTime));
	}

}
