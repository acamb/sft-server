package ac.project.sft.service;

import ac.project.sft.model.ScheduledTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class SchedulerService {

    @Autowired
    ManagerService managerService;
    @Autowired
    ScheduledTransactionService scheduledTransactionService;

    Logger logger = LoggerFactory.getLogger(SchedulerService.class);

    public void executeScheduledTransactions(){
        List<ScheduledTransaction> transactionList = scheduledTransactionService.getScheduledTransactionToBeExecuted(LocalDate.now());
        for(ScheduledTransaction scheduled : transactionList){
            try{
                managerService.processScheduledTransaction(scheduled);
            }
            catch(Exception ex){
                logger.error("Error processing schedule transaction: " + ex.getMessage(),ex);
            }
        }

    }
}
