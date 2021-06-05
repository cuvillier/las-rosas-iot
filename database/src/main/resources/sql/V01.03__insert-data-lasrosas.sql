SET SQL_SAFE_UPDATES = 0;

DELETE FROM t_thg_thing;
DELETE FROM t_thg_thing_type;
DELETE FROM t_thg_gateway;

INSERT INTO t_thg_gateway (gtw_natural_id) VALUES ('RAK7942');
SET @GATEWAY := LAST_INSERT_ID();

INSERT INTO t_thg_thing_type (tty_readable, tty_manufacturer, tty_model) VALUES ('Adeunis-ARF8170BA', 'Adeunis', 'ARF8180BA');
SET @Adeunis_ARF8170BA := LAST_INSERT_ID();

INSERT INTO t_thg_thing_type (tty_readable, tty_manufacturer, tty_model) VALUES ('Adeunis-ARF8170BA', 'Adeunis', 'ARF8170BA');
SET @Adeunis_ARF8170BA := LAST_INSERT_ID();

INSERT INTO t_thg_thing_type (tty_readable, tty_manufacturer, tty_model) VALUES ('Elsys-ERS', 'Elsys', 'ERS');
SET @Elsys_ERS := LAST_INSERT_ID();

INSERT INTO t_thg_thing_type (tty_readable, tty_manufacturer, tty_model) VALUES ('Elsys-MB7389', 'Elsys', 'MB7389');
SET @Elsys_MB7389 := LAST_INSERT_ID();

INSERT INTO t_thg_thing_type (tty_readable, tty_manufacturer, tty_model) VALUES ('MFC88-LW13IO70', 'MFC88', 'LW13IO');
SET @MFC88_LW13IO := LAST_INSERT_ID();

SET @ADN_ARF8180BA := (SELECT tty_techid FROM t_thg_thing_type WHERE tty_manufacturer='Adeunis' AND tty_model='ARF8180BA');

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_discriminator, lor_dev_eui) VALUES
(@GATEWAY, @Elsys_MB7389, 'Water tank level sensor','lor','a81758fffe053159');
SET @THING_LEVEL_WATER_TANK := LAST_INSERT_ID();

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_discriminator, lor_dev_eui) VALUES
(@GATEWAY, @MFC88_LW13IO, 'Water pump','lor','70b3d58ff10184b8');
SET @THING_LEVEL_WATER_TANK := LAST_INSERT_ID();

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_discriminator, lor_dev_eui) VALUES
(@GATEWAY, @MFC88_LW13IO, 'Unused activator','lor','70b3d58ff10184df');
SET @THING_ACTIVATOR := LAST_INSERT_ID();

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_discriminator, lor_dev_eui) VALUES
(@GATEWAY, @ADN_ARF8180BA, 'Temp sensor Adeunis','lor','0018b2200000093c');
SET @THING_TEMP_SENSOR_ADN := LAST_INSERT_ID();

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_discriminator, lor_dev_eui) VALUES
(@GATEWAY, @Elsys_ERS, 'Temp sensor Elsys','lor','a81758fffe0346aa');
SET @THING_TEMP_SENSOR_ELSYS := LAST_INSERT_ID();

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_discriminator, lor_dev_eui) VALUES
(@GATEWAY, @Adeunis_ARF8170BA, 'Dry contact Adeunis','lor','0018b21000003d4f');
SET @THING_DRY_CONTACT := LAST_INSERT_ID();
