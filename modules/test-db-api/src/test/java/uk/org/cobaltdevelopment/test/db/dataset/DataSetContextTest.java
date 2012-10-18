package uk.org.cobaltdevelopment.test.db.dataset;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

public class DataSetContextTest {

	private DataSetContext testedObject;
	private MyObject myObject;

	//@formatter:off
	public static class MyObject{
		private String name;
		public String getName() {return name;}
		public void setName(String name) {this.name = name;}
		public String toString() {return "name="+name;}
	}
	//@formatter:on

	@Before
	public void set_up() {
		testedObject = new DataSetContext();
		myObject = new MyObject();
		myObject.setName("DBUNIT");
	}

	@Test
	public void test_set_and_get_with_cast() {
		populateContext(testedObject);

		assertThat((String) testedObject.get("STR"), is(equalTo("SPRING")));
		assertThat((MyObject) testedObject.get("MYOBJ"), is(equalTo(myObject)));
		assertThat((Long) testedObject.get("NUM"), is(equalTo(102l)));

	}

	@Test
	public void test_set_and_get_with_generics() {
		populateContext(testedObject);

		assertThat(testedObject.get(String.class, "STR"), is(equalTo("SPRING")));
		assertThat(testedObject.get(MyObject.class, "MYOBJ"),
				is(equalTo(myObject)));
		assertThat(testedObject.get(Long.class, "NUM"), is(equalTo(102l)));

	}

	@Test(expected = IllegalArgumentException.class)
	public void test_unknown_key() {
		testedObject.get("FOOBAR");
	}

	@Test
	public void testKeys() {
		testedObject.set("A", new String("A VALUE"));
		testedObject.set("B", new String("B VALUE"));
		testedObject.set("C", new String("C VALUE"));

		Iterator<String> keys = testedObject.keys();
		assertThat(keys.hasNext(), is(equalTo(true)));
		assertThat(keys.next(), is(equalTo("A")));
		assertThat(keys.next(), is(equalTo("B")));
		assertThat(keys.next(), is(equalTo("C")));
		assertThat(keys.hasNext(), is(equalTo(false)));
	}

	private void populateContext(DataSetContext testedObject) {
		Object obj = new Object();
		testedObject.set("OBJ", obj);
		testedObject.set("STR", new String("SPRING"));
		testedObject.set("MYOBJ", myObject);
		testedObject.set("NUM", 102l);
	}
}
