import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;


public class Trainer {
	private double C;
	private double epsilon;
	private Kernel k;
	private boolean isVerbose = false;
	private InputData input = null;//TODO support multi-data source 
	private int inputSize;
	private double[] alpha;
	private TreeSet<Integer> a0;
	private TreeSet<Integer> ac;
	private TreeSet<Integer> a0_c;
	private double b;

	
	public Trainer() {
		C = 1.0;
		epsilon = 0.001;
		k = new RBFKernel(1.0);
	}
	
	public Trainer(double C, 
			double epsilon, 
			Kernel k, 
			boolean isVerbose,
			InputData input) {
		this.C = C;
		this.epsilon = epsilon;
		this.k = k;
		this.isVerbose = isVerbose;
		this.input = input;
		inputSize = input.size();
	}
	
	public boolean iterate() throws DataPointTypeMismatchException {
		if (alpha == null) {
			alpha = new double[inputSize];
			b = 0.0;
			a0 = new TreeSet<Integer>();
			ac = new TreeSet<Integer>();
			a0_c = new TreeSet<Integer>();
			//set all alpha to 0, so all of them are in set a0
			for (int i = 0; i < inputSize; i++) {
				a0.add(i);
			}
		}
		
		//choose a_1
		HashSet<Integer> a1Mark = new HashSet<Integer>();
		while (true) {
			int a1 = chooseA1(a1Mark);
			if (a1 == -1) {
				break;
			}
			a1Mark.add(a1);
			//choose a_2
			int a2 = chooseA2();
			if (a2 != -1) {
				//update alpha
				double L = 0;
				double H = 0;
				if (input.get(a1).y == input.get(a2).y) {
					L = Math.max(0,  alpha[a2] - alpha[a1]);
					H = Math.min(C, C + alpha[a2] - alpha[a1]);
				} else {
					L = Math.max(0, alpha[a2] + alpha[a1] -C);
					H = Math.min(C, alpha[a2] + alpha[a1]);
				}
				double ita = k.k(input.get(a1), input.get(a1))
							+k.k(input.get(a2), input.get(a2))
							-2 * k.k(input.get(a2), input.get(a1));
				double a2New = alpha[a2] + input.get(a2).y
						*(getE(a1)-getE(a2)) / ita;
				if (a2New > H)
					a2New = H;
				else if (a2New < L)
					a2New = L;
				double a1New = alpha[a1]+input.get(a1).y*input.get(a2).y*(alpha[a2]-a2New);
				
				double b1New = -getE(a1)-input
			}
		}
			
				
		return true;	
		
	}
	
	private int chooseA1(Set<Integer> mark) throws DataPointTypeMismatchException {
		int a1 = -1;
		double maxError = 0.0;
		for (Integer i : a0_c) {
			if (mark.contains(i))
				continue;
			double g = getG(input.get(i));
			if (!equals(input.get(i).y * g, 1.0)) {
				if (Math.abs(input.get(i).y * g - 1.0) > maxError) {
					maxError = Math.abs(input.get(i).y * g - 1.0);
					a1 = i;
				}
			}
		}
		
		if (a1 == -1) {
			maxError = 0.0;
			for (Integer i : a0) {
				if (mark.contains(i))
					continue;
				double g = getG(input.get(i));
				if (input.get(i).y * g < 1.0 - epsilon) {
					if (Math.abs(input.get(i).y * g - 1.0) > maxError) {
						maxError = Math.abs(input.get(i).y * g - 1.0);
						a1 = i;
					}
				}
			}
			for (Integer i : ac) {
				if (mark.contains(i))
					continue;
				double g = getG(input.get(i));
				if (input.get(i).y * g > 1.0 + epsilon) {
					if (Math.abs(input.get(i).y * g - 1.0) > maxError) {
						maxError = Math.abs(input.get(i).y * g - 1.0);
						a1 = i;
					}
				}
			}
		}
		return a1;
	}

	private double getG(DataPoint dp) throws DataPointTypeMismatchException {
		double ret = 0.0;
		for (int i = 0; i < inputSize; i++) {
			ret += alpha[i]*input.get(i).y*k.k(dp, input.get(i)); 
		}
		return ret + b;
	}

	private boolean equals(double d, double e) {
		return (d < e + epsilon) && (d > e - epsilon); 
	}

	
	
	private void output(Object out) {
		if (isVerbose)
			System.out.println(out);
	}
	
	public double getC() {
		return C;
	}
	public void setC(double c) {
		C = c;
	}
	public double getEpsilon() {
		return epsilon;
	}
	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
	}
	public Kernel getK() {
		return k;
	}
	public void setK(Kernel k) {
		this.k = k;
	}




	public InputData getInput() {
		return input;
	}

	public void setInput(InputData input) {
		this.input = input;
	}
}
