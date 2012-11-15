
public class ArrayData implements InputData {
	private DataPoint[] data;
	
	public ArrayData(double[][] x, int[] y) {
		data = new DataPoint[x.length];
		for (int i = 0; i < x.length; i++) {
			data[i] = new VectorPoint(x[i], y[i]);
		}
	}

	@Override
	public DataPoint get(int i) {
		return data[i];
	}

	@Override
	public int size() {
		return data.length;
	}

}
