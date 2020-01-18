--
SET search_path TO public;

--data:

insert into petrol_price (id, uuid, amount, created, currency, lat, lon, name, petrol_type, status)
VALUEs (1, 'e0cbe344-b095-4621-a1ce-69351175daab', 4.59, now(), 'PLN', 100, 25, 'Name', 'BENZINE_95', 'PENDING')

