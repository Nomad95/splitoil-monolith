package com.splitoil.travel.flowcontrol.web;

import com.splitoil.travel.travel.application.TravelFlowFacade;
import com.splitoil.travel.travel.web.dto.RouteDto;
import com.splitoil.travel.travel.web.dto.SelectTravelBeginningCommand;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/flowcontrol")
public class FlowControlController {

    private final TravelFlowFacade travelFlowFacade;

    @PostMapping("route/beginning")
    public EntityModel<RouteDto> selectTravelBeginning(@RequestBody @Valid @NonNull final SelectTravelBeginningCommand selectTravelBeginningCommand) {
        travelFlowFacade.selectTravelBeginning(selectTravelBeginningCommand);
        final RouteDto newRoute = travelFlowFacade.getRoute(selectTravelBeginningCommand.getTravelId());

        final Link self = linkTo(FlowControlController.class).slash("route").slash("beginning").withSelfRel();

        return new EntityModel<>(newRoute)
            .add(self);
    }

    private Link addStartTravelLink() {
        return linkTo(FlowControlController.class).slash("start").withRel("start-travel");
    }

}
