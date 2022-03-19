package com.braindocs.controllers;

import com.braindocs.common.Options;
import com.braindocs.models.HistoryStampModel;
import com.braindocs.services.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/history")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;
    private final Options options;

    @GetMapping("")
    public List<HistoryStampModel> getAllStamps() {
        return historyService.getAllStamps();
    }

    @GetMapping("/search")
    public List<HistoryStampModel> getAllStampsBetween(@RequestParam String since, @RequestParam String before) {
        LocalDate sinceDate = LocalDate.parse(since, options.getDateTimeFormatter());
        LocalDate beforeDate = LocalDate.parse(before, options.getDateTimeFormatter());
        return historyService.findAllByCreateTimeBetween(sinceDate.atStartOfDay(), beforeDate.atStartOfDay());
    }
}
