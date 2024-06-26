import java.util.ArrayList;

public class ColliderScripts extends scripts.CompScriptStructure {
    final static ArrayList<RectCollider> rc = new ArrayList<>();
    public static boolean isCollide(RectCollider col_1, RectCollider col_2) {
        if (col_1.TLdot[0] < col_2.TLdot[0] &&
            col_1.TRdot[0] > col_2.TLdot[0] &&
            col_1.TLdot[1] < col_2.DLdot[1] &&
            col_1.DLdot[1] > col_2.TLdot[1])
        {
            return true;
        } else if (col_2.TLdot[0] < col_1.TLdot[0] &&
                col_2.TRdot[0] > col_1.TLdot[0] &&
                col_2.TLdot[1] < col_1.DLdot[1] &&
                col_2.DLdot[1] > col_1.TLdot[1])
            {
                return true;
            }
        return false;
    }

    private void init() {
        rc.clear();
        for (Entity ent : App.ents) {
            if (ent.getComponent(RectCollider.class) != null) {
                System.out.println("name: "+ent.name);
                rc.add(ent.getComponent(RectCollider.class));
            }
        }
    }

    public static short whatSideIsCollide(RectCollider col_1, RectCollider col_2) { // 0 = left, 1 = right, 2 = top, 3 = bottom, -1 = null
        boolean HorOrVer = true;
        boolean blockSide = false;
        float sub_x;
        float sub_y;
        if (ColliderScripts.isCollide(col_1, col_2) && !col_1.equals(col_2) && col_2.isCollider && !col_1.isCollider) {
            sub_x = col_1.posX - col_2.posX;
            sub_y = col_1.posY - col_2.posY;
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

    @Override
    public void updateAll() {
        for (Entity ent : App.ents) {
            if (ent.transform.rectCollider != null) {
                init();
                ent.transform.rectCollider.update();
            }
        }
    }

    public static RectCollider getRectColliderByEntity(Entity ent) {
        return ent.getComponent(RectCollider.class);
    }
}
