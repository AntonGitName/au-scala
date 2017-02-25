/**
 * @author antonpp
 * @since 25/02/2017
 */
public class Inh extends Base {

    public static void run() {
        InhHolder.instance.run();
    }

    public int goo() {
        return Base.goo(this);
    }

    public int x() {
        return x;
    }

    public void x_setter(int newX) {
        this.x = newX;
    }

    private int y_lazy_getter() {
        synchronized (this) {
            if (!bitmap$0) {
                y = Base.y(this);
                bitmap$0 = true;
            }
        }
        return y;
    }

    public int y() {
        return bitmap$0 ? y : y_lazy_getter();
    }

    public int foo() {
        return 3;
    }

    public Inh() {
        Base.init(this);
    }

    private int x;
    private int y;
    private volatile boolean bitmap$0;
}
