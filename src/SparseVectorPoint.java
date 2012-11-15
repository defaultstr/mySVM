/**
 * DataPoint whose x is a vector of double with almost all 0
 * @author defaultstr
 *
 */
public class SparseVectorPoint extends DataPoint {
	public int[] pos;
	public double[] val;
	public SparseVectorPoint(int len) {
		pos = new int[len];
		val = new double[len];
	}
	public SparseVectorPoint(int[] pos, double[] val, int y) {
		this.y = y;
		this.pos = pos;
		this.val = val;
	}
	public SparseVectorPoint(double[] x, int y) {
		this.y = y;
		int j = 0;
		for (int i = 0; i < x.length; i++) {
			if (x[i] != 0) {
				pos[j] = i;
				val[j] = x[i];
				j++;
			}
		}
	}
	@Override
	public String outputString() {
		String ret = "x:{";
		for (int i = 0; i < pos.length-1; i++) {
			ret += pos[i]+":"+val[i]+",";
		}
		ret += pos[pos.length-1]+":"+val[pos.length-1]+" }";
		ret += " y:" + y;
		return ret;
	}
}
