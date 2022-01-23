ALTER TABLE worker
    ALTER phone_number SET NOT NULL,
    ADD CONSTRAINT worker_phone_number UNIQUE (phone_number);