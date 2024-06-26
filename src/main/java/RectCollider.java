public class RectCollider extends ComponentStruct {
    float posXoffset, posYoffset;
    public float posX, posY;
    float sizeX, sizeY;
    public float[] TLdot, TRdot, DLdot, DRdot, Center;
    public float[][] dots;
    public boolean isCollider; // for example: collider = wall (true), collision = player (false)

    public RectCollider(float posXoffset, float posYoffset, float sizeX, float sizeY, boolean isCollider) {
        this.posXoffset = posXoffset;
        this.posYoffset = posYoffset;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.isCollider = isCollider;
        TLdot = new float[] {posX, posY};
        TRdot = new float[] {posX+sizeX, posY};
        DLdot = new float[] {posX, posY+sizeY};
        DRdot = new float[] {posX+sizeX, posY+sizeY};
        Center = new float[] {posX+(sizeX/2), posY+(sizeY/2)};
        dots = new float[][] {TLdot, TRdot, DLdot, DRdot, Center};
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
        dots = new float[][] {TLdot, TRdot, DLdot, DRdot, Center};
    }
}
