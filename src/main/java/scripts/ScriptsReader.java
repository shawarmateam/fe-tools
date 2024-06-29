package scripts;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;

public class ScriptsReader {
    private ArrayList<String> SCRs;
    private static boolean hasStarted = false;
    final ArrayList<Object> SCRsCLS = new ArrayList<>();
    public ScriptsReader() {
        SCRs = readScripts();
        try {
            runStartInSCRs(SCRs);
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
            System.out.println("ERROR DURING runStartInSCRs(SCRs);\n");
            e.printStackTrace();
        }
    }

    private static ArrayList<String> readScripts() {
        JSONParser parser = new JSONParser();
        try (FileReader r = new FileReader("assets/scripts.json")) {
            Object obj = parser.parse(r);
            JSONObject list = (JSONObject) obj;
            return (ArrayList<String>) list.get("SCRs");
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static ArrayList<String> readComponents() {
        JSONParser parser = new JSONParser();
        try (FileReader r = new FileReader("assets/scripts.json")) {
            Object obj = parser.parse(r);
            JSONObject list = (JSONObject) obj;
            return (ArrayList<String>) list.get("Comps");
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void runStartInSCRs(ArrayList<String> SCRs) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        if (hasStarted) {
            System.out.println("YOU CAN START ONLY ONCE!");
        } else {
            for (int i = 0; i < SCRs.size(); i++) {
                Class<FilesScripts> FileClass = (Class<FilesScripts>) Class.forName(SCRs.get(i));
                SCRsCLS.add(FileClass.getDeclaredConstructor().newInstance());
                Method start = FileClass.getDeclaredMethod("start");
                start.invoke(SCRsCLS.get(i));
            }
        }
    }
    public void runUpdateInSCRs(float dt) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        //ArrayList<String> SCRs = readScripts();
        ArrayList<String> Comps = readComponents();

        for (String comp : Comps) {
            Class<CompScriptStructure> FileClass = (Class<CompScriptStructure>) Class.forName(comp);
            Method updateAll = FileClass.getDeclaredMethod("updateAll");
            Object t = FileClass.getDeclaredConstructor().newInstance();
            updateAll.invoke(t);
        }

        for (int i = 0; i < SCRsCLS.size(); i++) {
            Class<FilesScripts> FileClass = (Class<FilesScripts>) Class.forName(SCRs.get(i));
            Method update = FileClass.getDeclaredMethod("update", float.class);
            update.invoke(SCRsCLS.get(i), dt);
        }
    }
}
