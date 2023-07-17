package br.com.banco.bankTransfer.specifications;

import br.com.banco.bankTransfer.BankTransfer;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class BankTransferWithAccountId implements Specification<BankTransfer> {
    private final Long accountId;

    public BankTransferWithAccountId(Long accountId) {
        this.accountId = accountId;
    }

    @Override
    public javax.persistence.criteria.Predicate toPredicate(Root<BankTransfer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (this.accountId == null) {
            return criteriaBuilder.isTrue((criteriaBuilder.literal(true)));
        }

        return criteriaBuilder.equal(root.get("contaId"), this.accountId);
    }
}
