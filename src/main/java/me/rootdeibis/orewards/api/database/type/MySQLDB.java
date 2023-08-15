package me.rootdeibis.orewards.api.database.type;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.rootdeibis.orewards.ORewardsLogger;
import me.rootdeibis.orewards.ORewardsMain;
import me.rootdeibis.orewards.api.database.IDatabase;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLDB extends IDatabase {


        private String db_name;
        private String user;
        private String password;
        private String host;
        private String port;
        private HikariDataSource ds;

        public MySQLDB(String db_name, String user, String password, String host, String port) {
            this.db_name = db_name;
            this.user = user;
            this.password = password;
            this.host = host;
            this.port = port;



            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(String.format("jdbc:mysql://%s:%s/%s", host, port, db_name));
            config.setUsername(user);
            config.setPassword(password);
            config.setPoolName("HikariMysqlPool");
            config.setMaximumPoolSize(10);

            config.setThreadFactory(
                    new ThreadFactoryBuilder()
                            .setNameFormat(ORewardsMain.getMain().getDescription().getName() + " Pool Thread #%1$d")
                            // Hikari create daemons by default. We could use daemon threads for our own scheduler too
                            // because we safely shut down
                            .setDaemon(true)
                            .build());

            config.addDataSourceProperty("cachePrepStmts", true);
            // default prepStmtCacheSize 25 - amount of cached statements
            config.addDataSourceProperty("prepStmtCacheSize", 250);
            // default prepStmtCacheSqlLimit 256 - length of SQL
            config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
            // default false - available in newer versions caches the statements server-side
            config.addDataSourceProperty("useServerPrepStmts", true);
            // default false - prefer use of local values for autocommit and
            // transaction isolation (alwaysSendSetIsolation) should only be enabled if we always use the set* methods
            // instead of raw SQL
            // https://forums.mysql.com/read.php?39,626495,626512
            config.addDataSourceProperty("useLocalSessionState", true);
            // rewrite batched statements to a single statement, adding them behind each other
            // only useful for addBatch statements and inserts
            config.addDataSourceProperty("rewriteBatchedStatements", true);
            // cache result metadata
            config.addDataSourceProperty("cacheResultSetMetadata", true);
            // cache results of show variables and collation per URL
            config.addDataSourceProperty("cacheServerConfiguration", true);
            // default false - set auto commit only if not matching
            config.addDataSourceProperty("elideSetAutoCommits", true);



            ds = new HikariDataSource(config);

            try {
                Connection connection = ds.getConnection();

                connection.close();

                ORewardsLogger.send("&aYou have successfully connected to the database.");
                this.tested(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }




    @Override
    public Connection createConnection() throws Exception {
        return ds.getConnection();
    }

    @Override
    public void disconnect() {
       if(this.ds != null && !this.ds.isClosed()) {
           this.ds.close();
       }
    }

}
