/**
 * @author antonpp
 * @since 25/02/2017
 */
public class InhHolder implements Runnable {

    public static final InhHolder instance;

    private InhHolder() {}

    static {
        instance = new InhHolder();
    }

    public void run() {
    }

    private int foo() {
        return 4;
    }
}
