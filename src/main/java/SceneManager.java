import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.*;
import physic.Timer;
import scripts.ScriptsReader;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class SceneManager {
    public static ArrayList<Entity> ents = new ArrayList<>();
    private static Camera cam;
    private static long window;
    private static int FPS = 60;
    public static boolean canRender = true;
    public double sec_per_frame = 1.0/FPS;
    private float speedCam = 10f;
    private boolean cooldown_m = false;
    private boolean cooldown_p = false;
    public void run() {
        window = SceneManagersWindow.getWindow();
        GL.createCapabilities();
        cam = new Camera(new Transform(0,0,600,600,1000,1000));
        glEnable(GL_TEXTURE_2D);
    }
    public void loop() {
        Shader shader = new Shader("shader");
        Matrix4f projection = new Matrix4f()
                .ortho2D(-cam.transform.sizeX/2, cam.transform.sizeX/2, -cam.transform.sizeY/2, cam.transform.sizeY/2);
        Matrix4f scale = new Matrix4f()
                .translate(new Vector3f(0, 0, 0))
                .scale(80);
        Matrix4f target = new Matrix4f();
        projection.mul(scale, target);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        PosTexture texture = new PosTexture();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        double start = Timer.getTime();
        double end = Timer.getTime();
        SceneLoader.readScene(new File("/home/adisteyf/IdeaProjects/FilesEngine/assets/sample.lvl"));

        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();
            if (canRender) {
                start = end;
                glfwSwapBuffers(window); // swap the color buffer

                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                //                texture.renderTexture(testent.texture, testent.transform.getX(), testent.transform.getY(), shader, scale, cam);
                for (Entity ent : ents) {
                    texture.renderTexture(ent.texture, ent.transform.getX(), ent.transform.getY(), shader, scale, cam);
                }

                if (isPressed(GLFW_KEY_W)) {
                    cam.transform.addY(speedCam);
                }
                if (isPressed(GLFW_KEY_A)) {
                    cam.transform.addX(-speedCam);
                }
                if (isPressed(GLFW_KEY_S)) {
                    cam.transform.addY(-speedCam);
                }
                if (isPressed(GLFW_KEY_D)) {
                    cam.transform.addX(speedCam);
                }
                cam.init();
                if (isPressed(GLFW_KEY_MINUS) && !cooldown_m) {
                    if (speedCam > 1) {
                        speedCam -= 1;
                        cooldown_m = true;
                    }
                } else if (!isPressed(GLFW_KEY_MINUS)) {
                    cooldown_m = false;
                }

                if (isPressed(GLFW_KEY_EQUAL) && isPressed(GLFW_KEY_LEFT_SHIFT) && !cooldown_p) {
                    if (speedCam < 25) {
                        speedCam += 1;
                        cooldown_p = true;
                    }
                } else if (!isPressed(GLFW_KEY_EQUAL) || !isPressed(GLFW_KEY_LEFT_SHIFT)) {
                    cooldown_p = false;
                }

                System.out.println(speedCam);
                end = Timer.getTime();
                canRender=false;
            }
            if (Timer.getTime() > start+sec_per_frame) {
                canRender=true;
            }
        }
    }

    public static boolean isPressed(int key) {
        return glfwGetKey(window, key) == GL_TRUE;
    }

    public static void main(String[] args) {
        new SceneManagersWindow().run();
    }
}