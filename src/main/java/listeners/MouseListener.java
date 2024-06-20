package listeners;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private static double xPos;
    private static double yPos;
    private static boolean mouse1;
    private static boolean mouse2;
    private static boolean mouse3;
    private static double xOff;
    private static double yOff;

    public static void mousePosCallback(long window, double xp, double yp) {
        xPos = xp;
        yPos = yp;
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            switch (button) {
                case 0:
                    mouse1=true;
                    break;
                case 1:
                    mouse2=true;
                    break;
                case 2:
                    mouse3=true;
                    break;
            }
        } else if (action == GLFW_RELEASE) {
            switch (button) {
                case 0:
                    mouse1=false;
                    break;
                case 1:
                    mouse2=false;
                    break;
                case 2:
                    mouse3=false;
                    break;
            }
        }
        System.out.println(mouse1);
    }

    public static void mouseScrollCallback(long window, double x_off, double y_off) {
        xOff = x_off;
        yOff = y_off;
        System.out.println(xOff+" "+yOff);
    }

    public double get_xPos() {return xPos;}
    public double get_yPos() {return yPos;}
    public double get_xOff() {return xOff;}
    public double get_yOff() {return yOff;}
    public boolean get_mouse1() {return mouse1;}
    public boolean get_mouse2() {return mouse2;}
    public boolean get_mouse3() {return mouse3;}
}
