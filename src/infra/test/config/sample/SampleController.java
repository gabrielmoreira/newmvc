package config.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class SampleController {

	@Autowired
	private SampleBean2 sampleBean;

	public void success() {
	}

	public void error() {
		throw new UnsupportedOperationException("Erro de teste");
	}

	public void successComDelay() {
		sampleBean.doDelay();
		sampleBean.doDelay();
	}

	public void successComPoucoDelay() {
		sampleBean.poucoDelay();
	}

}
