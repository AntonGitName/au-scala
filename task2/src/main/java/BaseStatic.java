/**
 * @author antonpp
 * @since 25/02/2017
 */
public class BaseStatic {
    public static void init(Base base) {
        base.x_setter(1);
    }

    public static int y(Base base) {
        return base.goo();
    }

    public static int goo(Base base) {
        return base.x() + 2;
    }
}
