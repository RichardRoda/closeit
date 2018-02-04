# Closeit
Provides functional interfaces for using lambda expressions as the target of a try-with-resources construct.

Java 7 introduced a useful feature known as the `try-with-resources` construct. In order to take advantage of it, a class must implement `AutoCloseable`. However, there are classes that could benefit from this interface that do not implement it. Two that come to mind are `Context` and `ExecutorService`. Although `AutoCloseable` is a functional interface because it implements exactly 1 abstract method, it is often not what is needed as a lambda target because `AutoCloseable::close` throws `Exception`. 

**Example 1: Close a Context With AutoCloseable**
  
    public void useContext(Context ctx) throws Exception {
        try(AutoCloseable it = ctx::close) {
            doSomethingWithContext(ctx);
        }
    }

The CloseIt interfaces provide a series of generic interfaces that are parameterized with the checked exceptions thrown by the lambda. The CloseIt interfaces may be conceptually viewed as an interface named "CloseIt" followed by a number from 0-5 specifying how many checked exceptions are specified as generic class arguments. So, if you lambda throws no checked exceptions, `CloseIt0` is used. If it throws one checked exception, `CloseIt1` is used. Up to five (`CloseIt5`) checked exceptions may be supported in this manner.

**Example 2: Close a Context with CloseIt**
  
    public void useContext(Context ctx) throws NamingException {
        try(CloseIt1<NamingException> it = ctx::close) {
            doSomethingWithContext(ctx);
        }
    }

**Example 3: Shutdown an ExecutorService with CloseIt**
  
    public void useExecutorService() {
        ExecutorService es = Executors.newSingleThreadExecutor();
        try(CloseIt0 it = es::shutdown) {
            doSomethingWithExecutorService(es);
        }
    }

## Closeit as a finally replacement

The CloseIt interfaces with a lambda expression may be used instead of a `finally` block.  There are good reasons for doing so.  The `try-with-resources` feature was created because exceptions that occur when closing resources within a finally block can interfere with the processing of other resources, and exceptions within the finally block can conceal exceptions within the try block.  Concealing exceptions within the try block is particularly problematic because the exceptions in the finally block are often a result of the bad state that caused an exception in the try block in the first place.  By concealing the try block exceptions, the root cause analysis of the failure becomes more difficult.  the `try-with-resources` feature gives an easy way to express a set of resources to be closed in order, with exceptions that occur properly suppressed into a superseding exception when a superseding exception has occurred.

The above issues of exception hiding and execution interference can exist with *any* finally block, not just resource blocks.  Any method called within a finally block may throw an exception or error.  The lack of a `throws` clause is no guarantee that a `RuntimeException` will not be thrown by a given method call.  For this reason, a project may consider replacing `finally` blocks with `try-with-resources` lambdas.  Here is an example of code that has finally clause issues.

**Example 4: Problematic Finally block**

    public void useContextAndExecutorService(Context ctx, ExecutorService es) throws NamingException, InterruptedException {
        try {
            doSomething(ctx, es);
        } finally {
            es.shutdown();
            es.awaitTermination(1, TimeUnit.DAYS);
            ctx.close();
        }
    }

There are clearly issues with these 7 lines of code.  If the call to `shutdown` or `awaitTermination` throws an exception, `close` is never called.  Also, any exception in the `finally` block will discard an exception from the `try` block.  We want to close all resources, and suppress any exceptions that occur in the `finally` clause while throwing the original exception.

**Example 5: Handle and Suppress all Exceptions**

    public void useContextAndExecutorService1(Context ctx, ExecutorService es) throws NamingException, InterruptedException {
        Throwable originalException = null;
        try {
            doSomething(ctx, es);
        } catch (Throwable ex) {
            originalException = ex;
        } finally {
            try {
                es.shutdown();
                es.awaitTermination(1, TimeUnit.DAYS);
            } catch (Throwable ex) {
                originalException = processException(originalException, ex);
            } finally {
                try {
                    ctx.close();
                } catch (Throwable ex) {
                    originalException = processException(originalException, ex);
                }
            }
        }
        if (originalException != null) {
            try {
                throw originalException;
            } catch (RuntimeException | NamingException | InterruptedException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new IllegalStateException(ex); // Should never happen.
            }
        }
    }

    private Throwable processException(Throwable originalException, Throwable ex) {
        if (originalException == null) {
            originalException = ex;
        } else {
            originalException.addSuppressed(ex);
        }
        return originalException;
    }

Ugh.  This code hurts to read.  Assuming the `processException` method would be imported from a common library, it has taken 28 lines of code to use and properly close two resources.  More resources would result in correspondingly more nested `try-catch-finally` blocks to process the exceptions and then call the next resource to be closed.  Even worse, there is no compile time enforcement that the list of exceptions in the `multi-catch` will list all of the exceptions in the `throws` clause, or the checked exceptions that are actually thrown.  Any new or omitted checked exceptions may end up inadvertently nested in the `IllegalStateException` thrown when an exception doesn't match any of the `multi-catch` exceptions.

**Example 6: Rewrite using CloseIt**

    public void useContextAndExecutorService(Context ctx, ExecutorService es) throws NamingException, InterruptedException {
        try (CloseIt1<NamingException> thing1 = ctx::close;
             CloseIt1<InterruptedException> thing2 = ()-> {
                 es.shutdown();
                 es.awaitTermination(1, TimeUnit.DAYS);}) 
        {
            doSomething(ctx, es);
        }
    }

Ah, much better.  What took 28 lines to accomplish above has been condensed back to 7 lines like the original `try` and `finally` blocks.  It is easier to see that the code is cleaning up two resources that it uses in the `try` block.  Substituting the `finally` clause with the `try-with-resourses` lets the compiler and Java run time deal with the ugly details of nesting and re-throwing the exceptions, while the programmer can focus on the problem at hand. Unlike the code in Example 5, there is compile time checking of all the exceptions that are thrown within the `try-with-resources` construct.  

There is a trick to converting `finally` blocks to `try-with-resources`.  The lambdas must be declared in the *opposite* order they are closed or cleaned up.  Note that `ctx::close` is declared first, but will be executed last.  When reasoning about this, you may think of the lambdas as being like code blocks, the inner code block will exit first, followed by the outer code block.  Likewise the "inner" cleanup lambda code will run before the "outer" lambda cleanup code.

Some static source code (SSC) security analyzers consider any use of multiple objects within the same `finally` statement a bad exception handling issue.  They require that subsequent object use be nested in a `try-finally` construct under the first object usage.  Some SSC analyzers will also flag `finally` clauses that do not catch and discard their exceptions (a bad practice in and of itself) to be a bad exception handling issue.  This framework may be used to quickly remediate such issues without necessitating an ugly rewrite.