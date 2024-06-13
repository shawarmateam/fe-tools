import static org.lwjgl.opengl.GL11.glViewport;

public class WindowSizeListener {

    public static void resizeCallback(long window, int screenWidth, int screenHeight) {
        SceneManagersWindow.setWidth(screenWidth);
        SceneManagersWindow.setHeight(screenHeight);

        // Figure out the largest area that fits this target aspect ratio
//        int aspectWidth = screenWidth;
//        int aspectHeight = (int)((float)aspectWidth / SceneManagersWindow.getTargetAspectRatio());
//        if (aspectHeight > screenHeight) {
//            // It doesn't fit our height, we must switch to pillarbox
//            aspectHeight = screenHeight;
//            aspectWidth = (int)((float)aspectHeight * SceneManagersWindow.getTargetAspectRatio());
//        }
//
//        // Center rectangle
//        int vpX = (int)(((float)screenWidth / 2f) - ((float)aspectWidth / 2f));
//        int vpY = (int)(((float)screenHeight / 2f) - ((float)aspectHeight / 2f));

//        glViewport(vpX, vpY, aspectWidth, aspectHeight);
    }
}
