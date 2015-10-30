package com.palominolabs.metrics.guice;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import org.aopalliance.intercept.MethodInterceptor;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

/**
 * A listener which adds method interceptors to methods that should be instrumented for exceptions
 */
public class ExceptionPercentMeteredListener implements TypeListener {
    private final MetricRegistry metricRegistry;
    private final MetricNamer metricNamer;

    public ExceptionPercentMeteredListener(MetricRegistry metricRegistry, MetricNamer metricNamer) {
        this.metricRegistry = metricRegistry;
        this.metricNamer = metricNamer;
    }

    @Override
    public <T> void hear(TypeLiteral<T> literal,
        TypeEncounter<T> encounter) {
        final Class<?> klass = literal.getRawType();
        for (Method method : klass.getDeclaredMethods()) {
            final MethodInterceptor interceptor = getInterceptor(metricRegistry, method);

            if (interceptor != null) {
                encounter.bindInterceptor(Matchers.only(method), interceptor);
            }
        }
    }

    @Nullable
    private MethodInterceptor getInterceptor(MetricRegistry metricRegistry, Method method) {
        final ExceptionPercentMetered annotation = method.getAnnotation(ExceptionPercentMetered.class);
        if (annotation != null) {
            final Meter metere = metricRegistry.meter(metricNamer.getNameForExceptionPercentMetered(method, annotation) +"-exceptions");
            final Meter metert = metricRegistry.meter(metricNamer.getNameForExceptionPercentMetered(method, annotation) +"-total");


            metricRegistry.register(metricNamer.getNameForExceptionPercentMetered(method, annotation) + "-exceptions_percentage",
                    new Gauge<Double>() {
                        @Override
                        public Double getValue() {
                            return (double)metere.getCount()/metert.getCount() * 100;
                        }
                    });

            return new ExceptionPercentMeteredInterceptor(metere, metert, annotation.cause());
        }
        return null;
    }
}
