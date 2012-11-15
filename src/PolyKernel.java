
public class PolyKernel implements Kernel {
	public double p;
	public PolyKernel(double p) {
		this.p = p;
	}
	@Override
	public double k(DataPoint d1, DataPoint d2)
			throws DataPointTypeMismatchException {
		LinearKernel lk = new LinearKernel();
		double x = lk.k(d1, d2) + 1;
		return Math.pow(x, p);
	}

}
