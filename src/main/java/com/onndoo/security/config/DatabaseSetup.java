package com.onndoo.security.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

/*@DataSourceDefinition(
	name = "java:global/MyDS",
	className = "org.h2.jdbcx.JdbcDataSource",
	url="jdbc:h2:~/SoteriaTestDB;DB_CLOSE_ON_EXIT=FALSE"
)*/

//@Singleton
//@Startup
@ApplicationScoped
public class DatabaseSetup {

	@Resource(lookup="java:comp/env/jdbc/H2DB")
    private DataSource dataSource;

    @Inject
    private Pbkdf2PasswordHash passwordHash;

    @PostConstruct
    public void init() {
    	
        Map<String, String> parameters= new HashMap<>();
        parameters.put("Pbkdf2PasswordHash.Iterations", "3072");
        parameters.put("Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA512");
        parameters.put("Pbkdf2PasswordHash.SaltSizeBytes", "64");
        passwordHash.initialize(parameters);

        executeUpdate(dataSource, "DROP TABLE IF EXISTS caller");
        executeUpdate(dataSource, "DROP TABLE IF EXISTS caller_groups");

        executeUpdate(dataSource, "CREATE TABLE IF NOT EXISTS caller(name VARCHAR(64) PRIMARY KEY, password VARCHAR(255))");
        executeUpdate(dataSource, "CREATE TABLE IF NOT EXISTS caller_groups(caller_name VARCHAR(64), group_name VARCHAR(64))");

        executeUpdate(dataSource, "INSERT INTO caller VALUES('reza', '" + passwordHash.generate("secret1".toCharArray()) + "')");
        executeUpdate(dataSource, "INSERT INTO caller VALUES('alex', '" + passwordHash.generate("secret2".toCharArray()) + "')");
        executeUpdate(dataSource, "INSERT INTO caller VALUES('arjan', '" + passwordHash.generate("secret2".toCharArray()) + "')");
        executeUpdate(dataSource, "INSERT INTO caller VALUES('werner', '" + passwordHash.generate("secret2".toCharArray()) + "')");

        executeUpdate(dataSource, "INSERT INTO caller_groups VALUES('reza', 'foo')");
        executeUpdate(dataSource, "INSERT INTO caller_groups VALUES('reza', 'bar')");

        executeUpdate(dataSource, "INSERT INTO caller_groups VALUES('alex', 'foo')");
        executeUpdate(dataSource, "INSERT INTO caller_groups VALUES('alex', 'bar')");

        executeUpdate(dataSource, "INSERT INTO caller_groups VALUES('arjan', 'foo')");
        executeUpdate(dataSource, "INSERT INTO caller_groups VALUES('werner', 'foo')");
    }

    @PreDestroy
    public void destroy() {
    	try {
    		executeUpdate(dataSource, "DROP TABLE IF EXISTS caller");
    		executeUpdate(dataSource, "DROP TABLE IF EXISTS caller_groups");
    	} catch (Exception e) {
    		// silently ignore, concerns in-memory database
    	}
    }

    private void executeUpdate(DataSource dataSource, String query) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
           throw new IllegalStateException(e);
        }
    }
}
