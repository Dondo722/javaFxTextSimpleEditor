package sample;


import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Window;

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

public class FileMenuBar extends HBox {
    Menu fileMenu = new Menu("File");
    WorkingArea workingArea;
    MenuItem openItem = new MenuItem("Open");
    MenuItem saveItem = new MenuItem("Save");
    MenuItem newItem = new MenuItem("New");


    public FileMenuBar(WorkingArea workingArea){
        super();
        this.workingArea = workingArea;
        fileMenu.getItems().addAll(newItem,openItem,saveItem);
        newItem.setOnAction(e -> workingArea.newTextFlow());
        openItem.setOnAction(e -> openItemCase());
        saveItem.setOnAction(e -> saveItemCase());


        MenuBar menuBar = new MenuBar(){ public void requestFocus(){}};
        menuBar.getMenus().addAll(fileMenu);
        super.getChildren().addAll(menuBar);
    }
    public void saveItemCase(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\Users\\Lenovo\\Desktop\\saves"));
        Window stage = this.getScene().getWindow();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("save as txt","*.txt"),
                new FileChooser.ExtensionFilter("save as xml", "*.xml"));
        File file = fileChooser.showSaveDialog(stage);
        fileChooser.setInitialDirectory(file.getParentFile());
        if (getFileFormat(file.getName()).equals(".txt"))saveFile(file);
        else if (getFileFormat(file.getName()).equals(".xml")) saveFileXML(file);
    }
    public void openItemCase(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\Users\\Lenovo\\Desktop\\saves"));
        Window stage = this.getScene().getWindow();
        fileChooser.setTitle("Open");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("open txt" , "*.txt"),
               new FileChooser.ExtensionFilter("open xml","*.xml"));
        File file = fileChooser.showOpenDialog(stage);

        if(file.exists()){
            fileChooser.setInitialDirectory(file.getParentFile());
            workingArea.newTextFlow();
            if (getFileFormat(file.getName()).equals(".txt")) openFileTXT(file);
            else if (getFileFormat(file.getName()).equals(".xml")) openFileXML(file);
        }
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
            CustomText customText = new CustomText();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            XMLEncoder encoder = new XMLEncoder(fileOutputStream);
            encoder.writeObject(workingArea.textFlow.getChildren().size()-1);
            for (int i = 0; i < workingArea.textFlow.getChildren().size(); i++){
                if(i == workingArea.caret.caretIndex(workingArea.textFlow)) continue;
                CustomText text = new CustomText((Text)workingArea.textFlow.getChildren().get(i));
                customText.textToCustom(text);
                System.out.println(text.getFont());
                encoder.writeObject(text);
            }

            encoder.close();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }
    public void openFileTXT(File file){
        try {
            FileReader reader;
            reader = new FileReader(file);
            Scanner scan = new Scanner(reader);
            String string = "";
            while (scan.hasNext()){
                string+=scan.nextLine();
                string +="\n";
            }
            for (int i = 0; string.length() > i; i++)
            {
                CustomText text = new CustomText(String.valueOf(string.charAt(i)));
                workingArea.insert(text);
            }
            reader.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
    public  void openFileXML(File file){
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            XMLDecoder decoder = new XMLDecoder(fileInputStream);
            int size = (int)decoder.readObject();
            for (int i = 0 ; i < size; i++){
                CustomText text = (CustomText)decoder.readObject();
                text.setFont();
                workingArea.insert(text);
            }
            decoder.close();
            fileInputStream.close();

        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public String getFileFormat(String filename){
        int id = filename.lastIndexOf(".");
        return filename.substring(id);
    }
}
