INSERT INTO t_thg_thing_type (tty_readable, tty_manufacturer, tty_model) VALUES ('Adeunis-ARF8180BA', 'Adeunis', 'ARF8180BA');
INSERT INTO t_thg_thing_type (tty_readable, tty_manufacturer, tty_model) VALUES ('Adeunis-ARF8170BA', 'Adeunis', 'ARF8170BA');
INSERT INTO t_thg_thing_type (tty_readable, tty_manufacturer, tty_model) VALUES ('DRAGINO-LHT65', 'DRAGINO', 'LHT65');
INSERT INTO t_thg_thing_type (tty_readable, tty_manufacturer, tty_model) VALUES ('Elsys-ERS', 'Elsys', 'ERS');
INSERT INTO t_thg_thing_type (tty_readable, tty_manufacturer, tty_model) VALUES ('Elsys-MB7389', 'Elsys', 'MB7389');
INSERT INTO t_thg_thing_type (tty_readable, tty_manufacturer, tty_model) VALUES ('MFC88-LW13IO70', 'MFC88', 'LW13IO');

INSERT INTO t_dtw_space(spa_name) VALUES ('GlobalSpace');

INSERT INTO t_dtw_digital_twin_type(twt_name, twt_discriminator, twt_fk_spa_space)
VALUES ('WaterTank', 'wat', (SELECT spa_techid FROM t_dtw_space WHERE spa_name='GlobalSpace'));

INSERT INTO t_dtw_digital_twin_type(twt_name, twt_discriminator, twt_fk_spa_space)
VALUES ('MultiSwitch', 'mst', (SELECT spa_techid FROM t_dtw_space WHERE spa_name='GlobalSpace'));

INSERT INTO t_dtw_digital_twin_type(twt_name, twt_discriminator, twt_fk_spa_space)
VALUES ('Fridge', 'wat', (SELECT spa_techid FROM t_dtw_space WHERE spa_name='GlobalSpace'));

INSERT INTO t_dtw_reactor_type(rat_bean) VALUES ('WaterTankReactor');
INSERT INTO t_dtw_reactor_type(rat_bean) VALUES ('ForwardReactor');
INSERT INTO t_dtw_reactor_type(rat_bean) VALUES ('MultiSwitchReactor');
INSERT INTO t_dtw_reactor_type(rat_bean) VALUES ('FridgeReactor');

INSERT INTO t_dtw_reactor_receiver_type(rrt_role, rrt_schema, rrt_fk_rat_reactor_type)
	VALUES ('incoming', null, (SELECT rat_techid FROM t_dtw_reactor_type WHERE rat_bean='ForwardReactor'));

INSERT INTO t_dtw_reactor_receiver_type(rrt_role, rrt_schema, rrt_fk_rat_reactor_type)
	VALUES ('level', 'DistanceMeasurement', (SELECT rat_techid FROM t_dtw_reactor_type WHERE rat_bean='WaterTankReactor'));

INSERT INTO t_dtw_reactor_receiver_type(rrt_role, rrt_schema, rrt_fk_rat_reactor_type)
	VALUES ('state', null, (SELECT rat_techid FROM t_dtw_reactor_type WHERE rat_bean='MultiSwitchReactor'));

INSERT INTO t_dtw_reactor_receiver_type(rrt_role, rrt_schema, rrt_fk_rat_reactor_type)
	VALUES ('temperature', 'AirEnvironment', (SELECT rat_techid FROM t_dtw_reactor_type WHERE rat_bean='FridgeReactor'));
