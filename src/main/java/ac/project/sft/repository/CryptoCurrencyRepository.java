package ac.project.sft.repository;

import ac.project.sft.model.Category;
import ac.project.sft.model.CryptoCurrency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoCurrencyRepository extends CrudRepository<CryptoCurrency,Long> {
}
