package structures;

public interface DynamicSet<K extends Comparable<K>> {
	public K find(K key);
	public void insert(K key);
	public void remove(K key);
	public K getMin();
	public K getMax();
	public String toString();
	public String toDotString();
}
