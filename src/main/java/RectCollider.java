import java.util.ArrayList;

public class RectCollider extends ComponentStruct {
    float posXoffset, posYoffset;
    public float posX, posY;
    float sizeX, sizeY;
    public float[] TLdot, TRdot, DLdot, DRdot, Center;
    public boolean isCollider; // for example: collider = wall (true), collision = player (false)
    public ArrayList<String> dep = new ArrayList<>(); // what collider will collide /w collision

    public RectCollider(float posXoffset, float posYoffset, float sizeX, float sizeY, ArrayList<String> dep) {
        this.posXoffset = posXoffset;
        this.posYoffset = posYoffset;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.dep = !dep.isEmpty() ? dep : null;
        this.isCollider = dep.isEmpty();
        TLdot = new float[] {posX, posY};
        TRdot = new float[] {posX+sizeX, posY};
        DLdot = new float[] {posX, posY+sizeY};
        DRdot = new float[] {posX+sizeX, posY+sizeY};
        Center = new float[] {posX+(sizeX/2), posY+(sizeY/2)};
    }

    public RectCollider(float posXoffset, float posYoffset, float sizeX, float sizeY) {
        this.posXoffset = posXoffset;
        this.posYoffset = posYoffset;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.isCollider = true;
        TLdot = new float[] {posX, posY};
        TRdot = new float[] {posX+sizeX, posY};
        DLdot = new float[] {posX, posY+sizeY};
        DRdot = new float[] {posX+sizeX, posY+sizeY};
        Center = new float[] {posX+(sizeX/2), posY+(sizeY/2)};
    }

    @Override
    public void update() {
        posX = ent.transform.getX() + posXoffset;
        posY = ent.transform.getY() + posYoffset;

        TLdot = new float[] {posX, posY};
        TRdot = new float[] {posX+sizeX, posY};
        DLdot = new float[] {posX, posY+sizeY};
        DRdot = new float[] {posX+sizeX, posY+sizeY};
        Center = new float[] {posX+(sizeX/2), posY+(sizeY/2)};
    }

    public ColliderStructure getColliderStruct() {
        return new ColliderStructure(TLdot, TRdot, DLdot, DRdot, Center);
    }
}
