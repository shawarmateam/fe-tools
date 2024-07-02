import fonts.CFont;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.*;
import physic.Timer;
import java.util.ArrayList;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class SceneManager {
    public static ArrayList<Entity> ents = new ArrayList<>();
    public static Camera cam;
    private static long window;
    private static int FPS = 60;
    public static boolean canRender = true;
    public double sec_per_frame = 1.0/FPS;
    private static boolean cooldown_m = false;
    private static boolean cooldown_p = false;
    public static boolean updateProjSize = false;
    public static boolean isSceneStarted = false;
    public static int[] scaleOfCam = new int[] {80};
    public static boolean updateScaleOfCam = false;
    public static FrameBuffer frameBuffer = null;

    public void run() {
        window = SceneManagersWindow.getWindow();
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
                .scale(scaleOfCam[0]);
        Matrix4f target = new Matrix4f();
        projection.mul(scale, target);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        double start = Timer.getTime();
        double end = Timer.getTime();
        double dt = 0;

        Batch test = new Batch();
        test.font = new CFont("assets/fonts/JetBrainsMonoNL-Medium.ttf", 25);
        test.shader = shader;
        test.initBatch();
        RenderTexture text = new RenderTexture(test.getText("Not a single project was launched.", "#ffffff", test.font.getBitmap()));
        SceneManagersWindow.imGuiLayer = new ImGuiLayer(window);
        SceneManagersWindow.imGuiLayer.initImGui();

        frameBuffer = new FrameBuffer(1920, 1080);

        WindowSizeListener.resizeCallbackSM(window, SceneManagersWindow.getWidth(), SceneManagersWindow.getHeight());
        glfwSetScrollCallback(window, MouseListener::mouseScrollCallback);
        glfwSetMouseButtonCallback(window, MouseListener::mouseButtonCallback);
        glfwSetKeyCallback(window, KeyListener::keyCallback);

        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();
            if (canRender) {
                start = end;
//                frameBuffer.bind();
                glfwSwapBuffers(window); // swap the color buffer
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                glLoadIdentity();
                frameBuffer.bind();
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

                for (Entity ent : ents) {
                    new PosTexture(ent.texture.getWidth(), ent.texture.getHeight()).renderTexture(ent.texture,
                            ent.transform.getX(), ent.transform.getY(),
                            shader, scale, cam);
                }

                if (!isSceneStarted) {
                    new PosTexture(text.getWidth(), text.getHeight()).renderTexture(text, -3.4f, 1, shader, scale, cam);
                }

                frameBuffer.unbind();
                SceneManagersWindow.imGuiLayer.update((float) dt);

                if (updateScaleOfCam) {
                    scale = new Matrix4f()
                            .translate(new Vector3f(0, 0, 0))
                            .scale(scaleOfCam[0]);
                    updateScaleOfCam=false;
                }
                if (updateProjSize) {
                    projection = new Matrix4f()
                            .ortho2D(cam.transform.sizeX/2, -cam.transform.sizeX/2, cam.transform.sizeY/2, -cam.transform.sizeY/2);
                    projection.mul(scale, target);
                    updateProjSize=false;
                }

                if (isSceneStarted) {
                    checkHotKey();
                }
                // TODO: сделать так, чтобы все сущ. камеры были в ArrayList из камер

                end = Timer.getTime();
                dt = (end - start)/sec_per_frame;
                canRender=false;
            }
            if (Timer.getTime() > start+sec_per_frame) {
                canRender=true;
            }
        }
    }

    public static void checkHotKey() {
        System.out.println(MouseListener.isDraggedMouse1()+" "+MouseListener.isDraggedMouse2()+" "+MouseListener.isDraggedMouse3());
        if (KeyListener.isKeyPressed(GLFW_KEY_W)) {
            if (ImGuiLayer.invertCamMovement)
                ImGuiLayer.camPos[1] -= ImGuiLayer.camSpeed[0];
            else
                ImGuiLayer.camPos[1] += ImGuiLayer.camSpeed[0];
            ImGuiLayer.applyCamPos();
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_A)) {
            if (ImGuiLayer.invertCamMovement)
                ImGuiLayer.camPos[0] += ImGuiLayer.camSpeed[0];
            else
                ImGuiLayer.camPos[0] -= ImGuiLayer.camSpeed[0];
            ImGuiLayer.applyCamPos();
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_S)) {
            if (ImGuiLayer.invertCamMovement)
                ImGuiLayer.camPos[1] += ImGuiLayer.camSpeed[0];
            else
                ImGuiLayer.camPos[1] -= ImGuiLayer.camSpeed[0];
            ImGuiLayer.applyCamPos();
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_D)) {
            if (ImGuiLayer.invertCamMovement)
                ImGuiLayer.camPos[0] -= ImGuiLayer.camSpeed[0];
            else
                ImGuiLayer.camPos[0] += ImGuiLayer.camSpeed[0];
            ImGuiLayer.applyCamPos();
        }
        cam.init();
        if (KeyListener.isKeyPressed(GLFW_KEY_MINUS) && !cooldown_m) {
            if (ImGuiLayer.camSpeed[0] > 1) {
                ImGuiLayer.camSpeed[0] -= 1;
                cooldown_m = true;
            }
        } else if (!KeyListener.isKeyPressed(GLFW_KEY_MINUS)) {
            cooldown_m = false;
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_EQUAL) && KeyListener.isKeyPressed(GLFW_KEY_LEFT_SHIFT) && !cooldown_p) {
            if (ImGuiLayer.camSpeed[0] < 5) {
                ImGuiLayer.camSpeed[0] += 1;
                cooldown_p = true;
            }
        } else if (!KeyListener.isKeyPressed(GLFW_KEY_EQUAL) || !KeyListener.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            cooldown_p = false;
        }

        if (MouseListener.get_yOff() > 0) {
            scaleOfCam[0]++;
            updateScaleOfCam=true;
        }
        if (MouseListener.get_yOff() < 0 && scaleOfCam[0] > 1) {
            scaleOfCam[0]--;
            updateScaleOfCam=true;
        }
    }

    public static FrameBuffer getFrameBuffer() {
        return frameBuffer;
    }

    public static void main(String[] args) {
        new SceneManagersWindow().run();
    }
}