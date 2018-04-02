import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;
import database.*;
import logic.dbaccess.DatabaseAccessModel;
import logic.exceptions.MatlabEngineException;
import logic.exceptions.SQLConnectionException;

import static view.util.Constants.REFRESH_MILLIS;
import static view.util.Helper.showDialog;

import logic.matlab.MatlabHandler;
import view.*;

public class AudioEditor {
    private static final String DRIVER = "org.sqlite.JDBC";
    private static final String URL = "jdbc:sqlite:music.sqlite";
    private static final String MATLAB_INIT_ERROR = "Matlab initialization error.";
    private static final String DATABASE_ERROR = "Database error.";
    private static final String MATLAB_CONNECTION_ERROR = "Matlab connection error.";

    public static void main(String s[]) throws ClassNotFoundException
    {
        MatlabEngine eng;
        final DatabaseDao database = new DatabaseDaoImpl(DRIVER, URL);
        // DatabaseDao database = new MockDatabase();
        final Cache cache = new Cache(database, REFRESH_MILLIS);
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
            } catch (MatlabEngineException mee) {
                showDialog(MATLAB_INIT_ERROR);
            } catch (SQLConnectionException sce) {
                showDialog(DATABASE_ERROR);
            } catch (EngineException ee) {
                showDialog(MATLAB_CONNECTION_ERROR);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}