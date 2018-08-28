package org.gov.uk.homeoffice.digital.permissions.passenger.admin.version;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@TestPropertySource(properties = { "app.version=TEST" })
public class VersionControllerTest {

    private static final String HOST = "http://localhost:%d/version";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void shouldReturn200WithVersionNumberForVersion() {
//        final ResponseEntity<String> entity = testRestTemplate.getForEntity(format(HOST, port), String.class);
//        assertThat(entity.getStatusCodeValue(), is(200));
//        assertThat(entity.getBody(), is("TEST"));
    }

}