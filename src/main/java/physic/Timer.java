package physic;

public class Timer {
    public static double frame_per_sec = 1.0/60;
    public static double getTime() {
        return (double)System.nanoTime()/(double)1000000000L;
    }

    public static void changeFps(int fps) {
        frame_per_sec = 1.0/fps;
    }
}
