package structures;

import java.io.PrintStream;

public interface DynamicSet<K extends Comparable<K>> {
	public K find(K key);
	public void insert(K key);
	public void remove(K key);
	public K getMin();
	public K getMax();
	public void print(PrintStream p);
	public void dump(String filename);
}
