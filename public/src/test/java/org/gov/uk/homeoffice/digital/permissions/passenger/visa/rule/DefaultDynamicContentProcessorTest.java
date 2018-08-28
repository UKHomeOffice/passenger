package org.gov.uk.homeoffice.digital.permissions.passenger.visa.rule;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class DefaultDynamicContentProcessorTest {

    private DefaultDynamicContentProcessor underTest = new DefaultDynamicContentProcessor();
    private DynamicContentObject object = new DynamicContentObject("Bruce", "Lee", "bruce.lee@jeetkune.do");

    @Test
    public void shouldProcessSingleVariableUsingMethodName() {
        assertThat(underTest.transform("Hello ${firstName}!", object), is("Hello Bruce!"));
    }

    @Test
    public void shouldProcessMultipleVariablesUsingMethodName() {
        assertThat(underTest.transform("Hello ${firstName} ${lastName}!", object), is("Hello Bruce Lee!"));
    }

    @Test
    public void shouldProcessSingleVariableUsingFieldName() {
        assertThat(underTest.transform("Email ${emailAddress}", object), is("Email bruce.lee@jeetkune.do"));
    }

    @Test
    public void shouldProcessSingleVariableUsingMethodOnly() {
        assertThat(underTest.transform("Date of birth: ${dateOfBirth}", object), is("Date of birth: 27/07/1940"));
    }

    @Test
    public void shouldReturnFieldNameForUnknownField() {
        assertThat(underTest.transform("Error: ${invalidVar}", object), is("Error: invalidVar"));
    }

    @Test
    public void shouldEmptyStringForEmptyInput() {
        assertThat(underTest.transform("", object), is(""));
    }

    @Test
    public void shouldReplaceAllInstancesOfField() {
        assertThat(underTest.transform("${firstName}!  That's my name, ${firstName}", object), is("Bruce!  That's my name, Bruce"));
    }

    @Test
    public void shouldFindFieldsInMultipleObjects() {
        var martialArt = new DynamicContentObject2("Jeet Kune Do");
        assertThat(underTest.transform("${firstName} practices ${martialArt}", object, martialArt), is("Bruce practices Jeet Kune Do"));
    }

    @Data
    @AllArgsConstructor
    private class DynamicContentObject {
        private String firstName;
        private String lastName;
        public String emailAddress;
        public String dateOfBirth() {
            return "27/07/1940";
        }
    }

    @Data
    @AllArgsConstructor
    private class DynamicContentObject2 {
        private String martialArt;
    }

}