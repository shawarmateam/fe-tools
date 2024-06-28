import java.util.ArrayList;

public class ColliderScripts extends scripts.CompScriptStructure {
    final static ArrayList<RectCollider> rc = new ArrayList<>();
    public static boolean isCollide(ColliderStructure col_1, ColliderStructure col_2) {
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
                rc.add(ent.getComponent(RectCollider.class));
            }
        }
    }

    public static short whatSideIsCollide(RectCollider collision, RectCollider collider) { // 0 = left, 1 = right, 2 = top, 3 = bottom, -1 = null
        boolean HorOrVer = true;
        boolean blockSide = false;
        float sub_x;
        float sub_y;
        if (ColliderScripts.isCollide(collision.getColliderStruct(), collider.getColliderStruct()) && !collision.equals(collider) && collider.isCollider && !collision.isCollider) {
            sub_x = collision.posX - collider.posX;
            sub_y = collision.posY - collider.posY;
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
                for (RectCollider rect_col : rc) {
                    if (!rect_col.isCollider) {
                        for (String ent_col : rect_col.dep) {
                            //RectCollider collider = EntityScripts.getEntityByName(ent_col).getComponent(RectCollider.class);
//                            switch (whatSideIsCollide(rect_col, EntityScripts.getEntityByName(ent_col).getComponent(RectCollider.class))) {
//                                case -1:
//                                    System.out.println(1);
//                                    break;
//                                case 0:
////                                    EntityScripts.getEntityByName(ent_col).transform.addX(-(EntityScripts.getEntityByName(ent_col).getComponent(RectCollider.class).TRdot[0]-rect_col.posX));
//                                    System.out.println(2+" "+EntityScripts.getEntityByName(ent_col).getComponent(RectCollider.class).TRdot[0]+"+"+rect_col.posX+"="+(EntityScripts.getEntityByName(ent_col).getComponent(RectCollider.class).TRdot[0]-rect_col.posX));
//                                    break;
//                                case 1:
////                                    EntityScripts.getEntityByName(ent_col).transform.addX(.1f);
//                                    System.out.println(3);
//                                    break;
//                                case 2:
//                                    EntityScripts.getEntityByName(ent_col).transform.addY(-(EntityScripts.getEntityByName(ent_col).getComponent(RectCollider.class).DLdot[1]-rect_col.posY));
//                                    System.out.println(4+" "+-(EntityScripts.getEntityByName(ent_col).getComponent(RectCollider.class).DLdot[1]-rect_col.posY));
//                                    break;
//                                case 3:
//                                    EntityScripts.getEntityByName(ent_col).transform.addY(.1f);
//                                    System.out.println(5);
//                                    break; // TODO: сделать ограничение на передвижение. пока они касаются отнимать.прибавлять 0.001
//                            }
                        }
                    }
                }
                ent.transform.rectCollider.update();
            }
        }
    }

    public static RectCollider getRectColliderByEntity(Entity ent) {
        return ent.getComponent(RectCollider.class);
    }
}
