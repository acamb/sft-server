package ac.project.sft.repository.specifications;

import ac.project.sft.dto.TransactionType;
import ac.project.sft.model.Category;
import ac.project.sft.model.ScheduledTransaction;
import ac.project.sft.model.Transaction;
import ac.project.sft.model.Wallet;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ScheduledTransactionSpecifications {

    public static Specification<ScheduledTransaction> startDate(LocalDate date){
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("date"),date);
    }

    public static Specification<ScheduledTransaction> endDate(LocalDate date){
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("date"),date);
    }

    public static Specification<ScheduledTransaction> category(Category category){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category"),category);
    }
    public static Specification<ScheduledTransaction> name(String name){
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"),"%"+name+"%");
    }
    public static Specification<ScheduledTransaction> wallet(Wallet wallet){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("wallet"),wallet);
    }
}
