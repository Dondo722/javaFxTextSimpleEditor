package sample;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;


public class ChangeMenuBar extends HBox{
        WorkingArea workingArea;
        Toolbar toolbar;
        Menu textMenu = new Menu("Text");
        Menu fontMenu = new Menu("Font");
        Menu sizeMenu = new Menu("Size");
        MenuItem boldMenu = new MenuItem("Bold");
        MenuItem italicMenu = new MenuItem("Italic");
        MenuItem underlineMenu = new MenuItem("Underline");

        public ChangeMenuBar(WorkingArea workingArea,Toolbar toolbar){
            super();
            this.workingArea = workingArea;
            this.toolbar = toolbar;

            for(String string : Font.getFontNames()) {
                fontMenu.getItems().add(new MenuItem(string));
            }
            fontMenu.setOnAction(e -> fontChange(e.getTarget()));
            for(int i = 12; i < 45; i+=4){
                sizeMenu.getItems().add(new MenuItem(String.valueOf(i)));
            }
            sizeMenu.setOnAction(e -> fontSizeChange(e.getTarget()));
            boldMenu.setOnAction(e-> toolbar.boldBox.fire());
            italicMenu.setOnAction(e-> toolbar.italicBox.fire());
            underlineMenu.setOnAction(e-> toolbar.underlineBox.fire());
            textMenu.getItems().addAll(fontMenu,sizeMenu,new SeparatorMenuItem(),boldMenu,italicMenu,underlineMenu);
            MenuBar menuBar = new MenuBar(){ public void requestFocus(){}};
            menuBar.getMenus().addAll(textMenu);
            super.getChildren().addAll(menuBar);
        }
        public void fontChange(Object object){
            for (int i = 0; i < fontMenu.getItems().size() ; i++){
                if(object.equals(fontMenu.getItems().get(i)))
                    toolbar.fontBox.setValue(fontMenu.getItems().get(i).getText());
            }
        }
        public void fontSizeChange(Object object){
            for (int i = 0; i < sizeMenu.getItems().size() ; i++){
                if(object.equals(sizeMenu.getItems().get(i)))
                    toolbar.textSizeBox.setValue(sizeMenu.getItems().get(i).getText());
            }
        }
}
