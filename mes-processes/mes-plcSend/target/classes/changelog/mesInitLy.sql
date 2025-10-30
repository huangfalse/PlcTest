-- liquibase formatted sql

-- changeset huang:20250708121
-- preconditions onFail:CONTINUE onError:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM raw_glass_storage_station;
INSERT INTO raw_glass_storage_station (id, device_id, slot, shelf, enable_state)
VALUES (1, 1, 1, 1, 1);
INSERT INTO raw_glass_storage_station (id, device_id, slot, shelf, enable_state)
VALUES (2, 2, 2, 2, 1);
INSERT INTO raw_glass_storage_station (id, device_id, slot, shelf, enable_state)
VALUES (3, 2, 3, 3, 1);
INSERT INTO raw_glass_storage_station (id, device_id, slot, shelf, enable_state)
VALUES (4, 3, 4, 4, 1);
INSERT INTO raw_glass_storage_station (id, device_id, slot, shelf, enable_state)
VALUES (5, 3, 5, 5, 1);
INSERT INTO raw_glass_storage_station (id, device_id, slot, shelf, enable_state)
VALUES (6, 4, 6, 6, 1);
INSERT INTO raw_glass_storage_station (id, device_id, slot, shelf, enable_state)
VALUES (7, 4, 7, 7, 1);
INSERT INTO raw_glass_storage_station (id, device_id, slot, shelf, enable_state)
VALUES (8, 5, 8, 8, 1);
INSERT INTO raw_glass_storage_station (id, device_id, slot, shelf, enable_state)
VALUES (9, 5, 9, 9, 1);
INSERT INTO raw_glass_storage_station (id, device_id, slot, shelf, enable_state)
VALUES (10, 6, 10, 10, 1);
INSERT INTO raw_glass_storage_station (id, device_id, slot, shelf, enable_state)
VALUES (11, 6, 11, 11, 1);
INSERT INTO raw_glass_storage_station (id, device_id, slot, shelf, enable_state)
VALUES (12, 7, 12, 12, 1);
INSERT INTO raw_glass_storage_station (id, device_id, slot, shelf, enable_state)
VALUES (13, 7, 13, 13, 1);
INSERT INTO raw_glass_storage_station (id, device_id, slot, shelf, enable_state)
VALUES (14, 8, 14, 14, 1);
INSERT INTO raw_glass_storage_station (id, device_id, slot, shelf, enable_state)
VALUES (15, 8, 15, 15, 1);
INSERT INTO raw_glass_storage_station (id, device_id, slot, shelf, enable_state)
VALUES (16, 9, 16, 16, 1);
INSERT INTO raw_glass_storage_station (id, device_id, slot, shelf, enable_state)
VALUES (17, 10, 17, 17, 1);
INSERT INTO raw_glass_storage_station (id, device_id, slot, shelf, enable_state)
VALUES (18, 11, 18, 18, 1);
INSERT INTO raw_glass_storage_station (id, device_id, slot, shelf, enable_state)
VALUES (19, 11, 19, 19, 1);
INSERT INTO raw_glass_storage_station (id, device_id, slot, shelf, enable_state)
VALUES (20, 12, 20, 20, 1);
INSERT INTO raw_glass_storage_station (id, device_id, slot, shelf, enable_state)
VALUES (21, 12, 21, 21, 1);
INSERT INTO raw_glass_storage_station (id, device_id, slot, shelf, enable_state)
VALUES (22, 13, 22, 22, 1);
INSERT INTO raw_glass_storage_station (id, device_id, slot, shelf, enable_state)
VALUES (23, 13, 23, 23, 1);
INSERT INTO raw_glass_storage_station (id, device_id, slot, shelf, enable_state)
VALUES (24, 14, 24, 24, 1);
INSERT INTO raw_glass_storage_station (id, device_id, slot, shelf, enable_state)
VALUES (25, 14, 25, 25, 1);
INSERT INTO raw_glass_storage_station (id, device_id, slot, shelf, enable_state)
VALUES (26, 15, 26, 26, 1);
INSERT INTO raw_glass_storage_station (id, device_id, slot, shelf, enable_state)
VALUES (27, 15, 27, 27, 1);
INSERT INTO raw_glass_storage_station (id, device_id, slot, shelf, enable_state)
VALUES (28, 16, 28, 28, 1);
INSERT INTO raw_glass_storage_station (id, device_id, slot, shelf, enable_state)
VALUES (29, 16, 29, 29, 1);
INSERT INTO raw_glass_storage_station (id, device_id, slot, shelf, enable_state)
VALUES (30, 17, 30, 30, 1);
INSERT INTO raw_glass_storage_station (id, device_id, slot, shelf, enable_state)
VALUES (31, 17, 31, 31, 1);
INSERT INTO raw_glass_storage_station (id, device_id, slot, shelf, enable_state)
VALUES (32, 18, 32, 32, 1);
INSERT INTO raw_glass_storage_station (id, device_id, slot, shelf, enable_state)
VALUES (33, 19, 34, 34, 1);
INSERT INTO raw_glass_storage_station (id, device_id, slot, shelf, enable_state)
VALUES (34, 20, 36, 36, 1);

-- changeset huang:20250807001
-- preconditions onFail:CONTINUE onError:CONTINUE
-- precondition-sql-check expectedResult:0 select count(*) from raw_glass_storage_details
INSERT INTO raw_glass_storage_details (device_id, slot, state, shelf)
select device_id, slot, 100, shelf
from raw_glass_storage_station;
INSERT INTO raw_glass_storage_details (device_id, slot, state, shelf)
select device_id, slot, 100, shelf
from raw_glass_storage_station;
