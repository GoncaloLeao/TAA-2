package benchmarks;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * To transform data in csv format.
 * 
 * @author Matheus Rosa
 *
 */
public class Print2CSV {

	PrintWriter file;
	String fileName;

	public Print2CSV(String fileName) {
		this.fileName = fileName; 
	}

	/**
	 * Receive the data of a matrix and print it in csv format. Outputs to standard
	 * output or specific file.
	 * 
	 * @param labels
	 *            - the names of each row in the matrix
	 * @param data
	 *            - the matrix of data
	 * @param step
	 *            - size of test in each column of the matrix
	 * @param toFile
	 *            - to indicate if the print will be at standard or file output (false to standard output)
	 */
	public void data2CSVFormat(ArrayList<String> labels, ArrayList<ArrayList<Long>> data, int step, boolean toFile) {
		
		try {
			file = new PrintWriter(fileName);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		StringBuilder string = new StringBuilder();
		string.append("Number of elements");
		for (int i = 0; i < data.get(0).size(); i++)
			string.append("," + (i + 1) * step);
		string.append("\n");
		for (int i = 0; i < data.size(); i++) {
			string.append(labels.get(i));
			for (int j = 0; j < data.get(i).size(); j++) {
				string.append("," + data.get(i).get(j));
			}
			string.append("\n");
		}

		if (toFile) file.print(string.toString());
		else System.out.println(string);
		
		file.close();
	}

}
