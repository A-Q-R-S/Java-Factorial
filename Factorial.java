

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

	
	public Factorial () {
		this.threadCount 		= 0;
		this.result 			= BigInteger.ONE;
		this.threads 			= null;
	}
	
	public Factorial (int threadCount) {
		if (threadCount == 0 || threadCount == 1) {
			this.threadCount 	= 0;
			this.result 		= BigInteger.ONE;
			this.threads 		= null;
		}
		else {
			this.threadCount 	= threadCount;
			this.result 		= BigInteger.ONE;
			this.threads 		= new Thread[threadCount];
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

	
	//------------------------------------	
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
			
			PrintWriter out2 = new PrintWriter(
					resManager.getResource("outFileName_threads"), resManager.getResource("encoding"));
			out2.println(fact.result.toString());
			out2.close();
		}

		
		
		//TODO: clean up and secure the class
		
	}

}
