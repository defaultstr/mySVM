import java.io.IOException;


public class Main {
	public static void main(String[] args) {
		/* simple test
		double [][] x = {{3,3},{4,3},{1,1}};
		int[] y = {1,1,-1};
		ArrayData data = new ArrayData(x, y);
		Trainer t = new Trainer(10, 0.001, new RBFKernel(1.0/data.size()), true, data);
		try {
			Classifier c = t.train();
			for (int i = 0; i < c.alpha.length; i++) {
				System.out.println(c.alpha[i]);
				System.out.println(c.sv[i].outputString());
			}
			System.out.println(c.b);
		} catch (DataPointTypeMismatchException e) {
			e.printStackTrace();
		}
		*/
		/* support libSVM format*/

		try {
			LibSVMData lsd = new LibSVMData("gauss30.1");
			LibSVMData testSet = new LibSVMData("gauss200", lsd.getTags());
			Trainer t = new Trainer(4, 0.001, new RBFKernel(100), false, lsd);
			Classifier c = t.train();
			c.output2DSupportVector("gauss30rbf1.sv");
			System.out.println(c.b);
			System.out.println("acc: " + Tester.test(c, testSet));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DataPointTypeMismatchException e) {
			e.printStackTrace();
		}
		
	}
}
