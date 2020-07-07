package com.splitoil.travel.flowcontrol.web;

import com.splitoil.travel.flowcontrol.application.FlowControlQuery;
import com.splitoil.travel.flowcontrol.web.dto.GpsPoints;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/flowcontrol/query")
public class FlowControlQueryController {

    private final FlowControlQuery flowControlQuery;

    @GetMapping("/gps/{id}")
    public GpsPoints receiveGpsPoint(@PathVariable("id") @NonNull final UUID flowControlId) {
        return GpsPoints.of(flowControlQuery.getGpsPointsList(flowControlId));
    }

}
