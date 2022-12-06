INSERT INTO VEHICLE_TYPES (NAME, PRICE_PER_KM) VALUES
                                                   ('STANDARDNO', 2),
                                                   ('LUKSUZNO', 5),
                                                   ('KOMBI', 7);

INSERT INTO USERS (ADDRESS,EMAIL,IS_ACTIVATED,IS_BLOCKED,NAME,PASSWORD,PROFILE_PICTURE,SURNAME,TELEPHONE_NUMBER) VALUES
                  ('Bulevar oslobođenja 74', 'imenko@mail.com', true, false, 'Imenko', 'neprobojna_lozinka', 'XYZ', 'Prezimić', '065338295504');

INSERT INTO PASSENGERS VALUES 1;

INSERT INTO USER_ACTIVATIONS(DATE_CREATED,MINUTES_VALID) VALUES
    (CURRENT_TIMESTAMP,25);

INSERT INTO USERS (ADDRESS,EMAIL,IS_ACTIVATED,IS_BLOCKED,NAME,PASSWORD,PROFILE_PICTURE,SURNAME,TELEPHONE_NUMBER,USER_ACTIVATION_ID) VALUES
    ('Adresa','mail_inactive@mail.com',false,false,'Imenko','neprobojna_lozinka','XYZ','Prezimić','065338295504',1);

INSERT INTO VEHICLES(VEHICLE_TYPE) VALUES(1);

INSERT INTO DRIVERS(ID,VEHICLE_ID) VALUES (2,1);



