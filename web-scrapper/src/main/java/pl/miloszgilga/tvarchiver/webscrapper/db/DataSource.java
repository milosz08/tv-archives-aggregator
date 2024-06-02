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
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public class DataSource {
	private final HikariDataSource dataSource;
	private final InetSocketAddress dbHost;

	public DataSource(String username, String password, String dbName) {
		this("localhost", 3306, username, password, dbName);
	}

	public DataSource(String host, int port, String username, String password, String dbName) {
		dataSource = new HikariDataSource();
		dbHost = new InetSocketAddress(host, port);
		dataSource.setJdbcUrl(String.format("jdbc:mysql://%s:%d/%s", host, port, dbName));
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setMaximumPoolSize(5);
	}

	public boolean isSuccessfullyConnected() {
		try (final Connection connection = dataSource.getConnection();
			 final Statement statement = connection.createStatement()) {
			statement.executeQuery("select 1");
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	public JdbcTemplate getJdbcTemplate() {
		return new JdbcTemplate(dataSource);
	}

	public void closeConnection() {
		dataSource.close();
		log.info("Connection with {} closed", dbHost);
	}
}
