package com.palominolabs.metrics.guice;

import com.codahale.metrics.annotation.ExceptionMetered;

import java.lang.annotation.*;

/**
 * An annotation for marking a method of an annotated object as metered.
 * <p/>
 * Given a method like this:
 * <pre><code>
 *     {@literal @}ExceptionMetered(name = "fancyName", cause=IllegalArgumentException.class)
 *     public String fancyName(String name) {
 *         return "Sir Captain " + name;
 *     }
 * </code></pre>
 * <p/>
 * A meter for the defining class with the name {@code fancyName} will be created and each time the
 * {@code #fancyName(String)} throws an exception of type {@code cause} (or a subclass), the meter
 * will be marked.
 * <p/>
 * A name for the metric can be specified as an annotation parameter, otherwise, the metric will be
 * named based on the method name.
 * <p/>
 * For instance, given a declaration of
 * <pre><code>
 *     {@literal @}ExceptionMetered
 *     public String fancyName(String name) {
 *         return "Sir Captain " + name;
 *     }
 * </code></pre>
 * <p/>
 * A meter named {@code fancyName.exceptions} will be created and marked every time an exception is
 * thrown.
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.METHOD })
public @interface ExceptionPercentMetered {
    String DEFAULT_NAME_SUFFIX = "exceptionPercentage";

    String name() default "";

    boolean absolute() default false;

    Class<? extends Throwable> cause() default Exception.class;
}
