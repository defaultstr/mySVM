/**
 * An utility class for test
 * @author defaultstr
 *
 */
public class Tester {
	/**
	 * test classifier c on test set
	 * @param c
	 * @param p test urn accuracy on test set 
	 */
	public static double test(Classifier c, DataPoint[] p) {
		int rightCount = 0;
		for (DataPoint dp : p) {
			if (c.classify(dp) == dp.y)
				rightCount ++;
		}
		return 1.0 * rightCount / p.length;
	}
	
	public static double test(Classifier c, InputData id) {
		int rightCount = 0;
		for (int i = 0; i < id.size(); i++) {
			if (c.classify(id.get(i)) == id.get(i).y)
				rightCount ++;
		}
		return 1.0 * rightCount / id.size();
	}
	
	public static double crossValidation(DataPoint[] p,
										Kernel k,
										double C,
										double epsilon,
										int fold) {
		
		return 0;
	}
}
