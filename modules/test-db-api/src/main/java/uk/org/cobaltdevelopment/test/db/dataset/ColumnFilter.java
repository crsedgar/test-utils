package uk.org.cobaltdevelopment.test.db.dataset;

import static uk.org.cobaltdevelopment.test.db.dataset.ColumnFilter.FilterTypes.EXCLUDE;

import org.dbunit.dataset.ITable;

/**
 * Specifies how columns should be ignored when performing a {@link ITable}
 * assertion.
 * 
 * <p>
 * DBUnit only supports excluding columns from an assertion, this is supported
 * using the EXCLUDE <code>FilterType</code>, this happens to be the default
 * value. In some instances specifying what should be included if more robust in
 * this instance use the include and specify all columns you would like to be
 * used in the pending assertion.
 * </p>
 * 
 * @author "Christopher Edgar"
 * 
 */
public @interface ColumnFilter {

	enum FilterTypes {
		INCLUDE, EXCLUDE
	}

	/**
	 * List of column names to be used in column filter. The semantics of this
	 * list are controlled by the <code>filterType</code> value.
	 * 
	 * @see Table
	 */
	String[] columnNames() default {};

	/**
	 * (Optional) Type of filter to use when constructing list of columns to
	 * ignore in DBUnit assertion.
	 * <ul>
	 * <li>Exclude use all columns in <code>columnNames</code> will be ignored
	 * from ITable assertion. This is the default filter provided by DBUnit.</li>
	 * <li>Include any columns in table not listed in <code>columneNames</code>
	 * will be ignored when performing the ITable assertion. This filter is
	 * useful when you have a table with many columns and you only need to
	 * validate a small number of columns.</li>
	 * </ul>
	 * 
	 * @see ITable
	 */
	FilterTypes filterType() default EXCLUDE;

}
