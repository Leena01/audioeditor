import com.jtattoo.plaf.*;
import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;
import database.*;
import model.*;
import model.exceptions.SQLConnectionException;
import static util.Utils.showDialog;

import model.MatlabHandler;
import view.*;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;

public class AudioEditor {

    public static void main(String s[]) throws ClassNotFoundException
    {
        MatlabEngine eng;
        final String DRIVER = "org.sqlite.JDBC";
        final String URL = "jdbc:sqlite:music.sqlite";
        final DatabaseDao database = new DatabaseDaoImpl(DRIVER, URL);
        // DatabaseDao database = new MockDatabase();
        final Cache cache = new Cache(database, 1);
        final Persistence persistence = new Persistence(cache);
        final Model model = new Model(persistence);
        MatlabHandler matlabHandler;
        MainWindow mainWindow;

        if (!model.isConnected())
            showDialog("Error: DatabaseDao connection failed.");
        else {
            try {
                model.createTable();
                eng = MatlabEngine.startMatlab();
                matlabHandler = MatlabHandler.getInstance(eng);
                mainWindow = new MainWindow(model, matlabHandler);
                mainWindow.run();

                mainWindow.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent we) {
                        matlabHandler.close();
                        model.close();
                    }
                });
            } catch (SQLConnectionException e) {
                showDialog("DatabaseDao error.");
            } catch (EngineException e) {
                showDialog("Matlab connection error.");
            } catch (Exception e) {
                showDialog(e.getMessage());
            }
        }
    }
}