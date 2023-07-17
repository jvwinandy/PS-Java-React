package br.com.banco.bankTransfer;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BankTransferRepository extends CrudRepository<BankTransfer, Long>,
        PagingAndSortingRepository<BankTransfer, Long>, JpaSpecificationExecutor<BankTransfer>
{
    Optional<BankTransfer> findById(Long id);

    @Query("SELECT DISTINCT t.nomeOperadorTransacao from BankTransfer t")
    Set<String> findDistinctNomeOperadorTransacao();
    List<BankTransfer> findByNomeOperadorTransacao(String nomeOperadorTransacao);
}
