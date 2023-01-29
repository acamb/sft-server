package ac.project.sft.service;

import ac.project.sft.exceptions.BadRequestException;
import ac.project.sft.exceptions.NotFoundException;
import ac.project.sft.model.CryptoCurrency;
import ac.project.sft.repository.CryptoCurrencyRepository;
import ac.project.sft.repository.CryptoCurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Service
@Validated
public class CryptoCurrencyService {

    public static final String NOT_FOUND_KEY = "cryptoCurrency.notExists";
    public static final String FOUND_KEY = "cryptoCurrency.exists";
    @Autowired
    CryptoCurrencyRepository repository;

    @Transactional
    public CryptoCurrency create(@Valid CryptoCurrency CryptoCurrency){
        if(CryptoCurrency.getId() != null){
            throw new BadRequestException(FOUND_KEY);
        }
        return repository.save(CryptoCurrency);
    }

    @Transactional
    public CryptoCurrency update(@Valid CryptoCurrency cryptoCurrency){
        CryptoCurrency cryptoCurrencyDb = repository.findById(cryptoCurrency.getId()).orElseThrow(()-> new NotFoundException(NOT_FOUND_KEY));
        cryptoCurrencyDb.setTicker(cryptoCurrency.getTicker());
        cryptoCurrencyDb.setName(cryptoCurrency.getName());
        return repository.save(cryptoCurrencyDb);
    }

    @Transactional
    public void delete(Long id){
        CryptoCurrency cryptoCurrency = repository.findById(id).orElseThrow(()-> new NotFoundException(NOT_FOUND_KEY));
        repository.delete(cryptoCurrency);
    }

    public List<CryptoCurrency> getAll(){
        return (List<CryptoCurrency>) repository.findAll();
    }

    public CryptoCurrency get(Long id){
        return repository.findById(id).orElseThrow(()-> new NotFoundException(NOT_FOUND_KEY));
    }

}
