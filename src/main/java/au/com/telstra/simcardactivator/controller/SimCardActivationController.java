package au.com.telstra.simcardactivator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import au.com.telstra.simcardactivator.api.ActivationRequest;
import au.com.telstra.simcardactivator.api.ActivationResult;
import au.com.telstra.simcardactivator.service.ActivationService;

@RestController
@RequestMapping("/simcards")
public class SimCardActivationController {

    private final ActivationService activationService;

    public SimCardActivationController(ActivationService activationService) {
        this.activationService = activationService;
    }

    @PostMapping("/activate")
    public ResponseEntity<ActivationResult> activate(@RequestBody ActivationRequest request) {
        ActivationResult result = activationService.activate(request);
        return ResponseEntity.ok(result);
    }
}
