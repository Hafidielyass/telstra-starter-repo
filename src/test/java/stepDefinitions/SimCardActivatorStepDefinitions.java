package stepDefinitions;

import au.com.telstra.simcardactivator.SimCardActivator;
import au.com.telstra.simcardactivator.api.ActivationRequest;
import au.com.telstra.simcardactivator.api.ActivationResult;
import au.com.telstra.simcardactivator.api.SimCardResponse;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = SimCardActivator.class, loader = SpringBootContextLoader.class)
public class SimCardActivatorStepDefinitions {
    @Autowired
    private TestRestTemplate restTemplate;

    private ResponseEntity<ActivationResult> activationResponse;
    private ResponseEntity<SimCardResponse> queryResponse;

    @When("I submit an activation request with ICCID {string} and email {string}")
    public void submitActivationRequest(String iccid, String email) {
        ActivationRequest request = new ActivationRequest(iccid, email);
        activationResponse = restTemplate.postForEntity(
                "http://localhost:8080/simcards/activate",
                request,
                ActivationResult.class
        );
    }

    @Then("the activation response should indicate success")
    public void verifyActivationSuccess() {
        assertThat(activationResponse.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(activationResponse.getBody()).isNotNull();
        assertThat(activationResponse.getBody().isSuccess()).isTrue();
    }

    @Then("the activation response should indicate failure")
    public void verifyActivationFailure() {
        assertThat(activationResponse.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(activationResponse.getBody()).isNotNull();
        assertThat(activationResponse.getBody().isSuccess()).isFalse();
    }

    @When("when I query the record with ID {long}")
    public void queryRecord(Long id) {
        queryResponse = restTemplate.getForEntity(
                "http://localhost:8080/simcards/" + id,
                SimCardResponse.class
        );
    }

    @And("when I query the record with ID {long}")
    public void queryRecordAnd(Long id) {
        queryRecord(id);
    }

    @Then("the returned record should have ICCID {string}, email {string}, and active status {boolean}")
    public void verifyRecordDetails(String expectedIccid, String expectedEmail, Boolean expectedActive) {
        assertThat(queryResponse.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(queryResponse.getBody()).isNotNull();
        
        SimCardResponse record = queryResponse.getBody();
        assertThat(record.getIccid()).isEqualTo(expectedIccid);
        assertThat(record.getCustomerEmail()).isEqualTo(expectedEmail);
        assertThat(record.getActive()).isEqualTo(expectedActive);
    }
}
