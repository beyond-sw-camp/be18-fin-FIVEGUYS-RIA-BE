package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSourceLogger {

  private final javax.sql.DataSource dataSource;

  @PostConstruct
  public void logDataSource() throws Exception {
    try (var conn = dataSource.getConnection();
         var stmt = conn.createStatement();
         var rs = stmt.executeQuery("select @@hostname, @@port, database()")) {

      if (rs.next()) {
        System.out.println("[DEBUG-DB] host=" + rs.getString(1)
            + ", port=" + rs.getString(2)
            + ", database=" + rs.getString(3));
      }
    }
  }
}
