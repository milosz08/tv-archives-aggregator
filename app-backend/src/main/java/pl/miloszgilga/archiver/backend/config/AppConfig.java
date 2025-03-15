package pl.miloszgilga.archiver.backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "app-properties")
public class AppConfig {
  private List<String> chartColors;
  private String defaultChartColor;
  private String databaseName;
}
