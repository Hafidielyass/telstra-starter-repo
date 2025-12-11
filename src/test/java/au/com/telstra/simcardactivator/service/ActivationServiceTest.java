package au.com.telstra.simcardactivator.service;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import au.com.telstra.simcardactivator.api.ActivationRequest;
import au.com.telstra.simcardactivator.api.ActivationResult;
import au.com.telstra.simcardactivator.config.RestTemplateConfig;

@RestClientTest(ActivationService.class)
@Import(RestTemplateConfig.class)
class ActivationServiceTest {

    @Autowired
    private ActivationService activationService;

    @Autowired
    private MockRestServiceServer server;

    @Value("${simcard.actuator.url:http://localhost:8444/actuate}")
    private String actuatorUrl;

    @Test
    void activatesSuccessfullyWhenActuatorReturnsSuccess() {
        ActivationRequest request = new ActivationRequest("1234567890123456789", "customer@example.com");

        server.expect(requestTo(actuatorUrl))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("{\"success\":true}", MediaType.APPLICATION_JSON));

        ActivationResult result = activationService.activate(request);

        assertThat(result.isSuccess()).isTrue();
        server.verify();
    }

    @Test
    void returnsFailureWhenActuatorReturnsFailure() {
        ActivationRequest request = new ActivationRequest("1234567890123456789", "customer@example.com");

        server.expect(requestTo(actuatorUrl))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("{\"success\":false}", MediaType.APPLICATION_JSON));

        ActivationResult result = activationService.activate(request);

        assertThat(result.isSuccess()).isFalse();
        server.verify();
    }
}
