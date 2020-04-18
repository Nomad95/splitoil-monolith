--
SET search_path TO public;

--data:
insert into application_user (id, aggregate_id, default_currency, email, login, password, roles)
values (2, '0ea7db01-5f68-409b-8130-e96e8d96060a', 'PLN', 'driver1@splitoil.pl', 'driver1', '$2y$10$195iN5erJVPhuWhXZ6o.GeIfHDOX74ZZLpp8G33sRsboD.HqxAhc6',
        '{"roles":["ROLE_ADMIN", "ROLE_USER"]}')
