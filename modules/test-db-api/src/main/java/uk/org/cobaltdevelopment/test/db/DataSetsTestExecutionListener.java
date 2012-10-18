package uk.org.cobaltdevelopment.test.db;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Iterator;

import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import uk.org.cobaltdevelopment.test.db.dataset.ColumnFilter;
import uk.org.cobaltdevelopment.test.db.dataset.DataSets;
import uk.org.cobaltdevelopment.test.db.dataset.TableUtility;

/**
 * Test execution listener implementation that supports working with DBUnit
 * DataSets via Annotations the <code>DataSets</code> annotation.
 * 
 * @see DataSets
 */
public class DataSetsTestExecutionListener extends
		AbstractTestExecutionListener {

	/**
	 * Token used in DataSet XML files to represent a SQL null.
	 */
	private static final String SQL_NULL_TOKEN = "[NULL]";
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DataSetsTestExecutionListener.class);

	/**
	 * Looks for a <code>DataSets</code> annotation on test method and runs a
	 * setup DB Operation using explicit data set name from
	 * <code>setUpDataSet</code> attribute or constructs a name from the test
	 * class simple name and adds a suffix of <code>-dataset.xml</code>.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void beforeTestMethod(TestContext testContext) throws Exception {
		LOGGER.debug("BeforeMethod");
		if (haveTestMethod(testContext)) {
			Method method = testContext.getTestMethod();
			if (haveAnnotation(method)) {
				DataSets dataSetAnnotation = getAnnotation(method);

				LOGGER.debug("SetUp data set name = {}",
						dataSetAnnotation.setUpDataSet());
				IDataSet dataset = null;

				if (StringUtils.hasText(dataSetAnnotation.setUpDataSet())) {
					dataset = getReplacementDataset(
							dataSetAnnotation.setUpDataSet(), testContext);
				} else {
					dataset = getReplacementDataset(testContext);
				}

				IDatabaseConnection dataSource = connection(testContext);
				doCleanInsert(dataset, dataSource);
			}
		}

	}

	/**
	 * Looks for a <code>DataSets</code> annotation on test method and runs a
	 * assertion DB Operation using explicit data set name from
	 * <code>assertDataSet</code> attribute else does nothing.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void afterTestMethod(TestContext testContext) throws Exception {
		LOGGER.debug("AfterMethod");
		if (haveTestMethod(testContext)) {
			if (haveAnnotation(testContext.getTestMethod())) {
				DataSets dataSetAnnotation = getAnnotation(testContext
						.getTestMethod());
				LOGGER.debug("AssertDataSet name = {}",
						dataSetAnnotation.assertDataSet());

				if (StringUtils.hasText(dataSetAnnotation.assertDataSet())) {

					IDataSet expectedDataSet = getReplacementDataset(
							dataSetAnnotation.assertDataSet(), testContext);

					IDatabaseConnection dbUnitConnection = connection(testContext);
					IDataSet actualDataSet = dbUnitConnection.createDataSet();

					if (executingTableAssertion(dataSetAnnotation)) {
						LOGGER.debug("Using ITable Assertion");
						ITable expectedTableDataSet = expectedDataSet
								.getTable(dataSetAnnotation.table().name());
						ITable actualTableDataSet = actualDataSet
								.getTable(dataSetAnnotation.table().name());

						String[] excludeColumnNames = {};
						if (filteringColumns(dataSetAnnotation)) {

							excludeColumnNames = createExcludeColumns(
									actualTableDataSet, dataSetAnnotation
											.table().columnFilter());

						}
						Assertion.assertEqualsIgnoreCols(expectedTableDataSet,
								actualTableDataSet, excludeColumnNames);
					} else {
						LOGGER.debug("Using IDataSet Assertion");
						Assertion.assertEquals(expectedDataSet, actualDataSet);
					}

				}

			}
		}
	}

	private boolean filteringColumns(DataSets dataSetAnnotation) {
		return dataSetAnnotation.table().columnFilter() != null
				&& dataSetAnnotation.table().columnFilter().columnNames() != null
				&& dataSetAnnotation.table().columnFilter().columnNames().length > 0;
	}

	private boolean executingTableAssertion(DataSets dataSetAnnotation) {
		return (dataSetAnnotation.table() != null && StringUtils
				.hasText(dataSetAnnotation.table().name()));
	}

	private String[] createExcludeColumns(ITable table,
			ColumnFilter columnFilter) throws DataSetException {

		String[] result = null;
		if (columnFilter.filterType().equals(ColumnFilter.FilterTypes.INCLUDE)) {
			TableUtility tableUtility = new TableUtility(table);
			result = tableUtility.columnsFiltered(columnFilter.columnNames());
		} else {
			result = columnFilter.columnNames();
		}
		return result;
	}

	private void doCleanInsert(IDataSet setUpDataSet,
			IDatabaseConnection dataSource) {
		try {
			DatabaseOperation.CLEAN_INSERT.execute(dataSource, setUpDataSet);
		} catch (DatabaseUnitException e) {
			handleException(e);
		} catch (SQLException e) {
			handleException(e);
		} catch (Exception e) {
			handleException(e);
		}
	}

	private boolean haveTestMethod(TestContext testContext) {
		return testContext.getTestMethod() != null;
	}

	private boolean haveAnnotation(Method method) {
		return (method.isAnnotationPresent(DataSets.class));
	}

	private DataSets getAnnotation(Method method) {
		DataSets dataSetAnnotation;
		dataSetAnnotation = method.getAnnotation(DataSets.class);
		return dataSetAnnotation;
	}

	private ResourceException handleException(Exception e) {
		LOGGER.error("Error executing DBUNit Operation.", e);
		throw new ResourceException(e);
	}

	/**
	 * Return DBUnit dataset as a convention using the class name of test class
	 * to form the flat xml file
	 * 
	 */
	private IDataSet getReplacementDataset(TestContext testContext)
			throws Exception {
		String dataSetFileName = String.format("%s-dataset.xml", testContext
				.getTestInstance().getClass().getSimpleName());
		return getReplacementDataset(dataSetFileName, testContext);
	}

	/**
	 * Using name retrieve the DBunit flat xml file.
	 */
	private IDataSet getDataset(String name, TestContext testContext) {

		LOGGER.debug("ENTER - getDataSet({})", name);

		FlatXmlDataSet dataset = null;
		try {
			InputStream inputStream = testContext.getTestInstance().getClass()
					.getResourceAsStream(name);

			Assert.notNull(inputStream,
					"Unable to read DBUnit DataSet from classpath");

			dataset = new FlatXmlDataSetBuilder().build(inputStream);
		} catch (DatabaseUnitException e) {
			handleException(e);
		}

		LOGGER.debug("EXIT - getDataSet()");

		return dataset;
	}

	/**
	 * Decorate basic DataSet with replacement capability and replace any
	 * reference to tokens defined in <code>DataSetContext</code> with long
	 * value.
	 */
	private IDataSet getReplacementDataset(String name, TestContext testContext) {
		final IDataSet dataset = getDataset(name, testContext);
		final ReplacementDataSet replacementDataSet = new ReplacementDataSet(
				dataset);

		AbstractDbUnitTestCase testInstance = (AbstractDbUnitTestCase) testContext
				.getTestInstance();

		replaceTokens(replacementDataSet, testInstance);

		return replacementDataSet;
	}

	private void replaceTokens(final ReplacementDataSet replacementDataSet,
			AbstractDbUnitTestCase testInstance) {

		if (testInstance.dataSetContext != null) {
			for (Iterator<String> it = testInstance.dataSetContext.keys(); it
					.hasNext();) {
				String key = it.next();
				LOGGER.debug("Replacing token {}", formatReplacementToken(key));
				replacementDataSet.addReplacementObject(
						formatReplacementToken(key),
						testInstance.dataSetContext.get(Object.class, key));
			}
		}
		replacementDataSet.addReplacementObject(SQL_NULL_TOKEN, null);
	}

	private String formatReplacementToken(String key) {
		return String.format("[%s]", key);
	}

	private IDatabaseConnection connection(TestContext testContext) {

		IDatabaseConnection connection = testContext.getApplicationContext()
				.getBean(IDatabaseConnection.class);

		Assert.notNull(
				connection,
				"Test Executioner Listener requires a IDatabaseConnection implementation defined in the ApplicationContext");
		return connection;
	}

}
