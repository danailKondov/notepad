package sample;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access obj.
 */
public class Model {
    private List<Note> allNotes = new ArrayList<Note>();

    public List<Note> getAllNotes() {
        return allNotes;
    }

    public void addNote(Note noteToSave) {
        allNotes.add(noteToSave);
    }
}
