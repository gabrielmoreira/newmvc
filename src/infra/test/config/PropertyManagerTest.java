package config;

import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

public class PropertyManagerTest {

	@Test(expected = NullPointerException.class)
	public void testNull() {
		new PropertyManager(null);
	}

	@Test
	public void testGetEmptySection() {
		Properties newProperty = new PropertyManager(new Properties()).getSection("teste");
		Assert.assertNotNull(newProperty);
		Assert.assertEquals(0, newProperty.size());
	}

	@Test
	public void testGetEmptySection2() {
		Properties properties = new Properties();
		properties.setProperty("a", "1");
		Properties newProperty = new PropertyManager(properties).getSection("teste");
		Assert.assertNotNull(newProperty);
		Assert.assertEquals(0, newProperty.size());
	}

	@Test
	public void testGetEmptySection3() {
		Properties properties = new Properties();
		properties.setProperty("a", "1");
		properties.setProperty("teste", "a");
		Properties newProperty = new PropertyManager(properties).getSection("teste");
		Assert.assertNotNull(newProperty);
		Assert.assertEquals(0, newProperty.size());
	}

	@Test
	public void testGetEmptySection4() {
		Properties properties = new Properties();
		properties.setProperty("a", "1");
		properties.setProperty("teste.", "a");
		Properties newProperty = new PropertyManager(properties).getSection("teste");
		Assert.assertNotNull(newProperty);
		Assert.assertEquals(0, newProperty.size());
	}

	@Test
	public void testGetSectionWithoutPrefix() {
		Properties properties = new Properties();
		properties.setProperty("a", "1");
		properties.setProperty("teste.a", "2");
		Properties newProperty = new PropertyManager(properties).getSection("teste");
		Assert.assertNotNull(newProperty);
		Assert.assertEquals(1, newProperty.size());
		Assert.assertEquals("2", newProperty.get("a"));
	}

	@Test
	public void testGetSectionWithPrefix() {
		Properties properties = new Properties();
		properties.setProperty("a", "1");
		properties.setProperty("teste.a", "2");
		Properties newProperty = new PropertyManager(properties).getSection("teste", false);
		Assert.assertNotNull(newProperty);
		Assert.assertEquals(1, newProperty.size());
		Assert.assertEquals("2", newProperty.get("teste.a"));
	}

	@Test
	public void testGetSectionWithoutPrefix2() {
		Properties properties = new Properties();
		properties.setProperty("a", "1");
		properties.setProperty("teste.a", "2");
		Properties newProperty = new PropertyManager(properties, "").getSection("teste");
		Assert.assertNotNull(newProperty);
		Assert.assertEquals(1, newProperty.size());
		Assert.assertEquals("2", newProperty.get(".a"));
	}

	@Test
	public void testGetSectionWithPrefix2() {
		Properties properties = new Properties();
		properties.setProperty("a", "1");
		properties.setProperty("teste.a", "2");
		Properties newProperty = new PropertyManager(properties, "").getSection("teste", false);
		Assert.assertNotNull(newProperty);
		Assert.assertEquals(1, newProperty.size());
		Assert.assertEquals("2", newProperty.get("teste.a"));
	}

	@Test
	public void testGetSectionWithoutPrefix3() {
		Properties properties = new Properties();
		properties.setProperty("a", "1");
		properties.setProperty("teste.a", "2");
		Properties newProperty = new PropertyManager(properties, null).getSection("teste");
		Assert.assertNotNull(newProperty);
		Assert.assertEquals(1, newProperty.size());
		Assert.assertEquals("2", newProperty.get(".a"));
	}

	@Test
	public void testGetSectionWithPrefix3() {
		Properties properties = new Properties();
		properties.setProperty("a", "1");
		properties.setProperty("teste.a", "2");
		Properties newProperty = new PropertyManager(properties, null).getSection("teste", false);
		Assert.assertNotNull(newProperty);
		Assert.assertEquals(1, newProperty.size());
		Assert.assertEquals("2", newProperty.get("teste.a"));
	}
}
