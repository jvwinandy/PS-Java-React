package br.com.banco;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/transfers")
public class BankTransferController {
    private final BankTransferRepository bankTransferRepository;

    public BankTransferController(BankTransferRepository bankTransferRepository) {
        this.bankTransferRepository = bankTransferRepository;
    }

    @GetMapping("/{requestedId}")
    public ResponseEntity<BankTransfer> findById(@PathVariable Long requestedId) {
        Optional<BankTransfer> bankTransferOptional = bankTransferRepository.findById(requestedId);
        return bankTransferOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Iterable<BankTransfer>> findAll(@RequestParam(required = false) String operatorName) {

        return ResponseEntity.ok(bankTransferRepository.findAll());
    }
}
