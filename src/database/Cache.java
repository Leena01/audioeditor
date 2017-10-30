package database;
import database.entities.*;
import java.util.*;

/**
 * Cache for refreshing data
 */
public class Cache implements DatabaseDao {
    private DatabaseDao database;
    private Timer timer;
    private int interval;
    private List<Song> songs;

    /**
     * Constructor
     * @param database DatabaseDao class
     */
    public Cache(DatabaseDao database, int minutes) {
        this.database = database;
        refreshCache();
        setRefreshInterval(minutes);
    }

    @Override
    public boolean createTable() {
        return database.createTable();
    }

    @Override
    public List<Song> getSongs() {
        return songs;
    }

    @Override
    public boolean addSong(Song s) {
        boolean success = database.addSong(s);
        refreshCache();
        return success;
    }

    @Override
    public boolean deleteSong(int id) {
        boolean success = database.deleteSong(id);
        refreshCache();
        return success;
    }

    @Override
    public boolean editSong(Song s) {
        boolean success = database.editSong(s);
        refreshCache();
        return success;
    }

    @Override
    public boolean isConnected() {
        return database.isConnected();
    }

    @Override
    public void commit() {
        database.commit();
    }

    @Override
    public void rollback() {
        database.rollback();
    }

    @Override
    public void close() {
        database.close();
    }

    private void refreshCache() {
        this.songs = database.getSongs();
    }

    /**
     * Set refresh interval
     * Default: 5 mins
     * @param minutes to be set
     */
    private void setRefreshInterval(int minutes) {
        if (minutes <= 0)
            interval = 300000;
        else
            interval = minutes * 60000;
        startTimer();
    }

    /**
     * Start timer
     */
    private void startTimer() {
        if (timer != null)
            timer.cancel();
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {
                refreshCache();
            }
        }, interval, interval);
    }
}
