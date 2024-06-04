package text;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;//package text;
import org.joml.Vector2f;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class RenderText {
    public static class Character {
        public int textureID;
        public Vector2f size;
        public Vector2f bearing;
        public int advance;
    }
    private static UnicodeFont font;

    public static void setUpFonts() {
//        java.awt.Font awtFont = new java.awt.Font("Times New Roman", java.awt.Font.BOLD, 18);
        Font awtFont = null;
        try {
            awtFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/def.ttf"));
            awtFont = awtFont.deriveFont(10f); // <<
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
        font = new UnicodeFont(awtFont);
        font.getEffects().add(new ColorEffect(java.awt.Color.white));
        font.addAsciiGlyphs();
        try {
            font.loadGlyphs();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public static void drawText(int x, int y, String text) {
        font.drawString(x,y,text);
    }

    public static void main(String[] args) {
        new RenderText();
    }
}