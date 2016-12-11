package com.spider.dao.datasource.unpooled;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;


//=======================================================
//		          .----.
//		       _.'__    `.
//		   .--(^)(^^)---/#\
//		 .' @          /###\
//		 :         ,   #####
//		  `-..__.-' _.-\###/
//		        `;_:    `"'
//		      .'"""""`.
//		     /,  ya ,\\
//		    //狗神保佑  \\
//		    `-._______.-'
//		    ___`. | .'___
//		   (______|______)
//=======================================================

/**
 * 提供了Connection链接的获取<br>
 * 实现了JBDC驱动的注册<br>
 * 实现了{@link DataSource}接口<br>
 * 
 * 
 * @author seven[hacker.kill07@gmail.com]
 *         <p>
 * @zhihu https://www.zhihu.com/people/Sweets07
 * @date 2016年10月9日-下午4:42:17
 */
public class UnpooledDataSource implements DataSource {

	private ClassLoader driverClassLoader;
	private Properties driverProperties;
	private boolean driverInitialized;
	private String driver;
	private String url;
	private String username;
	private String password;
	private boolean autoCommit;
	private Integer defaultTransactionIsolationLevel;

	static {
		DriverManager.getDrivers();
	}

	public UnpooledDataSource() {
	}

	public UnpooledDataSource(String driver, String url, String username, String password) {
		this.driver = driver;
		this.url = url;
		this.username = username;
		this.password = password;
	}
	
	public UnpooledDataSource(String driver, String url, Properties driverProperties) {
		this.driver = driver;
		this.url = url;
		this.driverProperties = driverProperties;
	}

	public UnpooledDataSource(ClassLoader driverClassLoader, String driver, String url, String username,
			String password) {
		this.driverClassLoader = driverClassLoader;
		this.driver = driver;
		this.url = url;
		this.username = username;
		this.password = password;
	}

	public UnpooledDataSource(ClassLoader driverClassLoader, String driver, String url, Properties driverProperties) {
		this.driverClassLoader = driverClassLoader;
		this.driver = driver;
		this.url = url;
		this.driverProperties = driverProperties;
	}

	public Connection getConnection() throws SQLException {
		return doGetConnection(username, password);
	}

	public Connection getConnection(String username, String password) throws SQLException {
		return doGetConnection(username, password);
	}

	public void setLoginTimeout(int loginTimeout) throws SQLException {
		DriverManager.setLoginTimeout(loginTimeout);
	}

	public int getLoginTimeout() throws SQLException {
		return DriverManager.getLoginTimeout();
	}

	public void setLogWriter(PrintWriter logWriter) throws SQLException {
		DriverManager.setLogWriter(logWriter);
	}

	public PrintWriter getLogWriter() throws SQLException {
		return DriverManager.getLogWriter();
	}

	public ClassLoader getDriverClassLoader() {
		return driverClassLoader;
	}

	public void setDriverClassLoader(ClassLoader driverClassLoader) {
		this.driverClassLoader = driverClassLoader;
	}

	public Properties getDriverProperties() {
		return driverProperties;
	}

	public void setDriverProperties(Properties driverProperties) {
		this.driverProperties = driverProperties;
	}

	public String getDriver() {
		return driver;
	}

	public synchronized void setDriver(String driver) {
		this.driver = driver;
		driverInitialized = false;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAutoCommit() {
		return autoCommit;
	}

	public void setAutoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
	}

	public Integer getDefaultTransactionIsolationLevel() {
		return defaultTransactionIsolationLevel;
	}

	public void setDefaultTransactionIsolationLevel(Integer defaultTransactionIsolationLevel) {
		this.defaultTransactionIsolationLevel = defaultTransactionIsolationLevel;
	}

	private Connection doGetConnection(String username, String password) throws SQLException {
		Properties props = new Properties(driverProperties);
		if (username != null) {
			props.setProperty("user", username);
		}
		if (password != null) {
			props.setProperty("password", password);
		}
		return doGetConnection(props);
	}

	private Connection doGetConnection(Properties properties) throws SQLException {
		initializeDriver();
		Connection connection = DriverManager.getConnection(url, properties);
		configureConnection(connection);
		return connection;
	}

	private synchronized void initializeDriver() throws SQLException {
		if (!driverInitialized) {
			driverInitialized = true;
			try {
				if (driverClassLoader != null) {
					Class.forName(driver, true, driverClassLoader);
				} else {
				//	Resources.classForName(driver);
				}
			} catch (Exception e) {
				throw new SQLException("Error setting driver on UnpooledDataSource. Cause: " + e);
			}
		}
	}

	private void configureConnection(Connection conn) throws SQLException {
		if (autoCommit != conn.getAutoCommit()) {
			conn.setAutoCommit(autoCommit);
		}
		if (defaultTransactionIsolationLevel != null) {
			conn.setTransactionIsolation(defaultTransactionIsolationLevel);
		}
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new SQLException(getClass().getName() + " is not a wrapper.");
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	public Logger getParentLogger() {
		return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME); 
	}

}
