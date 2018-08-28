package org.gov.uk.homeoffice.digital.permissions.passenger.authentication;

public class RemoteIPThreadLocal {

    public static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void set(String value) {
        threadLocal.set(value);
    }

    public static void unset() {
        threadLocal.remove();
    }

    public static String get() {
        return threadLocal.get();
    }
}
