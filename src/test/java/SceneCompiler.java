import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;

public class SceneCompiler {
    public static ArrayList<Entity> readScene(String scene_path) {
        JSONParser jsonParser = new JSONParser();
        Integer[] limit;
        ArrayList<Entity> ents = new ArrayList<>();

        try (FileReader reader = new FileReader(scene_path)) {
            Object obj = jsonParser.parse(reader);
            JSONObject jsonObj = (JSONObject) obj;

            limit = (Integer[]) jsonObj.get("limit");
            for (Object key : jsonObj.keySet()) {
                String name = (String) key;
                JSONObject ent = (JSONObject) jsonObj.get(key);

                Integer[] pos = (Integer[]) ent.get("pos");
                Integer[] size = (Integer[]) ent.get("size");
                String texture_path = (String) ent.get("texture_path");
                ents.add(
                        new Entity(
                                new RenderTexture(texture_path),
                                new Transform(pos[0], pos[1], size[0], size[1], limit[0], limit[1]),
                                name
                        )
                );
            }

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        return ents;
    }

    public static void runScene(String scene_path) {
        App.ents.clear();
        SceneManager.ents.clear();
        ArrayList<Entity> ents = readScene(scene_path);
        App.ents.addAll(ents);
        SceneManager.ents.addAll(ents);
    }

    public static void compileScene(String scene_path, String output_name) {
        ArrayList<Entity> ents = readScene(scene_path);
        String ent_pattern = "Entity e{0}=new Entity(new RenderTexture(\"{1}\"),new Transform({2},{3},{4},{5},{6},{7}),{8})";
        StringBuilder sceneCode = new StringBuilder();
        sceneCode.append("public class "+output_name+"{public static void ents(){");
        for (int i=0; i<ents.size(); ++i) {
            sceneCode.append(MessageFormat.format(ent_pattern, i, ents.get(i).texture.texture_path,
                    ents.get(i).transform.getX(), ents.get(i).transform.getY(),
                    ents.get(i).transform.sizeX, ents.get(i).transform.sizeY,
                    ents.get(i).transform.getXlimit(), ents.get(i).transform.getYlimit(),
                    ents.get(i).name));
        }
        sceneCode.append("App.ents.clear();");
        for (int i = 0; i < ents.size(); i++) {
            sceneCode.append("App.ents.add(e"+i+");");
        }
        sceneCode.append("}}");
    }
}
