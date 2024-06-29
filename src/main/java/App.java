import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.*;
import physic.Timer;
import scripts.ScriptsReader;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class App {
    public static ArrayList<Entity> ents = new ArrayList<>();
    public static Camera cam;
    private static long window;
    private static int FPS = 60;
    public static boolean canRender = true;
    public double sec_per_frame = 1.0/FPS;
    public static boolean updateProjSize;
    public void run() {
        window = Window.getWindow();
        GL.createCapabilities();
        cam = new Camera(new Transform(0,0,600,600,1000,1000));
        glEnable(GL_TEXTURE_2D);
    }
    public void loop() {
        Shader shader = new Shader("shader.glsl");
        Matrix4f projection = new Matrix4f()
                .ortho2D(cam.transform.sizeX/2, -cam.transform.sizeX/2, cam.transform.sizeY/2, -cam.transform.sizeY/2);
        Matrix4f scale = new Matrix4f()
                .translate(new Vector3f(0, 0, 0))
                .scale(80);
        Matrix4f target = new Matrix4f();
        projection.mul(scale, target);
//        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
//        PosTexture texture = new PosTexture();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        ScriptsReader scrReader = new ScriptsReader();
        double start = Timer.getTime();
        double end = Timer.getTime();
        double dt = 0;
        WindowSizeListener.resizeCallbackApp(window, Window.getWidth(), Window.getHeight());

        while (!glfwWindowShouldClose(window)) {
            MouseListener.isChanged=false;
            glfwPollEvents();
            if (canRender) {
                start = end;
                glfwSwapBuffers(window); // swap the color buffer
                try {
                    scrReader.runUpdateInSCRs((float) dt);
                } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                         IllegalAccessException | InstantiationException e) {
                    throw new RuntimeException(e);
                }

                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                for (Entity ent : ents) {
                    new PosTexture(ent.texture.getWidth(), ent.texture.getHeight()).renderTexture(ent.texture, ent.transform.getX(), ent.transform.getY(), shader, scale, cam);
                }

                if (updateProjSize) {
                    projection = new Matrix4f()
                            .ortho2D(cam.transform.sizeX/2, -cam.transform.sizeX/2, cam.transform.sizeY/2, -cam.transform.sizeY/2);
                    projection.mul(scale, target);
                    updateProjSize=false;
                }

                end = Timer.getTime();
                dt = (end - start)/sec_per_frame;
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
        if (args.length == 0)
            new Window().run();
        else if (Objects.equals(args[0], "SM") || Objects.equals(args[0], "scene-manager")) {
            SceneManager.main(args);
        }
    }
}