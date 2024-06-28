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
//            boolean HorOrVer = true; // true = horizontal, false = vertical
//            boolean blockSide = true; // for x: true = right, false = left for y: true = top, false = down
//            boolean isCollide = false;
//            boolean addY = true;
            float sub_x;
            float sub_y;
            boolean addX = true;
            for (RectCollider rect_col : rc) {
                boolean HorOrVer = false; // true = horizontal, false = vertical
                boolean blockSide = true; // for x: true = right, false = left for y: true = top, false = down
                boolean isCollide = false;
                if (ColliderScripts.isCollide(rect_col, rectCollider) && !rect_col.equals(rectCollider)) {
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
                    System.out.println(3333);
                    if (!blockSide && x > 0 || blockSide && x < 0) {
                        if (addX) {
                            System.out.println(1111);
                            this.x += x;
                            rectCollider.update();
                            addX=false;
                        }
                    }
                    if (ColliderScripts.isCollide(rect_col, rectCollider)) {
                        System.out.println(6666);
                        this.x -= x; // TODO: сделать сдесь адекватное отнятие взависимости от вычисления насколько он вдавился в стену
                        rectCollider.update();
                    }
                } else if (!rect_col.equals(rectCollider)) {
                    if (addX) {
                        System.out.println(2222);
                        this.x += x;
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
                if (ColliderScripts.isCollide(rect_col, rectCollider) && !rect_col.equals(rectCollider)) {
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
                    System.out.println(3333);
                    if (!blockSide && y > 0 || blockSide && y < 0) {
                        if (addY) {
                            System.out.println(1111);
                            this.y += y;
                            rectCollider.update();
                            if (ColliderScripts.isCollide(rect_col, rectCollider)) {
                                System.out.println(6666);
                                this.y -= y*2;
                                rectCollider.update();
                            }
                            addY=false;
                        }
                    }
                } else if (!rect_col.equals(rectCollider)) {
                    if (addY) {
                        System.out.println(2222);
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
