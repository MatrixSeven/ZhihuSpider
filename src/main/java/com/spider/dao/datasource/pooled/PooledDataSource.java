package com.spider.dao.datasource.pooled;


import com.spider.dao.datasource.unpooled.UnpooledDataSource;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.*;
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
 * 包装连接池实体容器
 * @author seven[hacker.kill07@gmail.com]<p>
 * @zhihu https://www.zhihu.com/people/Sweets07
 * @date   2016年10月8日-下午2:16:00
 */
public class PooledDataSource implements DataSource {

	private final PoolState state = new PoolState(this);
	private final UnpooledDataSource dataSource;
	protected int poolMaximumActiveConnections = 10;
	protected int poolMaximumIdleConnections = 5;
	protected int poolMaximumCheckoutTime = 20000;
	protected int poolTimeToWait = 20000;
	protected String poolPingQuery = "NO PING QUERY SET";
	protected boolean poolPingEnabled = false;
	protected int poolPingConnectionsNotUsedFor = 0;

	private int expectedConnectionTypeCode;

	public PooledDataSource() {
		dataSource = new UnpooledDataSource();
	}

	public PooledDataSource(String driver, String url, String username, String password) {
		dataSource = new UnpooledDataSource(driver, url, username, password);
		expectedConnectionTypeCode = assembleConnectionTypeCode(dataSource.getUrl(), dataSource.getUsername(),
				dataSource.getPassword());
	}

	public PooledDataSource(String driver, String url, Properties driverProperties) {
		dataSource = new UnpooledDataSource(driver, url, driverProperties);
		expectedConnectionTypeCode = assembleConnectionTypeCode(dataSource.getUrl(), dataSource.getUsername(),
				dataSource.getPassword());
	}

	public PooledDataSource(ClassLoader driverClassLoader, String driver, String url, String username,
			String password) {
		dataSource = new UnpooledDataSource(driverClassLoader, driver, url, username, password);
		expectedConnectionTypeCode = assembleConnectionTypeCode(dataSource.getUrl(), dataSource.getUsername(),
				dataSource.getPassword());
	}

	public PooledDataSource(ClassLoader driverClassLoader, String driver, String url, Properties driverProperties) {
		dataSource = new UnpooledDataSource(driverClassLoader, driver, url, driverProperties);
		expectedConnectionTypeCode = assembleConnectionTypeCode(dataSource.getUrl(), dataSource.getUsername(),
				dataSource.getPassword());
	}

	public Connection getConnection() throws SQLException {
		return popConnection(dataSource.getUsername(), dataSource.getPassword()).getProxyConnection();
	}

	public Connection getConnection(String username, String password) throws SQLException {
		return popConnection(username, password).getProxyConnection();
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

	public void setDriver(String driver) {
		dataSource.setDriver(driver);
		forceCloseAll();
	}

	public void setUrl(String url) {
		dataSource.setUrl(url);
		forceCloseAll();
	}

	public void setUsername(String username) {
		dataSource.setUsername(username);
		forceCloseAll();
	}

	public void setPassword(String password) {
		dataSource.setPassword(password);
		forceCloseAll();
	}

	public void setDefaultAutoCommit(boolean defaultAutoCommit) {
		dataSource.setAutoCommit(defaultAutoCommit);
		forceCloseAll();
	}

	public void setDefaultTransactionIsolationLevel(Integer defaultTransactionIsolationLevel) {
		dataSource.setDefaultTransactionIsolationLevel(defaultTransactionIsolationLevel);
		forceCloseAll();
	}

	public void setDriverProperties(Properties driverProps) {
		dataSource.setDriverProperties(driverProps);
		forceCloseAll();
	}

	public void setPoolMaximumActiveConnections(int poolMaximumActiveConnections) {
		this.poolMaximumActiveConnections = poolMaximumActiveConnections;
		forceCloseAll();
	}
	public void setPoolMaximumIdleConnections(int poolMaximumIdleConnections) {
		this.poolMaximumIdleConnections = poolMaximumIdleConnections;
		forceCloseAll();
	}
	public void setPoolMaximumCheckoutTime(int poolMaximumCheckoutTime) {
		this.poolMaximumCheckoutTime = poolMaximumCheckoutTime;
		forceCloseAll();
	}
	public void setPoolTimeToWait(int poolTimeToWait) {
		this.poolTimeToWait = poolTimeToWait;
		forceCloseAll();
	}
	public void setPoolPingQuery(String poolPingQuery) {
		this.poolPingQuery = poolPingQuery;
		forceCloseAll();
	}
	public void setPoolPingEnabled(boolean poolPingEnabled) {
		this.poolPingEnabled = poolPingEnabled;
		forceCloseAll();
	}
	public void setPoolPingConnectionsNotUsedFor(int milliseconds) {
		this.poolPingConnectionsNotUsedFor = milliseconds;
		forceCloseAll();
	}

	public String getDriver() {
		return dataSource.getDriver();
	}

	public String getUrl() {
		return dataSource.getUrl();
	}

	public String getUsername() {
		return dataSource.getUsername();
	}

	public String getPassword() {
		return dataSource.getPassword();
	}

	public boolean isAutoCommit() {
		return dataSource.isAutoCommit();
	}

	public Integer getDefaultTransactionIsolationLevel() {
		return dataSource.getDefaultTransactionIsolationLevel();
	}

	public Properties getDriverProperties() {
		return dataSource.getDriverProperties();
	}

	public int getPoolMaximumActiveConnections() {
		return poolMaximumActiveConnections;
	}

	public int getPoolMaximumIdleConnections() {
		return poolMaximumIdleConnections;
	}

	public int getPoolMaximumCheckoutTime() {
		return poolMaximumCheckoutTime;
	}

	public int getPoolTimeToWait() {
		return poolTimeToWait;
	}

	public String getPoolPingQuery() {
		return poolPingQuery;
	}

	public boolean isPoolPingEnabled() {
		return poolPingEnabled;
	}

	public int getPoolPingConnectionsNotUsedFor() {
		return poolPingConnectionsNotUsedFor;
	}

	public void forceCloseAll() {
		synchronized (state) {
			expectedConnectionTypeCode = assembleConnectionTypeCode(dataSource.getUrl(), dataSource.getUsername(),
					dataSource.getPassword());
			for (int i = state.activeConnections.size(); i > 0; i--) {
				try {
					PooledConnection conn = state.activeConnections.remove(i - 1);
					conn.invalidate();

					Connection realConn = conn.getRealConnection();
					if (!realConn.getAutoCommit()) {
						realConn.rollback();
					}
					realConn.close();
				} catch (Exception e) {
					//
				}
			}
			for (int i = state.idleConnections.size(); i > 0; i--) {
				try {
					PooledConnection conn = state.idleConnections.remove(i - 1);
					conn.invalidate();

					Connection realConn = conn.getRealConnection();
					if (!realConn.getAutoCommit()) {
						realConn.rollback();
					}
					realConn.close();
				} catch (Exception e) {
					//
				}
			}
		}
	}

	public PoolState getPoolState() {
		return state;
	}

	private int assembleConnectionTypeCode(String url, String username, String password) {
		return ("" + url + username + password).hashCode();
	}

	/**
	 * 归还连接
	 * @author Seven
	 * @param conn
	 * @throws SQLException 
	 * @return void
	 * @time 2016年10月10日-下午2:11:08
	 */
	protected void pushConnection(PooledConnection conn) throws SQLException {
		synchronized (state) {
			state.activeConnections.remove(conn);
			if (conn.isValid()) {
				if (state.idleConnections.size() < poolMaximumIdleConnections
						&& conn.getConnectionTypeCode() == expectedConnectionTypeCode) {
					state.accumulatedCheckoutTime += conn.getCheckoutTime();
					if (!conn.getRealConnection().getAutoCommit()) {
						conn.getRealConnection().rollback();
					}
					PooledConnection newConn = new PooledConnection(conn.getRealConnection(), this);
					state.idleConnections.add(newConn);
					newConn.setCreatedTimestamp(conn.getCreatedTimestamp());
					newConn.setLastUsedTimestamp(conn.getLastUsedTimestamp());
					conn.invalidate();
					state.notifyAll();
				} else {
					state.accumulatedCheckoutTime += conn.getCheckoutTime();
					if (!conn.getRealConnection().getAutoCommit()) {
						conn.getRealConnection().rollback();
					}
					conn.getRealConnection().close();
					conn.invalidate();
				}
			} else {
				state.badConnectionCount++;
			}
		}
	}

	private PooledConnection popConnection(String username, String password) throws SQLException {
		boolean countedWait = false;
		PooledConnection conn = null;
		long t = System.currentTimeMillis();
		int localBadConnectionCount = 0;

		while (conn == null) {
			synchronized (state) {
				//取出可用连接
				if (state.idleConnections.size() > 0) {
					conn = state.idleConnections.remove(0);
				} else {
					//获取连接
					if (state.activeConnections.size() < poolMaximumActiveConnections) {
						conn = new PooledConnection(dataSource.getConnection(), this);
						@SuppressWarnings("unused")
						Connection realConn = conn.getRealConnection();
					} else {
						//强制回收
						PooledConnection oldestActiveConnection = state.activeConnections.get(0);
						long longestCheckoutTime = oldestActiveConnection.getCheckoutTime();
						if (longestCheckoutTime > poolMaximumCheckoutTime) {
							state.claimedOverdueConnectionCount++;
							state.accumulatedCheckoutTimeOfOverdueConnections += longestCheckoutTime;
							state.accumulatedCheckoutTime += longestCheckoutTime;
							state.activeConnections.remove(oldestActiveConnection);
							if (!oldestActiveConnection.getRealConnection().getAutoCommit()) {
								oldestActiveConnection.getRealConnection().rollback();
							}
							conn = new PooledConnection(oldestActiveConnection.getRealConnection(), this);
							oldestActiveConnection.invalidate();
						} else {
							//等待
							try {
								if (!countedWait) {
									state.hadToWaitCount++;
									countedWait = true;
								}
								long wt = System.currentTimeMillis();
								state.wait(poolTimeToWait);
								state.accumulatedWaitTime += System.currentTimeMillis() - wt;
							} catch (InterruptedException e) {
								break;
							}
						}
					}
				}
				if (conn != null) {
					//强行杀死回滚
					if (conn.isValid()) {
						if (!conn.getRealConnection().getAutoCommit()) {
							conn.getRealConnection().rollback();
						}
						conn.setConnectionTypeCode(assembleConnectionTypeCode(dataSource.getUrl(), username, password));
						conn.setCheckoutTimestamp(System.currentTimeMillis());
						conn.setLastUsedTimestamp(System.currentTimeMillis());
						state.activeConnections.add(conn);
						state.requestCount++;
						state.accumulatedRequestTime += System.currentTimeMillis() - t;
					} else {
						state.badConnectionCount++;
						localBadConnectionCount++;
						conn = null;
						if (localBadConnectionCount > (poolMaximumIdleConnections + 3)) {
							throw new SQLException(
									"PooledDataSource: Could not get a good connection to the database.");
						}
					}
				}
			}

		}

		if (conn == null) {
			throw new SQLException(
					"PooledDataSource: Unknown severe error condition.  The connection pool returned a null connection.");
		}

		return conn;
	}
	protected boolean pingConnection(PooledConnection conn) {
		boolean result = true;

		try {
			result = !conn.getRealConnection().isClosed();
		} catch (SQLException e) {
			result = false;
		}

		if (result) {
			if (poolPingEnabled) {
				if (poolPingConnectionsNotUsedFor >= 0
						&& conn.getTimeElapsedSinceLastUse() > poolPingConnectionsNotUsedFor) {
					try {
						Connection realConn = conn.getRealConnection();
						Statement statement = realConn.createStatement();
						ResultSet rs = statement.executeQuery(poolPingQuery);
						rs.close();
						statement.close();
						if (!realConn.getAutoCommit()) {
							realConn.rollback();
						}
						result = true;
					} catch (Exception e) {
						try {
							conn.getRealConnection().close();
						} catch (Exception e2) {
						}
						result = false;
					}
				}
			}
		}
		return result;
	}

	public static Connection unwrapConnection(Connection conn) {
		if (Proxy.isProxyClass(conn.getClass())) {
			InvocationHandler handler = Proxy.getInvocationHandler(conn);
			if (handler instanceof PooledConnection) {
				return ((PooledConnection) handler).getRealConnection();
			}
		}
		return conn;
	}

	protected void finalize() throws Throwable {
		forceCloseAll();
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
