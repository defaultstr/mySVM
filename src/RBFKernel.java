/**
 * RBF kernel function
 * k(x,z)= exp(-||x-z||_2/sigma)
 * @author defaultstr
 *
 */
public class RBFKernel implements Kernel {
	private double sigma = 1;
	/**
	 * set sigma 
	 * @param sigma
	 */
	public RBFKernel(double sigma) {
		this.sigma = sigma;
	}

	@Override
	public double k(DataPoint d1, DataPoint d2)
			throws DataPointTypeMismatchException {
		if (d1.getClass() == VectorPoint.class) {
			if (d2.getClass() != VectorPoint.class)
				throw new DataPointTypeMismatchException();
			double f = 0.0;
			VectorPoint v1 = (VectorPoint)d1;
			VectorPoint v2 = (VectorPoint)d2;
			for (int i = 0; i < v1.x.length; i++) {
				f += (v1.x[i] - v2.x[i]) * (v1.x[i] - v2.x[i]);
			}
			return Math.exp(-f/sigma);
		} else if (d1.getClass() == SparseVectorPoint.class) {
			if (d2.getClass() != SparseVectorPoint.class)
				throw new DataPointTypeMismatchException();
			SparseVectorPoint s1 = (SparseVectorPoint)d1;
			SparseVectorPoint s2 = (SparseVectorPoint)d2;
			int i = 0;
			int j = 0;
			double f = 0.0;
			while (i < s1.pos.length && j < s2.pos.length) {
				if (s1.pos[i] == s2.pos[j]) {
					f += (s1.val[i] - s2.val[j]) * (s1.val[i] - s2.val[j]);
					i ++;
					j ++;
				} else if (s1.pos[i] < s2.pos[j]) {
					f += s1.val[i] * s1.val[i];
					i ++;
				} else if (s1.pos[i] > s2.pos[j]) {
					f += s2.val[j] * s2.val[j];
					j ++;
				}
			}
			return Math.exp(-f/sigma);
		} else {
			throw new DataPointTypeMismatchException();
		}
	}

}
