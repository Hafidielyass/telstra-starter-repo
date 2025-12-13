package au.com.telstra.simcardactivator.service;

import au.com.telstra.simcardactivator.api.ActivationRequest;
import au.com.telstra.simcardactivator.api.ActivationResult;
import au.com.telstra.simcardactivator.repository.SimCardRepository;
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
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ActivationServiceIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RestTemplate actuatorRestTemplate;

    @Autowired
    private SimCardRepository simCardRepository;

    private MockRestServiceServer server;

    @BeforeEach
    void setUp() {
        server = MockRestServiceServer.createServer(actuatorRestTemplate);
        simCardRepository.deleteAll();
    }

    @Test
    void activationIsSuccessfulAndPersisted() {
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

        // Verify record was saved to database
        assertThat(simCardRepository.count()).isEqualTo(1);
        var saved = simCardRepository.findAll().get(0);
        assertThat(saved.getIccid()).isEqualTo("1234567890123456789");
        assertThat(saved.getCustomerEmail()).isEqualTo("customer@example.com");
        assertThat(saved.getActive()).isTrue();

        server.verify();
    }

    @Test
    void failedActivationIsPersistedAsInactive() {
        server.expect(requestTo("http://localhost:8444/actuate"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("{\"success\":false}", org.springframework.http.MediaType.APPLICATION_JSON));

        ActivationRequest request = new ActivationRequest("9876543210987654321", "other@example.com");
        ResponseEntity<ActivationResult> response = restTemplate.exchange(
                "/simcards/activate",
                HttpMethod.POST,
                new HttpEntity<>(request),
                ActivationResult.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();

        // Verify record was saved with active=false
        assertThat(simCardRepository.count()).isEqualTo(1);
        var saved = simCardRepository.findAll().get(0);
        assertThat(saved.getIccid()).isEqualTo("9876543210987654321");
        assertThat(saved.getActive()).isFalse();

        server.verify();
    }
}
