# CloseIt #
Provides functional interfaces for using lambda expressions as the target of a `try-with-resources` construct.

This project is available from Maven Central Repository.  To use it, add the following dependency:
```xml
<dependency>
    <groupId>com.github.richardroda.util</groupId>
    <artifactId>closeit</artifactId>
    <version>1.4</version>
</dependency>
```
Use this dependency for Gradle

	compile "com.github.richardroda.util:closeit:1.4"

Java 7 introduced a useful feature known as the `try-with-resources` construct. In order to take advantage of it, a class must implement `AutoCloseable`. However, there are classes that could benefit from this interface that do not implement it. Two examples are `Context` and `ExecutorService`. Although `AutoCloseable` is a functional interface because it implements exactly 1 abstract method, it is often not what is needed as a lambda target because `AutoCloseable::close` throws `Exception`. 

**Example 1: Close a Context With AutoCloseable**
```java  
public void useContext(Context ctx) throws Exception {
    try(AutoCloseable it = ctx::close) {
        doSomethingWithContext(ctx);
    }
}
```

The CloseIt interfaces provide a series of generic interfaces that are parameterized with the checked exceptions thrown by the lambda. The CloseIt interfaces may be conceptually viewed as an interface named "CloseIt" followed by a number from 0-5 specifying how many checked exceptions are specified as generic class arguments. So, if you lambda throws no checked exceptions, `CloseIt0` is used. If it throws one checked exception, `CloseIt1` is used. Up to five (`CloseIt5`) checked exceptions may be supported in this manner.

**Example 2: Shutdown an ExecutorService with CloseIt**
```java    
import com.github.richardroda.util.closeit.*;
...
public void useExecutorService() {
    ExecutorService es = Executors.newSingleThreadExecutor();
    try(CloseIt0 it = es::shutdown) {
        doSomethingWithExecutorService(es);
    }
}
```
**Example 3: Close a Context with CloseIt**
```java  
import com.github.richardroda.util.closeit.*;
...
public void useContext(Context ctx) throws NamingException {
    try(CloseIt1<NamingException> it = ctx::close) {
        doSomethingWithContext(ctx);
    }
}
```
**Example 4: Close Multiple Nested - LDAP Search**

This example shows how to define multiple `CloseIt` lambdas in a single `try-with-resources` construct, when an outer resource must be closed when the creation of an inner resource fails.
```java
import com.github.richardroda.util.closeit.*;
...
public void searchLdap(DirContext ctx) throws NamingException {
    NamingEnumeration<SearchResult> searchResult;
    try (CloseIt1<NamingException> outer = ctx::close;
         CloseIt1<NamingException> inner = 
            (searchResult = ctx.search(CRITERIA, ATTRIBUTES))::close) {
        doSomethingWithSearchResult(searchResult);
    }
}
```
The compiler allows the assignment to `searchResult` because the assignment occurs outside the lambda definition.  It is the *result* of the assignment that forms the lambda expression with the close method.

**Example 5: Consume Exceptions that Occur Within the Close Method**

This example shows how to consume an exception (both checked an unchecked) that occurs within the close method.  The consumed exception is not re-thrown.  In the case of  consuming an exception, the exception is not wrapped: the consumer gets the actual  exception thrown by the `close()` method.  There is also a `CloseIt0.consumeException` method which only consumes checked exceptions, and a `CloseIt0.consumeAllThrowable` which consumes all throwables.

```java
import com.github.richardroda.util.closeit.*;
...
public void useContext(Context ctx) {
    try(CloseIt0 it = CloseIt0.consumeAllException(ctx::close, 
            exception->logger.log(Level.WARNING, exception.getMessage(), exception))) {
        doSomethingWithContext(ctx); // Throws no checked exceptions.
    }
}
```
This technique also works with "traditional" `AutoCloseable` classes.

```java
import com.github.richardroda.util.closeit.*;
...
public void queryDatabase(Connection con) throws SQLException {
    Statement stmt;
    ResultSet rs;
    try (CloseIt0 c1 = CloseIt0.consumeAllException(con, 
         exception->logger.log(Level.WARNING, exception.getMessage(), exception));
            CloseIt0 c2 = CloseIt0.consumeAllException(stmt = con.createStatement(), 
            exception->logger.log(Level.WARNING, exception.getMessage(), exception));
                CloseIt0 c3 = CloseIt0.consumeAllException(rs = stmt.executeQuery("select * from foo"), 
                exception->logger.log(Level.WARNING, exception.getMessage(), exception))) {
        processResultSet(rs);
    }
}
```

Note: There is an important difference between the processing illustrated here with consume exception, and with catching a `NotClosedException` in Example 8.  In the queryDatabase example above, the close exception will be consumed and logged regardless of the context it is thrown in.  Thus, there will be 0-3 logging statements corresponding to each close method that throws an exception.  In Example 8, 0 or 1 `NotClosedException` are caught and they are not always caught when an exception occurs in a `close()` method.

**Example 6: Ignore Checked Exceptions that Occur Within the Close Method**

This example shows how to ignore a checked exception that occurs within a close method.  There is also a `CloseIt0.ignoreAllException` which ignores all exceptions (checked and unchecked) and `CloseIt0.ignoreAllThrowable` which ignores all throwables.  As with Example 5, this technique also works with "traditional" `AutoClosable` classes.

```java
import com.github.richardroda.util.closeit.*;
...
public void useContext(Context ctx) {
    try(CloseIt0 it = CloseIt0.ignoreException(ctx::close)) {
        doSomethingWithContext(ctx); // Throws no checked exceptions.
    }
}
```

**Example 7: Wrap a Checked Exception from the Close Method**

This example shows how to wrap a checked exception from the `close` method into the unchecked `NotClosedException`.  `CloseIt0.wrapException` only wraps checked exceptions.  Note: if a checked exception occurs within the body of the `try-with-resources` block, the `close` exception, if one occurs, will be a suppressed exception and will be wrapped.

```java
import com.github.richardroda.util.closeit.*;
...
public void useContext(Context ctx) {
    try(CloseIt0 it = CloseIt0.wrapException(ctx::close)) {
        doSomethingWithContext(ctx); // Throws no checked exceptions.
    }
}
```

There are also `CloseIt0.toCloseIt0`, `CloseIt0.toCloseIt0AllException`, and `CloseIt0.toCloseIt0AllThrowable` that wrap checked exceptions, all exceptions, and all throwables using a user supplied function.  These are useful when a different unchecked exception other than `NotClosedException` is required.  The following wraps a checked exception in an `IllegalStateException`.

```java
import com.github.richardroda.util.closeit.*;
...
public void useContext(Context ctx) {
    try(CloseIt0 it = CloseIt0.toCloseIt0(ctx::close, IllegalStateException::new)) {
        doSomethingWithContext(ctx); // Throws no checked exceptions.
    }
}
```

**Example 8: Catch and Process Exceptions That Occur Within the Close Method**

This example shows how to catch any exceptions that occur within the `close` method when an exception does not occur within the `try-with-resources` block.  `CloseIt0.wrapAllException` wraps all exceptions, both checked and unchecked, that occur within the `close` method.  This allows for attaching a catch clause to the `try-with-resources` block to catch a failed `close` call when there is no exception within the `try-with-resources` block. As above, if an exception occurs in the `try-with-resources` block and the `close` method, the `close` exception will be a suppressed exception that is wrapped within a `NotClosedException`.  There is also a `CloseIt0.wrapException` that only wraps checked exceptions, and `CloseIt0.wrapAllThrowable` which wraps any throwable.

```java
import com.github.richardroda.util.closeit.*;
...
public void useContext(Context ctx) throws NamingException {
    try(CloseIt0 it = CloseIt0.wrapAllException(ctx::close)) {
        doSomethingWithContext(ctx);
    } catch (NotClosedException ex) {
        logger.log(Level.WARNING, ex.getCause().getMessage(), ex.getCause());
    }
}
```

This technique also works with "traditional" `AutoCloseable` classes.

```java
import com.github.richardroda.util.closeit.*;
...
public void queryDatabase(Connection con) throws SQLException {
    Statement stmt;
    ResultSet rs;
    try (CloseIt0 c1 = CloseIt0.wrapAllException(con);
            CloseIt0 c2 = CloseIt0.wrapAllException(stmt = con.createStatement());
                CloseIt0 c3 = CloseIt0.wrapAllException(rs = stmt.executeQuery("select * from foo"))) {
        processResultSet(rs);
    } catch (NotClosedException ex) {
        logger.log(Level.WARNING, ex.getCause().getMessage(), ex.getCause());
    }
}
```

Note: There is an important difference between the processing illustrated here with catching a `NotClosedException`, and consuming an exception in Example 5.  In the queryDatabase example above, the close exception will be caught 0 or 1 times.  The `NotClosedException` will not be caught if `processResultSet(rs)` throws an exception, but will instead be a suppressed exception attached to the `processResultSet(rs)` exception.  Likewise, if `processResultSet(rs)` does not throw an exception, but multiple `close()` methods throw an exception, the innermost `close()` exception will be caught, and any other `close()` exceptions will be suppressed exceptions of the caught `NotClosedException`.  In Example 5, any exception thrown in a `close()` method is *always* consumed by the consumer.

**Example 9: Wrap Close Exceptions in an Application Exception**

This example shows how to wrap a checked close exception in an application exception.  As above, it may be used both with lambda expressions and "traditional" autocloseables.  If an exception occurs in the `try-with-resources` block and the `close` method, the `close` exception will be a suppressed exception that is wrapped within the application exception.  There is also a `CloseIt1.wrapAllException` which will wrap both checked and unchecked exceptions, and `CloseIt1.wrapAllThrowable` which will wrap all throwables.  In this example, it is assumed that there is an exception class called `AppException` with a public constructor that takes a single argument of type `Throwable`.

```java
import com.github.richardroda.util.closeit.*;
...
public void useContext(Context ctx) throws AppException {
    try(CloseIt1<AppException> it = CloseIt1.wrapException(ctx::close, AppException::new)) {
        doSomethingWithContext(ctx); // Throws AppException.
    }
}
```
**Example 10: Hide Close Exceptions**

This example shows how to hide exceptions from the compiler.  The exceptions are thrown as-is, but they are "hidden" from the compiler and checked exceptions are not flagged as a compilation error.  This should be used with caution because it breaks the Java programming language invariant of declaring checked exceptions.  Calling code may not behave correctly when confronted with an undeclared checked exception.


```java
import com.github.richardroda.util.closeit.*;
...
public void hideException(Context ctx) {
    try (CloseIt0 it = CloseIt0.hideException(ctx::close)) {
        doSomethingWithContext(ctx);
    }
}
```


## CloseIt as a finally replacement ##

The CloseIt interfaces with a lambda expression may be used instead of a `try-finally` block.  There are good reasons for doing so.  The `try-with-resources` feature was created because exceptions that occur when closing resources within a `finally` block can interfere with the processing of other resources, and exceptions within the `finally` block can conceal exceptions within the `try` block.  Concealing exceptions within the `try` block is particularly problematic because the exceptions in the `finally` block are often a result of the bad state that caused an exception in the `try` block in the first place.  By concealing the `try` block exceptions, the root cause analysis of the failure becomes more difficult.  The `try-with-resources` feature gives an easy way to express a set of resources to be closed in order, with exceptions that occur properly suppressed into a superseding exception when a superseding exception has occurred.

The above issues of exception hiding and execution interference can exist with *any* finally block, not just resource blocks.  Any method called within a finally block may throw an exception or error.  The lack of a `throws` clause is no guarantee that a `RuntimeException` will not be thrown by a given method call.  For this reason, a project may consider replacing `finally` blocks with `try-with-resources` lambdas.  Here is an example of code that has finally clause issues.

**Example 11: Problematic Finally block**
```java
public void useContextAndExecutorService(Context ctx, ExecutorService es) throws NamingException, InterruptedException {
    try {
        doSomething(ctx, es);
    } finally {
        es.shutdown();
        es.awaitTermination(1, TimeUnit.DAYS);
        ctx.close();
    }
}
```
There are issues with these 7 lines of code.  If the call to `shutdown` or `awaitTermination` throws an exception, `close` is never called on the context.  Also, any exception in the `finally` block will discard an exception from the `try` block.  We want to cleanup all all resources, and suppress any exceptions that occur in the `finally` clause while throwing the original exception.


**Example 12: Rewrite using CloseIt**
```java  
import com.github.richardroda.util.closeit.*;
...
public void useContextAndExecutorService(Context ctx, ExecutorService es) throws NamingException, InterruptedException {
    try (CloseIt1<NamingException> outer = ctx::close;
         CloseIt1<InterruptedException> inner = ()-> {
             es.shutdown();
             es.awaitTermination(1, TimeUnit.DAYS);
         }) {
        doSomething(ctx, es);
    }
}
```
Substituting the `finally` clause with the `try-with-resourses` fixes all of the issues identified above with the `try-finally` clause. There is a trick to converting `finally` blocks to `try-with-resources`.  The lambdas must be declared in the *opposite* order they are cleaned up in a `finally` clause.  Note that `ctx::close` is declared first, but will be executed last.  When reasoning about this, you may think of the lambdas as being like code blocks, the inner code block will exit first, followed by the outer code block.  Likewise the "inner" cleanup lambda code will run before the "outer" lambda cleanup code.

Some static code analysis (SCA) tools consider most use of multiple objects within the same `finally` statement a bad exception handling issue.  They require that a subsequent object use be nested in a `try-finally` construct under the first object usage.  Some SCA tools will also flag `finally` clauses that do not catch and discard their exceptions (a bad practice in and of itself) to be a bad exception handling issue.  This framework may be used to quickly remediate such issues by declaring a lambda for each object which needs to be cleaned up.

[BSD 2-Clause License](LICENSE "Click here to view the license")