package config.sample;

public class SampleBean2 {

	public void doDelay() {
		delay(1001);
	}

	private void delay(long duration) {
		try {
			Thread.sleep(duration);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public void poucoDelay() {
		delay(1000);

	}

}
