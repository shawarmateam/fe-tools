import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;

public class MouseListener {
    private static double xPos;
    private static double yPos;
    private static boolean mouse1;
    private static boolean mouse2;
    private static boolean mouse3;
    private static boolean[] isDragged = new boolean[3]; // 0 == mouse1, 1 == mouse2, 2 == mouse3
    private static double xOff;
    private static double yOff;
    public static boolean isChanged=false;

    public static void mousePosCallback(long window, double xp, double yp) {
        isDragged[0] = mouse1;
        isDragged[1] = mouse2;
        isDragged[2] = mouse3;
        xPos = xp;
        yPos = yp;
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            switch (button) {
                case 0:
                    mouse1 = true;
                    break;
                case 1:
                    mouse2 = true;
                    break;
                case 2:
                    mouse3 = true;
                    break;
            }
        } else if (action == GLFW_RELEASE) {
            switch (button) {
                case 0:
                    mouse1 = false;
                    isDragged[0]=false;
                    break;
                case 1:
                    mouse2 = false;
                    isDragged[1]=false;
                    break;
                case 2:
                    mouse3 = false;
                    isDragged[2]=false;
                    break;
            }
        }
        ImGuiLayer.changeMouseParams(button, action);
    }

    public static void mouseScrollCallback(long window, double x_off, double y_off) {
        isChanged = (yOff == y_off);
        xOff = x_off;
        yOff = y_off;
    }

    public static double get_xPos() {return xPos;}
    public static double get_yPos() {return yPos;}
    public static double get_xOff() {
        if (isChanged)
            xOff=0;
        return xOff;
    }
    public static double get_yOff() {
        if (isChanged)
            yOff=0;
        return yOff;
    }
    public static boolean get_mouse1() {return mouse1;}
    public static boolean get_mouse2() {return mouse2;}
    public static boolean get_mouse3() {return mouse3;}
    public static boolean isDraggedMouse1() {return isDragged[0];}
    public static boolean isDraggedMouse2() {return isDragged[1];}
    public static boolean isDraggedMouse3() {return isDragged[2];}
}
