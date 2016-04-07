package de.skuzzle.inject.async;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

import com.google.inject.Key;
import com.google.inject.internal.Annotations;
import com.google.inject.internal.Errors;

import de.skuzzle.inject.async.annotation.Executor;
import de.skuzzle.inject.async.annotation.Scheduler;

class ExecutorKeyService {

    /** Fall back key if the user did not bind any executor service */
    private static final Key<? extends ExecutorService> DEFAULT_EXECUTOR_KEY =
            Key.get(ExecutorService.class, DefaultExecutor.class);

    /** Fall back key if the user did not bind any scheduled executor service */
    private static final Key<? extends ScheduledExecutorService> DEFAULT_SCHEDULER_KEY =
            Key.get(ScheduledExecutorService.class, DefaultExecutor.class);

    /**
     * Finds the key of the {@link ExecutorService} to use to execute the given method.
     *
     * @param method The method to find the key for.
     * @return The ExecutorService key.
     */
    @SuppressWarnings("unchecked")
    public Key<? extends ExecutorService> getExecutorKey(Method method) {
        final Class<? extends ExecutorService> type;
        boolean executorSpecified = false;
        if (method.isAnnotationPresent(Executor.class)) {
            type = method.getAnnotation(Executor.class).value();
            executorSpecified = true;
        } else {
            type = ExecutorService.class;
        }
        return (Key<? extends ExecutorService>) createKey(type, method,
                DEFAULT_EXECUTOR_KEY, executorSpecified);
    }

    public Key<? extends ScheduledExecutorService> getSchedulerKey(Method method) {
        final Class<? extends ScheduledExecutorService> type;
        boolean executorSpecified = false;
        if (method.isAnnotationPresent(Scheduler.class)) {
            type = method.getAnnotation(Scheduler.class).value();
            executorSpecified = true;
        } else {
            type = ScheduledExecutorService.class;
        }
        return (Key<? extends ScheduledExecutorService>) createKey(type, method,
                DEFAULT_SCHEDULER_KEY, executorSpecified);
    }

    private static Key<?> createKey(
            Class<?> type,
            Method method,
            Key<?> defaultKey,
            boolean typeGiven) {

        final Errors errors = new Errors(method);
        final Annotation bindingAnnotation = Annotations.findBindingAnnotation(errors,
                method, method.getAnnotations());
        errors.throwConfigurationExceptionIfErrorsExist();
        final Key<?> key;
        if (bindingAnnotation == null && typeGiven) {
            key = Key.get(type);
        } else if (bindingAnnotation == null) {
            key = defaultKey;
        } else {
            key = Key.get(type, bindingAnnotation);
        }
        return key;
    }
}
