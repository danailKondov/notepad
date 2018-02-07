package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

/**
 * Class is stage for creating new notes.
 */
public class EditStage extends Stage {

    private Stage primaryStage;
    private Controller controller;
    private TableView<Note> table;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy kk:mm")
            .withLocale(new Locale("ru"));

    public EditStage(Stage primaryStage, Controller controller, TableView<Note> table) {
        this.primaryStage = primaryStage;
        this.controller = controller;
        this.table = table;
        init();
    }

    private void init() {
        setTitle("Edit text");
        initModality(Modality.WINDOW_MODAL);
        initOwner(primaryStage);

        VBox box = new VBox();
        final TextField dataField = new TextField();
        setDateTimeField(dataField);

        final TextField noteTitleField = new TextField();
        noteTitleField.setText("Write title here");

        final TextArea noteTextField = new TextArea();
        setTextField(noteTextField);

        Button button = new Button("Save text");
        button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                String dataText = dataField.getText();
                boolean parsingIsOk = false;
                try {
                    LocalDateTime.parse(dataText, formatter);
                    parsingIsOk = true;
                } catch (DateTimeParseException e) {
                    setDateTimeField(dataField);
                }

                if (parsingIsOk) {
                    String noteTitleText = noteTitleField.getText();
                    String noteText = noteTextField.getText();

                    // add to DB
                    controller.saveNote(noteTitleText, noteText, dataText);

                    // refresh table
                    List<Note> notes = controller.getNotes();
                    table.getItems().clear();
                    table.getItems().addAll(notes);

                    close();
                }
            }
        });

        box.setSpacing(20);
        box.getChildren().addAll(dataField, noteTitleField, noteTextField, button);

        Scene scene = new Scene(new Group());
        ((Group) scene.getRoot()).getChildren().addAll(box);
        setWidth(Main.WIDTH);
        setHeight(Main.HEIGHT);
        setResizable(false);
        setScene(scene);
        showAndWait();
    }

    private void setTextField(TextArea noteTextField) {
        noteTextField.setWrapText(true); // перенос строки
        // limiting symbols
        noteTextField.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                String text = noteTextField.getText();
                if (text.length() > Main.MAX_TEXT_LENGTH) {
                    text = text.substring(0, Main.MAX_TEXT_LENGTH);
                    noteTextField.setText(text);
                    // alert
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning!");
                    alert.setContentText("Maximum text length exceeded!");
                    alert.showAndWait();

                }
            }
        });
    }

    private void setDateTimeField(TextField dataField) {
        LocalDateTime date = LocalDateTime.now();
        String preDate = date.format(formatter);
        dataField.setText(preDate);
    }

}
