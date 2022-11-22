package ac.project.sft.controller;

import ac.project.sft.controller.payloads.request.CreateScheduledPayload;
import ac.project.sft.dto.ScheduledTransactionDto;
import ac.project.sft.mappers.DtoMapper;
import ac.project.sft.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scheduled")
public class ScheduledTransactionController {

    @Autowired
    ManagerService managerService;
    @Autowired
    DtoMapper mapper;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ScheduledTransactionDto create(@RequestBody CreateScheduledPayload payload, Authentication authentication){
        return mapper.scheduledTransactionToDto(
                managerService.addScheduled(
                    mapper.dtoToWallet(payload.getWallet()),
                    mapper.dtoToScheduledTransaction(payload.getScheduledTransaction()),
                    authentication.getName() )
        );
    }

    @PutMapping
    public ScheduledTransactionDto update(@RequestBody CreateScheduledPayload payload,Authentication authentication){
        return mapper.scheduledTransactionToDto(
                managerService.updateScheduled(
                        mapper.dtoToWallet(payload.getWallet()),
                        mapper.dtoToScheduledTransaction(payload.getScheduledTransaction()),
                        authentication.getName()
                )
        );
    }

    @DeleteMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody CreateScheduledPayload payload, Authentication authentication){
        managerService.deleteScheduled(
                mapper.dtoToWallet(payload.getWallet()),
                mapper.dtoToScheduledTransaction(payload.getScheduledTransaction()),
                authentication.getName()
        );
    }

    @GetMapping("/all")
    public List<ScheduledTransactionDto> getAll(@RequestBody Long id,Authentication authentication){
        return mapper.scheduledTransactionListToDto(
                managerService.getAllScheduled(id,authentication.getName())
        );
    }

}
