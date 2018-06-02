package structures;

/**
 * A general definition for the structures implemented in this project.
 *
 * @author Gonçalo Leão & Matheus Rosa
 *
 */
public interface DynamicSet<K extends Comparable<K>> {
	/**
	 * Finds an element in the set.
	 * 
	 * @param key Key to search for.
	 * @return If the element exists, key is returned. If not, NULL is returned.
	 */
	public K find(K key);
	/**
	 * Inserts an element in the set.
	 * 
	 * @param key Key to insert.
	 */
	public void insert(K key);
	/**
	 * Removes an element from the set.
	 * 
	 * @param key Key to remove.
	 */
	public void remove(K key);
	/**
	 * Retrieves the mininum element of the set.
	 * 
	 * @return Minimum key.
	 */
	public K getMin();
	/**
	 * Retrieves the maximum element of the set.
	 * 
	 * @return Maximum key.
	 */
	public K getMax();
	/**
	 * Returns a string that allows the user to check the set's internal structure.
	 * This method is useful for the unit tests.
	 * 
	 * @return String that represents the set.
	 */
	public String toString();
	/**
	 * Returns a string in DOT language that allows the set to be drawn using GraphViz.
	 * This method is useful for GUI.
	 * 
	 * @return DOT string that represents the set.
	 */
	public String toDotString();
}
