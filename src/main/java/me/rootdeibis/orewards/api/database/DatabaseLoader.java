package me.rootdeibis.orewards.api.database;

import me.rootdeibis.commonlib.database.MySQLDatabase;
import me.rootdeibis.commonlib.database.SQLiteDatabase;
import me.rootdeibis.orewards.ORewardsMain;
import me.rootdeibis.orewards.api.ORewardsCore;
import me.rootdeibis.orewards.api.configuration.RFile;
import me.rootdeibis.orewards.api.rewards.Reward;

import java.io.File;

public class DatabaseLoader {

    private IDatabase database;
    private ORewardsCore core;

    public DatabaseLoader() {


    }

    public void load() {
        this.core = ORewardsMain.getCore();

        boolean isMySQL = this.core.getFileManager().use("config.yml").getBoolean("Options.MysqlConnection.Enabled");
        this.database = isMySQL ? this.prepareDatabaseMySQL() : this.prepareDatabaseSQLite();



        if (this.database.isTested()) {
            this.database.checkTables("player_times");
            this.database.checkTables(this.core.getRewardManager().getRewards().stream().map(Reward::getName).toArray(String[]::new));
        }

    }


    private IDatabase prepareDatabaseMySQL() {
        RFile config = this.core.getFileManager().use("config.yml");

        String host = config.getString("Options.MysqlConnection.Data.hostname");
        String port = config.getString("Options.MysqlConnection.Data.port");
        String username = config.getString("Options.MysqlConnection.Data.user");
        String password = config.getString("Options.MysqlConnection.Data.password");
        String db_name = config.getString("Options.MysqlConnection.Data.databaseName");

        return new IDatabase(new MySQLDatabase("mysql", host, port, db_name, username, password));
    }

    private IDatabase prepareDatabaseSQLite() {
        RFile config = this.core.getFileManager().use("config.yml");
        String fileName = config.getString("Options.SQLConnection.FileName");

        File dbFile = new File(ORewardsMain.getMain().getDataFolder(), fileName);

        return new IDatabase(new SQLiteDatabase(dbFile));
    }

    public IDatabase getDatabase() {
        return database;
    }
}
