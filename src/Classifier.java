import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Represent the classification function
 * @author defaultstr
 *
 */
public class Classifier {
	private Kernel k;
	public double[] alpha;
	public DataPoint[] sv;
	public double b;
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
		f += b;
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
	public void output2DSupportVector(String filename) throws IOException {
		// for matlab
		File fout = new File(filename);
		Writer w = new OutputStreamWriter(new FileOutputStream(fout));
		w.write(b + "\n");
		SparseVectorPoint vp;
		for (int i = 0; i < sv.length; i++) {
			vp = (SparseVectorPoint)sv[i];
			w.write(sv[i].y + " " + alpha[i] + " " + vp.val[0] + " " + vp.val[1] + "\n");
		}
		w.close();
	}
}
