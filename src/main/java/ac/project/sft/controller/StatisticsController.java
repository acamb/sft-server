package ac.project.sft.controller;

import ac.project.sft.dto.WalletPrevisionDto;
import ac.project.sft.dto.WalletStatisticDto;
import ac.project.sft.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    StatisticsService service;

    @GetMapping("/{wallet}")
    public WalletStatisticDto getStatistics(
            @PathVariable("wallet") Long wallet,
            @Param("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @Param("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate
            ){
        return service.getStatistics(wallet,startDate,endDate);
    }

    @GetMapping("/prevision/{wallet}")
    public WalletPrevisionDto getPrevision(
            @PathVariable("wallet") Long wallet,
            @Param("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @Param("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate
    ){
        return service.getPrevisions(wallet,startDate,endDate);
    }
}
