

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Scanner;

public class Factorial {
	private int 		threadCount = 0;
	private Thread[] 	threads = null;
	private BigInteger 	result = BigInteger.ONE;
	private static ResourceManager resManager;
	private Tripple[] 	trippleArray 	= null;
	
	public Factorial () {
		this.threadCount 		= 0;
		this.result 			= BigInteger.ONE;
		this.threads 			= null;
		this.trippleArray 		= null;
	}
	
	public Factorial (int threadCount) {
		if (threadCount == 0 || threadCount == 1) {
			this.threadCount 	= 0;
			this.result 		= BigInteger.ONE;
			this.threads 		= null;
			this.trippleArray 	= null;
		}
		else {
			this.threadCount 	= threadCount;
			this.result 		= BigInteger.ONE;
			this.threads 		= new Thread[threadCount];
			this.trippleArray 	= new Tripple[threadCount];
		}
	}
	
	public BigInteger getResult() {
		return this.result;
	}

	public void setResult(BigInteger result) {
		this.result = result;
	}

	public int getThreadCount() {
		return this.threadCount;
	}
	
	public static int setThreadCount (Scanner in) {
		
		int threadCount = 0;
		boolean flag = false;
		
		while (flag == false) {
			System.out.println(resManager.getResource("threadCount"));
			while (in.hasNextInt() == false && in.hasNext()) {
				in.next();
				System.out.println(resManager.getResource("threadCount_invalid"));
			}
			threadCount = in.nextInt();
			if (threadCount >= 0) {
				flag = true;
			}
		}
		return threadCount;
	}
	
	public static int setNumber (Scanner in) {
		
		int 	number 	= 0;
		boolean flag 	= false;
		
		while (!flag) {
			System.out.println(resManager.getResource("numberToSolve"));

			while (in.hasNextInt() == false && in.hasNext()) {
				in.next();
				System.out.println(resManager.getResource("numberToSolve_invalid"));
			}
			
			number = in.nextInt();
			
			if (number > 0) {
				flag = true;
			} else {
				number = 0;
			}
		}
		
		return number;
	}
	
	public void fillTrippleArray(int cell, BigInteger threadResult) {
		trippleArray[cell].fillTripple(threadResult);
	}

	public boolean searcher (FactorialRunnable object) {
		
		boolean searching_1 = true,
				searching_2 = true,
				result		= false;
		int	i = 0,
			k = 0;

		for (; i<threadCount && searching_1; i++) {
			if (trippleArray[i].getFlag() == true) {
				searching_1 = false;
				
				for (k=i+1; k<threadCount && searching_2; k++) {
					if (trippleArray[k].getFlag() == true) {
						searching_2 = false;
					}
				}
			}
		}
		i--; k--;
		
		if (searching_1 == false && searching_2 == false)  {
			trippleArray[i].setFlag(false);
			trippleArray[k].setFlag(false);
			object.setFirst(i);
			object.setSecond(k);
			result = true;
		}
		return result;
	}
	
	public boolean calculator (int first, int second) {
		
		boolean result = false;

		if (first >= 0 && second >= 0) {
			if (trippleArray[first].getFlag()==false && trippleArray[second].getFlag()==false) {
				int 		scoreFirst 		= trippleArray[first].getScore(),
							scoreSecond		= trippleArray[second].getScore();
				BigInteger 	num1 			= trippleArray[first].getNumber(),
							num2 			= trippleArray[second].getNumber();
				
				trippleArray[first].setNumber(num1.multiply(num2));
				trippleArray[first].setScore(scoreFirst+scoreSecond);		
				
				if (scoreFirst+scoreSecond == threadCount) {
					setResult(trippleArray[first].getNumber());
				}
				else {
					trippleArray[first].setFlag(true);
					result = true;
				}
			}
		}
		return result;
	}
	
	// without threads
	// TODO: /**/ Could time measurements be moved out to a c++ style class constructor/destructor?
	private static BigInteger solve(int number, int threadCount) {
		BigInteger result 	= BigInteger.ONE;
		BigInteger temp 	= BigInteger.ONE;
		if (threadCount == 0) {
			threadCount = 1;
		}
		
		/**/long startTime = System.currentTimeMillis();
		for (int i=0; i<threadCount; i++) {
			for (int k=i+1; k <= number; k += threadCount) {
				temp = temp.multiply(BigInteger.valueOf(k));
			}
			result = result.multiply(temp);
			temp = BigInteger.ONE;
		}
		
		
		/**/long endTime = System.currentTimeMillis();
		/**/System.out.println(resManager.getResource("time_noThreads") + (endTime - startTime));
		return result;
	}

	
//------------------------------------------------------------------------------------------------------------	
//---------------------------------------------- M A I N -----------------------------------------------------
//------------------------------------------------------------------------------------------------------------
	public static void main(String[] args)
			throws InterruptedException, FileNotFoundException, UnsupportedEncodingException {
		
		resManager 					= new ResourceManager();
		Scanner in 					= new Scanner(System.in);
		int threadCount 			= 0,
			number					= 0,
			simulatedThreadCount 	= 0;
		
		//TODO: Add clues and specifics for the user
		threadCount = setThreadCount(in);
		if (threadCount == 0) {
			System.out.println(resManager.getResource("simulationChoice"));
			simulatedThreadCount = setThreadCount(in);
		}
		number = setNumber(in);
		in.close();
		
		
		
		Factorial fact = new Factorial (threadCount);
		
		// without threads
		if (fact.getThreadCount()==0 || fact.getThreadCount()==1) {
			BigInteger result = solve(number, simulatedThreadCount);
			System.out.println(resManager.getResource("resultResponse"));
			PrintWriter out = new PrintWriter(
					resManager.getResource("outFileName_noThreads"), resManager.getResource("encoding"));
			out.println(result.toString());
			out.close();
		}
		
		// with threads
		//TODO: /**/ Could time measurements be moved out to a c++ style class constructor/destructor?
		else {
			for (int i=0; i<threadCount; i++) {
				fact.trippleArray[i] = new Tripple();
			}
			
			/**/long startTime = System.currentTimeMillis();
			
			for (int i = 0; i < threadCount; i++) {
				fact.threads[i] = new Thread(new FactorialRunnable(fact, i, number));
				fact.threads[i].start();
			}

			for (int i = 0; i < threadCount; i++) {
				fact.threads[i].join();
			}
			
			/**/long endTime = System.currentTimeMillis();
			/**/System.out.println(
					resManager.getResource("time_threads") + (endTime - startTime) + resManager.getResource("units"));
			/**/System.out.println(resManager.getResource("resultResponse"));
			PrintWriter out2 = new PrintWriter(
					resManager.getResource("outFileName_threads"), resManager.getResource("encoding"));
			out2.println(fact.result.toString());
			out2.close();
		}

		
		
		//TODO: clean up and secure the class
		
	}

}
