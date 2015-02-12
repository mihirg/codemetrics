package in.gore.plugin;

import java.util.ArrayList;
import java.util.List;

public class CombinedMetrics {
	
	private List<ClassMetrics> classMetrics;
	
	public CombinedMetrics() {
		
	}

	public List<ClassMetrics> getClassMetrics() {
		if (classMetrics == null)
			classMetrics = new ArrayList<ClassMetrics>();
		return classMetrics;
	}

	public void setClassMetrics(List<ClassMetrics> classMetrics) {
		this.classMetrics = classMetrics;
	}

}
