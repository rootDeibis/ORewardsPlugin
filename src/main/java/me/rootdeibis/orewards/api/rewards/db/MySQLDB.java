package me.rootdeibis.orewards.api.rewards.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.rootdeibis.orewards.ORewardsLogger;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLDB extends IDatabase  {


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

}
