package ga;

public abstract class BaseMutation {
	BaseGA ga;
	
	// mutation rate
	double rate = 0.0;
	
	public BaseMutation(double r) {
		rate = r;
	}

	public BaseMutation() {
	}

	public void setGA(BaseGA ga) {
		this.ga = ga;
	}
	
	public abstract void doMutation(IGenome g);

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}
}
