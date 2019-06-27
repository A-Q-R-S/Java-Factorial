import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Scanner;

public class Factorial {
	private static ResourceManager resManager;
	private int					numberToSolve	= 0;
	private int 				threadCount 	= 0;
	private Thread[] 			threads 		= null;
	private BigInteger 			result 			= BigInteger.ONE;
	private Tripple[] 			trippleArray 	= null;
	public static final int[]	workSplit 		= {30,25,20,15,10,5};
	
	public Factorial () {
		this.threadCount 		= 0;
		this.result 			= BigInteger.ONE;
		this.threads 			= null;
		this.trippleArray 		= null;
	}
	
	public Factorial (int numberToSolve, int threadCount) {
		if (threadCount == 0 || threadCount == 1) {
			this.threadCount 	= 0;
			this.threads 		= null;
			this.trippleArray 	= null;
		}
		else {
			this.threadCount 	= threadCount;
			this.threads 		= new Thread[threadCount];
			this.trippleArray 	= new Tripple[threadCount * workSplit.length];
		}
		this.numberToSolve	= numberToSolve;
		this.result 		= BigInteger.ONE;
	}
	
	public BigInteger getResult() {
		return this.result;
	}

	public void setResult(BigInteger result) {
		this.result = result;
	}

	public int getNumberToSolve() {
		return this.numberToSolve;
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

	public void fillTrippleArray(int cell, BigInteger threadResult, int score) {
		trippleArray[cell].fillTripple(threadResult, score);
	}

	public boolean searcher (FactorialRunnable object) {
		
		boolean searchingA 	= true,
				searchingB 	= true,
				result		= false;
		int	i = 0,
			k = 0;

		for (; i < threadCount * workSplit.length && searchingA; i++) {
			if (trippleArray[i].getFlag() == true) {
				searchingA = false;
				
				for (k=i+1; k < threadCount * workSplit.length && searchingB; k++) {
					if (trippleArray[k].getFlag() == true) {
						searchingB = false;
					}
				}
			}
		}
		i--; k--;
		
		if (!searchingA && !searchingB)  {
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
			if (!trippleArray[first].getFlag() && !trippleArray[second].getFlag()) {
				int 		scoreFirst 		= trippleArray[first].getScore(),
							scoreSecond		= trippleArray[second].getScore();
				BigInteger 	num1 			= trippleArray[first].getNumber(),
							num2 			= trippleArray[second].getNumber();

				trippleArray[first] = new Tripple(true, num1.multiply(num2), scoreFirst + scoreSecond);
				trippleArray[second].setScore(0);
				result = true;
				
				if (scoreFirst + scoreSecond == numberToSolve) {
					setResult(trippleArray[first].getNumber());
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
			numberToSolve			= 0,
			simulatedThreadCount 	= 0;
		
		//TODO: Add clues and specifics for the user
		threadCount = setThreadCount(in);
		if (threadCount == 0) {
			System.out.println(resManager.getResource("simulationChoice"));
			simulatedThreadCount = setThreadCount(in);
		}
		numberToSolve = setNumber(in);
		in.close();
		
		
		
		Factorial fact = new Factorial (numberToSolve, threadCount);
		
		// without threads
		if (threadCount==0 || threadCount==1) {
			BigInteger result = solve(numberToSolve, simulatedThreadCount);
			System.out.println(resManager.getResource("resultResponse"));
			PrintWriter out = new PrintWriter(
					resManager.getResource("outFileName_noThreads"), resManager.getResource("encoding"));
			out.println(result.toString());
			out.close();
		}
		
		// with threads
		//TODO: /**/ Could time measurements be moved out to a c++ style class constructor/destructor?
		else {
			for (int i=0; i<threadCount * workSplit.length; i++) {
				fact.trippleArray[i] = new Tripple();
			}
			
			/**/long startTime = System.currentTimeMillis();
			
			for (int i = 0; i < threadCount; i++) {
				fact.threads[i] = new Thread(new FactorialRunnable(fact, i));
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
