/**
 * @author antonpp
 * @since 25/02/2017
 */
public abstract class Base {


    public abstract int x();
    public abstract void x_setter(int x);

    public int y() {
        return goo();
    }

    public abstract int foo();
    public int goo() {
        return x() + 2;
    }

    static void init(Base base) {
        base.x_setter(1);
    }

    static int y(Base base) {
        return base.y();
    }

    static int goo(Base base) {
        return base.goo();
    }



}
