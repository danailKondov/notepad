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
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.List;

public class Main extends Application {

    public static final double WIDTH = 500;
    public static final double HEIGHT = 600;
    public static final int MAX_TEXT_LENGTH = 100;

    private Controller controller = new Controller();
    private TableView<Note> table;
    private final VBox vBox = new VBox();

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
                new EditStage(primaryStage, controller, table);
            }
        });
        vBox.getChildren().add(button);
    }

    private void setTable(Stage primaryStage) {
        List<Note> notes = notes = controller.getNotes();
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

    @Override
    public void stop() throws Exception {
        controller.closeDB();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
