
INSERT INTO t_thg_thing_type (tty_readable, tty_manufacturer, tty_model, tty_battery_min_percentage) VALUES ('Adeunis-ARF8180BA', 'Adeunis', 'ARF8180BA', 0.22);
INSERT INTO t_thg_thing_type (tty_readable, tty_manufacturer, tty_model, tty_battery_min_percentage) VALUES ('Adeunis-ARF8170BA', 'Adeunis', 'ARF8170BA', 0.22);
INSERT INTO t_thg_thing_type (tty_readable, tty_manufacturer, tty_model, tty_battery_min_percentage) VALUES ('DRAGINO-LHT65', 'DRAGINO', 'LHT65', 0.22);
INSERT INTO t_thg_thing_type (tty_readable, tty_manufacturer, tty_model, tty_battery_min_percentage) VALUES ('Elsys-ERS', 'Elsys', 'ERS', 0.22);
INSERT INTO t_thg_thing_type (tty_readable, tty_manufacturer, tty_model, tty_battery_min_percentage) VALUES ('Elsys-MB7389', 'Elsys', 'MB7389', 0.22);
INSERT INTO t_thg_thing_type (tty_readable, tty_manufacturer, tty_model, tty_battery_min_percentage) VALUES ('MFC88-LW13IO70', 'MFC88', 'LW13IO', 0.22);

INSERT INTO t_thg_gateway (gtw_naturalid) VALUES ('TestGateway');

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_naturalid, thg_admin_state, thg_connection_timeout)
	VALUES ( 
		(SELECT gtw_techid FROM t_thg_gateway WHERE gtw_naturalid='TestGateway'),
		(SELECT tty_techid FROM t_thg_thing_type WHERE tty_manufacturer='Elsys' AND tty_model='MB7389'),
		'Test Elsys MB7389',
		
		'0100000000000001', 'CONNECTED', 12345);

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_naturalid, thg_admin_state, thg_connection_timeout) VALUES
((SELECT gtw_techid FROM t_thg_gateway WHERE gtw_naturalid='TestGateway'), (SELECT tty_techid FROM t_thg_thing_type WHERE tty_manufacturer='Elsys' AND tty_model='ERS'), 'Test ELSYS ERS','0100000000000002', 'CONNECTED', 12345);

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_naturalid, thg_admin_state, thg_connection_timeout) VALUES
((SELECT gtw_techid FROM t_thg_gateway WHERE gtw_naturalid='TestGateway'), (SELECT tty_techid FROM t_thg_thing_type WHERE tty_manufacturer='MFC88' AND tty_model='LW13IO'), 'Test MFC88 LW13IO','0200000000000001', 'CONNECTED', 12345);

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_naturalid, thg_admin_state, thg_connection_timeout) VALUES
((SELECT gtw_techid FROM t_thg_gateway WHERE gtw_naturalid='TestGateway'), (SELECT tty_techid FROM t_thg_thing_type WHERE tty_manufacturer='Adeunis' AND tty_model='ARF8180BA'), 'Test Adeunis ARF8180BA','0300000000000001', 'CONNECTED', 12345);

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_naturalid, thg_admin_state, thg_connection_timeout) VALUES
((SELECT gtw_techid FROM t_thg_gateway WHERE gtw_naturalid='TestGateway'), (SELECT tty_techid FROM t_thg_thing_type WHERE tty_manufacturer='Adeunis' AND tty_model='ARF8170BA'), 'Test Adeunis ARF8170BA','0300000000000002', 'CONNECTED', 12345);

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_naturalid, thg_admin_state, thg_connection_timeout) VALUES
((SELECT gtw_techid FROM t_thg_gateway WHERE gtw_naturalid='TestGateway'), (SELECT tty_techid FROM t_thg_thing_type WHERE tty_manufacturer='DRAGINO' AND tty_model='LHT65'), 'Test DRAGINO LHT65','0400000000000001', 'CONNECTED', 12345);
