--
SET search_path TO public;

--data:

insert into lobby (id, aggregate_id, name, driver_id, lobby_status, top_rate_per1km, travel_currency, cars)
values (1, '148091b1-c0f0-4a3e-9b0d-569e05cfcd0f', 'Lublin->Krasnystaw', '771a9985-dace-47bd-8e2b-5b36b18b04b2', 'IN_CONFIGURATION', '0', 'PLN',
        '{
  "cars": {
    "b9574b12-8ca1-4779-aab8-a25192e33739": {
      "carId": {
        "carId": "b9574b12-8ca1-4779-aab8-a25192e33739"
      },
      "driverId": "0ea7db01-5f68-409b-8130-e96e8d96060a",
      "numberOfSeats": 5,
      "seatsOccupied": 2
    },
    "2e1d5068-61e6-445a-a65e-9b8117b707d6": {
      "carId": {
        "carId": "2e1d5068-61e6-445a-a65e-9b8117b707d6"
      },
      "driverId": "c13d02de-c7a4-4ab0-9f3f-7ee0768b4a5f",
      "numberOfSeats": 5,
      "seatsOccupied": 1
    }
  }
}')