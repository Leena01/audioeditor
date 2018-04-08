import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;
import database.*;
import logic.dbaccess.DatabaseAccessModel;
import logic.exceptions.MatlabEngineException;
import logic.exceptions.SQLConnectionException;
import static common.util.Helper.showDialog;

import logic.matlab.MatlabHandler;
import common.properties.ConfigPropertiesLoader;
import common.properties.ImageLoader;
import common.properties.SongPropertiesLoader;
import view.*;

import java.io.IOException;

public class AudioEditor {
    private static final String CONFIG_PROPERTIES_FILE = "/config/config.properties";
    private static final String SONG_PROPERTIES_FILE = "/config/song.properties";
    private static final String IMAGES_PROPERTIES_FILE = "/config/images.properties";
    private static final String MATLAB_CONNECTION_ERROR = "Matlab connection error.";
    private static final String CANNOT_READ_PROPERIES_ERROR = "Properties file not found.";

    public static void main(String s[]) throws ClassNotFoundException
    {
        try {
            ConfigPropertiesLoader.init(CONFIG_PROPERTIES_FILE);
            SongPropertiesLoader.init(SONG_PROPERTIES_FILE);
            ImageLoader.init(IMAGES_PROPERTIES_FILE);
            MatlabEngine eng;
            final DatabaseDao database = new DatabaseDaoImpl(ConfigPropertiesLoader.getDriver(), ConfigPropertiesLoader.getUrl());
            // DatabaseDao database = new MockDatabase();
            final Cache cache = new Cache(database, ConfigPropertiesLoader.getRefreshMillis());
            final Persistence persistence = new Persistence(cache);
            final DatabaseAccessModel databaseAccessModel = new DatabaseAccessModel(persistence);
            MatlabHandler matlabHandler;
            MainWindow mainWindow;

            if (!databaseAccessModel.isConnected())
                showDialog("Error: DatabaseDao connection failed.");
            else {
                try {
                    databaseAccessModel.createTable();
                    eng = MatlabEngine.startMatlab();
                    matlabHandler = MatlabHandler.getInstance(eng);
                    matlabHandler.init();
                    mainWindow = new MainWindow(databaseAccessModel, matlabHandler);
                    mainWindow.run();
                } catch (MatlabEngineException | SQLConnectionException mee) {
                    showDialog(mee.getMessage());
                } catch (EngineException ee) {
                    showDialog(MATLAB_CONNECTION_ERROR);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ConfigPropertiesLoader.close();
            SongPropertiesLoader.close();
            ImageLoader.close();
        }
        catch (IOException ioe) {
            showDialog(CANNOT_READ_PROPERIES_ERROR);
        }
    }
}