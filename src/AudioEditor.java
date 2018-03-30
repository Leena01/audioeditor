import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;
import database.*;
import logic.dbaccess.DatabaseAccessModel;
import logic.exceptions.MatlabEngineException;
import logic.exceptions.SQLConnectionException;

import static util.Constants.REFRESH_MILLIS;
import static util.Utils.showDialog;

import logic.matlab.MatlabHandler;
import view.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AudioEditor {

    public static void main(String s[]) throws ClassNotFoundException
    {
        MatlabEngine eng;
        final String DRIVER = "org.sqlite.JDBC";
        final String URL = "jdbc:sqlite:music.sqlite";
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

                mainWindow.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent we) {
                        mainWindow.getGlassPane().setVisible(true);
                        try {
                            matlabHandler.close();
                        } catch (MatlabEngineException mee) {
                            showDialog(mee.getMessage());
                        }
                        databaseAccessModel.close();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ignored) { }
                        // TODO
                    }
                });
            } catch (MatlabEngineException mee) {
                showDialog("Matlab initialization error.");
            } catch (SQLConnectionException sce) {
                showDialog("DatabaseDao error.");
            } catch (EngineException ee) {
                showDialog("Matlab connection error.");
            } catch (Exception e) {
                showDialog(e.getMessage());
            }
        }
    }
}