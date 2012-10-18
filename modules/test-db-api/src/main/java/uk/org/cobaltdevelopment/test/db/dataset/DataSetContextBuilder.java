package uk.org.cobaltdevelopment.test.db.dataset;

public class DataSetContextBuilder {

	private DataSetContext context;

	private DataSetContextBuilder(DataSetContext dataSetContext) {
		this.context = dataSetContext;
	}

	public static DataSetContextBuilder create() {
		return new DataSetContextBuilder(new DataSetContext());
	}

	public <T> DataSetContextBuilder add(String key, T value) {
		context.set(key, value);
		return this;
	}

	public DataSetContext build() {
		return context;
	}
}