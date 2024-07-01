import fonts.CFont;
import fonts.CharInfo;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL15;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15C.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15C.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL31.GL_TEXTURE_BUFFER;

public class Batch {
    private int[] indices = {
            0, 1, 3,
            1, 2, 3
    };

    public static int BATCH_SIZE = 100;
    public static int VERTEX_SIZE = 7;
    public float[] vertices = new float[BATCH_SIZE * VERTEX_SIZE];
    public int size = 0;
    private Matrix4f projection = new Matrix4f();

    public int vao;
    public int vbo;
    public Shader shader;
    public CFont font;

    public void generateEbo() {
        int elementSize = BATCH_SIZE * 3;
        int[] elementBuffer = new int[elementSize];

        for (int i=0; i < elementSize; i++) {
            elementBuffer[i] = indices[(i % 6)] + ((i / 6) * 4);
        }

        int ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);
    }

    public void initBatch() {
        projection.identity();
        projection.ortho(0, 800, 0, 600, 1f, 100f);

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, Float.BYTES * VERTEX_SIZE * BATCH_SIZE, GL_DYNAMIC_DRAW);

        generateEbo();

        int stride = 7 * Float.BYTES;
        glVertexAttribPointer(0, 2, GL_FLOAT, false, stride, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, stride, 2 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, 2, GL_FLOAT, false, stride, 5 * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    public void flushBatch() {
        // Clear the buffer on the GPU, and then upload the CPU contents, and then draw
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, Float.BYTES * VERTEX_SIZE * BATCH_SIZE, GL_DYNAMIC_DRAW);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        // Draw the buffer that we just uploaded
        shader.bind();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_BUFFER, font.textureId);
        shader.setUniform("sampler", 0);
        shader.setUniform("projection", projection);

        glBindVertexArray(vao);

        glDrawElements(GL_TRIANGLES, size * 6, GL_UNSIGNED_INT, 0);

        // Reset batch for use on next draw call
        size = 0;
    }

    public BufferedImage getCharacter(char c, String rgb, BufferedImage bitmap) {
        CharInfo charInfo = font.getCharacter(c);
        BufferedImage letter = bitmap.getSubimage(charInfo.sourceX,charInfo.sourceY-charInfo.height,charInfo.width,charInfo.height+10);
//        Color color = Color.decode(rgb);
//        if (!color.equals(Color.WHITE)) {
//            for (int y = 0; y < letter.getHeight(); y++) {
//                for (int x = 0; x < letter.getWidth(); x++) {
//                    if (letter.getRGB(x, y) != 0) {
//                        letter.setRGB(x, y, color.getRGB());
//                    }
//                }
//            }
//        }
//        if (c=='g') {
//            File file = new File("letter.png");
//            try {
//                ImageIO.write(letter, "png", file);
//            } catch (IOException e) {
//                System.out.println("Ошибка при сохранении изображения: " + e.getMessage());
//            }
//        }
        return letter;
    }

    public BufferedImage getText(String text, String rgb, BufferedImage bitmap) {
        ArrayList<BufferedImage> letters = new ArrayList<>();
        int width = 0;
        int height = 0;
        int distance = 5;
        for (int i = 0; i < text.length(); i++) {
            letters.add(getCharacter(text.charAt(i), rgb, bitmap));
        }
        for (BufferedImage img : letters) {
            width += img.getWidth()+distance;
            height = Math.max(height, img.getHeight());
        }
        BufferedImage bufferedText = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedText.createGraphics();
        int currWidth = 0;
        for (BufferedImage img : letters) {
            g2d.drawImage(img, currWidth, 0, null);
            currWidth += img.getWidth()+distance;
        }
        g2d.dispose();
        return bufferedText;
    }

    public BufferedImage getText(String text, String rgb, String rgb_on, BufferedImage bitmap) {
        ArrayList<BufferedImage> letters = new ArrayList<>();
        int width = 0;
        int height = 0;
        int distance = 5;
        for (int i = 0; i < text.length(); i++) {
            letters.add(getCharacter(text.charAt(i), rgb, bitmap));
        }
        for (BufferedImage img : letters) {
            width += img.getWidth()+distance;
            height = Math.max(height, img.getHeight());
        }
        BufferedImage bufferedText = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedText.createGraphics();
        int currWidth = 0;
        for (BufferedImage img : letters) {
            g2d.drawImage(img, currWidth, 0, null);
            currWidth += img.getWidth()+distance;
        }
        g2d.dispose();

        Color color_on = Color.decode(rgb_on);
        for (int y = 0; y < bufferedText.getHeight(); y++) {
            for (int x = 0; x < bufferedText.getWidth(); x++) {
                if (bufferedText.getRGB(x, y) == 0) {
                    bufferedText.setRGB(x, y, color_on.getRGB());
                }
            }
        }

        return bufferedText;
    }
}
