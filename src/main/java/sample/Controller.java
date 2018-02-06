package sample;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private final Main main;
    private final Model model;
    private List<Note> noteTitles; // получаем список из БД

    public Controller(Main main) {
        this.main = main;
        model = new Model();
    }

    public List<Note> getNotes() {
        return model.getAllNotes();
    }

    public void saveNote(String noteTitle, String noteText, String dateTime) {
        Note noteToSave = new Note(noteTitle, noteText, dateTime);
        model.addNote(noteToSave);
    }
}
