import org.bisqt.Biscuit;
import org.bisqt.BiscuitEx;
import org.bisqt.Variable;
import physic.Timer;

public class BiscuitKernel {
    public static Biscuit conf = new Biscuit();
    public final static Thread thread = new Thread(new Runnable() {
        Biscuit conf;
        public Runnable getConf(Biscuit conf) {
            this.conf = conf;
            return this;
        }

        public void readLines(String lns) {
            try {
                conf.readLines(lns);
            } catch (BiscuitEx e) {
                System.out.println(e.getMessage());
            }
        }

        @Override
        public void run() {
            conf.setVarCallback(BiscuitKernel::varCallback);
            try {
                conf.readScript("/home/adisteyf/IdeaProjects/fe-tools/src/main/_SET_VARS.bsqt");
            } catch (BiscuitEx e) {
                System.out.println(e.getMessage());
            }
        }
    }.getConf(conf));

    public static void startBiscuit() {
        thread.start();
    }

    private static void varCallback(Variable var) {
        switch (var.name) {
            case "fps":
                if (Biscuit.checkType(var.getVal().toString()) == 1) Timer.changeFps((int) var.getVal());
                else System.out.println("WARNING: FPS CANNOT BE NOT INT");
                break;
        }
    }
}
