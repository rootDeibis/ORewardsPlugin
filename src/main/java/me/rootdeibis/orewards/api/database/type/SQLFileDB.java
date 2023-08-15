package me.rootdeibis.orewards.api.database.type;


import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.rootdeibis.orewards.ORewardsLogger;
import me.rootdeibis.orewards.ORewardsMain;
import me.rootdeibis.orewards.api.database.IDatabase;
import org.sqlite.JDBC;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;

public class SQLFileDB extends IDatabase {
    private File db_file;


    private HikariDataSource ds;
    public SQLFileDB(File db_file) {
        this.db_file = db_file;

        if(!db_file.exists()) {
            try {
                this.db_file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

            HikariConfig config = new HikariConfig();

            config.setDataSourceClassName("org.sqlite.SQLiteDataSource");


            config.setMaximumPoolSize(1);

            config.setThreadFactory(
             new ThreadFactoryBuilder()
                    .setNameFormat(ORewardsMain.getMain().getDescription().getName() + " Pool Thread #%1$d")
                    // Hikari create daemons by default. We could use daemon threads for our own scheduler too
                    // because we safely shut down
                    .setDaemon(true)
                    .build());

            config.addDataSourceProperty("url", JDBC.PREFIX + db_file);
            config.setConnectionTestQuery("SELECT 1");

            ORewardsLogger.send("&aThe connection to the sqlite database was successful, we recommend using mariadb.");


            ds = new HikariDataSource(config);

            this.tested(true);



    }

    @Override
    public Connection createConnection() throws Exception {
       return ds.getConnection();
    }

    @Override
    public void disconnect() {
        ds.close();
    }


}
