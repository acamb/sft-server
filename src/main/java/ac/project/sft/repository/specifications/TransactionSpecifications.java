package ac.project.sft.repository.specifications;

import ac.project.sft.dto.TransactionType;
import ac.project.sft.model.Category;
import ac.project.sft.model.Transaction;
import ac.project.sft.model.Wallet;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;

public class TransactionSpecifications {

    public static Specification<Transaction> startDate(LocalDate date){
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("date"),date);
    }

    public static Specification<Transaction> endDate(LocalDate date){
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("date"),date);
    }

    public static Specification<Transaction> category(Category category){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category"),category);
    }

    public static Specification<Transaction> type(TransactionType type){
        if(type.equals(TransactionType.INCOME)){
            return (root, query, criteriaBuilder) -> criteriaBuilder.ge(root.get("amount"),0);
        }
        else{
            return (root, query, criteriaBuilder) -> criteriaBuilder.le(root.get("amount"),0);
        }
    }

    public static Specification<Transaction> name(String name){
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"),"%"+name+"%");
    }

    public static Specification<Transaction> wallet(Wallet wallet){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("wallet"),wallet);
    }
}
