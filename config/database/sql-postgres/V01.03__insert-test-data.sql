DO
$$
DECLARE
BEGIN

INSERT INTO t_thg_gateway (gtw_natural_id, gtw_type_name) VALUES ('TestGateway', 'RAK7249');

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_discriminator, lor_dev_eui)
	VALUES ( 
		(SELECT gtw_techid FROM t_thg_gateway WHERE gtw_natural_id='TestGateway'),
		(SELECT tty_techid FROM t_thg_thing_type WHERE tty_manufacturer='Elsys' AND tty_model='MB7389'),
		'Test Elsys MB7389',
		'lor',
		'0100000000000001');

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_discriminator, lor_dev_eui) VALUES
((SELECT gtw_techid FROM t_thg_gateway WHERE gtw_natural_id='TestGateway'), (SELECT tty_techid FROM t_thg_thing_type WHERE tty_manufacturer='Elsys' AND tty_model='ERS'), 'Test ELSYS ERS','lor','0100000000000002');

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_discriminator, lor_dev_eui) VALUES
((SELECT gtw_techid FROM t_thg_gateway WHERE gtw_natural_id='TestGateway'), (SELECT tty_techid FROM t_thg_thing_type WHERE tty_manufacturer='MFC88' AND tty_model='LW13IO'), 'Test MFC88 LW13IO','lor','0200000000000001');

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_discriminator, lor_dev_eui) VALUES
((SELECT gtw_techid FROM t_thg_gateway WHERE gtw_natural_id='TestGateway'), (SELECT tty_techid FROM t_thg_thing_type WHERE tty_manufacturer='Adeunis' AND tty_model='ARF8180BA'), 'Test Adeunis ARF8180BA','lor','0300000000000001');

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_discriminator, lor_dev_eui) VALUES
((SELECT gtw_techid FROM t_thg_gateway WHERE gtw_natural_id='TestGateway'), (SELECT tty_techid FROM t_thg_thing_type WHERE tty_manufacturer='Adeunis' AND tty_model='ARF8170BA'), 'Test Adeunis ARF8170BA','lor','0300000000000002');

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_discriminator, lor_dev_eui) VALUES
((SELECT gtw_techid FROM t_thg_gateway WHERE gtw_natural_id='TestGateway'), (SELECT tty_techid FROM t_thg_thing_type WHERE tty_manufacturer='DRAGINO' AND tty_model='LHT65'), 'Test DRAGINO LHT65','lor','0400000000000001');

INSERT INTO t_dtw_digital_twin_type(twt_name, twt_discriminator, twt_fk_spa_space)
VALUES ('TestDynamic1', 'dyn', (SELECT spa_techid FROM t_dtw_space WHERE spa_name='GlobalSpace'));

INSERT INTO t_dtw_digital_twin_type(twt_name, twt_discriminator, twt_fk_spa_space)
VALUES ('TestDynamic2', 'dyn', (SELECT spa_techid FROM t_dtw_space WHERE spa_name='GlobalSpace'));

/* Create a Water tank dtwin with a level sensor */
INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_discriminator, lor_dev_eui) VALUES
((SELECT gtw_techid FROM t_thg_gateway WHERE gtw_natural_id='TestGateway'), (SELECT tty_techid FROM t_thg_thing_type WHERE tty_manufacturer='Elsys' AND tty_model='MB7389'), 'TestWaterTank sensor','lor','0100000000000010');

INSERT INTO t_dtw_digital_twin(
	twi_discriminator, twi_name, twi_fk_twt_type)
	VALUES (
		'wat', 
		'TestWaterTank', 
		(SELECT twt_techid FROM t_dtw_digital_twin_type WHERE twt_name='WaterTank')
	);

INSERT INTO t_dtw_water_tank(twi_techid, wat_length, wat_radius, wat_sensor_alt)
	VALUES (
		(SELECT twi_techid FROM t_dtw_digital_twin WHERE twi_name='TestWaterTank'),
		1.0,
		1.0,
		0.1
	);

INSERT INTO t_dtw_reactor_receiver(
	rre_discriminator, rre_readable, rre_sensor, rre_fk_twi_twin, rre_fk_rrt_type, rvt_fk_thg_thing)
	VALUES (
		'thg',
		'Water tank level',
		null,
		(SELECT twi_techid FROM t_dtw_digital_twin WHERE twi_name = 'TestWaterTank'),
		(
			SELECT rrt_techid FROM t_dtw_reactor_receiver_type
		 	JOIN t_dtw_reactor_type on t_dtw_reactor_receiver_type.rrt_fk_rat_reactor_type=t_dtw_reactor_type.rat_techid
			WHERE rat_bean='WaterTankReactor'
		),
		(SELECT thg_techid FROM t_thg_thing WHERE thg_readable = 'TestWaterTank sensor')
	);

/* Create Fridge dtwin with a temperature sensor */
INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_discriminator, lor_dev_eui) VALUES
((SELECT gtw_techid FROM t_thg_gateway WHERE gtw_natural_id='TestGateway'), (SELECT tty_techid FROM t_thg_thing_type WHERE tty_manufacturer='DRAGINO' AND tty_model='LHT65'), 'TestFridge sensor','lor','0400000000000010');

INSERT INTO t_dtw_digital_twin(
	twi_discriminator, twi_name, twi_fk_twt_type)
	VALUES (
		'fri', 
		'TestFridge',
		(SELECT twt_techid FROM t_dtw_digital_twin_type WHERE twt_name='Fridge')
	);

INSERT INTO t_dtw_fridge(twi_techid, fri_width, fri_height, fri_length, fri_inside_temp_min, fri_inside_temp_max)
VALUES (
		(SELECT twi_techid FROM t_dtw_digital_twin WHERE twi_name='TestFridge'),
		0.9,
		1.8,
		0.6,
		0.0,
		10.0
	);
INSERT INTO t_dtw_reactor_receiver(
	rre_discriminator, rre_readable, rre_sensor, rre_fk_twi_twin, rre_fk_rrt_type, rvt_fk_thg_thing)
	VALUES (
		'thg',
		'Fridge temperature/humidity Int/Ext',
		null,
		(
			SELECT twi_techid FROM t_dtw_digital_twin WHERE twi_name = 'TestFridge'
		),
		(
			SELECT rrt_techid FROM t_dtw_reactor_receiver_type
		 	JOIN t_dtw_reactor_type on t_dtw_reactor_receiver_type.rrt_fk_rat_reactor_type=t_dtw_reactor_type.rat_techid
			WHERE rat_bean='FridgeReactor'
		),
		(
			SELECT thg_techid FROM t_thg_thing WHERE thg_readable = 'TestFridge sensor'
		)
	);

/* Create a MultiSwith dtwin with an electrical relay sensor */
INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_discriminator, lor_dev_eui) VALUES
((SELECT gtw_techid FROM t_thg_gateway WHERE gtw_natural_id='TestGateway'), (SELECT tty_techid FROM t_thg_thing_type WHERE tty_manufacturer='MFC88' AND tty_model='LW13IO'), 'TestMultiSwitch sensor','lor','0200000000000010');

INSERT INTO t_dtw_digital_twin(
	twi_discriminator, twi_name, twi_fk_twt_type)
	VALUES (
		'msw', 
		'TestMultiSwitch', 
		(SELECT twt_techid FROM t_dtw_digital_twin_type WHERE twt_name='MultiSwitch')
	);

INSERT INTO t_dtw_multiswitch(twi_techid, msw_state, msw_expected_state, msw_connected, msw_state_when_connect)
	VALUES (
		(SELECT twi_techid FROM t_dtw_digital_twin WHERE twi_name='TestMultiSwitch'),
		0,
		0, 
		0,
		1);

INSERT INTO t_dtw_reactor_receiver(
	rre_discriminator, rre_readable, rre_sensor, rre_fk_twi_twin, rre_fk_rrt_type, rvt_fk_thg_thing)
	VALUES (
		'thg',
		'TestMultiSwitch state change',
		null,
		(
			SELECT twi_techid FROM t_dtw_digital_twin WHERE twi_name = 'TestMultiSwitch'
		),
		(
			SELECT rrt_techid FROM t_dtw_reactor_receiver_type
		 	JOIN t_dtw_reactor_type on t_dtw_reactor_receiver_type.rrt_fk_rat_reactor_type=t_dtw_reactor_type.rat_techid
			WHERE rat_bean='MultiSwitchReactor'
		),
		(
			SELECT thg_techid FROM t_thg_thing WHERE thg_readable = 'TestMultiSwitch sensor'
		)
	);

INSERT INTO public.t_dtw_digital_twin(
	twi_discriminator, twi_name, twi_fk_twt_type)
	VALUES (
		'dyn', 
		'TestDynamic1', 
		(SELECT twt_techid FROM t_dtw_digital_twin_type WHERE twt_name='TestDynamic1')
	);

INSERT INTO public.t_dtw_digital_twin(
	twi_discriminator, twi_name, twi_fk_twt_type)
	VALUES (
		'dyn',
		'TestDynamic2', 
		(SELECT twt_techid FROM t_dtw_digital_twin_type WHERE twt_name='TestDynamic2')
	);
END
$$
