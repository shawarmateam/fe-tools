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
            boolean addX = true;
            for (RectCollider rect_col : rc) {
                ColliderStructure check_collider = new ColliderStructure(rectCollider.TLdot, rectCollider.TRdot, rectCollider.DLdot, rectCollider.DRdot, rectCollider.Center);
                check_collider.mvAllx(x);

                if (rectCollider.isCollider && !rect_col.isCollider && ColliderScripts.isCollide(check_collider, rect_col.getColliderStruct())) {
                    switch (whatSideIsCollide(check_collider, rect_col.getColliderStruct())) {
                        case -1, 2, 3:
                            break;
                        case 0:
                            addX=false;
                            rectCollider.update();
                            this.x += (rect_col.posX-rectCollider.TRdot[0]);
                            break;
                        case 1:
                            addX=false;
                            rectCollider.update();
                            this.x -= (rectCollider.posX-rect_col.TRdot[0]);
                            break;
                    }
                } else if (addX && rectCollider.isCollider && !rect_col.isCollider) {
                    this.x += x;
                    addX=false;
                }
            }
        }
    }
    public void addY(float y) {
        if (this.y+y < y_limit && rectCollider == null)
            this.y += y;
        else if (rectCollider != null) {
            boolean addY = true;
            for (RectCollider rect_col : rc) {
                ColliderStructure check_collider = new ColliderStructure(rectCollider.TLdot, rectCollider.TRdot, rectCollider.DLdot, rectCollider.DRdot, rectCollider.Center);
                check_collider.mvAlly(y);

                if (rectCollider.isCollider && !rect_col.isCollider && ColliderScripts.isCollide(check_collider, rect_col.getColliderStruct())) {
                    switch (whatSideIsCollide(check_collider, rect_col.getColliderStruct())) {
                        case -1, 0, 1:
                            break;
                        case 2:
                            addY=false;
                            rectCollider.update();
                            this.y -= (rectCollider.posY-rect_col.DRdot[1]);
                            break;
                        case 3:
                            addY=false;
                            rectCollider.update();
                            this.y += (rect_col.posY-rectCollider.DRdot[1]);
                            break;
                    }
                } else if (addY && rectCollider.isCollider && !rect_col.isCollider) {
                    this.y += y;
                    addY=false;
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

    public static short whatSideIsCollide(ColliderStructure collision, ColliderStructure collider) { // 0 = left, 1 = right, 2 = top, 3 = bottom, -1 = null
        boolean HorOrVer = true;
        boolean blockSide = false;
        float sub_x;
        float sub_y;
        if (ColliderScripts.isCollide(collision, collider) && !collision.equals(collider)) {
            sub_x = collision.TLdot[0] - collider.TLdot[0];
            sub_y = collision.TLdot[1] - collider.TLdot[1];
            if (Math.abs(sub_x) > Math.abs(sub_y)) {
                HorOrVer = true;
                if (sub_x > 0)
                    blockSide = true;
                if (sub_x < 0)
                    blockSide = false;
            }
            if (Math.abs(sub_x) <= Math.abs(sub_y)) {
                HorOrVer = false;
                if (sub_y > 0)
                    blockSide = true;
                if (sub_y < 0)
                    blockSide = false;
            }
            if (HorOrVer && !blockSide)
                return 0;
            if (HorOrVer && blockSide)
                return 1;
            if (!HorOrVer && !blockSide)
                return 3;
            if (!HorOrVer && blockSide)
                return 2;
        }
        return -1;
    }
}
