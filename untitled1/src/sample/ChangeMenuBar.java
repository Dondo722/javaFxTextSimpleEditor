package sample;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;


public class ChangeMenuBar extends HBox{
        private final Toolbar toolbar;


        public ChangeMenuBar(Toolbar toolbar){
            super();
            this.toolbar = toolbar;
            int minTextSize = 12;
            int textChangeOn = 4;
            int maxTextSize = 45;
            Menu textMenu = new Menu("Text");
            Menu fontMenu = new Menu("Font");
            Menu sizeMenu = new Menu("Size");
            MenuItem boldMenu = new MenuItem("Bold");
            MenuItem italicMenu = new MenuItem("Italic");
            MenuItem underlineMenu = new MenuItem("Underline");


            for(String string : Font.getFontNames()) {
                fontMenu.getItems().add(new MenuItem(string));
            }
            fontMenu.setOnAction(e -> fontChange(e.getTarget(),fontMenu));
            for(int i = minTextSize; i < maxTextSize; i+=textChangeOn){
                sizeMenu.getItems().add(new MenuItem(String.valueOf(i)));
            }
            sizeMenu.setOnAction(e -> fontSizeChange(e.getTarget(),sizeMenu));
            boldMenu.setOnAction(e-> toolbar.getBoldBox().fire());
            italicMenu.setOnAction(e-> toolbar.getItalicBox().fire());
            underlineMenu.setOnAction(e-> toolbar.getUnderlineBox().fire());
            textMenu.getItems().addAll(fontMenu,sizeMenu,new SeparatorMenuItem(),boldMenu,italicMenu,underlineMenu);
            MenuBar menuBar = new MenuBar(){ public void requestFocus(){}};
            menuBar.getMenus().addAll(textMenu);
            super.getChildren().addAll(menuBar);
        }
        public void fontChange(Object object,Menu fontMenu){
            for (int i = 0; i < fontMenu.getItems().size() ; i++){
                if(object.equals(fontMenu.getItems().get(i)))
                    toolbar.getFontBox().setValue(fontMenu.getItems().get(i).getText());
            }
        }
        public void fontSizeChange(Object object,Menu sizeMenu){
            for (int i = 0; i < sizeMenu.getItems().size() ; i++){
                if(object.equals(sizeMenu.getItems().get(i)))
                    toolbar.getTextSizeBox().setValue(sizeMenu.getItems().get(i).getText());
            }
        }
}
