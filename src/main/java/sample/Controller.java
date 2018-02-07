package sample;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import sun.nio.ch.ThreadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Controller {
    private final Model model;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public Controller() {
        model = Model.getInstance();
    }

    public List<Note> getNotes() {
        List<Note> result = new ArrayList<>(); // no NPE
        Future<List<Note>> future = executor.submit(new Callable<List<Note>>() {
            @Override
            public List<Note> call() throws Exception {
                return model.getAllNotes();
            }
        });
        try {
            result = future.get();
        } catch (InterruptedException | ExecutionException e) {
            showMessage(e.getMessage());
        }
        return result;
    }

    public void saveNote(String noteTitle, String noteText, String dateTime) {
        Note noteToSave = new Note(noteTitle, noteText, dateTime);
        executor.submit(new Runnable() {
            @Override
            public void run() {
                model.addNote(noteToSave);
            }
        });
    }

    private void showMessage(String s) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning!");
        alert.setContentText(getClass() + s);
        alert.showAndWait();
    }

    public void closeDB() {
        executor.shutdownNow();
        model.closeAll();
    }
}
