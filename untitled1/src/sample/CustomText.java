package sample;

import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class CustomText extends Text {
    private String textSize ;
    private String  textFont ;
    private FontWeight fontWeight ;
    private FontPosture fontPosture ;
    private boolean underline ;

    public CustomText(){
        super();

    }
    public CustomText(String text){
        super(text);
        textFont = "Times New Roman";
        fontWeight = FontWeight.NORMAL;
        fontPosture = FontPosture.REGULAR;
        underline = false;
        textSize = "16";
        setFont();
    }
    public CustomText(String text, String Size,String Font,FontWeight weight, FontPosture posture, boolean underline){
        super(text);
        this.textSize = Size;
        this.textFont = Font;
        this.fontWeight = weight;
        this.fontPosture = posture;
        this.underline = underline;
        setFont();
    }
    public CustomText(Text text){
        super(text.getText());
        this.textSize = String.valueOf(text.getFont().getSize());
        this.textFont = text.getFont().getName();
        this.underline = text.isUnderline();
        switch (text.getFont().getStyle()) {
            case "Regular" -> {
                this.fontWeight = FontWeight.NORMAL;
                this.fontPosture = FontPosture.REGULAR;
            }
            case "Bold" -> {
                this.fontWeight = FontWeight.BOLD;
                this.fontPosture = FontPosture.REGULAR;
            }
            case "Italic" -> {
                this.fontWeight = FontWeight.NORMAL;
                this.fontPosture = FontPosture.ITALIC;
            }
            case "Bold Italic" -> {
                this.fontWeight = FontWeight.BOLD;
                this.fontPosture = FontPosture.ITALIC;
            }
        }
        setFont();
    }

    public void setTextFont(String textFont) {
        this.textFont = textFont;
        setFont();
    }

    public void setTextSize(String textSize) {
        this.textSize = textSize;
        setFont();
    }

    public void setFontWeight(FontWeight fontWeight) {
        this.fontWeight = fontWeight;
        setFont();
    }

    public void setFontPosture(FontPosture fontPosture) {
        this.fontPosture = fontPosture;
       setFont();
    }


    public void setFont() {
        this.setFont(Font.font(textFont, fontWeight,fontPosture,Double.parseDouble(textSize)));
        if(underline) this.setUnderline(true);
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

    public void textToCustom(Text text){
        String style =  text.getFont().getStyle();
        Text text1 = new Text("fiasodpfoa");
        text1.setStyle(style);
        System.out.println(style);
        System.out.println(text1);
    }
}
