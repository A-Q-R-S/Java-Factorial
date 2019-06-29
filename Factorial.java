import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Scanner;

public class Factorial {
	private static ResourceManager resManager;
	private int						numberToSolve	= 0;
	private int 					threadCount 	= 0;
	private Thread[] 				threads 		= null;
	public static Tripple[] 		trippleArray 	= null;
	public static BigInteger 		result 			= BigInteger.valueOf(-1);
	public static final int[]		workSplit 		= {30,25,20,15,10,5};
	
	public Factorial () {
		this.threadCount 			= 0;
		Factorial.result 			= BigInteger.valueOf(-1);
		this.threads 				= null;
		Factorial.trippleArray 		= null;
	}
	
	public Factorial (int numberToSolve, int threadCount) throws Exception
	{	
		if (numberToSolve < 0) {
			throw new Exception("Negative numberToSolve!");
		}
		
		this.threadCount 			= threadCount;
		this.threads 				= new Thread[threadCount];
		this.numberToSolve			= numberToSolve;
		
		Factorial.trippleArray 		= new Tripple[threadCount * workSplit.length];
		Factorial.result 			= BigInteger.valueOf(-1);
	}

	public static void setResult(BigInteger result) {
		Factorial.result = result;
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
	
	// without threads
	private static BigInteger solve(int number, int threadCount)
	{
		BigInteger 	result 	= BigInteger.ONE;
		Timer 		timer 	= new Timer();
		
		if (threadCount == 0) {
			threadCount = 1;
		}
		
		for (int i=0; i<threadCount; i++) {
			BigInteger temp = BigInteger.ONE;
			for (int k=i+1; k <= number; k += threadCount) {
				temp = temp.multiply(BigInteger.valueOf(k));
			}
			result = result.multiply(temp);
		}		
		
		System.out.println(resManager.getResource("time_noThreads") + timer.getCurrent());
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
		
		threadCount = setThreadCount(in);
		if (threadCount == 0) {
			System.out.println(resManager.getResource("simulationChoice"));
			simulatedThreadCount = setThreadCount(in);
		}
		numberToSolve = setNumber(in);
		in.close();
		
		
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
		else {
			Factorial fact;
			try {
				fact = new Factorial (numberToSolve, threadCount);
			
				for (int i=0; i<threadCount * Factorial.workSplit.length; i++) {
					Factorial.trippleArray[i] = new Tripple();
				}
				
				Timer timer = new Timer();
				
				for (int i = 0; i < threadCount; i++) {
					fact.threads[i] = new Thread(new FactorialRunnable(fact, i));
					fact.threads[i].start();
				}
	
				for (int i = 0; i < threadCount; i++) {
					fact.threads[i].join();
				}
				
				System.out.println(
						resManager.getResource("time_threads") + timer.getCurrent() + resManager.getResource("units"));
				System.out.println(resManager.getResource("resultResponse"));
				PrintWriter out2 = new PrintWriter(
						resManager.getResource("outFileName_threads"), resManager.getResource("encoding"));
				out2.println(Factorial.result.toString());
				out2.close();

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
