package uk.org.cobaltdevelopment.test.db.dataset;

/**
 * <p>
 * Metadata that configures how the assertion engine should validate the state
 * of the database after executing <code>testMethod</code>. When provided the
 * assertion engine will only assert the contents of one table within the
 * configured schema.
 * </p>
 * 
 * @author "Christopher Edgar"
 * 
 * @see ColumnFilter
 * @see DataSets
 * 
 */
public @interface Table {

	/**
	 * Name of database table that should be used in the assertion post
	 * <code>testMethod</code> execution.
	 * <p>
	 * Preconditions:
	 * </p>
	 * <p>
	 * Database table must existing in the configured data source.
	 * </p>
	 */
	String name() default "";

	/**
	 * <p>
	 * (Optional) The column filter that instructs the underlying assertion
	 * engine to only assert the contents of one database table.
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
	 * All columns specified in <code>columnFilter</code> must be present in
	 * table identified by <code>name</code> attribute.
	 * </p>
	 * 
	 */
	ColumnFilter columnFilter() default @ColumnFilter;
}
