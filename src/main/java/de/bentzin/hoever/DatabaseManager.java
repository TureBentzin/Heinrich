package de.bentzin.hoever;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/*
 This database will store the following data:
 - Guilds (guildid, channel, event_link)
 - Files  (url, name, topic)
 - History (channel, url)
 */

/**
 * @author Ture Bentzin
 * @since 26-03-2024
 */
public class DatabaseManager {

    @NotNull
    public static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    @NotNull
    private final String sqlitePath;

    @NotNull
    private final Set<Connection> connections = new HashSet<>();

    protected DatabaseManager(@NotNull String sqlitePath) {
        this.sqlitePath = sqlitePath;
    }

    public synchronized void close() {
        connections.forEach(connection -> {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        connections.clear();
    }

    @NotNull
    public synchronized Connection connect() {
        try {
            if (Bot.debug) logger.debug("Connecting to database: {}", sqlitePath);
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + sqlitePath);
            connections.add(connection);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* Specialized methods for the database */

    public void createTables() {
        try (Connection connection = connect()) {
            logger.info("Creating tables in database...");
            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS guilds (guildid INTEGER, channel INTEGER PRIMARY KEY, event_link TEXT)");
            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS files (url TEXT PRIMARY KEY, name TEXT, topic TEXT)");
            connection.createStatement().execute(
                    """
                            CREATE TABLE IF NOT EXISTS history (
                                channel INTEGER,
                                url TEXT,
                                PRIMARY KEY (channel, url)
                            );""");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setChannel(long guildid, long channelid, @NotNull String eventLink) {
        //remove old channel and add new one
        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM guilds WHERE guildid = ? AND event_link = ?");
            statement.setLong(1, guildid);
            statement.setString(2, eventLink);
            statement.execute();
            logger.info("Deleted old channel for guild {} with event link {}", guildid, eventLink);
            statement = connection.prepareStatement("INSERT INTO guilds (guildid, channel, event_link) VALUES (?, ?, ?)");
            statement.setLong(1, guildid);
            statement.setLong(2, channelid);
            statement.setString(3, eventLink);
            statement.execute();
            logger.info("Set channel {} for guild {} with event link {}", channelid, guildid, eventLink);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
