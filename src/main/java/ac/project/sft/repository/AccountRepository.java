package ac.project.sft.repository;

import ac.project.sft.model.Account;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface AccountRepository extends CrudRepository<Account,Long> {
}
