package br.com.banco.specifications;

import br.com.banco.BankTransfer;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class BankTransferWithOperatorName implements Specification<BankTransfer> {
    private final String operatorName;

    public BankTransferWithOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    @Override
    public javax.persistence.criteria.Predicate toPredicate(Root<BankTransfer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (this.operatorName == null) {
            return criteriaBuilder.isTrue((criteriaBuilder.literal(true)));
        }

        return criteriaBuilder.equal(root.get("nomeOperadorTransacao"), this.operatorName);
    }
}
