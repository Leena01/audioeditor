package org.ql.audioeditor.database;

import org.ql.audioeditor.database.entities.Song;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/**
 * Database implementation.
 */
public final class DatabaseDaoImpl implements DatabaseDao {
    private final Statement stat;
    private Connection connection;
    private ResultSet rs;

    /**
     * Constructor.
     *
     * @param driver Driver
     * @param url    URL
     */
    public DatabaseDaoImpl(String driver, String url)
        throws SQLException, ClassNotFoundException {
        connection = null;
        Class.forName(driver);
        connection = DriverManager.getConnection(url);
        connection.setAutoCommit(false);
        stat = connection.createStatement();
    }

    @Override
    public boolean createTable() {
        try {
            String query =
                "create table if not exists fav(song_id INTEGER PRIMARY KEY, "
                    + "song_title VARCHAR(255) NOT NULL, "
                    + "song_track VARCHAR(255) NOT NULL, "
                    + "song_artist VARCHAR(255) NOT NULL, "
                    + "song_album VARCHAR(255) NOT NULL, "
                    + "song_year VARCHAR(255) NOT NULL, "
                    + "song_genre VARCHAR(255) NOT NULL, "
                    + "song_comment VARCHAR(255) NOT NULL, "
                    + "song_path VARCHAR(255) NOT NULL UNIQUE);";
            stat.executeUpdate(query);
            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {
            }
            return false;
        }
    }

    @Override
    public List<Song> getSongs() {
        try {
            List<Song> result = new LinkedList<>();
            rs = stat.executeQuery("select * from fav;");
            while (rs.next()) {
                result.add(new Song(
                    rs.getInt("song_id"),
                    rs.getString("song_title"),
                    rs.getString("song_track"),
                    rs.getString("song_artist"),
                    rs.getString("song_album"),
                    rs.getString("song_year"),
                    rs.getString("song_genre"),
                    rs.getString("song_comment"),
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
            String query =
                "insert into fav(song_title, song_track, song_artist, "
                    + "song_album, song_year, song_genre, song_comment, "
                    + "song_path) values (\""
                    + s.getTitle() + "\", \""
                    + s.getTrack() + "\", \""
                    + s.getArtist() + "\", \""
                    + s.getAlbum() + "\", \""
                    + s.getYear() + "\", \""
                    + s.getGenre() + "\", \""
                    + s.getComment() + "\", \""
                    + s.getPath() + "\");";
            stat.executeUpdate(query);
            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {
            }
            return false;
        }
    }

    @Override
    public boolean deleteSong(Song s) {
        try {
            String query =
                "delete from fav where song_id = \"" + s.getId() + "\";";
            stat.executeUpdate(query);
            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {
            }
            return false;
        }
    }

    @Override
    public boolean editSong(Song s) {
        try {
            String query = "update fav set song_title = \"" + s.getTitle()
                + "\", song_track = \"" + s.getTrack()
                + "\", song_artist = \"" + s.getArtist()
                + "\", song_album = \"" + s.getAlbum()
                + "\", song_year = \"" + s.getYear()
                + "\", song_genre = \"" + s.getGenre()
                + "\", song_comment = \"" + s.getComment()
                + "\", song_path = \"" + s.getPath()
                + "\" where `id` = \"" + s.getId() + "\";";
            stat.executeUpdate(query);
            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {
            }
            return false;
        }
    }

    @Override
    public void commit() {
        try {
            connection.commit();
        } catch (SQLException ignored) {
        }
    }

    @Override
    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException ignored) {
        }
    }

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
        } catch (Exception ignored) {
        }
    }
}
