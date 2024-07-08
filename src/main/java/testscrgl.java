import scripts.FilesScripts;
import java.io.File;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class testscrgl extends FilesScripts {
    Entity test_ent;
    Entity wall_test;
    @Override
    public void start() {
        System.out.println("starting...");
        RenderTexture test = new RenderTexture("./assets/textures/based_tex.jpg");

        // Creating entities
        Entity testent = new Entity(test, new Transform(0,0,100,100,1000,1000), "testent");
        EntityScripts.entityCreate(testent);
        Entity testwall = new Entity(test, new Transform(1.5f,1.5f,100,100,1000,1000), "testwall");
        EntityScripts.entityCreate(testwall);

        // adding components
        ArrayList<String> testdep = new ArrayList<>();
        testdep.add("test");
        RectCollider testcol = new RectCollider(0,0,1,1);
        RectCollider colwall = new RectCollider(0,0,1,1, testdep);

        // read scene
        SceneLoader.readScene(new File("assets/sample.lvl"));

//        EntityScripts.getEntityByName("test").transform.setX(0);
//        EntityScripts.getEntityByName("test").transform.setY(0);
//        EntityScripts.getEntityByName("wall_test").transform.setX(1.5f);
//        EntityScripts.getEntityByName("wall_test").transform.setY(1.5f);

        test_ent = EntityScripts.getEntityByName("test");
        wall_test = EntityScripts.getEntityByName("wall_test");
        test_ent.addComponent(testcol);
        wall_test.addComponent(colwall);
        test_ent.transform.rectCollider = testcol;
        wall_test.transform.rectCollider = colwall;
        EntityScripts.initAll();
    }

    @Override
    public void update(float dt) { // TODO: fix problem with dt
        if (KeyListener.isKeyPressed(GLFW_KEY_F)) {
            test_ent.transform.addX(-.100f*dt);
        } else if (KeyListener.isKeyPressed(GLFW_KEY_G)) {
            test_ent.transform.addX(.100f*dt);
        } else if (KeyListener.isKeyPressed(GLFW_KEY_T)) {
            test_ent.transform.addY(.100f*dt);
        } else if (KeyListener.isKeyPressed(GLFW_KEY_V)) {
            test_ent.transform.addY(-.100f*dt);
        } else if (KeyListener.isKeyPressed(GLFW_KEY_K)) {
            System.out.println(test_ent.transform.getX() + " " + test_ent.transform.getY());
        } else if (KeyListener.isKeyPressed(GLFW_KEY_Z)) {
            test_ent.transform.addX(0);
        }
    }
}
