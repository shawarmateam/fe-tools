import static org.lwjgl.opengl.GL11.glViewport;

public class WindowSizeListener {

    public static void resizeCallbackSM(long window, int screenWidth, int screenHeight) {
        SceneManagersWindow.setWidth(screenWidth);
        SceneManagersWindow.setHeight(screenHeight);
        SceneManager.cam.transform.sizeX = screenWidth;
        SceneManager.cam.transform.sizeY = screenHeight;
        SceneManager.cam.init();
        SceneManager.updateProjSize = true;
        //glViewport(0, 0, screenWidth, screenHeight);
        System.out.println(screenWidth+" "+screenHeight);

        // Figure out the largest area that fits this target aspect ratio
        int aspectWidth = screenWidth;
        int aspectHeight = (int)((float)aspectWidth / SceneManagersWindow.getTargetAspectRatio());
        if (aspectHeight > screenHeight) {
            // It doesn't fit our height, we must switch to pillarbox
            aspectHeight = screenHeight;
            aspectWidth = (int)((float)aspectHeight * SceneManagersWindow.getTargetAspectRatio());
        }

        // Center rectangle
        int vpX = (int)(((float)screenWidth / 2f) - ((float)aspectWidth / 2f));
        int vpY = (int)(((float)screenHeight / 2f) - ((float)aspectHeight / 2f));

        glViewport(vpX, vpY, aspectWidth, aspectHeight);
    }

    public static void resizeCallbackApp(long window, int screenWidth, int screenHeight) {
        Window.setWidth(screenWidth);
        Window.setHeight(screenHeight);
        App.cam.transform.sizeX = screenWidth;
        App.cam.transform.sizeY = screenHeight;
        App.cam.init();
        App.updateProjSize = true;
        //glViewport(0, 0, screenWidth, screenHeight);
        System.out.println(screenWidth+" "+screenHeight);

        // Figure out the largest area that fits this target aspect ratio
        int aspectWidth = screenWidth;
        int aspectHeight = (int)((float)aspectWidth / Window.getTargetAspectRatio());
        if (aspectHeight > screenHeight) {
            // It doesn't fit our height, we must switch to pillarbox
            aspectHeight = screenHeight;
            aspectWidth = (int)((float)aspectHeight * Window.getTargetAspectRatio());
        }

        // Center rectangle
        int vpX = (int)(((float)screenWidth / 2f) - ((float)aspectWidth / 2f));
        int vpY = (int)(((float)screenHeight / 2f) - ((float)aspectHeight / 2f));

        glViewport(vpX, vpY, aspectWidth, aspectHeight);
    }
}
