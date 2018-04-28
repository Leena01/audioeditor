package org.ql.audioeditor.database;

import org.ql.audioeditor.database.entities.Song;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
     *
     * @param database DatabaseDao class
     */
    public Cache(DatabaseDao database, int millis) {
        this.database = database;
        refreshCache();
        setRefreshInterval(millis);
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
    public boolean deleteSong(Song s) {
        boolean success = database.deleteSong(s);
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
     * Set refresh interval Default: 5 mins
     *
     * @param millis to be set
     */
    private void setRefreshInterval(int millis) {
        interval = millis;
        startTimer();
    }

    /**
     * Start timer
     */
    private void startTimer() {
        if (timer != null)
            timer.cancel();
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                refreshCache();
            }
        }, interval, interval);
    }
}