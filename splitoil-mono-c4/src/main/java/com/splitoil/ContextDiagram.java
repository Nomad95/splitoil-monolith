package com.splitoil;

import com.structurizr.Workspace;
import com.structurizr.model.InteractionStyle;
import com.structurizr.model.Model;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.PaperSize;
import com.structurizr.view.StaticView;
import com.structurizr.view.ViewSet;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.function.Function;

import static lombok.AccessLevel.PRIVATE;

class ContextDiagram {
    static ExternalSystems create(Workspace workspace, Model model, SoftwareSystem petrolShare) {
        ExternalSystems externalSystems = new ExternalSystems(model);
        externalSystems.createUsages(petrolShare);
        setupContextView(workspace, petrolShare);
        return externalSystems;
    }

    private static void setupContextView(Workspace workspace, SoftwareSystem eMobility) {
        Function<ViewSet, StaticView> contextViewCrator = views ->
                views.createSystemContextView(
                        eMobility,
                        "context diagram",
                        "context view");
        ViewCreator.setupView(workspace, contextViewCrator, PaperSize.A5_Landscape);
    }

}

@Getter
@FieldDefaults(makeFinal = true, level = PRIVATE)
class ExternalSystems {
    SoftwareSystem carsGpsGateway;
    SoftwareSystem mobileApp;
    Person splitoilUser;


    ExternalSystems(Model model) {
        carsGpsGateway = model.addSoftwareSystem("Cars GPS gateway", "Gateway sending GPS location of cars");
        mobileApp = model.addSoftwareSystem("Mobile app", "Mobile app for Splitoil users");
        splitoilUser = model.addPerson("Splitoil User", "A user");
    }

    SoftwareSystem createUsages(SoftwareSystem pertrolShareSystem) {
        carsGpsGateway.uses(pertrolShareSystem, "send gps location", Protocols.WEBSOCKET, InteractionStyle.Asynchronous);
        splitoilUser.uses(mobileApp, "interact", Protocols.INPROCESS, InteractionStyle.Synchronous);
        mobileApp.uses(pertrolShareSystem, "interact", Protocols.REST, InteractionStyle.Synchronous);
        return pertrolShareSystem;
    }

}
