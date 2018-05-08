package benchmarks;

/** 
 * @author Matheus Rosa
 *
 */
public class NotEmptySetException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public NotEmptySetException() {
		super("The given set is not empty.");
	}
	
}
