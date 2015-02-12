package in.gore.plugin;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class ClassMetrics {
	
	private String name;
	private String packageName;	
	private List<Metric> metrics;
	

	public ClassMetrics() {
		
	}
	
	public ClassMetrics(String name, String packageName) {
		this.name = name;
		this.packageName = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public List<Metric> getMetrics() {
		if (metrics == null)
			metrics = new ArrayList<Metric>();
		return metrics;
	}
	
	@XmlElementWrapper(name = "metricsList")
	@XmlElement(name = "metric")
	public void setMetrics(List<Metric> metrics) {
		this.metrics = metrics;
	}

}
