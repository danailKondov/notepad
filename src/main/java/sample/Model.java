package sample;

import javafx.scene.control.Alert;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Data access obj.
 */
public class Model {

    private static Model INSTANCE;
    private Connection connection;

    private Model() {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("notepad.properties");
             InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8")) {
            properties.load(reader);
            String url = properties.getProperty("url");
            String username = properties.getProperty("user");
            String password = properties.getProperty("password");
            this.connection = DriverManager.getConnection(url, username, password);
            initiateDB();
        } catch (Exception e) {
            showMessage(e.getMessage());
        }
    }

    /**
     * Initiates DB: create table if not exist.
     */
    private void initiateDB() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS notes (" +
                    "id serial PRIMARY KEY, " +
                    "note_title TEXT, " +
                    "note_text TEXT, " +
                    "date_of_publishing CHARACTER VARYING (30)) "
            );
        } catch (SQLException e) {
            showMessage(e.getMessage());
        }
    }

    public static Model getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Model();
        }
        return INSTANCE;
    }

    public List<Note> getAllNotes() {
        ArrayList<Note> result = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT note_title, note_text, date_of_publishing " +
                    "FROM notes "
            );
            while(rs.next()) {
                String note_title = rs.getString(1);
                String note_text = rs.getString(2);
                String datePublishing = rs.getString(3);
                Note note = new Note(note_title, note_text, datePublishing);
                result.add(note);
            }
        } catch (SQLException e) {
            showMessage(e.getMessage());
        }
        return result;
    }

    public void addNote(Note noteToSave) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO notes " +
                             "(note_title, note_text, date_of_publishing) " +
                             "VALUES (?, ?, ?)"
        )) {
            statement.setString(1, noteToSave.getNoteTitle());
            statement.setString(2, noteToSave.getNoteText());
            statement.setString(3, noteToSave.getDateTime());
            statement.executeUpdate();
        } catch (SQLException e) {
            showMessage(e.getMessage());
        }
    }

    private void showMessage(String s) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning!");
        alert.setContentText(getClass() + " " + s);
        alert.showAndWait();
    }

    public void closeAll() {
        try {
            connection.close();
        } catch (SQLException e) {
            showMessage(e.getMessage());
        }
    }
}
