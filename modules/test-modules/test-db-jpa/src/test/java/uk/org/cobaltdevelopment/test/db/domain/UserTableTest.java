package uk.org.cobaltdevelopment.test.db.domain;

import static uk.org.cobaltdevelopment.test.db.dataset.ColumnFilter.FilterTypes.EXCLUDE;
import static uk.org.cobaltdevelopment.test.db.dataset.ColumnFilter.FilterTypes.INCLUDE;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import uk.org.cobaltdevelopment.test.db.AbstractDbUnitTestCase;
import uk.org.cobaltdevelopment.test.db.dataset.ColumnFilter;
import uk.org.cobaltdevelopment.test.db.dataset.DataSets;
import uk.org.cobaltdevelopment.test.db.dataset.Table;

@ContextConfiguration
public class UserTableTest extends AbstractDbUnitTestCase {

	@PersistenceContext
	private EntityManager em;

	@Test
	@DataSets(assertDataSet = "UserTableTestAfterUpdate-dataset.xml", table = @Table(name = "T_USER"))
	@Transactional
	public void test_with_table_only() {

		User user = em.find(User.class, 1);
		user.setUserName("cmedgar");
		em.merge(user);
		em.flush();
	}

	@Test
	@DataSets(assertDataSet = "UserTableTestAfterUpdateColumnFiltered-dataset.xml", 
				table = @Table(name = "T_USER", 
				columnFilter = @ColumnFilter(columnNames = { "ACCOUNT_ID", "DATE_REGISTERED" }, filterType = EXCLUDE)))
	@Transactional
	public void test_with_table_and_col_exclusion() {

		User user = em.find(User.class, 1);
		user.setUserName("cmedgar");
		em.merge(user);
		em.flush();
	}

	@Test
	@DataSets(assertDataSet = "UserTableTestAfterUpdateColumnFiltered-dataset.xml", 
			  table = @Table(name = "T_USER", 
			  columnFilter= @ColumnFilter(columnNames={"ID", "USER_NAME"},filterType=INCLUDE)))
	@Transactional
	public void test_with_table_and_col_inclusion() {

		User user = em.find(User.class, 1);
		user.setUserName("cmedgar");
		em.merge(user);
		em.flush();
	}
}
