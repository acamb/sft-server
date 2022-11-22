package ac.project.sft.service;

import ac.project.sft.exceptions.BadRequestException;
import ac.project.sft.exceptions.NotFoundException;
import ac.project.sft.mappers.DtoMapper;
import ac.project.sft.model.Category;
import ac.project.sft.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;
import javax.validation.Valid;
import java.util.List;

@Service
@Validated
public class CategoryService {

    public static final String NOT_FOUND_KEY = "category.not.exists";
    @Autowired
    CategoryRepository repository;

    @Transactional
    public Category create(@Valid Category category){
        if(category.getId() != null){
            throw new BadRequestException("category.exists");
        }
        return repository.save(category);
    }

    @Transactional
    public Category update(@Valid Category category){
        Category categoryDb = repository.findById(category.getId()).orElseThrow(()-> new NotFoundException(NOT_FOUND_KEY));
        categoryDb.setDescription(category.getDescription());
        categoryDb.setName(category.getName());
        return repository.save(categoryDb);
    }

    @Transactional
    public void delete(Long id){
        Category category = repository.findById(id).orElseThrow(()-> new NotFoundException(NOT_FOUND_KEY));
        repository.delete(category);
    }

    public List<Category> getAll(){
        return (List<Category>) repository.findAll();
    }

    public Category get(Long id){
        return repository.findById(id).orElseThrow(()-> new NotFoundException(NOT_FOUND_KEY));
    }

}
