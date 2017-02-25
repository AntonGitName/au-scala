/**
 * @author antonpp
 * @since 25/02/2017
 */
public class InhHolder implements Runnable {

    public static InhHolder instance;

    private InhHolder() {
        instance = this;
    }

    static {
        new InhHolder();
    }

    public void run() {
    }

    private int foo() {
        return 4;
    }
}
