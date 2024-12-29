/*
 * Copyright (c) 2024 by Miłosz Gilga <https://miloszgilga.pl>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     <http://www.apache.org/licenses/LICENSE-2.0>
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.miloszgilga.tvarchiver.webscrapper.db;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;

import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public class DataSource {
	private final HikariDataSource dataSource;
	@Getter
	private final InetSocketAddress dbHost;
	@Getter
	private final String dbName;

	public DataSource(String username, String password, String dbName) {
		this(new InetSocketAddress("localhost", 3306), username, password, dbName);
	}

	public DataSource(InetSocketAddress addr, String username, String password, String dbName) {
		this.dbName = dbName;
		dataSource = new HikariDataSource();
		dbHost = new InetSocketAddress(addr.getHostString(), addr.getPort());
		dataSource.setJdbcUrl(String.format("jdbc:mysql://%s:%d/%s", addr.getHostString(), addr.getPort(), dbName));
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setMaximumPoolSize(5);
	}

	public boolean isSuccessfullyConnected() {
		try (final Connection connection = dataSource.getConnection();
		     final Statement statement = connection.createStatement()) {
			statement.executeQuery("SELECT 1");
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	public Jdbi getJdbi() {
		return Jdbi.create(dataSource);
	}

	public void closeConnection() {
		dataSource.close();
		log.info("Connection with {} closed", dbHost);
	}
}
