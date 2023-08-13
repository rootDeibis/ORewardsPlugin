package me.rootdeibis.orewards.api.database;

import me.rootdeibis.orewards.api.rewards.Reward;
import me.rootdeibis.orewards.utils.DurationParser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.UUID;

public abstract class IDatabase {


    private final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS %s ( UUID TEXT, until varchar(40), server TEXT);";
    private final String SELECT_UNTIL = "SELECT * FROM %s WHERE UUID = '%s' LIMIT 1;";

    private final String CREATE_ROW = "INSERT INTO %s (UUID, until, server) VALUES ('%s', '%s', '%s');";

    private final String UPDATE_ROW = "UPDATE %s SET until = '%s' WHERE UUID = '%s';";
    private boolean tested = false;
    protected abstract Connection createConnection() throws Exception;


    private boolean execute(String query, Object... values) {
        boolean result = false;

        try {
            Connection conn = this.createConnection();
            Statement statement = conn.createStatement();


            statement.executeUpdate(String.format(query, values));

            result = true;


            statement.close();
            conn.close();

        }catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    public void checkTables(String... tables) {


            this.execute(
                    String.join("", Arrays.stream(tables).map(r -> String.format(CREATE_TABLE_SQL, r))
                            .toArray(String[]::new))
            );

    }


    public boolean create(Reward reward, UUID uuid) {

        return this.execute(CREATE_ROW,reward.getName(), uuid.toString(), DurationParser.addToDate("1s").getTime(), "none");
    }

    public boolean update(String rewardName, long timeUntil, UUID uuid) {
        return this.execute(UPDATE_ROW, rewardName, timeUntil, uuid.toString());
    }
    public long get(Reward reward, UUID uuid) {
        long until = 0;

        try {
            Connection connection = this.createConnection();
            PreparedStatement statement = connection.prepareStatement(String.format(SELECT_UNTIL, reward.getName(), uuid.toString()));

            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            if (resultSet.next()) {

                until = resultSet.getLong("until");
            }

            statement.close();
            connection.close();

        }catch (Exception e) {
            e.printStackTrace();
        }

        return until;
    }

    protected void tested(boolean b) {
        this.tested = b;
    }

    public boolean isTested() {
        return tested;
    }


    public abstract void disconnect();
}