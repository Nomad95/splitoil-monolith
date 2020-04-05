--
SET search_path TO public;

--data:
insert into application_user (id, aggregate_id, default_currency, email, login, password, roles)
values (1, '41a6fd60-6f64-4f37-beb8-4edbce18b92c', 'PLN', 'admin@splitoil.pl', 'admin', '$2y$10$195iN5erJVPhuWhXZ6o.GeIfHDOX74ZZLpp8G33sRsboD.HqxAhc6
', '{"roles":["ROLE_ADMIN", "ROLE_USER"]}')
