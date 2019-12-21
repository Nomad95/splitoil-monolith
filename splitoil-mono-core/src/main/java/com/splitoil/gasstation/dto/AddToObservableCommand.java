package com.splitoil.gasstation.dto;

public interface AddToObservableCommand {

    Geo getGeoPoint();

    String getGasStationName();

    Long getDriverId();
}
