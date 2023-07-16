package br.com.banco.specifications;

import br.com.banco.BankTransfer;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BankTransferWithTimePeriod implements Specification<BankTransfer> {
    private Timestamp startTime;
    private Timestamp endTime;

    public BankTransferWithTimePeriod(String startTime, String endTime) {
        if (startTime != null && endTime != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            try {
                Date startDate = dateFormat.parse(startTime);
                this.startTime = new Timestamp(startDate.getTime());
                Date endDate = dateFormat.parse(endTime);
                this.endTime = new Timestamp(endDate.getTime());
            } catch (ParseException e) {
                // keep a null value.
            }
        }
    }

    @Override
    public javax.persistence.criteria.Predicate toPredicate(Root<BankTransfer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (this.startTime == null || this.endTime == null) {
            return criteriaBuilder.isTrue((criteriaBuilder.literal(true)));
        }

        System.out.println(this.startTime);
        System.out.println(this.endTime);
        return criteriaBuilder.between(root.get("dataTransferencia"), this.startTime, this.endTime);
    }
}
