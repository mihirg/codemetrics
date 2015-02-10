package in.gore.metrics;

public class Metric {
	private String name;
	private int covered;
	private int missed;
	
	public Metric() {
		
	}
	
	public Metric (String name, int covered, int missed) {
		this.name = name;
		this.covered = covered;
		this.missed = missed;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCovered() {
		return covered;
	}
	public void setCovered(int covered) {
		this.covered = covered;
	}
	public int getMissed() {
		return missed;
	}
	public void setMissed(int missed) {
		this.missed = missed;
	}
	
}
