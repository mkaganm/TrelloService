package core.base;

public class ServiceCommons extends ServiceBase {

    @Deprecated
    public static final ServiceCommons core = new ServiceCommons();

    private static final ThreadLocal<ServiceCommons> coreTl = new ThreadLocal<>();

    public static ServiceCommons getCore() {
        if (coreTl.get() == null) coreTl.set(new ServiceCommons());
        return coreTl.get();
    }


}
