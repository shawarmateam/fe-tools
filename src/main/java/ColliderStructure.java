public class ColliderStructure {
    public float[] TLdot, TRdot, DLdot, DRdot, Center;
    public ColliderStructure(float[] TLdot, float[] TRdot, float[] DLdot, float[] DRdot, float[] Center) {
        this.TLdot = TLdot;
        this.TRdot = TRdot;
        this.DLdot = DLdot;
        this.DRdot = DRdot;
        this.Center = Center;
    }

    public void mvAllx(float x) {
        this.TLdot[0] += x;
        this.TRdot[0] += x;
        this.DLdot[0] += x;
        this.DRdot[0] += x;
        this.Center[0] += x;
    }
}
