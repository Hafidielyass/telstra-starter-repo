package au.com.telstra.simcardactivator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import au.com.telstra.simcardactivator.api.ActivationRequest;
import au.com.telstra.simcardactivator.api.ActivationResult;

@Service
public class ActivationService {
    private static final Logger log = LoggerFactory.getLogger(ActivationService.class);

    private final RestTemplate restTemplate;
    private final String actuatorUrl;

    public ActivationService(RestTemplate restTemplate,
                             @Value("${simcard.actuator.url:http://localhost:8444/actuate}") String actuatorUrl) {
        this.restTemplate = restTemplate;
        this.actuatorUrl = actuatorUrl;
    }

    public ActivationResult activate(ActivationRequest request) {
        ActuatorRequest actuatorRequest = new ActuatorRequest(request.getIccid());
        ResponseEntity<ActuatorResponse> response = restTemplate.postForEntity(actuatorUrl, actuatorRequest, ActuatorResponse.class);

        boolean success = response.getStatusCode().is2xxSuccessful()
                && response.getBody() != null
                && response.getBody().isSuccess();

        log.info("SIM activation for ICCID {} success={}", request.getIccid(), success);
        System.out.println("Activation result for ICCID " + request.getIccid() + ": " + success);

        return new ActivationResult(success);
    }

    private static class ActuatorRequest {
        private String iccid;

        ActuatorRequest(String iccid) {
            this.iccid = iccid;
        }

        public String getIccid() {
            return iccid;
        }

        public void setIccid(String iccid) {
            this.iccid = iccid;
        }
    }

    private static class ActuatorResponse {
        private boolean success;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }
}
