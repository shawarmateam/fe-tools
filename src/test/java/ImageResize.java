import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageResize {
    public static void main(String[] args) {
        try {
            BufferedImage originalImage = ImageIO.read(new File("input.jpg"));
            int newWidth = 40; // Новая ширина
            int newHeight = 40; // Новая высота
            BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, originalImage.getType());

            double scaleX = (double) originalImage.getWidth() / newWidth;
            double scaleY = (double) originalImage.getHeight() / newHeight;

            for (int x = 0; x < newWidth; x++) {
                for (int y = 0; y < newHeight; y++) {
                    int rgb = originalImage.getRGB((int) (x * scaleX), (int) (y * scaleY));
                    resizedImage.setRGB(x, y, rgb);
                }
            }

            File output = new File("resized.jpg");
            ImageIO.write(resizedImage, "jpg", output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
