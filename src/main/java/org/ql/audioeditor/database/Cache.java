package org.ql.audioeditor.database;

import org.ql.audioeditor.database.entities.Song;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Cache for refreshing data.
 */
public final class Cache implements DatabaseDao {
    private final DatabaseDao database;
    private Timer timer;
    private int interval;
    private List<Song> songs;

    /**
     * Constructor.
     *
     * @param database DatabaseDao class
     * @param millis   Refresh interval (milliseconds)
     */
    public Cache(DatabaseDao database, int millis) {
        this.database = database;
        refreshCache();
        setRefreshInterval(millis);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean createTable() {
        return database.createTable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Song> getSongs() {
        return songs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addSong(Song s) {
        boolean success = database.addSong(s);
        refreshCache();
        return success;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteSong(Song s) {
        boolean success = database.deleteSong(s);
        refreshCache();
        return success;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean editSong(Song s) {
        boolean success = database.editSong(s);
        refreshCache();
        return success;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commit() {
        database.commit();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rollback() {
        database.rollback();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        database.close();
    }

    /**
     * Refreshes the cache.
     */
    private void refreshCache() {
        this.songs = database.getSongs();
    }

    /**
     * Sets the refresh interval.
     *
     * @param millis to be set
     */
    private void setRefreshInterval(int millis) {
        interval = millis;
        startTimer();
    }

    /**
     * Starts the timer.
     */
    private void startTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                refreshCache();
            }
        }, interval, interval);
    }
}
