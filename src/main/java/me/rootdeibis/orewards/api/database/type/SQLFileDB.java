package me.rootdeibis.orewards.api.database.type;


import me.rootdeibis.orewards.ORewardsLogger;
import me.rootdeibis.orewards.api.database.IDatabase;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLFileDB extends IDatabase {
    private File db_file;


    private Connection connection;
    public SQLFileDB(File db_file) {
        this.db_file = db_file;

        if(!db_file.exists()) {
            try {
                this.db_file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        try {
            connection = createConnection();


            ORewardsLogger.send("&aThe connection to the sqlite database was successful, we recommend using mariadb.");

            this.tested(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public Connection createConnection() throws Exception {
        if(this.connection == null || this.connection.isClosed()) {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + this.db_file);
        }
        return connection;
    }

    @Override
    public void disconnect() {
        try {
            if(this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}