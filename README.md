#Test Utils
A collection of unit testing utilities.

##Modules List
* Test DB API

##Test DB API
Introduction
Test DB API is a Java 5 API abstraction layer over the popular DBUnit unit testing tool. The Annotation based API fully integrates with the Spring 3.0 Test project.

There are two sub modules that illustrate how to use the API with a Spring 3 JDBC project and a Spring 3 enabled JPA project. 

##Background
Database testing can be a real chore, without any specific framework you typically need to;  

1.  initialise the database with vendor specific sql files when testing your data retrieval code
2.  write lines of code to validate the state of the Database after running your mutation code.  

The following sources have served as the main inspiration for this module;

1. [JUnit in Action 2nd Edition](http://www.manning.com/tahchiev/) - excellent book that contains a working example of using annotations to abstract away DBUnit. Uses older version of JUnit and does not integrate with Spring.
2. [Unitils](http://www.unitils.org) - leverages Java 5 annotations for configuring DBUnit and does provide Spring framework, however it currently supports version 2.5 of the framework.
 
Spring provides some database unit testing support in the shape of <code>AbstractTransactionalJUnit4SpringContextTests</code> class and though using this does make you code more efficient it doesn't tackle the main issues identified above. 

##Installation
###[Maven](http://apache.maven.org/) Users  
Download the source and run <code>mvn install</code>. Then add following dependency to your Maven project.

	<dependency>
			<groupId>uk.org.cobaltdevelopment.test</groupId>
			<artifactId>test-db-api</artifactId>
			<version>1.0-SNAPSHOT</version>
			<scope>test</scope>
	</dependency>

###Usage
Extend the AbstractDbUnitTestCase and add the <code>DataSets</code> annotation to any test method that you would like to hook into DBUnit.

####Setting Up the Database
As a convention a set up dataset is read from the classpath by constructing the name from the following pattern <code>${SimpleClassName}-dataset.xml</code> e.g. A test class of JdbcUserDAOTest would resolve to <code>JdbcUserDAOTest-dataset.xml</code>.     

If using a standard maven project it is quite convenient to place all dataset XML files within the <code>src/test/resources/</code> folder.  

You can override the setup dataset by passing in the relative dataset file name with the <code>setUpDataSet</code> element.

	@ContextConfiguration
	@Transactional
	public class UserDAOImplTest extends AbstractDbUnitTestCase {

		@Autowired
		private UserDAO userDAO;

		@Test
		@DataSets
		public void test_find() {
			User user = userDAO.find(1l);
			assertThat(user.getId(), is(equalTo(1l)));
			assertThat(user.getUserName(), is(equalTo("jtbrown")));
		}
	
####Asserting changes
The other use case of DBUnit is validating the state of a database after running DAO mutation code.

The <code>assertDataSet</code> element on the <code>DataSets</code> annotation is used to explicitly state what dataset should be used to validate the state of the database after the test method has finished executing (dataset assertions will not run by default).  

The test-db-api TestExecutionListener uses the DBUnit ReplacementDataSet when executing a dataset assertion, allowing you to place values generated at runtime into a dataset context. This feature is useful for primary keys for example.  

The <code>DataSets</code> annotation allows you to narrow the assertion scope by specifying a database table name to use in the assertion. You can also filter the columns used in this assertion (inclusion/exclusion), useful when working with tables with many columns.

	@Test
	@DataSets(assertDataSet = "UserDAOImplTestAfterAdd-dataset.xml")
	public void test_add() {

		User newUser = UserBuilder.create().user("cmedgar")
				.dateRegistered(createDate(2012, Calendar.AUGUST, 21))
				.role("ADMIN").build();

		long id = userDAO.add(newUser);
		assertThat(id, is(not(equalTo(0l))));
		assertThat(newUser.getId(), is(equalTo(id)));

		dataSetContext = DataSetContextBuilder.create()
				.add("ID", newUser.getId()).build();

	}
	
For fuller examples, be sure to check out the two sub modules <code>test-db-jdbc</code> and <code>test-db-jpa</code> for working examples that illustrate various idioms of the API and DBUnit. 

