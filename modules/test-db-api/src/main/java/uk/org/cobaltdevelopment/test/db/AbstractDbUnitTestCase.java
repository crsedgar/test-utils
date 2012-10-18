package uk.org.cobaltdevelopment.test.db;

import org.junit.runner.RunWith;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import uk.org.cobaltdevelopment.test.db.dataset.DataSetContext;


/**
 * @author "Christopher Edgar"
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(value = {
		DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class,
		DataSetsTestExecutionListener.class })
public abstract class AbstractDbUnitTestCase {

	protected DataSetContext dataSetContext = null;

}
