public class ColliderScripts extends scripts.CompScriptStructure {
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

    @Override
    public void updateAll() {
        for (Entity ent : App.ents) {
            if (ent.transform.rectCollider != null) {
                ent.transform.rectCollider.update();
            }
        }
    }

    public static RectCollider getRectColliderByEntity(Entity ent) {
        return ent.getComponent(RectCollider.class);
    }
}
