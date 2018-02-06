package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Class is simple note.
 */
public class Note {

    private StringProperty noteTitle;
    private StringProperty noteText;
    private StringProperty dateTime;

    public Note(String noteTitle, String noteText, String dateTime) {
        this.noteTitle = new SimpleStringProperty(noteTitle);
        this.noteText = new SimpleStringProperty(noteText);
        this.dateTime = new SimpleStringProperty(dateTime);
    }

    public String getNoteTitle() {
        return noteTitle.get();
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle.set(noteTitle);
    }

    public StringProperty noteTitleProperty() {
        return noteTitle;
    }

    public String getNoteText() {
        return noteText.get();
    }

    public void setNoteText(String noteText) {
        this.noteText.set(noteText);
    }

    public StringProperty noteTextProperty() {
        return noteText;
    }

    public String getDateTime() {
        return dateTime.get();
    }

    public void setDateTime(String dateTime) {
        this.dateTime.set(dateTime);
    }

    public StringProperty dateTimeProperty() {
        return dateTime;
    }
}
