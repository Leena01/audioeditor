package org.ql.audioeditor;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;
import org.ql.audioeditor.common.matlab.MatlabFileLoader;
import org.ql.audioeditor.common.properties.ConfigPropertiesLoader;
import org.ql.audioeditor.common.properties.ImageLoader;
import org.ql.audioeditor.common.properties.SongPropertiesLoader;
import org.ql.audioeditor.database.Cache;
import org.ql.audioeditor.database.DatabaseDao;
import org.ql.audioeditor.database.DatabaseDaoImpl;
import org.ql.audioeditor.database.Persistence;
import org.ql.audioeditor.logic.dbaccess.DatabaseAccessModel;
import org.ql.audioeditor.logic.exceptions.MatlabEngineException;
import org.ql.audioeditor.logic.exceptions.SQLConnectionException;
import org.ql.audioeditor.logic.matlab.MatlabHandler;
import org.ql.audioeditor.view.MainWindow;

import java.io.IOException;
import java.sql.SQLException;

import static org.ql.audioeditor.common.util.GeneralUtils.getPath;
import static org.ql.audioeditor.common.util.ViewUtils.showDialog;

/**
 * Main class.
 */
public final class AudioEditor {
    private static final String CONFIG_PROPERTIES_FILE =
        "/config/config.properties";
    private static final String SONG_PROPERTIES_FILE =
        "/config/song.properties";
    private static final String IMAGES_PROPERTIES_FILE =
        "/config/images.properties";
    private static final String MATLAB_CONNECTION_ERROR =
        "MATLAB connection error.";
    private static final String MATLAB_CONNECTION_INTERRUPTED =
        "MATLAB connection is interrupted.";
    private static final String CANNOT_READ_ERROR =
        "Some of the required files are not found.";
    private static final String DATABASE_CONNECTION_FAILED =
        "Database connection failed.";

    /**
     * Private constructor.
     */
    private AudioEditor() {
        throw new AssertionError();
    }

    /**
     * Main method.
     *
     * @param s Arguments
     */
    public static void main(String[] s) {
        try {
            ConfigPropertiesLoader.init(CONFIG_PROPERTIES_FILE);
            SongPropertiesLoader.init(SONG_PROPERTIES_FILE);
            ImageLoader.init(IMAGES_PROPERTIES_FILE);
            MatlabFileLoader.init();
            final DatabaseDao database =
                new DatabaseDaoImpl(ConfigPropertiesLoader.getDriver(),
                    ConfigPropertiesLoader.getJdbc() + getPath()
                        + ConfigPropertiesLoader.getUrl());
            final Cache cache = new Cache(database,
                ConfigPropertiesLoader.getRefreshMillis());
            final Persistence persistence = new Persistence(cache);
            final DatabaseAccessModel databaseAccessModel =
                new DatabaseAccessModel(persistence);
            MatlabEngine eng;
            try {
                databaseAccessModel.createTable();
                eng = MatlabEngine.startMatlab();
                MatlabHandler matlabHandler = MatlabHandler.getInstance(eng);
                matlabHandler.init();
                MainWindow mainWindow =
                    new MainWindow(databaseAccessModel, matlabHandler);
                mainWindow.run();
            } catch (MatlabEngineException | SQLConnectionException mee) {
                showDialog(mee.getMessage());
            } catch (EngineException ee) {
                showDialog(MATLAB_CONNECTION_ERROR);
            } catch (Exception e) {
                showDialog(MATLAB_CONNECTION_INTERRUPTED);
            }
            ConfigPropertiesLoader.close();
            SongPropertiesLoader.close();
            ImageLoader.close();
        } catch (IOException ioe) {
            showDialog(CANNOT_READ_ERROR);
        } catch (ClassNotFoundException | SQLException e) {
            showDialog(DATABASE_CONNECTION_FAILED);
        }
    }
}
