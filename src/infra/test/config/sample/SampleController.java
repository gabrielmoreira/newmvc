package config.sample;

import org.springframework.stereotype.Controller;

@Controller
public class SampleController {

	public void success() {
	}

	public void error() {
		throw new UnsupportedOperationException("Erro de teste");
	}

}
