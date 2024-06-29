import java.lang.Math;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Transform {
    private float x, y;
    private float x_limit, y_limit;
    public float sizeX, sizeY;
    public RectCollider rectCollider = null;
    ArrayList<RectCollider> rc = new ArrayList<>();
    public Transform(float x, float y, float sizeX, float sizeY, float x_limit, float y_limit) {
        this.x = x;
        this.y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.x_limit = x_limit;
        this.y_limit = y_limit;

        for (Entity ent_in_gp : App.ents) {
            if (ent_in_gp.getComponent(RectCollider.class) != null) {
                rc.add(ent_in_gp.getComponent(RectCollider.class));
            }
        }
    }

    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
    public void setX(float x) {
        if (x < x_limit)
            this.x = x;
    }
    public void setY(float y) {
        if (y < y_limit)
            this.y = y;
    }

    public void addX(float x) {
        if (this.x+x < x_limit && rectCollider == null)
            this.x += x;
        else if (rectCollider != null) {
            float sub_x;
            float sub_y;
            boolean addX = true;
            for (RectCollider rect_col : rc) {
                boolean HorOrVer = false; // true = horizontal, false = vertical
                boolean blockSide = true; // for x: true = right, false = left for y: true = top, false = down
                boolean isCollide = false;
                if (ColliderScripts.isCollide(rect_col.getColliderStruct(), rectCollider.getColliderStruct()) && !rect_col.equals(rectCollider)) {
                    isCollide=true;
                    sub_x = rect_col.posX - rectCollider.posX;
                    sub_y = rect_col.posY - rectCollider.posY;
                    if (Math.abs(sub_x) >= Math.abs(sub_y)) {
                        HorOrVer = true;
                        if (sub_x > 0)
                            blockSide = true;
                        if (sub_x < 0)
                            blockSide = false;
                    }
                    if (Math.abs(sub_x) < Math.abs(sub_y)) {
                        HorOrVer = false;
                    }
                    if (!blockSide && x > 0 || blockSide && x < 0) {
                        if (addX) {
                            //this.x += x;
                            rectCollider.update();
                            addX=false;
                        }
                    }
                    ColliderStructure check_collider = new ColliderStructure(rectCollider.TLdot, rectCollider.TRdot, rectCollider.DLdot, rectCollider.DRdot, rectCollider.Center);
                    if (x > 0) {
                        check_collider.mvAllx(x);
                        System.out.println(11);
                    }
                    else {
                        check_collider.mvAllx(-x);
                        System.out.println(12);
                    }
                    if (ColliderScripts.isCollide(rect_col.getColliderStruct(), check_collider)) {
                        if (!blockSide && HorOrVer) {
                            this.x += rect_col.TRdot[0] - rectCollider.posX;
                            System.out.println(13);
                        }
                        else if (blockSide && HorOrVer) {
                            this.x -= rect_col.TRdot[0] - rectCollider.posX;
                            System.out.println(14);
                        }
                        rectCollider.update();
                    } else {
                        if (addX) {
                            this.x += x;
                            System.out.println(15);
                            addX = false;
                        }
                    }
                } else if (!rect_col.equals(rectCollider)) {
                    if (addX) {
                        this.x += x;
                        System.out.println(16);
                        addX=false;
                    }
                }
            }
        }
    }
    public void addY(float y) {
        if (this.y+y < y_limit && rectCollider == null)
            this.y += y;
        else if (rectCollider != null) {
//            boolean HorOrVer = true; // true = horizontal, false = vertical
//            boolean blockSide = true; // for x: true = right, false = left for y: true = top, false = down
//            boolean isCollide = false;
//            boolean addY = true;
            float sub_x;
            float sub_y;
            boolean addY = true;
            for (RectCollider rect_col : rc) {
                boolean HorOrVer = false; // true = horizontal, false = vertical
                boolean blockSide = true; // for x: true = right, false = left for y: true = top, false = down
                boolean isCollide = false;
                if (ColliderScripts.isCollide(rect_col.getColliderStruct(), rectCollider.getColliderStruct()) && !rect_col.equals(rectCollider)) {
                    isCollide=true;
                    sub_x = rect_col.posX - rectCollider.posX;
                    sub_y = rect_col.posY - rectCollider.posY;
                    if (Math.abs(sub_x) >= Math.abs(sub_y)) {
                        HorOrVer = true;
                    }
                    if (Math.abs(sub_x) < Math.abs(sub_y)) {
                        HorOrVer = false;
                        if (sub_y > 0)
                            blockSide = true;
                        if (sub_y < 0)
                            blockSide = false;
                    }
                    if (!blockSide && y > 0 || blockSide && y < 0) {
                        if (addY) {
                            this.y += y;
                            rectCollider.update();
                            addY=false;
                        }
                    }
                    if (ColliderScripts.isCollide(rect_col.getColliderStruct(), rectCollider.getColliderStruct())) {
                        this.y -= y;
                        rectCollider.update();
                    }
                } else if (!rect_col.equals(rectCollider)) {
                    if (addY) {
                        this.y += y;
                        addY=false;
                    }
                }
            }
        }
    }
    public void init() {
        rc.clear();
        for (Entity ent : App.ents) {
            if (ent.getComponent(RectCollider.class) != null) {
                rc.add(ent.getComponent(RectCollider.class));
            }
        }
    }
}
