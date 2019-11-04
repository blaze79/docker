package org.silentpom.docker;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testng.annotations.*;

import javax.sql.DataSource;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.*;

/**
 * Created by Vlad on 04.11.2019.
 */
public class MainTest {

    static Logger LOGGER = LoggerFactory.getLogger(MainTest.class);

    private final Set<HikariDataSource> datasourcesForCleanup = new HashSet<>();

    private PostgreSQLContainer postgres;

    @BeforeClass
    public void setUp() throws Exception {
        postgres = new PostgreSQLContainer<>("postgres:9.6.8")
                .withDatabaseName("silentdb");

        postgres.start();
    }

    @AfterClass
    public void tearDown() throws Exception {
        if (postgres != null) {
            postgres.close();
        }
    }

    DataSource getDataSource(JdbcDatabaseContainer container) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(container.getJdbcUrl());
        hikariConfig.setUsername(container.getUsername());
        hikariConfig.setPassword(container.getPassword());
        hikariConfig.setDriverClassName(container.getDriverClassName());

        final HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        datasourcesForCleanup.add(dataSource);

        return dataSource;
    }

    @AfterTest
    public void cleanTest() {
        datasourcesForCleanup.forEach(HikariDataSource::close);
        datasourcesForCleanup.clear();
    }


    //@Test
    void testServerRun() {
        final DataSource dataSource = getDataSource(postgres);
        assertNotNull(dataSource);
    }

    @Test
    void testMigrate() {
        final DataSource dataSource = getDataSource(postgres);
        assertNotNull(dataSource);

        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.migrate();
    }

    private final String SELECT_ALL_SQL = "SELECT article_id, article_name, article_desc FROM article";

    @Test(dependsOnMethods = {"testMigrate"})
    void testSelect() throws SQLException {
        final DataSource dataSource = getDataSource(postgres);

        try(final Connection connection = dataSource.getConnection()) {

            try(final PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL)) {
               try(final ResultSet resultSet = statement.executeQuery()) {
                   while (resultSet.next()) {
                        LOGGER.info("Article id: {}, name: {}, and title: {}",
                                resultSet.getInt("article_id"),
                                resultSet.getString("article_name"),
                                resultSet.getString("article_desc")
                        );
                   }
               }
            }
        }
    }

    private final String CREATE_TABLE = "CREATE TABLE article (\n" +
            "    article_id bigserial primary key,\n" +
            "    article_name varchar(20) NOT NULL,\n" +
            "    article_desc text NOT NULL,\n" +
            "    date_added timestamp default NULL\n" +
            ")";

    private final String INSERT_SQL = "INSERT INTO article (\n" +
            "    article_name ,\n" +
            "    article_desc ,\n" +
            "    date_added\n" +
            ") VALUES (\n" +
            " 'art1', \n" +
            " 'first article', \n" +
            " current_timestamp)";

    //@Test
    void testCreateTable() throws SQLException {
        final DataSource dataSource = getDataSource(postgres);
        assertNotNull(dataSource);

        try(final Connection connection = dataSource.getConnection()) {
            try (final Statement statement = connection.createStatement()) {
                statement.execute(CREATE_TABLE);
            }

            try(final PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {
                statement.execute();
            }
        }
    }


}