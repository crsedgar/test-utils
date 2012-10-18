package uk.org.cobaltdevelopment.test.db.dataset;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableMetaData;

import uk.org.cobaltdevelopment.test.db.ResourceException;

public class TableUtility {

	private ITableMetaData metaData;

	public TableUtility(ITable table) {
		this.metaData = table.getTableMetaData();
	}

	public String[] columns() {
		String[] result = null;
		Column[] columns = null;
		try {
			columns = metaData.getColumns();
			result = new String[columns.length];
			for (int i = 0; i < columns.length; i++) {
				Array.set(result, i, columns[i].getColumnName());
			}
		} catch (DataSetException e) {
			throw new ResourceException(e);
		}
		return result;
	}

	public String[] columnsFiltered(String[] excludeColumnNames) {

		String[] result = null;

		List<String> exList = Arrays.asList(excludeColumnNames);
		List<String> resultList = new ArrayList<String>();

		try {
			List<Column> colsList = Arrays.asList(metaData.getColumns());

			for (Column column : colsList) {
				if (!exList.contains(column.getColumnName())) {
					resultList.add(column.getColumnName());
				}
			}

			result = resultList.toArray(new String[0]);

		} catch (DataSetException e) {
			throw new ResourceException(e);
		}

		return result;

	}
}
