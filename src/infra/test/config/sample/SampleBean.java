package config.sample;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class SampleBean {

	@PersistenceContext
	private EntityManager em;

	public EntityManager getEm() {
		return em;
	}

}
