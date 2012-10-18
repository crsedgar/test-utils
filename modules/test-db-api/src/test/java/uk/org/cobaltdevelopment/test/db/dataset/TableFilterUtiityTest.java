package uk.org.cobaltdevelopment.test.db.dataset;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableMetaData;
import org.dbunit.dataset.datatype.DataType;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

public class TableFilterUtiityTest {

	private TableUtility testedObject;
	private ITable tableMock;
	private ITableMetaData tableMetaDataMock;

	@Before
	public void setup() {
		tableMock = EasyMock.createMock(ITable.class);
		tableMetaDataMock = EasyMock.createMock(ITableMetaData.class);
	}

	@Test
	public void testColumns() throws DataSetException {
		String[] expectedResult = { "A", "B", "C", "D", "E" };

		Column[] expectedColumns = expectedColumns();

		EasyMock.expect(tableMock.getTableMetaData()).andReturn(
				tableMetaDataMock);
		EasyMock.expect(tableMetaDataMock.getColumns()).andReturn(
				expectedColumns);

		EasyMock.replay(tableMock, tableMetaDataMock);

		testedObject = new TableUtility(tableMock);

		String[] actualResult = testedObject.columns();

		EasyMock.verify();

		assertThat(actualResult, is(equalTo(expectedResult)));
	}

	@Test
	public void testExcludeColumns() throws DataSetException {

		String[] expectedResult = { "A", "B", "E" };

		Column[] expectedColumns = expectedColumns();

		EasyMock.expect(tableMock.getTableMetaData()).andReturn(
				tableMetaDataMock);
		EasyMock.expect(tableMetaDataMock.getColumns()).andReturn(
				expectedColumns);

		EasyMock.replay(tableMock, tableMetaDataMock);

		testedObject = new TableUtility(tableMock);

		String[] actualResult = testedObject.columnsFiltered(new String[] {
				"C", "D" });

		EasyMock.verify();

		assertThat(actualResult, is(equalTo(expectedResult)));

	}

	private Column[] expectedColumns() {
		Column[] expectedColumns = { new Column("A", DataType.VARCHAR),
				new Column("B", DataType.VARCHAR),
				new Column("C", DataType.VARCHAR),
				new Column("D", DataType.VARCHAR),
				new Column("E", DataType.VARCHAR) };
		return expectedColumns;
	}

}
