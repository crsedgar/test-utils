package uk.org.cobaltdevelopment.test.db;

import static org.springframework.util.Assert.notNull;
import static uk.org.cobaltdevelopment.test.db.IDatabaseConnectionFactoryBean.DatabaseVendor.GENERIC;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.ext.db2.Db2Connection;
import org.dbunit.ext.h2.H2Connection;
import org.dbunit.ext.hsqldb.HsqldbConnection;
import org.dbunit.ext.mysql.MySqlConnection;
import org.dbunit.ext.oracle.OracleConnection;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

public class IDatabaseConnectionFactoryBean implements
		FactoryBean<IDatabaseConnection> {

	public enum DatabaseVendor {
		ORACLE, MYSQL, H2, DB2, HSQL, GENERIC
	};

	private TransactionAwareDataSourceProxy dataSource;
	private String schema = "";
	private DatabaseVendor dbVendor = GENERIC;

	public void setDataSource(TransactionAwareDataSourceProxy dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * 
	 * @param Optionally
	 *            provision the database schema identifier
	 */
	public void setSchema(String schema) {
		this.schema = schema;
	}

	/**
	 * Optionally provision the database vendor type, used to configure the
	 * appropriate DB Unit {@link IDatabaseConnection} implementation. Generic
	 * database connection used by default.
	 * 
	 */
	public void setDbVendor(DatabaseVendor dbVendor) {
		this.dbVendor = dbVendor;
	}

	@Override
	public IDatabaseConnection getObject() throws Exception {

		notNull(dbVendor, "Database vendor must be initialised.");

		IDatabaseConnection result = null;
		switch (dbVendor) {
		case MYSQL:
			result = new MySqlConnection(dataSource.getConnection(), schema);
			break;
		case H2:
			result = new H2Connection(dataSource.getConnection(), schema);
			break;
		case ORACLE:
			result = new OracleConnection(dataSource.getConnection(), schema);
			break;
		case GENERIC:
			result = new DatabaseConnection(dataSource.getConnection(), schema);
			break;
		case DB2:
			result = new Db2Connection(dataSource.getConnection(), schema);
			break;
		case HSQL:
			result = new HsqldbConnection(dataSource.getConnection(), schema);
			break;
		}
		return result;
	}

	@Override
	public Class<?> getObjectType() {
		return IDatabaseConnection.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

}
