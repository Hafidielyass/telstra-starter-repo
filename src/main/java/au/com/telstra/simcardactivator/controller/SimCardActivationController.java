package au.com.telstra.simcardactivator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import au.com.telstra.simcardactivator.api.ActivationRequest;
import au.com.telstra.simcardactivator.api.ActivationResult;
import au.com.telstra.simcardactivator.api.SimCardResponse;
import au.com.telstra.simcardactivator.repository.SimCardRepository;
import au.com.telstra.simcardactivator.service.ActivationService;

@RestController
@RequestMapping("/simcards")
public class SimCardActivationController {

    private final ActivationService activationService;
    private final SimCardRepository simCardRepository;

    public SimCardActivationController(ActivationService activationService, SimCardRepository simCardRepository) {
        this.activationService = activationService;
        this.simCardRepository = simCardRepository;
    }

    @PostMapping("/activate")
    public ResponseEntity<ActivationResult> activate(@RequestBody ActivationRequest request) {
        ActivationResult result = activationService.activate(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{simCardId}")
    public ResponseEntity<SimCardResponse> getSimCard(@PathVariable Long simCardId) {
        return simCardRepository.findById(simCardId)
                .map(simCard -> new SimCardResponse(simCard.getIccid(), simCard.getCustomerEmail(), simCard.getActive()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
