package au.com.telstra.simcardactivator.controller;

import au.com.telstra.simcardactivator.api.SimCardResponse;
import au.com.telstra.simcardactivator.entity.SimCard;
import au.com.telstra.simcardactivator.repository.SimCardRepository;
import au.com.telstra.simcardactivator.SimCardActivator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = SimCardActivator.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SimCardQueryControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SimCardRepository simCardRepository;

    @BeforeEach
    void setUp() {
        simCardRepository.deleteAll();
    }

    @Test
    void getSimCardReturnsRecord() {
        SimCard saved = simCardRepository.save(new SimCard("1234567890123456789", "customer@example.com", true));

        ResponseEntity<SimCardResponse> response = restTemplate.getForEntity(
                "/simcards/" + saved.getId(),
                SimCardResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getIccid()).isEqualTo("1234567890123456789");
        assertThat(response.getBody().getCustomerEmail()).isEqualTo("customer@example.com");
        assertThat(response.getBody().getActive()).isTrue();
    }

    @Test
    void getSimCardReturnsNotFoundForMissingId() {
        ResponseEntity<SimCardResponse> response = restTemplate.getForEntity(
                "/simcards/9999",
                SimCardResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
