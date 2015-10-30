package com.palominolabs.metrics.guice;

import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;

public class InstrumentedWithExceptionPercentMetered {

    @ExceptionPercentMetered(name = "exceptionCounter")
    String explodeWithPublicScope(boolean explode) {
        if (explode) {
            throw new RuntimeException("Boom!");
        } else {
            return "calm";
        }
    }

    @ExceptionPercentMetered
    String explodeForUnnamedMetric() {
        throw new RuntimeException("Boom!");
    }

    @ExceptionPercentMetered(name = "n")
    String explodeForMetricWithName() {
        throw new RuntimeException("Boom!");
    }

    @ExceptionPercentMetered(name = "absoluteName", absolute = true)
    String explodeForMetricWithAbsoluteName() {
        throw new RuntimeException("Boom!");
    }

    @ExceptionPercentMetered
    String explodeWithDefaultScope() {
        throw new RuntimeException("Boom!");
    }

    @ExceptionPercentMetered
    protected String explodeWithProtectedScope() {
        throw new RuntimeException("Boom!");
    }

    @ExceptionPercentMetered(name = "failures", cause = MyException.class)
    public void errorProneMethod(RuntimeException e) {
        throw e;
    }

    @ExceptionPercentMetered(name = "things",
        cause = ArrayIndexOutOfBoundsException.class)
    public Object causeAnOutOfBoundsException() {
        final Object[] arr = {};
        return arr[1];
    }

    @Timed
    @ExceptionPercentMetered
    public void timedAndException(RuntimeException e) {
        if (e != null) {
            throw e;
        }
    }

    @Metered
    @ExceptionPercentMetered
    public void meteredAndException(RuntimeException e) {
        if (e != null) {
            throw e;
        }
    }
}
