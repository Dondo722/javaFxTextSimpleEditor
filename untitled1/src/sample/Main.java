package sample;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class Main extends Application {

    public static void main(String[] args)
    {
        launch(args);
    }
    public void start(Stage stage)
    {
        TextEditor textEditor = new TextEditor();
        Scene scene = new Scene(textEditor,400,400);
        textEditor.addEventHandlers();
        stage.setScene(scene);
        stage.show();
    }
}