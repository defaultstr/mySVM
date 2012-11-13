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
}
