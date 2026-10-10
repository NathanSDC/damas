package main.java.nathansdc.damas.util;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;

import main.java.nathansdc.damas.App;

public class FontLoader {
    
    public static Font getFont(String fontName){
        try {
            InputStream is = App.class.getResourceAsStream("/main/resources/" + fontName + ".ttf");

            if (is == null) {
                return new Font("Arial Black", Font.PLAIN, 20);
            }

            Font base = Font.createFont(Font.TRUETYPE_FONT, is);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

            ge.registerFont(base);

            return base.deriveFont(16.0f);

        } catch (Exception e) {
            e.printStackTrace();
            return new Font("Arial Black", Font.PLAIN, 20);
        }
    }

}
