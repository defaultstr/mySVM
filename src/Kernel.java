/**
 * interface for kernel function
 * @author defaultstr
 *
 */
public interface Kernel {
	public double k(DataPoint d1, DataPoint d2) throws DataPointTypeMismatchException;
}
