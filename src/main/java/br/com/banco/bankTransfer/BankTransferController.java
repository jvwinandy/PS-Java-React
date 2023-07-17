package br.com.banco.bankTransfer;

import br.com.banco.bankTransfer.specifications.BankTransferWithAccountId;
import br.com.banco.bankTransfer.specifications.BankTransferWithOperatorName;
import br.com.banco.bankTransfer.specifications.BankTransferWithTimePeriod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*")
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
    public ResponseEntity<List<BankTransfer>> findAll(
            @RequestParam(required = false) Long accountId,
            @RequestParam(required = false) String operatorName,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            Pageable pageable) {

        Specification<BankTransfer> specifications = Specification.where(new BankTransferWithAccountId(accountId))
                                                                    .and(new BankTransferWithOperatorName(operatorName))
                                                                    .and(new BankTransferWithTimePeriod(startTime, endTime));

        Sort sort = Sort.by(Sort.Direction.DESC, "dataTransferencia");
        Page<BankTransfer> page = bankTransferRepository.findAll(specifications, PageRequest.of(
                pageable.getPageNumber(),
                Integer.MAX_VALUE,
                pageable.getSortOr(sort)
        ));
        return ResponseEntity.ok(page.getContent());
    }

    @GetMapping("/operators")
    public ResponseEntity<Set<String>> findDistinctNomeOperadorTransacao() {
        Set<String> operators = bankTransferRepository.findDistinctNomeOperadorTransacao();
        return ResponseEntity.ok(operators);
    }
}
