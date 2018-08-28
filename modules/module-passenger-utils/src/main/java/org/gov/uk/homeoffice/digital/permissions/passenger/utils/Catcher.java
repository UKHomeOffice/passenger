package org.gov.uk.homeoffice.digital.permissions.passenger.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Catcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(Catcher.class);

    public static <R, P> R convertToRuntime(ExceptionThrowingFunction<R, P> function, P val) {
        try {
            return function.execute(val);
        } catch (Exception e) {
            throw new ConverterRuntimeException(e);
        }
    }

    public static <R> R convertToRuntime(ExceptionThrowingProvider<R> provider) {
        try {
            return provider.execute();
        } catch (Exception e) {
            throw new ConverterRuntimeException(e);
        }
    }

    public static <P> void convertToRuntime(ExceptionThrowingConsumer<P> consumer, P val) {
        try {
            consumer.execute(val);
        } catch (Exception e) {
            throw new ConverterRuntimeException(e);
        }
    }

    public static void convertToRuntime(ExceptionThrowingAction action) {
        try {
            action.execute();
        } catch (Exception e) {
            throw new ConverterRuntimeException(e);
        }
    }

    public static <R, P> R convert(ExceptionThrowingFunction<R, P> function, P val, RuntimeException exception) {
        try {
            return function.execute(val);
        } catch (Exception e) {
            LOGGER.error("converting exception", e);
            throw exception;
        }
    }

    public static <R> R convert(ExceptionThrowingProvider<R> provider, RuntimeException exception) {
        try {
            return provider.execute();
        } catch (Exception e) {
            LOGGER.error("converting exception", e);
            throw exception;
        }
    }

    public static <P> void convert(ExceptionThrowingConsumer<P> consumer, P val, RuntimeException exception) {
        try {
            consumer.execute(val);
        } catch (Exception e) {
            LOGGER.error("converting exception", e);
            throw new ConverterRuntimeException(exception);
        }
    }

    public static void convert(ExceptionThrowingAction action, RuntimeException exception) {
        try {
            action.execute();
        } catch (Exception e) {
            LOGGER.error("converting exception", e);
            throw new ConverterRuntimeException(exception);
        }
    }

    @FunctionalInterface
    public interface ExceptionThrowingProvider<R> {

        R execute() throws ConverterException, IOException;

    }

    @FunctionalInterface
    public interface ExceptionThrowingAction {

        void execute() throws ConverterException;

    }

    @FunctionalInterface
    public interface ExceptionThrowingConsumer<P> {

        void execute(P value) throws ConverterException;

    }

    @FunctionalInterface
    public interface ExceptionThrowingFunction<R, P> {

        R execute(P value) throws ConverterException;

    }

}
