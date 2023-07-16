package br.com.banco.bankTransfer.specifications;

import br.com.banco.bankTransfer.BankTransfer;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;

public class BankTransferWithTimePeriod implements Specification<BankTransfer> {
    private Timestamp startTime;
    private Timestamp endTime;

    public BankTransferWithTimePeriod(String startTime, String endTime) {
        if (startTime != null && endTime != null) {
            ZonedDateTime parsedStartTime = ZonedDateTime.parse(startTime);
            ZonedDateTime parsedEndTime = ZonedDateTime.parse(endTime);
            this.startTime = Timestamp.from(parsedStartTime.toInstant());
            this.endTime = Timestamp.from(parsedEndTime.toInstant());
        }
    }

    @Override
    public javax.persistence.criteria.Predicate toPredicate(Root<BankTransfer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (this.startTime == null || this.endTime == null) {
            return criteriaBuilder.isTrue((criteriaBuilder.literal(true)));
        }

        return criteriaBuilder.between(root.get("dataTransferencia"), this.startTime, this.endTime);
    }
}
