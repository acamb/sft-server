package ac.project.sft.controller.payloads.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
public class PaginatedResponse<T,R> {

    private long totalItems;
    private List<R> items;
    private long totalPages;
    private long currentPage;

    public PaginatedResponse(Page<T> page, Function<List<T>,List<R>> conversionFunction){
        items = conversionFunction.apply(page.toList());
        totalItems = page.getTotalElements();
        currentPage = page.getPageable().getPageNumber();
        totalPages = page.getTotalPages();
    }
}
