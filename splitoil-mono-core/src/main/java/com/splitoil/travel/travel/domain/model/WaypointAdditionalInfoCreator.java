package com.splitoil.travel.travel.domain.model;

import com.splitoil.travel.travel.web.dto.waypoint.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum WaypointAdditionalInfoCreator {
    I;

    WaypointAdditionalInfo from(final WaypointAdditionalInfoAware waypointAdditionalInfoAware) {
        final WaypointAdditionalInfoPayload additionalInfo = waypointAdditionalInfoAware.getAdditionalInfo();
        if (additionalInfo instanceof BoardingPlaceInfoDto) {
            BoardingPlaceInfoDto boardingPlaceInfo = (BoardingPlaceInfoDto) additionalInfo;
            return new PassengerBoardingPlaceInfo(boardingPlaceInfo.getPassengerId(), boardingPlaceInfo.getCarId());
        } else if (additionalInfo instanceof ExitPlaceInfoDto) {
            ExitPlaceInfoDto exitPlaceInfoDto = (ExitPlaceInfoDto) additionalInfo;
            return new PassengerExitPlaceInfo(exitPlaceInfoDto.getPassengerId(), exitPlaceInfoDto.getCarId());
        } else if (additionalInfo instanceof CarRefuelPlaceInfo) {
            final CarRefuelPlaceInfo refuelPlaceDto = (CarRefuelPlaceInfo) additionalInfo;
            return new CarRefuelPlaceInfo(refuelPlaceDto.getCarBeingRefueled());
        } else if (additionalInfo instanceof ReseatPlaceInfoDto) {
            final ReseatPlaceInfoDto reseatPlaceDto = (ReseatPlaceInfoDto) additionalInfo;
            return new PassengerReseatPlaceInfo(reseatPlaceDto.getPassengerId(), reseatPlaceDto.getCarFrom(), reseatPlaceDto.getCarTo());
        } else if (additionalInfo instanceof EmptyInfoDto) {
            return new EmptyInfo();
        }

        log.debug("Additional info type: {} not mapped so EmptyInfo implementation is used", additionalInfo.getClass());
        return new EmptyInfo();
    }
}
