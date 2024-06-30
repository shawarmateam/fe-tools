import java.util.ArrayList;

public class ColliderScripts extends scripts.CompScriptStructure {
    final static ArrayList<RectCollider> rc = new ArrayList<>();
    public static boolean isCollide(ColliderStructure col_1, ColliderStructure col_2) { // TODO: пофиксить тут проблему (если один квадрат 0, а др. 1 по X и одинакого по Y, то столкновение не читается)
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
