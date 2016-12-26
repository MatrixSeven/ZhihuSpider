package com.spider.dao.datasource.pooled;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

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
 * PooledConnection  
 * Hook代理 Close方法
 * @author seven[hacker.kill07@gmail.com]
 *         <p>
 * @zhihu https://www.zhihu.com/people/Sweets07
 * @date 2016年10月8日-上午9:10:37
 */
class PooledConnection implements InvocationHandler {

	private static final String CLOSE = "close";
	private static final Class<?>[] IFACES = new Class<?>[] { Connection.class };
	private int hashCode = 0;
	private PooledDataSource dataSource;
	private Connection realConnection;
	private Connection proxyConnection;
	private long checkoutTimestamp;
	private long createdTimestamp;
	private long lastUsedTimestamp;
	private int connectionTypeCode;
	private boolean valid;

	public PooledConnection(Connection connection, PooledDataSource dataSource) {
		this.hashCode = connection.hashCode();
		this.realConnection = connection;
		this.dataSource = dataSource;
		this.createdTimestamp = System.currentTimeMillis();
		this.lastUsedTimestamp = System.currentTimeMillis();
		this.valid = true;
		this.proxyConnection = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), IFACES, this);
	}

	public void invalidate() {
		valid = false;
	}

	public boolean isValid() {
		return valid && realConnection != null && dataSource.pingConnection(this);
	}

	public Connection getRealConnection() {
		return realConnection;
	}

	public Connection getProxyConnection() {
		return proxyConnection;
	}

	public int getRealHashCode() {
		if (realConnection == null) {
			return 0;
		} else {
			return realConnection.hashCode();
		}
	}

	public int getConnectionTypeCode() {
		return connectionTypeCode;
	}

	public void setConnectionTypeCode(int connectionTypeCode) {
		this.connectionTypeCode = connectionTypeCode;
	}

	public long getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(long createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public long getLastUsedTimestamp() {
		return lastUsedTimestamp;
	}

	public void setLastUsedTimestamp(long lastUsedTimestamp) {
		this.lastUsedTimestamp = lastUsedTimestamp;
	}

	public long getTimeElapsedSinceLastUse() {
		return System.currentTimeMillis() - lastUsedTimestamp;
	}

	public long getAge() {
		return System.currentTimeMillis() - createdTimestamp;
	}

	public long getCheckoutTimestamp() {
		return checkoutTimestamp;
	}

	public void setCheckoutTimestamp(long timestamp) {
		this.checkoutTimestamp = timestamp;
	}

	public long getCheckoutTime() {
		return System.currentTimeMillis() - checkoutTimestamp;
	}

	public int hashCode() {
		return hashCode;
	}

	public boolean equals(Object obj) {
		if (obj instanceof PooledConnection) {
			return realConnection.hashCode() == (((PooledConnection) obj).realConnection.hashCode());
		} else if (obj instanceof Connection) {
			return hashCode== obj.hashCode();
		} else {
			return false;
		}
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
		String methodName = method.getName();
		Object obj = null;
		if (CLOSE.hashCode() == methodName.hashCode() && CLOSE.equals(methodName)) {
			dataSource.pushConnection(this);
			return null;
		} else {
			try {
				if (method.getDeclaringClass() != Object.class) {
					checkConnection();
				}
				obj = method.invoke(realConnection, args);
			} catch (Exception t) {
				t.printStackTrace();
			}
		}
		return obj;
	}

	private void checkConnection() throws SQLException {
		if (!valid) {
			throw new SQLException("Error accessing PooledConnection. Connection is invalid.");
		}
	}

}
