import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class LibSVMData implements InputData {

	private ArrayList<DataPoint> data;
	
	public LibSVMData(String filename) throws IOException {
		data = new ArrayList<DataPoint>();
		File fin = new File(filename);
		InputStream is = new FileInputStream(fin);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = br.readLine();
		while(line != null) {
			String[] list = line.split(" ");
			SparseVectorPoint dp = new SparseVectorPoint(list.length - 1);
			if (list[0].startsWith("+"))
				dp.y = 1;
			else if (list[0].startsWith("-"))
				dp.y = -1;
			for (int i = 1; i < list.length; i++) {
				dp.pos[i-1] = Integer.parseInt(list[i].split(":")[0]);
				dp.val[i-1] = Double.parseDouble(list[i].split(":")[1]);
			}
			data.add(dp);
			line = br.readLine();
		}
	}
	@Override
	public DataPoint get(int i) {
		return data.get(i);
	}

	@Override
	public int size() {
		return data.size();
	}

}
