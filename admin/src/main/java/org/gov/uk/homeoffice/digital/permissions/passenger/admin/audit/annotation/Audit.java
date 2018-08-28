package org.gov.uk.homeoffice.digital.permissions.passenger.admin.audit.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.function.Function;

@Retention(RetentionPolicy.RUNTIME)
public @interface Audit {
    AuditAction auditAction() default AuditAction.VIEW;
    Class<?> resourceClass() default Object.class;
    String message() default "";
}
