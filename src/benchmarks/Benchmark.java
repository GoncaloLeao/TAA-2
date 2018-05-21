package benchmarks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.TreeSet;

import structures.DynamicSet;

/**
 * Given a Data Structure, test its main operations time execution: INSERT,
 * REMOVE, MAX, MIN
 * 
 * @author Matheus Rosa
 *
 */
public class Benchmark {

	private final Random RAND;
	private final int MAX_RAND = 100000000;
	private final int TESTS = 5;

	private int numberTests;
	private int stepSize;
	private int numberSamples;

	public static enum Type {
		FIND, INSERT, REMOVE, MAX, MIN;
	}

	public Benchmark(int numberTests, int stepSize, int numberSamples) {
		this.numberTests = numberTests;
		this.stepSize = stepSize;
		this.numberSamples = numberSamples;
		RAND = new Random(System.currentTimeMillis());
	}

	/**
	 * Realize exhaustive time test in a given structure using random inputs.
	 * 
	 * @param set
	 *            - to be tested
	 * @param inputFormat
	 * 			  - of the elements to be inserted. 0 for ascending, 1 for uniform and 2 for Gaussian.           
	 *            
	 * @return - The result for the tests INSERT, REMOVE, MAX and MIN.
	 * @throws NotEmptySetException
	 *             - if the set is not empty
	 */
	public ArrayList<ArrayList<Long>> timeTest(DynamicSet<Integer> set, int inputFormat) throws NotEmptySetException {
		if (set.getMin() != null)
			throw new NotEmptySetException();

		ArrayList<ArrayList<Long>> results = initializeArray();

		for (int j = 0; j < numberTests; j++) {
			System.out.println("Testing: "+j);
			for (int i = 0; i < numberSamples; i++) {
				System.out.println("\tSample: "+i);
				Long cont = 0L, start;
				TreeSet<Integer> auxSet = new TreeSet<>();
				ArrayList<Integer> auxArray = new ArrayList<>();
				
				for (int k = 0; k < stepSize * (j + 1); k++) {
					int newElement;
				
					if (inputFormat == 0) {
						newElement = k;
					} else if (inputFormat == 1) {
						newElement = (int) (RAND.nextGaussian()*MAX_RAND);
					} else {
						newElement = RAND.nextInt(MAX_RAND);
					}
					
					auxSet.add(newElement);
					auxArray.add(newElement);
					
					start = System.nanoTime();
					set.insert(newElement);
					cont += System.nanoTime() - start;
					incrementValue(results, j, Type.INSERT, cont);

					cont = 0L;
					start = System.nanoTime();
					set.getMax();
					cont += System.nanoTime() - start;
					incrementValue(results, j, Type.MAX, cont);

					cont = 0L;
					start = System.nanoTime();
					set.getMin();
					cont += System.nanoTime() - start;
					incrementValue(results, j, Type.MIN, cont);

				}

				Collections.shuffle(auxArray);
				
				for (Integer it : auxArray) {
					cont = 0L;
					start = System.nanoTime();
					set.find(it);
					cont += System.nanoTime() - start;
					incrementValue(results, j, Type.FIND, cont);
				}
				
				for (Integer it : auxArray) {
					cont = 0L;
					start = System.nanoTime();
					set.remove(it);
					cont += System.nanoTime() - start;
					incrementValue(results, j, Type.REMOVE, cont);
				}
			}
			System.out.println();
		}

		for (int i = 0; i < results.size(); i++) {
			for (int j = 0; j < results.get(i).size(); j++) {
				results.get(i).set(j, (results.get(i).get(j) / numberSamples)/(stepSize*(i+1)));
			}
		}

		return results;
	}

	private void incrementValue(ArrayList<ArrayList<Long>> target, int caseTest, Type operation, Long value) {
		
		switch (operation) {
		case FIND:
			target.get(0).set(caseTest, target.get(0).get(caseTest) + value);
			break;
		case INSERT:
			target.get(1).set(caseTest, target.get(1).get(caseTest) + value);
			break;
		case REMOVE:
			target.get(2).set(caseTest, target.get(2).get(caseTest) + value);
			break;
		case MAX:
			target.get(3).set(caseTest, target.get(3).get(caseTest) + value);
			break;
		case MIN:
			target.get(4).set(caseTest, target.get(4).get(caseTest) + value);
			break;
		default:
			System.out.println("Invalid opeartion!");
		}
	}

	private ArrayList<ArrayList<Long>> initializeArray() {
		ArrayList<ArrayList<Long>> tmp = new ArrayList<ArrayList<Long>>();
		for (int i = 0; i < TESTS; i++) {
			ArrayList<Long> aux = new ArrayList<>();
			for (int j = 0; j < numberTests; j++) {
				aux.add(0L);
			}
			tmp.add(aux);
		}
		return tmp;
	}

	public int getNumberTests() {
		return numberTests;
	}

	public void setNumberTests(int numberTests) {
		this.numberTests = numberTests;
	}

	public int getStepSize() {
		return stepSize;
	}

	public void setStepSize(int stepSize) {
		this.stepSize = stepSize;
	}
}
