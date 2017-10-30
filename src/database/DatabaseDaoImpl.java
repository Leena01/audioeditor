package database;

import database.entities.*;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DatabaseDaoImpl implements DatabaseDao {
    private Connection connection;
    private Statement stat;
    private ResultSet rs;
    private boolean connected;

    /**
     * Constructor
     * @param driver Drivers
     * @param url URL
     */
    public DatabaseDaoImpl(String driver, String url) {
        connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url);
            connection.setAutoCommit(false);
            stat = connection.createStatement();
            connected = true;
        } catch (ClassNotFoundException | SQLException e) {
            connected = false;
        }
    }

    @Override
    public boolean createTable() {
        try {
            String query = "create table if not exists fav(song_id INTEGER PRIMARY KEY, " +
                    "song_name VARCHAR(255) NOT NULL, " +
                    "song_artist VARCHAR(255) NOT NULL, " +
                    "song_path VARCHAR(255) NOT NULL UNIQUE);";
            stat.executeUpdate(query);
            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {}
            return false;
        }
    }

    @Override
    public List<Song> getSongs() {
        try {
            List<Song> result = new LinkedList<>();
            rs = stat.executeQuery("select * from fav;");
            while(rs.next())
            {
                result.add(new Song(
                        rs.getInt("song_id"),
                        rs.getString("song_name"),
                        rs.getString("song_artist"),
                        rs.getString("song_path")));
            }
            return result;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public boolean addSong(Song s) {
        try {
            String query = "insert into fav(song_name, song_artist, song_path) values (\"" +
                    s.getName() + "\", \"" +
                    s.getArtist() + "\", \"" +
                    s.getPath() + "\");";
            stat.executeUpdate(query);
            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {}
            return false;
        }
    }

    @Override
    public boolean deleteSong(int id) {
        try {
            String query = "delete from fav where song_id = \"" + id + "\";";
            stat.executeUpdate(query);
            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {}
            return false;
        }
    }

    @Override
    public boolean editSong(Song s) {
        try {
            String query = "update fav set song_name = \"" + s.getName() +
                    "\", song_artist = \"" + s.getArtist() +
                    "\", song_path = \"" + s.getPath() +
                    "\" where `id` = \"" + s.getId() + "\";";
            stat.executeUpdate(query);
            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {}
            return false;
        }
    }

    /**
     * Is connected
     * @return true if connected
     */
    @Override
    public boolean isConnected() {
        return connected;
    }

    /**
     * Commit changes
     */
    @Override
    public void commit() {
        try {
            connection.commit();
        } catch (SQLException ignored) {}
    }

    /**
     * Rollback changes
     */
    @Override
    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException ignored) {}
    }

    /**
     * Close connection
     */
    @Override
    public void close() {
        try {
            if (rs != null) {
                rs.close();
            }

            if (stat != null) {
                stat.close();
            }

            if (connection != null) {
                connection.close();
            }
        } catch (Exception ignored) {}
    }
}
