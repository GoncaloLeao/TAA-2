package benchmarks;

import java.util.ArrayList;

import structures.*;

/**
 * Test the execution time for structures operations calling the Benchmark class
 * for calculate the time and calling the Print2CSV to show the values.
 * 
 * @author Matheus Rosa
 *
 */
public class TimeTests {

	public static void main(String[] args) {
		Benchmark benchmark = new Benchmark(20, 1000, 2);
		Print2CSV export;

		RedBlackTree<Integer> redBlack = new RedBlackTree<>();
		Treap<Integer> treap = new Treap<>();
		SkipList<Integer> list = new SkipList<>();

		ArrayList<ArrayList<Long>> redBlackResult = benchmark.timeTest(redBlack);
		ArrayList<ArrayList<Long>> treapResult = benchmark.timeTest(treap);
		ArrayList<ArrayList<Long>> listReasult = benchmark.timeTest(list);

		ArrayList<String> labels = new ArrayList<>();
		//labels.add("RedBlack");
		labels.add("Treap");
		labels.add("SkipList");

		ArrayList<ArrayList<Long>> data = new ArrayList<>();
		//data.add(redBlackResult.get(0));
		data.add(treapResult.get(0));
		data.add(listReasult.get(0));
		
		export = new Print2CSV("output.csv");
		export.data2CSVFormat(labels, data, benchmark.getStepSize(), false);
	}

}
