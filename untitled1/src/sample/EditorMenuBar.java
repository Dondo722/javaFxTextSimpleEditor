package sample;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Window;

//
import java.beans.XMLEncoder;
import java.beans.XMLDecoder;




import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.io.FileInputStream;
import java.io.FileOutputStream;


import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EditorMenuBar extends HBox {
    Menu fileMenu = new Menu("File");
    WorkingArea workingArea;
    MenuItem openItem = new MenuItem("Open");
    MenuItem saveItem = new MenuItem("Save");
    FileChooser fileChooser = new FileChooser();
    EditorMenuBar(WorkingArea workingArea){
        super();
        this.workingArea = workingArea;
        fileMenu.getItems().addAll(openItem,saveItem);
        openItem.setOnAction(e -> openItemCase());
        saveItem.setOnAction(e -> saveItemCase());

        //FIlechooser
        fileChooser.setInitialDirectory(new File("C:\\Users\\Lenovo\\Desktop\\saves"));

        MenuBar menuBar = new MenuBar(){ public void requestFocus(){}};
        menuBar.getMenus().addAll(fileMenu);
        super.getChildren().addAll(menuBar);
    }
    public void saveItemCase(){
        Window stage = this.getScene().getWindow();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("save as txt","*.txt"),
                new FileChooser.ExtensionFilter("save as xml", "*.xml"));
        File file = fileChooser.showSaveDialog(stage);
        fileChooser.setInitialDirectory(file.getParentFile());
//        saveFile(file);
        saveFileXML(file);
    }
    public void openItemCase(){
        Window stage = this.getScene().getWindow();
        fileChooser.setTitle("Open");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("open txt" , "*.txt"),
               new FileChooser.ExtensionFilter("open xml","*.xml"));
        File file = fileChooser.showOpenDialog(stage);
        fileChooser.setInitialDirectory(file.getParentFile());
//        openFile(file);
        openFileXML(file);
    }
    public void saveFile(File file){
        try {
            FileWriter writer;
            writer = new FileWriter(file);
            writer.write(workingArea.getString());
            writer.close();
        }catch (IOException ex){
            Logger.getLogger(Main.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
    public  void saveFileXML(File file){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            XMLEncoder encoder = new XMLEncoder(fileOutputStream);
//            for (int i = 0; i < workingArea.textFlow.getChildren().size(); i++){
//                CustomText text = new CustomText(((CustomText)workingArea.textFlow.getChildren().get(i)));
//                System.out.println(text);
//                encoder.writeObject(text);
//            }
             CustomText text = new CustomText("custom","20","Times New Roman", FontWeight.NORMAL, FontPosture.ITALIC,true);
            CustomText text2 = new CustomText("nonono","40","Calibri", FontWeight.BOLD, FontPosture.REGULAR,false);
            System.out.println(text);
            encoder.writeObject(text);
              encoder.writeObject(text2);
            encoder.close();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }


    public void openFile(File file){
        try {
            FileReader reader;
            reader = new FileReader(file);
            Scanner scan = new Scanner(reader);
            String string = "";
            while (scan.hasNext()){
                string+=scan.next();
            }
            System.out.println(string);
            reader.close();
        }catch (IOException ex){
            Logger.getLogger(Main.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
    public  void openFileXML(File file){
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            XMLDecoder decoder = new XMLDecoder(fileInputStream);

            for (int i = 0 ; i < 2; i++){
                CustomText text = (CustomText)decoder.readObject();
                System.out.println(text);
                System.out.println(text.textFont);
                System.out.println(text.textSize);
                System.out.println(text.fontPosture);
                System.out.println(text.fontWeight);
            }

            decoder.close();
            fileInputStream.close();

        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
