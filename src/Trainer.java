import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;


public class Trainer {
	private int steps;
	private double C;
	private double epsilon;
	private Kernel k;
	private boolean isVerbose = false;
	private InputData input = null;
	private int inputSize;
	private double[] alpha;
	private double[] E;
	private double b;
	private Random rand = new Random();


	
	public Trainer() {
		steps = 0;
		C = 1.0;
		epsilon = 0.001;
		k = new RBFKernel(1.0);
	}
	
	public Trainer(double C, 
			double epsilon, 
			Kernel k, 
			boolean isVerbose,
			InputData input) {
		steps = 0;
		this.C = C;
		this.epsilon = epsilon;
		this.k = k;
		this.isVerbose = isVerbose;
		this.input = input;
		inputSize = input.size();
	}
	
	
	private boolean takeStep(int i1, int i2) throws DataPointTypeMismatchException{
		if (i1 == i2)
			return false;
		DataPoint p1 = input.get(i1);
		DataPoint p2 = input.get(i2);
		double alpha1 = alpha[i1];
		double alpha2 = alpha[i2];
		//new a1, a2
		double a1, a2;
		int y1 = p1.y;
		int y2 = p2.y;
		double E1 = E[i1];
		double E2 = E[i2];
		int s = y1 * y2;
		double L = 0, H = 0;
		if (y1 != y2) {
			L = Math.max(0,  alpha2 - alpha1);
			H = Math.min(C, C + alpha2 - alpha1);
		} else {
			L = Math.max(0, alpha2 + alpha1 -C);
			H = Math.min(C, alpha2 + alpha1);
		}
		if (equals(L,H))
			return false;
		double k11 = k.k(p1, p1);
		double k12 = k.k(p1, p2);
		double k22 = k.k(p2, p2);
		double eta = 2*k12 - k11 - k22;
		if (eta < 0) {
			a2 = alpha2 - y2 * (E1 - E2) / eta;
			if (a2 <  L) a2 = L;
			else if (a2 > H) a2 = H;
		} else {
			double param1 = 0;
			double param2 = 0;
			for (int i = 0; i < inputSize; i++) {
				if (i == i1 || i == i2)
					continue;
				param1 += input.get(i).y * alpha[i] * k.k(input.get(i), p1);
				param2 += input.get(i).y * alpha[i] * k.k(input.get(i), p2);
			}
			//if a2 = L
			a2 = L;
			a1 = alpha1 + s * (alpha2 - a2);
			double Lobj = 0.5 * k11 * a1 * a1
					     +0.5 * k22 * a2 * a2
						 +s * k12 * a1 * a2
						 -a1 - a2
						 +y1 * a1 * param1
						 +y2 * a2 * param2;
			//if a2 = H
			a2 = H;
			a1 = alpha1 + s * (alpha2 - a2);
			double Hobj = 0.5 * k11 * a1 * a1
					     +0.5 * k22 * a2 * a2
						 +s * k12 * a1 * a2
						 -a1 - a2
						 +y1 * a1 * param1
						 +y2 * a2 * param2;
			if (Lobj < Hobj - epsilon) {
				a2 = L;
			} else if (Lobj > Hobj + epsilon){
				a2 = H;
			} else {
				a2 = alpha2;
			}
		}
		if (Math.abs(a2 - alpha2) < epsilon * (a2 + alpha2 + epsilon))
			return false;
		a1 = alpha1 + s * (alpha2 - a2);
		//update b
		double b1New = -E[i1]-y1*k.k(p1, p1)*(a1 - alpha1)
				   -y2*k.k(p2, p1)*(a2 - alpha2) + b;
		double b2New = -E[i2]-y1*k.k(p1, p2)*(a1 - alpha1)
					   -y2*k.k(p2, p2)*(a2 - alpha2) + b;
		double bError = (b1New + b2New) / 2 - b;
		b = (b1New + b2New) / 2;
		//update E
		for (int i = 0; i < inputSize; i++) {
			E[i] = E[i] + y1*(a1 - alpha1)*k.k(p1, input.get(i))
				  +y2*(a2 - alpha2)*k.k(p2, input.get(i)) + bError;
		}
		alpha[i1] = a1;
		if (alpha[i1] < 0)
			alpha[i1] = 0;
		if (alpha[i1] > C)
			alpha[i1] = C;
		alpha[i2] = a2;
		steps ++;
		if (isVerbose) {
			System.out.println("step: " + steps);
			System.out.print("alpha: ");
			for (int i = 0; i < inputSize; i++)
				System.out.print(alpha[i] + " ");
			System.out.println();
		}
		return true;
	}
	
	private int examine(int i1) throws DataPointTypeMismatchException {
		DataPoint p1 = input.get(i1);
		double alpha1 = alpha[i1];
		double E1 = E[i1];
		double r1 = E1 * p1.y;
		if ((r1 < -epsilon && alpha1 < C) || (r1 > epsilon && alpha1 > 0)) {
			int i2 = -1;
			double maxError = epsilon;
			for (int i = 0; i < inputSize; i++) {
				if (Math.abs(E1 - E[i]) > maxError) {
					i2 = i;
					maxError = Math.abs(E1 - E[i]);
				}
			}
			if (i2 != -1 && takeStep(i1, i2))
				return 1;
			int startPos = rand.nextInt(inputSize);
			int count = 0;
			for (int i = startPos; 
				count < inputSize; 
				count++, i = (i + 1) % inputSize) {
				if (alpha[i] <= epsilon || alpha[i] >= C - epsilon)
					continue;
				if (takeStep(i1, i))
					return 1;
			}
			startPos = rand.nextInt(inputSize);
			for (int i = startPos; 
					count < inputSize; 
					count++, i = (i + 1) % inputSize) {
					if (takeStep(i1, i))
						return 1;
			}
		}
		return 0;
	}
	
	public Classifier train() throws DataPointTypeMismatchException {
		long time = System.currentTimeMillis();
		alpha = new double[inputSize];
		b = 0;
		//compute E
		E = new double[inputSize];
		for (int i = 0; i < inputSize;i ++) {
			E[i] = getG(input.get(i)) - input.get(i).y;
		}
		int numChanged = 0;
		boolean examineAll = true;
		while (numChanged > 0 || examineAll) {
			numChanged = 0; 
			if (examineAll) {
				for (int i = 0; i < inputSize; i++)
					numChanged += examine(i);
			} else {
				for (int i = 0; i < inputSize; i++) {
					if (alpha[i] > epsilon && alpha[i] < C - epsilon)
						numChanged += examine(i);
				}
			}
			if (examineAll)
				examineAll = false;
			else if (numChanged == 0) {
				examineAll = true;
			}
		}
		//count the num of support vector
		int count = 0;
		int bCount = 0;
		for (int i = 0; i < inputSize; i++) {
			if (alpha[i] > epsilon)
				count ++;
			if (alpha[i] >= C - epsilon)
				bCount ++;
		}
		double[] alphaRet = new double[count];
		DataPoint[] sv = new DataPoint[count];
		int j = 0;
		for (int i = 0; i < inputSize; i++) {
			if (alpha[i] > epsilon) {
				alphaRet[j] = alpha[i];
				sv[j] = input.get(i);
				j++;
			}
		}
		
		System.out.println("iter#: " + steps);
		System.out.println("nSV: " + count + " nBSV: " + bCount);
		System.out.println("time: " + (System.currentTimeMillis() - time) / 1000.0 + " s" );
				
		return new Classifier(k, alphaRet, sv, b);
	}
	/*
	public boolean iterate() throws DataPointTypeMismatchException {
		if (alpha == null) {
			alpha = new double[inputSize];
			b = 0.0;
		}
		
		//choose a_1
		HashSet<Integer> a1Mark = new HashSet<Integer>();
		while (true) {
			int a1 = chooseA1(a1Mark);
			if (a1 == -1) {
				return false;
			}
			a1Mark.add(a1);
			//choose a_2
			int a2 = chooseA2(a1);
			if (a2 != -1) {
				//update alpha
				double L = 0;
				double H = 0;
				int y1 = input.get(a1).y;
				int y2 = input.get(a2).y;
				DataPoint p1 = input.get(a1);
				DataPoint p2 = input.get(a2);	
				
				double ita = k.k(p1, p1)
							+k.k(p2, p2)
							-2 * k.k(p1, p2);
				double a2New = alpha[a2] + y2*(getE(a1)-getE(a2)) / ita;
				if (a2New > H)
					a2New = H;
				else if (a2New < L)
					a2New = L;
				double a1New = alpha[a1]+y1*y2*(alpha[a2]-a2New);
				
				//double b1New = -getE(a1)-y1*k.k(p1, p1)*(a1New - alpha[a1])
							   -y2*k.k(p2, p1)*(a2New - alpha[a2]) + b;
				//double b2New = -getE(a2)-y1*k.k(p1, p2)*(a1New - alpha[a1])
							   -y2*k.k(p2, p2)*(a2New - alpha[a2]) + b;
				//b = (b1New + b2New) / 2;
			}
		}
			
				
		return true;	
		
	}*/
	
	

	private int chooseA1(Set<Integer> mark) throws DataPointTypeMismatchException {
		int a1 = -1;
		double maxError = 0.0;
		for (int i = 0; i < inputSize; i ++) {
			if (alpha[i] <= 0 - epsilon || alpha[i] >= C + epsilon)
				continue;
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
			for (int i = 0; i < inputSize; i ++) {
				if (!equals(alpha[i], 0))
					continue;
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
			for (int i = 0; i < inputSize; i ++) {
				if (!equals(alpha[i], C))
					continue;
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
