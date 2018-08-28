package org.gov.uk.homeoffice.digital.permissions.passenger.visa.rule;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.gov.uk.homeoffice.digital.permissions.passenger.domain.VisaRuleContent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Optional.ofNullable;
import static org.springframework.util.ReflectionUtils.*;

@Component
public class DefaultDynamicContentProcessor implements DynamicContentProcessor {

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{(\\w+)}");

    @Override
    public String transform(@NonNull String input, final Object... dataObjects) {

        /* Check if the input requires processing */
        if (input.isEmpty() || dataObjects == null || dataObjects.length == 0)
            return input;

        /* Define a matcher to locate the variable fields. */
        final Matcher matcher = VARIABLE_PATTERN.matcher(input);

        /* Extract each of the variable types */
        while (matcher.find()) {
            final String contentField = matcher.group(1);
            final String value = find(contentField, dataObjects);

            /* Update the input string */
            input = input.replace("${" + contentField + "}", value);
        }

        return input;
    }

    @Override
    public VisaRuleContent transform(final VisaRuleContent visaRuleContent, Object... dataObjects) {
        visaRuleContent.setContent(transform(visaRuleContent.getContent(), dataObjects));
        return visaRuleContent;
    }

    private String find(final String contentField, final Object... dataObjects) {
        return Arrays.stream(dataObjects).map(obj ->
                        getFieldValue(obj, contentField).orElse(
                            getMethodValue(obj, contentField).orElse(
                                    getMethodValue(obj, generateGetterMethod(contentField)).orElse(
                                            null))))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(contentField);
    }

    private String generateGetterMethod(final String contentField) {
        return "get" + StringUtils.capitalize(contentField);
    }

    private Optional<String> getMethodValue(final Object obj, final String contentField) {
        try {
            return ofNullable(findMethod(obj.getClass(), contentField)).map(method -> {
                makeAccessible(method);
                return ofNullable(invokeMethod(method, obj)).map(Object::toString);
            }).orElse(Optional.empty());
        }
        catch (IllegalArgumentException | IllegalStateException e) { /* No-op */ }
        return Optional.empty();
    }

    private Optional<String> getFieldValue(final Object obj, final String contentField) {
        try {
            return ofNullable(findField(obj.getClass(), contentField)).map(field -> {
                makeAccessible(field);
                return ofNullable(getField(field, obj)).map(Object::toString);
            }).orElse(Optional.empty());
        }
        catch (IllegalStateException e) { /* No-op */ }
        return Optional.empty();
    }

}
