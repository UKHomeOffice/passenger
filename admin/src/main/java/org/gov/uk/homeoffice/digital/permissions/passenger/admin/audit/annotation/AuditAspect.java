package org.gov.uk.homeoffice.digital.permissions.passenger.admin.audit.annotation;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.gov.uk.homeoffice.digital.permissions.passenger.admin.authentication.SecurityUtil;
import org.gov.uk.homeoffice.digital.permissions.passenger.audit.AuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Aspect
@Component
public class AuditAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditAspect.class);
    public static final String UNKNOWN_RESPONSE = "UNKNOWN_RESPONSE";
    private final AuditService auditService;
    private final HttpServletRequest request;

    @Autowired
    public AuditAspect(@Qualifier("audit.admin") final AuditService auditService,
                       final HttpServletRequest request) {
        this.auditService = auditService;
        this.request = request;
    }

    @AfterReturning(pointcut = "@annotation(audit)")
    public void audit(JoinPoint joinPoint, Audit audit) {
        auditService.audit(getAction(audit, joinPoint), "SUCCESS", SecurityUtil.username());
    }

    @AfterThrowing(pointcut = "@annotation(audit)", throwing = "thrown")
    public void auditException(JoinPoint joinPoint, Audit audit, Throwable thrown) {
        auditService.audit(getAction(audit, joinPoint, thrown), "FAILURE", SecurityUtil.username());
    }

    private String getAction(Audit audit, JoinPoint joinPoint, Throwable thrown) {
            return getAction(audit, joinPoint) + String.format(", error='%s'", thrown.toString());
    }

    private String getAction(Audit audit, JoinPoint joinPoint) {

        if (audit.auditAction().isAnActionOnResource()) {
            return String.format("action='%s', entity='%s', %s",
                    audit.auditAction().getDescription(),
                    audit.resourceClass().getSimpleName(),
                    format(getPathVariables(joinPoint)));
        }

        if (!StringUtils.isEmpty(audit.message())) {
            return audit.message();
        }

        return audit.auditAction().getDescription();
    }

    private Map<String, Object> getPathVariables(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        String methodName = signature.getMethod().getName();
        String[] parameterNames = signature.getParameterNames();
        Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();
        Object[] parameterValues = joinPoint.getArgs();

        try {
            Annotation[][] parameterAnnotations = joinPoint.getTarget().getClass().getMethod(methodName, parameterTypes).getParameterAnnotations();

            Map<String, Object> keyparams = IntStream.range(0, parameterNames.length)
                    .filter(i -> Set.of(parameterAnnotations[i]).stream().anyMatch(a -> a.annotationType().equals(PathVariable.class)))
                    .boxed()
                    .collect(Collectors.toMap(i -> parameterNames[i], i -> parameterValues[i]));
            return keyparams;

        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private String format(Map<String, Object> map) {
        return map.entrySet()
                .stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .map(entry -> String.format("%s='%s'", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining(", "));
    }

}