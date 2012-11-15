import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class LibSVMData implements InputData {

	private List<String> tags;
	private ArrayList<DataPoint> data;
	
	public LibSVMData(String filename) throws IOException {
		tags = new ArrayList<String>();
		data = new ArrayList<DataPoint>();
		File fin = new File(filename);
		InputStream is = new FileInputStream(fin);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = br.readLine();
		while(line != null) {
			String[] list = line.split(" ");
			SparseVectorPoint dp = new SparseVectorPoint(list.length - 1);
			if (tags.size() == 0) {
				tags.add(list[0]);
			} else if (tags.size() == 1 && !list[0].equals(tags.get(0))) {
				tags.add(list[0]);
			}
			if (list[0].equals(tags.get(0))) {
				dp.y = +1;
			} else if (list[0].equals(tags.get(1))) {
				dp.y = -1;
			}
			for (int i = 1; i < list.length; i++) {
				dp.pos[i-1] = Integer.parseInt(list[i].split(":")[0]);
				dp.val[i-1] = Double.parseDouble(list[i].split(":")[1]);
			}
			data.add(dp);
			line = br.readLine();
		}
	}
	
	public LibSVMData(String filename, List<String> tags) throws IOException {
		this.tags = tags;
		data = new ArrayList<DataPoint>();
		File fin = new File(filename);
		InputStream is = new FileInputStream(fin);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = br.readLine();
		while(line != null) {
			String[] list = line.split(" ");
			SparseVectorPoint dp = new SparseVectorPoint(list.length - 1);
			if (list[0].equals(tags.get(0))) {
				dp.y = +1;
			} else if (list[0].equals(tags.get(1))) {
				dp.y = -1;
			}
			for (int i = 1; i < list.length; i++) {
				dp.pos[i-1] = Integer.parseInt(list[i].split(":")[0]);
				dp.val[i-1] = Double.parseDouble(list[i].split(":")[1]);
			}
			data.add(dp);
			line = br.readLine();
		}
	}
	
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	public List<String> getTags() {
		return this.tags;
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
