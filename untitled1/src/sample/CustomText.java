package sample;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class CustomText extends Text {
    public String textSize ;//= new SimpleStringProperty(this,"textSize","16");
    public String  textFont ;
    public FontWeight fontWeight ;
    public FontPosture fontPosture ;
    public boolean underline ;

    public CustomText(){
        super();

    }

    public CustomText(CustomText customText){
        super(customText.getText());
        textSize = customText.textSize;
        textFont = customText.textFont;
        fontWeight = customText.fontWeight;
        fontPosture = customText.fontPosture;

    }

    public CustomText(String text){
        super(text);
        textFont = "Times New Roman";
        fontWeight = FontWeight.NORMAL;
        fontPosture = FontPosture.REGULAR;
        underline = false;
        textSize = "16";
        setFont(this);
    }

    public CustomText(String text, String textSize){
        super(text);
        textFont = "Times New Roman";
        fontWeight = FontWeight.NORMAL;
        fontPosture = FontPosture.REGULAR;
        underline = false;
        this.textSize = (textSize);
        //setFont();
    }
    public CustomText(String text, String Size,String Font,FontWeight weight, FontPosture posture, boolean underline){
        super(text);
        this.textSize = Size;
        this.textFont = Font;
        this.fontWeight = weight;
        this.fontPosture = posture;
        this.underline = underline;
        setFont(this);
    }

    public void setTextFont(String textFont) {
        this.textFont = textFont;
        setFont(this);
    }

    public void setTextSize(String textSize) {
        this.textSize = textSize;
        setFont(this);
    }

    public void setFontWeight(FontWeight fontWeight) {
        this.fontWeight = fontWeight;
        setFont(this);
    }

    public void setFontPosture(FontPosture fontPosture) {
        this.fontPosture = fontPosture;
       setFont(this);
    }


    public void setFont(Text text) {
        text.setFont(Font.font(textFont, fontWeight,fontPosture,Double.parseDouble(textSize)));
        if(underline) text.setUnderline(true);
    }

    public String getTextSize() { return textSize;}

    public String getTextFont() {
        return textFont;
    }

    public FontWeight getFontWeight() {
        return fontWeight;
    }

    public FontPosture getFontPosture() {
        return fontPosture;
    }

}
