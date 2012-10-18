package uk.org.cobaltdevelopment.test.db.dataset;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlProducer;
import org.springframework.util.Assert;

public class ElAwareFlatXmlDataSet extends FlatXmlDataSet {

	private DataSetContext context;

	public ElAwareFlatXmlDataSet(DataSetContext context,
			FlatXmlProducer flatXmlProducer) throws DataSetException {
		super(flatXmlProducer);
		this.context = context;
	}

	@Override
	public void row(Object[] values) throws DataSetException {
		Assert.notNull(context, "No context available for ELFlatXMLDataSet");
		// TODO Auto-generated method stub
		super.row(values);
	}

}
