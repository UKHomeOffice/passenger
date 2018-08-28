package org.gov.uk.homeoffice.digital.permissions.passenger.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@RunWith(MockitoJUnitRunner.class)
public class TOTPTest {

    @InjectMocks
    private TOTP underTest;

    @Test
    public void shouldGenerateAndValidateCode() throws Exception {
        setField(underTest, "sharedSecret", "6YFX5HZT76OHHNMS");
        setField(underTest, "varianceSeconds", 2);
        setField(underTest, "timeStepSeconds", 30);

        final String code = underTest.generateCode();
        Thread.sleep(500);

        assertTrue(underTest.isCodeValid(code));
    }

    @Test
    public void shouldFailWhenCodeHasExpired() throws Exception {
        setField(underTest, "sharedSecret", "6YFX5HZT76OHHNMS");
        setField(underTest, "varianceSeconds", 1);
        setField(underTest, "timeStepSeconds", 1);

        final String code = underTest.generateCode();
        Thread.sleep(1500);

        assertFalse(underTest.isCodeValid(code));
    }

}