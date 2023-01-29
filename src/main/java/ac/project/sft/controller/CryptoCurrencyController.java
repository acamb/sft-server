package ac.project.sft.controller;

import ac.project.sft.dto.CategoryDto;
import ac.project.sft.dto.CryptoCurrencyDto;
import ac.project.sft.mappers.DtoMapper;
import ac.project.sft.model.Category;
import ac.project.sft.model.CryptoCurrency;
import ac.project.sft.service.CryptoCurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cryptocurrency")
public class CryptoCurrencyController {

    @Autowired
    CryptoCurrencyService cryptoCurrencyService;
    @Autowired
    DtoMapper mapper;

    @PostMapping("/")
    @ResponseStatus(value= HttpStatus.NO_CONTENT)
    public CryptoCurrencyDto create(@RequestBody CryptoCurrencyDto categoryDto){
        CryptoCurrency result = cryptoCurrencyService.create(mapper.dtoToCryptoCurrency(categoryDto));
        return mapper.cryptoCurrencyToDto(result);
    }

    @PutMapping("/")
    public CryptoCurrencyDto update(@RequestBody CryptoCurrencyDto categoryDto){
        CryptoCurrency result = cryptoCurrencyService.update(mapper.dtoToCryptoCurrency(categoryDto));
        return mapper.cryptoCurrencyToDto(result);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        cryptoCurrencyService.delete(id);
    }

    @GetMapping("/all")
    public List<CryptoCurrencyDto> getAll(){
        return mapper.cryptoCurrencyListToDto(cryptoCurrencyService.getAll());
    }

    @GetMapping("/{id}")
    public CryptoCurrencyDto get(@PathVariable Long id){
        return mapper.cryptoCurrencyToDto(cryptoCurrencyService.get(id));
    }

}
