import imgui.*;
import imgui.callback.ImStrConsumer;
import imgui.callback.ImStrSupplier;
import imgui.flag.*;
import imgui.gl3.ImGuiImplGl3;
import imgui.internal.ImRect;
import imgui.type.ImBoolean;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import imgui.type.ImString;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;

public class ImGuiLayer {
    public static boolean wantCaptureMouse = false;
    private static boolean lastWantCaptureMouse = false;
    private long glfwWindow;
    public static int[] camSpeed = new int[] {1};
    public static boolean invertCamMovement = false;
    public static boolean hideBar = false;
    public static float[] camPos = new float[] {0f, 0f, 0f};
    private final long[] mouseCursors = new long[ImGuiMouseCursor.COUNT];
    ArrayList<Boolean> selection = new ArrayList<>();
    ArrayList<String> ents_name = new ArrayList<>();
    ImBoolean[] selectionBoolean;
    private boolean startFileChooser = false;
    private boolean startFileSaver = false;
    private ImString file_name_to_save = new ImString();
    String pwd=(System.getenv("PWD")!=null)?System.getenv("PWD"):System.getenv("cd");
    ImString dir = new ImString(pwd);
    private String path_to_lvl;
    private boolean isOpenClickable = false;
    private boolean isSaveClickable = false;
    boolean isOpenClicked=false;
    boolean isSaveClicked=false;
    final ImBoolean[] isNotWinClosed = new ImBoolean[3];
    public static ImVec2 windowSize;
    public static ImVec2 windowPos;
    public static ImVec2 realWindowSize;
//    static ImVec2 viewportSize = null;

    // LWJGL3 renderer (SHOULD be initialized)
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    public ImGuiLayer(long glfwWindow) {
        this.glfwWindow = glfwWindow;
    }

    // Initialize Dear ImGui.
    public void initImGui() {
        // IMPORTANT!!
        // This line is critical for Dear ImGui to work.
        ImGui.createContext();

        for (int i = 0; i < isNotWinClosed.length; i++) {
            isNotWinClosed[i] = new ImBoolean(true);
        }

        // ------------------------------------------------------------
        // Initialize ImGuiIO config
        final ImGuiIO io = ImGui.getIO();

        io.setIniFilename("fe.ini");
        connectCallbacks();

        // Fonts configuration
        // Read: https://raw.githubusercontent.com/ocornut/imgui/master/docs/FONTS.txt

        final ImFontAtlas fontAtlas = io.getFonts();
        final ImFontConfig fontConfig = new ImFontConfig(); // Natively allocated object, should be explicitly destroyed

        fontConfig.setPixelSnapH(true);

        fontAtlas.clear();

        fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesCyrillic());
        fontAtlas.addFontFromFileTTF("assets/fonts/ProggyCleanRu.ttf", 13, fontConfig);


        fontConfig.setPixelSnapH(false);

        fontConfig.destroy();
        imGuiGl3.init("#version 430 core");
    }

    public void connectCallbacks() {
        final ImGuiIO io = ImGui.getIO();

        io.setConfigFlags(ImGuiConfigFlags.NavEnableKeyboard); // Navigation with keyboard
        io.setBackendFlags(ImGuiBackendFlags.HasMouseCursors); // Mouse cursors to display while resizing windows etc.
        io.setBackendPlatformName("imgui_java_impl_glfw");

        // ------------------------------------------------------------
        // Keyboard mapping. ImGui will use those indices to peek into the io.KeysDown[] array.
        final int[] keyMap = new int[ImGuiKey.COUNT];
        keyMap[ImGuiKey.Tab] = GLFW_KEY_TAB;
        keyMap[ImGuiKey.LeftArrow] = GLFW_KEY_LEFT;
        keyMap[ImGuiKey.RightArrow] = GLFW_KEY_RIGHT;
        keyMap[ImGuiKey.UpArrow] = GLFW_KEY_UP;
        keyMap[ImGuiKey.DownArrow] = GLFW_KEY_DOWN;
        keyMap[ImGuiKey.PageUp] = GLFW_KEY_PAGE_UP;
        keyMap[ImGuiKey.PageDown] = GLFW_KEY_PAGE_DOWN;
        keyMap[ImGuiKey.Home] = GLFW_KEY_HOME;
        keyMap[ImGuiKey.End] = GLFW_KEY_END;
        keyMap[ImGuiKey.Insert] = GLFW_KEY_INSERT;
        keyMap[ImGuiKey.Delete] = GLFW_KEY_DELETE;
        keyMap[ImGuiKey.Backspace] = GLFW_KEY_BACKSPACE;
        keyMap[ImGuiKey.Space] = GLFW_KEY_SPACE;
        keyMap[ImGuiKey.Enter] = GLFW_KEY_ENTER;
        keyMap[ImGuiKey.Escape] = GLFW_KEY_ESCAPE;
        keyMap[ImGuiKey.KeyPadEnter] = GLFW_KEY_KP_ENTER;
        keyMap[ImGuiKey.A] = GLFW_KEY_A;
        keyMap[ImGuiKey.C] = GLFW_KEY_C;
        keyMap[ImGuiKey.V] = GLFW_KEY_V;
        keyMap[ImGuiKey.X] = GLFW_KEY_X;
        keyMap[ImGuiKey.Y] = GLFW_KEY_Y;
        keyMap[ImGuiKey.Z] = GLFW_KEY_Z;
        io.setKeyMap(keyMap);

        // ------------------------------------------------------------
        // Mouse cursors mapping
        mouseCursors[ImGuiMouseCursor.Arrow] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.TextInput] = glfwCreateStandardCursor(GLFW_IBEAM_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeAll] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNS] = glfwCreateStandardCursor(GLFW_VRESIZE_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeEW] = glfwCreateStandardCursor(GLFW_HRESIZE_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNESW] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNWSE] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.Hand] = glfwCreateStandardCursor(GLFW_HAND_CURSOR);
        mouseCursors[ImGuiMouseCursor.NotAllowed] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);

        // ------------------------------------------------------------
        // GLFW callbacks to handle user input

        glfwSetKeyCallback(glfwWindow, (w, key, scancode, action, mods) -> {
            if (action == GLFW_PRESS) {
                io.setKeysDown(key, true);
            } else if (action == GLFW_RELEASE) {
                io.setKeysDown(key, false);
            }

            io.setKeyCtrl(io.getKeysDown(GLFW_KEY_LEFT_CONTROL) || io.getKeysDown(GLFW_KEY_RIGHT_CONTROL));
            io.setKeyShift(io.getKeysDown(GLFW_KEY_LEFT_SHIFT) || io.getKeysDown(GLFW_KEY_RIGHT_SHIFT));
            io.setKeyAlt(io.getKeysDown(GLFW_KEY_LEFT_ALT) || io.getKeysDown(GLFW_KEY_RIGHT_ALT));
            io.setKeySuper(io.getKeysDown(GLFW_KEY_LEFT_SUPER) || io.getKeysDown(GLFW_KEY_RIGHT_SUPER));
        });

        glfwSetCharCallback(glfwWindow, (w, c) -> {
            if (c != GLFW_KEY_DELETE) {
                io.addInputCharacter(c);
            }
        });

        glfwSetMouseButtonCallback(glfwWindow, (w, button, action, mods) -> {
            final boolean[] mouseDown = new boolean[5];

            mouseDown[0] = button == GLFW_MOUSE_BUTTON_1 && action != GLFW_RELEASE;
            mouseDown[1] = button == GLFW_MOUSE_BUTTON_2 && action != GLFW_RELEASE;
            mouseDown[2] = button == GLFW_MOUSE_BUTTON_3 && action != GLFW_RELEASE;
            mouseDown[3] = button == GLFW_MOUSE_BUTTON_4 && action != GLFW_RELEASE;
            mouseDown[4] = button == GLFW_MOUSE_BUTTON_5 && action != GLFW_RELEASE;

            io.setMouseDown(mouseDown);

            if (!io.getWantCaptureMouse() && mouseDown[1]) {
                ImGui.setWindowFocus(null);
            }
        });

        glfwSetScrollCallback(glfwWindow, (w, xOffset, yOffset) -> {
            io.setMouseWheelH(io.getMouseWheelH() + (float) xOffset);
            io.setMouseWheel(io.getMouseWheel() + (float) yOffset);
        });

        io.setSetClipboardTextFn(new ImStrConsumer() {
            @Override
            public void accept(final String s) {
                glfwSetClipboardString(glfwWindow, s);
            }
        });

        io.setGetClipboardTextFn(new ImStrSupplier() {
            @Override
            public String get() {
                final String clipboardString = glfwGetClipboardString(glfwWindow);
                if (clipboardString != null) {
                    return clipboardString;
                } else {
                    return "";
                }
            }
        });
    }

    public static void applyCamPos() {
        SceneManager.cam.transform.setX(camPos[0]);
        SceneManager.cam.transform.setY(camPos[1]);
        SceneManager.cam.setZ(camPos[2]);
        SceneManager.cam.init();
    }

    public void content() {
        if (isNotWinClosed[0].get() && ImGui.begin("Editor Settings", isNotWinClosed[0])) {
            ImGui.sliderInt("cam speed", camSpeed, 1, 5);
            boolean isClicked = ImGui.checkbox("invert cam movement", invertCamMovement);
            if (isClicked && !invertCamMovement)
                invertCamMovement=true;
            else if (isClicked)
                invertCamMovement=false;

            isClicked = ImGui.checkbox("Hide menu bar", hideBar);
            if (isClicked && !hideBar)
                hideBar=true;
            else if (isClicked)
                hideBar=false;

//            ImGui.inputFloat("cam zooming", camZ, 0.01f, 1.0f, "%.3f");
            if (ImGui.dragFloat3("cam pos", camPos, 0.1f, 0.1f, 0.1f, "%.3f")) {
                SceneManager.cam.transform.setX(camPos[0]);
                SceneManager.cam.transform.setY(camPos[1]);
                SceneManager.cam.setZ(camPos[2]);
                SceneManager.cam.init();
            }

            if (ImGui.dragInt("cam zooming", SceneManager.scaleOfCam)) {
                SceneManager.updateScaleOfCam = true;
            }
            ImGui.end();
        }

        if (isNotWinClosed[1].get() && ImGui.begin("Entities", isNotWinClosed[1])) {
            ents_name = new ArrayList<>();
            for (Entity i : SceneManager.ents) {
                ents_name.add(i.name);
            }

            if (ents_name.size() > selection.size()) {
                selection.clear();
                for (int i = 0; i < ents_name.size(); i++) {
                    selection.add(false);
                }
                selectionBoolean = new ImBoolean[selection.size()];
                for (int i = 0; i < selection.size(); i++) {
                    selectionBoolean[i] = new ImBoolean(selection.get(i));
                }
            }

            for (int i = 0; i < ents_name.size(); i++) {
                if (ImGui.selectable(ents_name.get(i), selectionBoolean[i])) {
                    if (!ImGui.getIO().getKeyCtrl())
                        Arrays.fill(selectionBoolean, new ImBoolean(false));
                    selectionBoolean[i] = new ImBoolean(true);
                }
            }
            ImGui.end();
        }

        if (isNotWinClosed[2].get() && ImGui.begin("Inspector", isNotWinClosed[2])) {
            Entity ent=null;
            for (int i = 0; i < ents_name.size(); i++) {
                if (selectionBoolean != null && selectionBoolean[i].get()) {
                    ent = EntityScripts.getEntityByName(ents_name.get(i));
                    break;
                }
            }

            if (ent == null)
                ImGui.text("No entity has been selected yet.");
            else {
                float[] ent_pos = new float[]{
                        ent.transform.getX(),
                        ent.transform.getY()
                };
                float[] ent_size = new float[] {
                        ent.transform.sizeX,
                        ent.transform.sizeY
                };

                if (ImGui.dragFloat2("pos", ent_pos))
                {
                    ent.transform.setX(ent_pos[0]);
                    ent.transform.setY(ent_pos[1]);
                }

                if (ImGui.dragFloat2("size", ent_size))
                {
                    ent.transform.sizeX=ent_size[0];
                    ent.transform.sizeY=ent_size[1];
                }
            }
            ImGui.end();
        }

        if (ImGui.begin("Game Viewport", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse)) {
            windowSize = getLargestSizeForViewport();
            windowPos = getCenteredPositionForViewport(windowSize);
            realWindowSize = ImGui.getWindowSize();

            lastWantCaptureMouse=wantCaptureMouse;
            wantCaptureMouse=ImGui.isWindowHovered() && !isMouseHoveringWinHeader();

            if (SceneManager.cam.transform.sizeX != windowSize.y || SceneManager.cam.transform.sizeY != windowSize.x) {
                SceneManager.cam.transform.sizeX = (int) windowSize.x;
                SceneManager.cam.transform.sizeY = (int) windowSize.y;
                SceneManager.cam.init();
                SceneManager.updateProjSize = true;
            }

            //ImGui.setCursorPos(windowPos.x, windowPos.y);
            ImGui.setCursorPos(windowPos.x, windowPos.y);
            System.out.println(windowSize);

            int texID = SceneManager.getFrameBuffer().getTextureId();

            ImGui.image(texID, windowSize.x, windowSize.y, 0, 1, 1, 0);

            ImGui.end();
        }

        if (startFileChooser) {
            startFileSaver=false;
            fileChooser();
        }

        if (startFileSaver) {
            startFileChooser=false;
            fileSaver();
        }

        if (isOpenClicked) {
            SceneLoader.readScene(new File(path_to_lvl));
            SceneManager.isSceneStarted=true;
            isOpenClicked=false;
        }

        if (isSaveClicked) {
            System.out.println(path_to_lvl);
            isSaveClicked=false;
        }

        if (!hideBar) {
            if (ImGui.beginMainMenuBar()) {
                if (ImGui.beginMenu("File")) {
                    if (ImGui.menuItem("Open")) {
                        startFileChooser=true;
                    }
                    if (SceneManager.isSceneStarted && ImGui.menuItem("Save")) {
                        startFileSaver=true;
                    }
                    ImGui.endMenu();
                }
                if (ImGui.beginMenu("Edit")) {
                    if (ImGui.menuItem("Editor Settings")) {
                        isNotWinClosed[0]=new ImBoolean(true);
                    }
                    if (ImGui.menuItem("Entities")) {
                        isNotWinClosed[1]=new ImBoolean(true);
                    }
                    if (ImGui.menuItem("Inspector")) {
                        isNotWinClosed[2]=new ImBoolean(true);
                    }
                    ImGui.endMenu();
                }
                ImGui.endMainMenuBar();
            }
        }
        ImGui.showDemoWindow();
    }

    public void fileChooser() {
        if (ImGui.begin("Open as")) {
            File dir_f = new File(dir.get());

            ImGui.inputText("##input", dir);
            ImGui.sameLine();
            if (ImGui.button("<")) {
                dir = new ImString(dir_f.getParent());
            }
            ImGui.separator();

            File[] files = dir_f.listFiles();
            if (files!=null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        if (ImGui.selectable("D: " + file.getName(), false)) {
                            //dir=new ImString(dir.get()+"/"+file.getName());
                            dir = new ImString(new File(dir_f, file.getName()).getPath());
                            ImGui.openPopup("Open as");
                        }
                    }
                    else {
                        boolean isDotPassed = false;
                        StringBuilder extension = new StringBuilder();
                        for (int i = 0; i < file.getName().length(); i++) {
                            if (file.getName().charAt(i) == '.') {
                                isDotPassed=true;
                            } else if (isDotPassed) {
                                extension.append(file.getName().charAt(i));
                            }
                        }
                        if (extension.toString().equals("lvl") && ImGui.selectable("F: " + file.getName(), false)) {
                            path_to_lvl = file.getPath();
                            isOpenClickable=true;
                        }
                    }
                }
            }

            ImGui.separator();
            if (ImGui.button("cancel"))
                startFileChooser=false;
            ImGui.sameLine();
            if (isOpenClickable && ImGui.button("open")) {
                startFileChooser=false;
                isOpenClicked=true;
            }
            ImGui.end();
        }
    }

    public void fileSaver() {
        if (ImGui.begin("Save as")) {
            File dir_f = new File(dir.get());

            ImGui.inputText("##input", dir);
            ImGui.sameLine();
            if (ImGui.button("<")) {
                dir = new ImString(dir_f.getParent());
            }
            ImGui.separator();

            File[] files = dir_f.listFiles();
            if (files!=null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        if (ImGui.selectable("D: " + file.getName(), false)) {
                            //dir=new ImString(dir.get()+"/"+file.getName());
                            dir = new ImString(new File(dir_f, file.getName()).getPath());
                            ImGui.openPopup("Save as");
                        }
                    }
                    else {
                        boolean isDotPassed = false;
                        StringBuilder extension = new StringBuilder();
                        for (int i = 0; i < file.getName().length(); i++) {
                            if (file.getName().charAt(i) == '.') {
                                isDotPassed=true;
                            } else if (isDotPassed) {
                                extension.append(file.getName().charAt(i));
                            }
                        }
                        if (extension.toString().equals("lvl") && ImGui.selectable("F: " + file.getName(), false)) {
                            path_to_lvl = file.getPath();
                            file_name_to_save.set(file.getName());
                            isSaveClickable=true;
                        }
                    }
                }
            }

            if (ImGui.beginPopupModal("Rewrite?")) {
                ImGui.text("This file already exists. Do you want to rewrite it?\n(this operation can't be undone!)");
                ImGui.separator();
                if (ImGui.button("OK")) {
                    startFileSaver = false;
                    isSaveClicked = true;
                }
                ImGui.sameLine();
                if (ImGui.button("Cancel")) {
                    ImGui.closeCurrentPopup();
                }
                ImGui.endPopup();
            }

            ImGui.separator();
            if (ImGui.inputText("file name", file_name_to_save)) {
                String div = (System.getProperty("os.name").toLowerCase().contains("linux")||System.getProperty("os.name").toLowerCase().contains("mac"))?"/":"\\";
                // if os name is Linux or macos div = "/". else div = "\"
                path_to_lvl=dir_f.getPath()+div+file_name_to_save.get();
            }
            if (ImGui.button("cancel"))
                startFileSaver=false;
            ImGui.sameLine();
            if (isSaveClickable && ImGui.button("save")) {
                if (new File(path_to_lvl).exists()) {
                    ImGui.openPopup("Rewrite?");
                } else {
                    startFileSaver = false;
                    isSaveClicked = true;
                }
            }
            ImGui.end();
        }
    }

    public void update(float dt) {
        startFrame(dt/50);

        // Any Dear ImGui code SHOULD go between ImGui.newFrame()/ImGui.render() methods
        ImGui.newFrame();
//        ImGui.showDemoWindow();
        content();
        ImGui.render();

        endFrame();
    }

    private void startFrame(final float deltaTime) {
        // Get window properties and mouse position
        float[] winWidth = {SceneManagersWindow.getWidth()};
        float[] winHeight = {SceneManagersWindow.getHeight()};
        double[] mousePosX = {0};
        double[] mousePosY = {0};
        glfwGetCursorPos(glfwWindow, mousePosX, mousePosY);

        // We SHOULD call those methods to update Dear ImGui state for the current frame
        final ImGuiIO io = ImGui.getIO();
        io.setDisplaySize(winWidth[0], winHeight[0]);
        io.setDisplayFramebufferScale(1f, 1f);
        io.setMousePos((float) mousePosX[0], (float) mousePosY[0]);
        io.setDeltaTime(deltaTime);

        if (!wantCaptureMouse && lastWantCaptureMouse) {
            KeyListener.clearKeyPressed();
            connectCallbacks();
        }
        else if (wantCaptureMouse && !lastWantCaptureMouse) {
            glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
            glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
            glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
            glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        }
        //wantCaptureMouse=io.getWantCaptureMouse();

        // Update the mouse cursor
        if (wantCaptureMouse) {
            final int imguiCursor = ImGui.getMouseCursor();
            glfwSetCursor(glfwWindow, mouseCursors[imguiCursor]);
            glfwSetInputMode(glfwWindow, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        }
    }

    private void endFrame() {
        // After Dear ImGui prepared a draw data, we use it in the LWJGL3 renderer.
        // At that moment ImGui will be rendered to the current OpenGL context.
        imGuiGl3.renderDrawData(ImGui.getDrawData());
    }

    // If you want to clean a room after yourself - do it by yourself
    private void destroyImGui() {
        imGuiGl3.dispose();
        ImGui.destroyContext();
    }

    private static ImVec2 getLargestSizeForViewport() {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float aspectWidth = windowSize.x;
        float aspectHeight = aspectWidth / SceneManagersWindow.getTargetAspectRatio();
        if (aspectHeight > windowSize.y) {
            // We must switch to pillar box mode
            aspectHeight = windowSize.y;
            aspectWidth = aspectHeight * SceneManagersWindow.getTargetAspectRatio();
        }

        return new ImVec2(aspectWidth, aspectHeight);
    }

    private static ImVec2 getCenteredPositionForViewport(ImVec2 aspectSize) {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float viewportX = (windowSize.x / 2.0f) - (aspectSize.x / 2.0f);
        float viewportY = (windowSize.y / 2.0f) - (aspectSize.y / 2.0f);

        return new ImVec2(viewportX + ImGui.getCursorPosX(),
                viewportY + ImGui.getCursorPosY());
    }

    public static boolean isMouseHoveringWinHeader() {
        ImVec2 window_pos = ImGui.getWindowPos();
        ImVec2 window_size = ImGui.getWindowSize();
        ImVec2 header_size = new ImVec2(window_size.x, ImGui.getTextLineHeightWithSpacing());

        ImVec2 cursor_pos = ImGui.getIO().getMousePos();

        return cursor_pos.x >= window_pos.x && cursor_pos.x <= window_pos.x + window_size.x &&
                cursor_pos.y >= window_pos.y && cursor_pos.y <= window_pos.y + header_size.y; // isMouseHoveringRect didn't work
    }

    public static void changeMouseParams(int button, int action) {
        final boolean[] mouseDown = new boolean[5];

        mouseDown[0] = button == GLFW_MOUSE_BUTTON_1 && action != GLFW_RELEASE;
        mouseDown[1] = button == GLFW_MOUSE_BUTTON_2 && action != GLFW_RELEASE;
        mouseDown[2] = button == GLFW_MOUSE_BUTTON_3 && action != GLFW_RELEASE;
        mouseDown[3] = button == GLFW_MOUSE_BUTTON_4 && action != GLFW_RELEASE;
        mouseDown[4] = button == GLFW_MOUSE_BUTTON_5 && action != GLFW_RELEASE;

        final ImGuiIO io = ImGui.getIO();
        io.setMouseDown(mouseDown);
    }
}