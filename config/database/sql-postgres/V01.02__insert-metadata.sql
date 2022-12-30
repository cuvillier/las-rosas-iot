DO
$$
BEGIN

DELETE FROM t_thg_thing_type;

INSERT INTO t_thg_thing_type (tty_readable, tty_manufacturer, tty_model) VALUES ('Adeunis-ARF8180BA', 'Adeunis', 'ARF8180BA');
INSERT INTO t_thg_thing_type (tty_readable, tty_manufacturer, tty_model) VALUES ('Adeunis-ARF8170BA', 'Adeunis', 'ARF8170BA');
INSERT INTO t_thg_thing_type (tty_readable, tty_manufacturer, tty_model) VALUES ('DRAGINO-LHT65', 'DRAGINO', 'LHT65');
INSERT INTO t_thg_thing_type (tty_readable, tty_manufacturer, tty_model) VALUES ('Elsys-ERS', 'Elsys', 'ERS');
INSERT INTO t_thg_thing_type (tty_readable, tty_manufacturer, tty_model) VALUES ('Elsys-MB7389', 'Elsys', 'MB7389');
INSERT INTO t_thg_thing_type (tty_readable, tty_manufacturer, tty_model) VALUES ('MFC88-LW13IO70', 'MFC88', 'LW13IO');

END
$$
