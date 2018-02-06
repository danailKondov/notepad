package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

public class Main extends Application {

    public static final double WIDTH = 500;
    public static final double HEIGHT = 600;
    public static final int MAX_TEXT_LENGTH = 100;

    private Controller controller = new Controller(this);;
    private TableView<Note> table;
    private final VBox vBox = new VBox();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy kk:mm")
            .withLocale(new Locale("ru"));

    @Override
    public void start(final Stage primaryStage) throws Exception{
        setLabel();
        setTable(primaryStage);
        setButton(primaryStage);

        primaryStage.setTitle("Notepad");
        Scene scene = new Scene(new Group());
        ((Group) scene.getRoot()).getChildren().addAll(vBox);
        primaryStage.setScene(scene);
        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void setLabel() {
        final Label label = new Label("Notepad");
        label.setFont(new Font("Arial", 30));
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10, 0, 0, 10));
        vBox.getChildren().add(label);
    }

    private void setButton(Stage primaryStage) {
        Button button = new Button("Enter new note");
        button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                showEditScene(primaryStage);
            }
        });
        vBox.getChildren().add(button);
    }

    private void setTable(Stage primaryStage) {
        List<Note> notes = controller.getNotes();
        ObservableList<Note> data = FXCollections.observableArrayList(notes);

        TableColumn noteTitle = new TableColumn("Note title");
        noteTitle.setMinWidth(280);
        noteTitle.setCellValueFactory(new PropertyValueFactory<Note, String>("noteTitle"));

        TableColumn noteDate = new TableColumn("Date and time of creation");
        noteDate.setMinWidth(170);
        noteDate.setCellValueFactory(new PropertyValueFactory<Note, String>("dateTime"));

        table = new TableView<Note>();
        table.setItems(data);
        table.getColumns().addAll(noteTitle, noteDate);
        vBox.getChildren().add(table);
    }

    private void showEditScene(Stage primaryStage) {
        Stage editStage = new Stage();
        editStage.setTitle("Edit text");
        editStage.initModality(Modality.WINDOW_MODAL);
        editStage.initOwner(primaryStage);

        VBox box = new VBox();
        final TextField dataField = new TextField();
        setDateTimeField(dataField);

        final TextField noteTitleField = new TextField();
        noteTitleField.setPromptText("Enter title: ");
        noteTitleField.setText("Title");

        final TextArea noteTextField = new TextArea();
        noteTextField.setWrapText(true); // перенос строки
        // limiting symbols
        noteTextField.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                String text = noteTextField.getText();
                if (text.length() > MAX_TEXT_LENGTH) {
                    text = text.substring(0, MAX_TEXT_LENGTH);
                    noteTextField.setText(text);
                    // alert
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning!");
                    alert.setContentText("Maximum text length exceeded!");
                    alert.showAndWait();

                }
            }
        });

        Button button = new Button("Save text");
        box.setSpacing(20);
        box.getChildren().addAll(dataField, noteTitleField, noteTextField, button);

        Scene scene = new Scene(new Group());
        ((Group) scene.getRoot()).getChildren().addAll(box);
        editStage.setWidth(WIDTH);
        editStage.setHeight(HEIGHT);
        editStage.setResizable(false);
        editStage.setScene(scene);

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

                    editStage.close();
                }
            }
        });

        editStage.showAndWait();
    }

    private void setDateTimeField(TextField dataField) {
        dataField.setPromptText("Set date and time: ");
        LocalDateTime date = LocalDateTime.now();
        String preDate = date.format(formatter);
        dataField.setText(preDate);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
