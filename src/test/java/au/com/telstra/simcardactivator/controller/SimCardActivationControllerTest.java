package au.com.telstra.simcardactivator.controller;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import org.springframework.web.client.RestTemplate;

import au.com.telstra.simcardactivator.SimCardActivator;
import au.com.telstra.simcardactivator.api.ActivationRequest;
import au.com.telstra.simcardactivator.api.ActivationResult;

@SpringBootTest(classes = SimCardActivator.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SimCardActivationControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RestTemplate actuatorRestTemplate;

    private MockRestServiceServer server;

    @BeforeEach
    void setUp() {
        server = MockRestServiceServer.createServer(actuatorRestTemplate);
    }

    @Test
    void activateEndpointReturnsSuccessFromActuator() {
        server.expect(requestTo("http://localhost:8444/actuate"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("{\"success\":true}", org.springframework.http.MediaType.APPLICATION_JSON));

        ActivationRequest request = new ActivationRequest("1234567890123456789", "customer@example.com");
        ResponseEntity<ActivationResult> response = restTemplate.exchange(
                "/simcards/activate",
                HttpMethod.POST,
                new HttpEntity<>(request),
                ActivationResult.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isTrue();
        server.verify();
    }
}
