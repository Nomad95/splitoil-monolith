package com.splitoil.travel.flowcontrol.application;

import com.splitoil.travel.flowcontrol.application.command.CreateFlowControlCommand;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CreateFlowControlCommandListener {

    private final FlowControlFacade flowControlFacade;

    @Async
    @EventListener
    public void handle(final @NonNull CreateFlowControlCommand createFlowControlCommand) {
        flowControlFacade.createFlowControl(createFlowControlCommand);
    }
}
