package br.com.banco.bankTransfer;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface BankTransferRepository extends CrudRepository<BankTransfer, Long>,
        PagingAndSortingRepository<BankTransfer, Long>, JpaSpecificationExecutor<BankTransfer>
{
    Optional<BankTransfer> findById(Long id);
    List<BankTransfer> findByNomeOperadorTransacao(String nomeOperadorTransacao);
}
