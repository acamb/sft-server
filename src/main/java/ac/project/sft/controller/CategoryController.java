package ac.project.sft.controller;

import ac.project.sft.dto.CategoryDto;
import ac.project.sft.mappers.DtoMapper;
import ac.project.sft.model.Category;
import ac.project.sft.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;
    @Autowired
    DtoMapper mapper;

    @PostMapping("/")
    @ResponseStatus(value= HttpStatus.CREATED)
    public CategoryDto create(@RequestBody CategoryDto categoryDto){
        Category result = categoryService.create(mapper.dtoToCategory(categoryDto));
        return mapper.categoryToDto(result);
    }

    @PutMapping("/")
    public CategoryDto update(@RequestBody CategoryDto categoryDto){
        Category result = categoryService.update(mapper.dtoToCategory(categoryDto));
        return mapper.categoryToDto(result);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        categoryService.delete(id);
    }

    @GetMapping("/all")
    public List<CategoryDto> getAll(){
        return mapper.categoryListToDto(categoryService.getAll());
    }

    @GetMapping("/{id}")
    public CategoryDto get(@PathVariable Long id){
        return mapper.categoryToDto(categoryService.get(id));
    }

}
