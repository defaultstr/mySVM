/**
 * DataPoint whose x is a vector of double
 * @author defaultstr
 *
 */
public class VectorPoint extends DataPoint{
	public double[] x;
	public VectorPoint(int len) {
		this.x = new double[len];
	}
	public VectorPoint(double[] x, int y) {
		this.x = x;
		this.y = y;
	}
	@Override
	public String outputString() {
		String ret = "x:[";
		for (int i = 0; i < x.length-1; i++)
			ret += x[i] +",";
		ret += x[x.length-1] + "]";
		ret += "] y:" + y;
		return ret;
	}
}
