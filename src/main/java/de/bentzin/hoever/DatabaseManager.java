package de.bentzin.hoever;

import de.bentzin.hoever.web.Item;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.*;

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
            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS files (url TEXT, event TEXT, name TEXT, topic TEXT, PRIMARY KEY (url, event))");
            connection.createStatement().execute(
                    """
                            CREATE TABLE IF NOT EXISTS history (
                                channel INTEGER,
                                url TEXT,
                                event TEXT,
                                messageID LONG,
                                PRIMARY KEY (channel, url, event)
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

    public void updateFileItems(@NotNull Map<String, Set<Item>> items) {
        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement("INSERT OR REPLACE INTO files (url, event, name, topic) VALUES (?, ?, ?, ?)");
            for (Map.Entry<String, Set<Item>> entries : items.entrySet()) {
                Set<Item> itemSet = entries.getValue();
                for (Item item : itemSet) {
                    statement.setString(1, item.getLink());
                    statement.setString(2, entries.getKey()); //event (Mathe1, Mathe2, etc)
                    statement.setString(3, item.getName());
                    statement.setString(4, item.getTopic());
                    try {
                        statement.execute();
                    } catch (SQLException e) {
                        logger.error("Failed to execute statement for item {}", item);
                        logger.error("SQLException: ", e);
                    }
                }


            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void reportMessage(long channel, @NotNull String url, @NotNull String event, long messageID) {
        try (Connection connection = connect()) {
            PreparedStatement statement = connection.prepareStatement("INSERT OR REPLACE INTO history (channel, url, event, messageID) VALUES (?, ?, ?, ?)");
            statement.setLong(1, channel);
            statement.setString(2, url);
            statement.setString(3, event);
            statement.setLong(4, messageID);
            statement.execute();
        } catch (SQLException e) {
            logger.error("Failed to update history for item {} from event {}. The Bot will restart", url, event);
            logger.error("SQLException: ", e);
            Bot.shutdown(Bot.RESTART_ERROR);
        }
    }

    @NotNull
    public List<URL> getEvents() {
        try (Connection connection = connect()) {

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT DISTINCT event_link FROM guilds");
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<URL> urls = new ArrayList<>();
            while (resultSet.next()) {
                urls.add(new URL(resultSet.getString("event_link")));
            }

            return urls;
        } catch (SQLException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public Set<SendOrder> getSendOrders() {
        try (Connection connection = connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT guilds.channel, guilds.event_link, files.url, files.event, files.name, files.topic FROM files, guilds\n" +
                    "WHERE guilds.event_link == files.event AND (guilds.channel, files.url, files.event) NOT IN (SELECT channel, url, event FROM history)");
            ResultSet resultSet = preparedStatement.executeQuery();
            Set<SendOrder> sendOrders = new HashSet<>();
            while (resultSet.next()) {
                sendOrders.add(new SendOrder(
                        resultSet.getString("url"),
                        resultSet.getString("name"),
                        resultSet.getString("topic"),
                        resultSet.getString("event"),
                        resultSet.getLong("channel")));
                logger.debug("Added send order for channel {} with event {} and url {}", resultSet.getLong("channel"), resultSet.getString("event"), resultSet.getString("url"));
            }
            return sendOrders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public Set<DBChannel> getChannels() {
        try (Connection connection = connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT guildid, channel, event_link FROM guilds");
            ResultSet resultSet = preparedStatement.executeQuery();
            Set<DBChannel> channels = new HashSet<>();
            while (resultSet.next()) {
                channels.add(new DBChannel(resultSet.getLong("guildid"), resultSet.getLong("channel"), resultSet.getString("event_link")));
            }
            return channels;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
