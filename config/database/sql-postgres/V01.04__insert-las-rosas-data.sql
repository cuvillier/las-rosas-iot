DO
$$
DECLARE
gateway integer;
Adeunis_ARF8180BA integer;
Adeunis_ARF8170BA integer;
Elsys_ERS integer;
Elsys_MB7389 integer;
MFC88_LW13IO integer;
THING_LEVEL_WATER_TANK integer;
SWITCH_WATER_PUMP integer;
THING_ACTIVATOR integer;
THING_TEMP_SENSOR_ADN integer;
THING_TEMP_SENSOR_ELSYS integer;
THING_DRY_CONTACT integer;
BEGIN

DELETE FROM t_thg_thing;
DELETE FROM t_thg_gateway;

INSERT INTO t_thg_gateway (gtw_natural_id) VALUES ('RAK7942');
GATEWAY := LASTVAL();

INSERT INTO t_thg_thing_type (tty_readable, tty_manufacturer, tty_model) VALUES ('Adeunis-ARF8170BA', 'Adeunis', 'ARF8180BA');
Adeunis_ARF8180BA := (SELECT tty_techid FROM t_thg_thing_type WHERE tty_model='ARF8180BA');

INSERT INTO t_thg_thing_type (tty_readable, tty_manufacturer, tty_model) VALUES ('Adeunis-ARF8170BA', 'Adeunis', 'ARF8170BA');
Adeunis_ARF8170BA := LASTVAL();

INSERT INTO t_thg_thing_type (tty_readable, tty_manufacturer, tty_model) VALUES ('Elsys-ERS', 'Elsys', 'ERS');
Elsys_ERS := LASTVAL();

INSERT INTO t_thg_thing_type (tty_readable, tty_manufacturer, tty_model) VALUES ('Elsys-MB7389', 'Elsys', 'MB7389');
Elsys_MB7389 := LASTVAL();

INSERT INTO t_thg_thing_type (tty_readable, tty_manufacturer, tty_model) VALUES ('MFC88-LW13IO70', 'MFC88', 'LW13IO');
MFC88_LW13IO := LASTVAL();

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_discriminator, lor_dev_eui) VALUES
(GATEWAY, Elsys_MB7389, 'Water tank level sensor','lor','a81758fffe053159');
THING_LEVEL_WATER_TANK := LASTVAL();

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_discriminator, lor_dev_eui) VALUES
(GATEWAY, MFC88_LW13IO, 'Water pump','lor','70b3d58ff10184b8');
SWITCH_WATER_PUMP := LASTVAL();

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_discriminator, lor_dev_eui) VALUES
(GATEWAY, MFC88_LW13IO, 'Unused activator','lor','70b3d58ff10184df');
THING_ACTIVATOR := LASTVAL();

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_discriminator, lor_dev_eui) VALUES
(GATEWAY, Adeunis_ARF8180BA, 'Temp sensor Adeunis','lor','0018b2200000093c');
THING_TEMP_SENSOR_ADN := LASTVAL();

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_discriminator, lor_dev_eui) VALUES
(GATEWAY, Elsys_ERS, 'Temp sensor Elsys','lor','a81758fffe0346aa');
THING_TEMP_SENSOR_ELSYS := LASTVAL();

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_discriminator, lor_dev_eui) VALUES
(GATEWAY, Adeunis_ARF8170BA, 'Dry contact Adeunis','lor','0018b21000003d4f');
THING_DRY_CONTACT := LASTVAL();
END 
$$
