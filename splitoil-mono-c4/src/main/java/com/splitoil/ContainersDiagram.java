package com.splitoil;

import com.structurizr.Workspace;
import com.structurizr.model.Container;
import com.structurizr.model.InteractionStyle;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.StaticView;
import com.structurizr.view.ViewSet;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.function.Function;

import static com.splitoil.Protocols.WEBSOCKET;
import static com.splitoil.ViewCreator.setupView;
import static lombok.AccessLevel.PRIVATE;

class CarShareContainersDiagram {
    static InternalContainers create(Workspace workspace, SoftwareSystem petrolShareSystem, ExternalSystems externalSystems) {
        InternalContainers internalContainers = new InternalContainers(petrolShareSystem);
        internalContainers.createUsages(externalSystems);
        setupContainerView(workspace, petrolShareSystem);
        return internalContainers;
    }

    private static void setupContainerView(Workspace workspace, SoftwareSystem eMobility) {
        Function<ViewSet, StaticView> containerViewCreator = views ->
                views.createContainerView(eMobility,
                        "petrol share containers",
                        "petrol share container view");
        setupView(workspace, containerViewCreator);
    }
}

@Getter
@FieldDefaults(makeFinal = true, level = PRIVATE)
class InternalContainers {
    Container bigMonilithicApplication;

    InternalContainers(SoftwareSystem eMobility) {
        bigMonilithicApplication = eMobility.addContainer("Deploy containers", "With lots of modules inside", "JAVA");
    }

    void createUsages(ExternalSystems externalSystems) {
        externalSystems.getCarsGpsGateway().uses(bigMonilithicApplication, "sends cars gps position", WEBSOCKET, InteractionStyle.Asynchronous);
    }
}
