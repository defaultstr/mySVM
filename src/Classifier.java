/**
 * Represent the classification function
 * @author defaultstr
 *
 */
public class Classifier {
	private Kernel k;
	private double[] alpha;
	private DataPoint[] sv;
	private double b;
	/**
	 * classification function
	 * @param k kernel function
	 * @param alpha multiplier of support vectors
	 * @param sv support vectors
	 * @param b b in classification function
	 */
	public Classifier(Kernel k, 
					double[] alpha, 
					DataPoint[] sv, 
					double b) {
		this.k = k;
		this.alpha = alpha;
		this.sv = sv;
		this.b = b;
	}
	/**
	 * predict which class the input DataPoint belongs to 
	 * @param p input DataPoint
	 * @return the class y predicted by SVM, y in {+1,-1}
	 */
	public int classify(DataPoint p) {
		double f = 0.0;
		try {
			for (int i = 0; i < alpha.length; i++) {
				f += alpha[i]*sv[i].y*k.k(p, sv[i]);
			}
		} catch (DataPointTypeMismatchException e) {
			e.printStackTrace();
		}	
		if (f >= 0)
			return 1;
		else
			return -1;
	}
	/**
	 * classify all the DataPoint in input array
	 * the result will be written in DataPoint's member y
	 * @param p
	 */
	public void classifyAll(DataPoint[] p) {
		for (DataPoint dp : p) {
			dp.y = classify(dp);
		}
	}
}
