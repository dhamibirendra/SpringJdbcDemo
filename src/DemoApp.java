import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Origina codes taken from the following original sources
 * 
 * <br/><a href = "http://www.java2s.com/Code/Java/Spring/UseParameterizedRowMapper.htm">http://www.java2s.com/Code/Java/Spring/UseParameterizedRowMapper.htm</a>
 * 
 * <br/><a href = "http://www.java2s.com/Code/Java/Spring/implementsRowMapperToMapRowToEntity.htm">Java2S - example Spring RowMapperToMapRowToEntity </a> and
 * 
 * <br/><a href = "http://www.hsqldb.org/doc/guide/apb.html">http://www.hsqldb.org/doc/guide/apb.html </a>
 * 
 * @author bchoomnuan
 *
 */

class DemoApp {
	
	private ApplicationContext ctx;

	private org.apache.commons.dbcp.BasicDataSource dataSource;

	private Connection connection;
	
	public ApplicationContext getCtx() {
		return ctx;
	}

	public org.apache.commons.dbcp.BasicDataSource getDataSource() {
		return dataSource;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public DemoApp() {	
		
		ctx = new ClassPathXmlApplicationContext("context.xml");
		dataSource = (org.apache.commons.dbcp.BasicDataSource) ctx.getBean("dataSource");

		try {
			connection = dataSource.getConnection();
		} catch (SQLException e) {
			//TODO: handle me properly..
			e.printStackTrace();
		}
	}
	
	private void populateSample(Connection connection) throws SQLException{
        Statement st = connection.createStatement(); 

        String expression = "INSERT INTO employee (" +
	        "id, " +
	    	"name_first, " +
	    	"name_middle, " +
	    	"name_last, " +
	    	"address_line1, " +
	    	"address_line2, " +
	    	"address_city, " +
	    	"address_state, " +
	    	"address_zip, " +
	        "age) VALUES (" + 
        "100," +
    	"'Joel', " +
    	"'K', " +
    	"'Graham', " +
    	"'28 Roydon St', " +
    	"'Apt 11', " +
    	"'Hampton East', " +
    	"'Victoria', " +
    	"'3088', " +
        "35)";
        
        int i = st.executeUpdate(expression);    // run the query

        if (i == -1) {
            System.out.println("db error : " + expression);
        }

        if (st != null) {
			st.close();
        }
    	
	}

	private void createTable(Connection connection) throws SQLException {
        Statement st = connection.createStatement();    // statements

        String expression = "CREATE TABLE employee ( " +
	        "id INTEGER IDENTITY, " +
	    	"name_first VARCHAR(256), " +
	    	"name_middle VARCHAR(256), " +
	    	"name_last VARCHAR(256), " +
	    	"address_line1 VARCHAR(256), " +
	    	"address_line2 VARCHAR(256), " +
	    	"address_city VARCHAR(256), " +
	    	"address_state VARCHAR(256), " +
	    	"address_zip VARCHAR(256), " +
	        "age INTEGER)";
        
        int i = st.executeUpdate(expression);    // run the query

        if (i == -1) {
            System.out.println("db error : " + expression);
        }

        if (st != null) {
			st.close();
        }
	}
	
	public static void main(String args[]) throws Exception {

		DemoApp app = new DemoApp();
		
		app.createTable(app.getConnection());
		app.populateSample(app.getConnection());

		// lookup the spring bean
		EmployeeDaoImpl ws = (EmployeeDaoImpl) app.getCtx().getBean("employeeDao");
		Employee employee = ws.load(100);

		// print the result..
		System.out.println(employee.toString());
	}	
}