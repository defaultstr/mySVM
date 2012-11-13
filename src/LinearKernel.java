/**
 * the dot product of two vector
 * @author defaultstr
 *
 */
public class LinearKernel implements Kernel {

	@Override
	public double k(DataPoint d1, DataPoint d2) throws DataPointTypeMismatchException {
		if (d1.getClass() == VectorPoint.class) {
			if (d2.getClass() != VectorPoint.class)
				throw new DataPointTypeMismatchException();
			double ret = 0.0;
			VectorPoint v1 = (VectorPoint)d1;
			VectorPoint v2 = (VectorPoint)d2;
			for (int i = 0; i < v1.x.length; i++) {
				ret += v1.x[i] * v2.x[i];
			}
			return ret;
		} else if (d1.getClass() == SparseVectorPoint.class) {
			if (d2.getClass() != SparseVectorPoint.class)
				throw new DataPointTypeMismatchException();
			SparseVectorPoint s1 = (SparseVectorPoint)d1;
			SparseVectorPoint s2 = (SparseVectorPoint)d2;
			int i = 0;
			int j = 0;
			double ret = 0;
			while (i < s1.pos.length && j < s2.pos.length) {
				if (s1.pos[i] == s2.pos[j]) {
					ret += s1.val[i] * s2.val[j];
					i ++;
					j ++;
				} else if (s1.pos[i] < s2.pos[j]) {
					i ++;
				} else if (s1.pos[i] > s2.pos[j]) {
					j ++;
				}
			}
			return ret;
		} else {
			throw new DataPointTypeMismatchException();
		}
	}

}
