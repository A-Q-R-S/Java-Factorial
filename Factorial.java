

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Scanner;

public class Factorial {
	private int COUNT_THREADS = 0;
	private Thread[] threads = null;
	private BigInteger result = BigInteger.ONE;

	
	public Factorial () {
		COUNT_THREADS 		= 0;
		result 				= BigInteger.ONE;
		threads 			= null;
	}
	
	public Factorial (int threadCount) {
		if (threadCount == 0 || threadCount == 1) {
			COUNT_THREADS 	= 0;
			result 			= BigInteger.ONE;
			threads 		= null;
		}
		else {
			COUNT_THREADS 	= threadCount;
			this.result 	= BigInteger.ONE;
			this.threads 	= new Thread[threadCount];
		}
	}
	
	public BigInteger getResult() {
		return result;
	}

	public void setResult(BigInteger result) {
		this.result = result;
	}

	public int getThreadCount() {
		return COUNT_THREADS;
	}
	
	public static int setThreadCount (Scanner in) {
		
		int threadCount = 0;
		boolean flag = false;
		
		// user sets COUNT_THREADS:
		while (flag == false) {
			System.out.println("Set COUNT_THREADS: ");		
			while (in.hasNextInt() == false && in.hasNext()) {
				in.next();
				System.out.println("Set valid COUNT_THREADS: ");
			}
			threadCount = in.nextInt();
			if (threadCount >= 0) {
				flag = true;
			}
		}
		return threadCount;
	}
	
	public static int setNumber (Scanner in) {
		
		int number = 0;
		boolean flag = false;
		
		// user sets number to solve:
		flag = false;
		while (flag == false) {
			System.out.println("Set number to solve: ");		
			while (in.hasNextInt() == false&& in.hasNext()) {
				in.next();
				System.out.println("Set valid number to solve: ");
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
		/**/System.out.println("Time without threads: " + (endTime - startTime));
		return result;
	}

	
	//------------------------------------	
	public static void main(String[] args)
			throws InterruptedException, FileNotFoundException, UnsupportedEncodingException {
		
		Scanner in 					= new Scanner(System.in);
		int threadCount 			= 0,
			number					= 0,
			simulatedThreadCount 	= 0;
		
		//TODO: Add clues and specifics for the user
		//TODO: configure resource strings, not hard-coded
		threadCount = setThreadCount(in);
		if (threadCount == 0) {
			System.out.println("/To enter simulation: (>1)/");
			simulatedThreadCount = setThreadCount(in);
		}
		number = setNumber(in);
		in.close();
		
		
		
		Factorial fact = new Factorial (threadCount);
		
		// without threads
		//TODO: configure resource strings, not hard-coded
		if (fact.getThreadCount()==0 || fact.getThreadCount()==1) {
			BigInteger result = solve(number, simulatedThreadCount);
			System.out.println("Result being printed in txt file...");
			PrintWriter out = new PrintWriter("out.txt", "UTF-8");
			out.println(result.toString());
			out.close();
		}
		
		// with threads
		//TODO: /**/ Could time measurements be moved out to a c++ style class constructor/destructor?
		//TODO: configure resource strings, not hard-coded
		else {
			/**/long startTime = System.currentTimeMillis();
			for (int i = 0; i < fact.COUNT_THREADS; i++) {
				fact.threads[i] = new Thread(new FactorialRunnable(fact, i, number));
				fact.threads[i].start();
			}

			for (int i = 0; i < fact.COUNT_THREADS; i++) {
				fact.threads[i].join();
			}
			/**/long endTime = System.currentTimeMillis();
			/**/System.out.println("Total time: " + (endTime - startTime) + " milliseconds");
			
			PrintWriter out2 = new PrintWriter("out2.txt", "UTF-8");
			out2.println(fact.result.toString());
			out2.close();
		}

		
		
		//TODO: clean up and secure the class
		
	}

}
