

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

	
	//------------------------------------	
	public static void main(String[] args)
			throws InterruptedException, FileNotFoundException, UnsupportedEncodingException {
		
		Scanner in 					= new Scanner(System.in);
		int threadCount 			= 0,
			number					= 0,
			simulatedThreadCount 	= 0;
		
		//TODO: Add clues and specifics for the user
		threadCount = setThreadCount(in);
		if (threadCount == 0) {
			System.out.println("/To enter simulation: (>1)/");
			simulatedThreadCount = setThreadCount(in);
		}
		number = setNumber(in);
		in.close();
		
		
		
		Factorial fact = new Factorial (threadCount);
		
		//TODO: implement simulation of threads
		//TODO: implement solver using real threads
		//TODO: measure time
		
		//TODO: clean up and secure the class
		
	}

}
