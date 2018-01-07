# closeit
Provides functional interfaces for using lambda expressions as the target of a try-with-resources construct.

Java 7 introduced a useful feature known as the try-with-resources construct. In order to take advantage of it, a class must implement AutoCloseable. However, there are classes that could benefit from this interface that do not implement it. Two that come to mind are `Context` and `ExecutorService`. Although `AutoCloseable` is a functional interface because it implements exactly 1 abstract method, it is often not what is needed as a lambda target because its close method throws `Exception`. The CloseIt interfaces provide a series of generic interfaces that are parameterized with the checked exceptions thrown by the lambda. The `CloseIt` interfaces may be conceptually viewed as an interface named "CloseIt" followed by a number from 0-5 specifying how many checked exceptions are specified as generic class arguments. So, if you lambda throws no checked exceptions, `CloseIt0` is used. If it throws one checked exception, `CloseIt1` is used. Up to five (`CloseIt5`) checked exceptions may be supported in this manner.

**Example 1: Close a Context**
  
    public void useContext(Context ctx) throws NamingException {
        try(CloseIt1<NamingException> it = ctx::close) {
            // Do something
        }
    }

**Example 2: Shutdown an ExecutorService**
  
    public void useExecutorService() {
        ExecutorService es = Executors.newSingleThreadExecutor();
        try(CloseIt0 it = es::shutdown) {
            // Do something
        }
    }
