package uk.org.cobaltdevelopment.test.db.dataset;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import uk.org.cobaltdevelopment.test.db.DataSetsTestExecutionListener;

/**
 * The <code>DataSets</code> annotation tells the
 * <code>DataSetsTestExecutionListener</code> if any setup and or assertion
 * datasets should be used during the execution of a test method.
 * 
 * @author "Christopher Edgar"
 * 
 * @see DataSetsTestExecutionListener
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface DataSets {

	/**
	 * Optionally specify the file name of <code>DataSet</code> used to set the
	 * database into a known state before running unit tests.
	 * <code>DataSet</code> is read from the classpath. As a convention the file
	 * is read from the current package location of the unit test class.
	 * 
	 */
	String setUpDataSet() default "";

	/**
	 * Optionally specify the file name of <code>DataSet</code> used to assert
	 * the state of database after the execution of a unit test.
	 * <code>DataSet</code> is read from the classpath. As a convention the file
	 * is read from the current package location of the unit test class.
	 * 
	 */
	String assertDataSet() default "";

	/**
	 * <p>
	 * Optionally provide instructions to the underlying assertion engine to
	 * only assert the contents of one database table.
	 * </p>
	 * 
	 * <p>
	 * An additional column filter allows to selectively include or exclude
	 * columns from the table.. This can be helpful when the table has many
	 * columns that are not F for current unit test.
	 * </p>
	 * 
	 * <p>
	 * Preconditions:
	 * </p>
	 * <p>
	 * The table must exist in the configured data source and optional schema.
	 * </p>
	 * <p>
	 * All columns specified in <code>columnFilter</code> must be present in
	 * table identified by <code>name</code> attribute.
	 * </p>
	 * 
	 */
	Table table() default @Table;

}
