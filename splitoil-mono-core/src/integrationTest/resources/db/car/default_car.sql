--
SET search_path TO public;

--data:

insert into car (id, aggregate_id, brand, calculated_avg1km_cost, calculated_avg_fuel_consumption, capacity, petrol_type, manual_avg_fuel_consumption,
 mileage, name, number_of_travels, driver_id, seat_count)
values (1, 'b9574b12-8ca1-4779-aab8-a25192e33739', 'Audi', 0, 0, 0, null, 0, 0, 'A4', 0, '0ea7db01-5f68-409b-8130-e96e8d96060a', 5);