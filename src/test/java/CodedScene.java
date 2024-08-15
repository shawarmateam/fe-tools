public class CodedScene{public static void ents(){
        Entity e0=new Entity(new RenderTexture("assets/textures/based_tex.png"),new Transform(0,0,20,20,500,500),"test");
        Entity e1=new Entity(new RenderTexture("assets/textures/based_tex.png"),new Transform(0,0,20,20,500,500),"wall_test");
        App.ents.clear();
        App.ents.add(e0);
        App.ents.add(e1);}}