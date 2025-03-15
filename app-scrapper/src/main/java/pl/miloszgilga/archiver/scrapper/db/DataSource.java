package pl.miloszgilga.archiver.scrapper.db;

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

  public DataSource(InetSocketAddress addr, String username, String password, String dbName) {
    this.dbName = dbName;
    dataSource = new HikariDataSource();
    dbHost = new InetSocketAddress(addr.getHostString(), addr.getPort());
    dataSource.setJdbcUrl(
      String.format("jdbc:mysql://%s:%d/%s", addr.getHostString(), addr.getPort(), dbName)
    );
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
