package com.palominolabs.metrics.guice;

import com.codahale.metrics.Meter;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * A method interceptor which measures the rate at which the annotated method throws exceptions of a given type.
 */
class ExceptionPercentMeteredInterceptor implements MethodInterceptor {

    private final Meter metere;
    private final Meter metert;
    private final Meter meterp;
    private final Class<? extends Throwable> klass;

    ExceptionPercentMeteredInterceptor(Meter metere, Meter metert, Meter meterp, Class<? extends Throwable> klass) {
        this.metere = metere;
        this.metert = metert;
        this.meterp = meterp;
        this.klass = klass;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            return invocation.proceed();
        } catch (Throwable t) {
            if (klass.isAssignableFrom(t.getClass())) {
                metere.mark();
            }
            throw t;
        } finally{
            metert.mark();
            meterp.mark(metere.getCount()/metert.getCount());
        }
    }
}
