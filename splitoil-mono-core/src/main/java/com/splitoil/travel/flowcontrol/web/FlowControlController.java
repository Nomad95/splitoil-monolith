package com.splitoil.travel.flowcontrol.web;

import com.splitoil.travel.flowcontrol.application.FlowControlFacade;
import com.splitoil.travel.flowcontrol.web.dto.AddGpsPointCommand;
import com.splitoil.travel.flowcontrol.web.dto.AddWaypointReachedGpsPointCommand;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/flowcontrol")
public class FlowControlController {

    private final FlowControlFacade flowControlFacade;

    @PostMapping("/receivegpspoint")
    public void receiveGpsPoint(@RequestBody @Valid @NonNull final AddGpsPointCommand addGpsPointCommand) {
        flowControlFacade.addGpsPoint(addGpsPointCommand);
    }

    @PostMapping("/receivewaypoint")
    public void receiveWaypoint(@RequestBody @Valid @NonNull final AddWaypointReachedGpsPointCommand addGpsPointCommand) {
        flowControlFacade.addWaypointGpsPoint(addGpsPointCommand);
    }

}
