INSERT INTO t_thg_gateway (gtw_naturalid, gtw_type, gtw_driver) VALUES ('RAK7249-finca-las-rosas', 'LORAWAN', 'RAK');

/* Insert Things */
INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_naturalid)
	VALUES (
		(SELECT gtw_techid FROM t_thg_gateway WHERE gtw_naturalid='RAK7249-finca-las-rosas'),
		(SELECT tty_techid FROM t_thg_thing_type WHERE
						tty_manufacturer='Adeunis' AND
						tty_model='ARF8180BA'
		),
		'Adeunis/ARF8180BA/0018B2200000093C',
		
		'0018B2200000093C');

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_naturalid)
	VALUES (
		(SELECT gtw_techid FROM t_thg_gateway WHERE gtw_naturalid='RAK7249-finca-las-rosas'),
		(SELECT tty_techid FROM t_thg_thing_type WHERE
						tty_manufacturer='Adeunis' AND
						tty_model='ARF8170BA'
		),
		'Adeunis/ARF8170BA/0018b21000003d4f',
		
		'0018b21000003d4f');
		
INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_naturalid)
	VALUES ( 
		(SELECT gtw_techid FROM t_thg_gateway WHERE gtw_naturalid='RAK7249-finca-las-rosas'),
		(SELECT tty_techid FROM t_thg_thing_type WHERE
						tty_manufacturer='Elsys' AND
						tty_model='ERS'
		),
		'Elsys/ERS/a81758fffe0346aa',
		
		'a81758fffe0346aa');

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_naturalid)
	VALUES ( 
		(SELECT gtw_techid FROM t_thg_gateway WHERE gtw_naturalid='RAK7249-finca-las-rosas'),
		(SELECT tty_techid FROM t_thg_thing_type WHERE
						tty_manufacturer='Elsys' AND
						tty_model='MB7389'
		),
		'Sonda de nivel del deposito de agua',
		
		'a81758fffe053159');

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_naturalid)
	VALUES ( 
		(SELECT gtw_techid FROM t_thg_gateway WHERE gtw_naturalid='RAK7249-finca-las-rosas'),
		(SELECT tty_techid FROM t_thg_thing_type WHERE
						tty_manufacturer='MFC88' AND
						tty_model='LW13IO'
		),
		'Bomba de agua',
		
		'70b3d58ff10184b8');

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_naturalid)
	VALUES ( 
		(SELECT gtw_techid FROM t_thg_gateway WHERE gtw_naturalid='RAK7249-finca-las-rosas'),
		(SELECT tty_techid FROM t_thg_thing_type WHERE
						tty_manufacturer='MFC88' AND
						tty_model='LW13IO'
		),
		'Multiswitch sensor for testing',
		
		'70b3d58ff10184df');

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_naturalid)
	VALUES (
		(SELECT gtw_techid FROM t_thg_gateway WHERE gtw_naturalid='RAK7249-finca-las-rosas'),
		(SELECT tty_techid FROM t_thg_thing_type WHERE
						tty_manufacturer='DRAGINO' AND
						tty_model='LHT65'
		),
		'Available DRAGINO LHT65 1',
		
		'a8404114e18446ec');

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_naturalid)
	VALUES (
		(SELECT gtw_techid FROM t_thg_gateway WHERE gtw_naturalid='RAK7249-finca-las-rosas'),
		(SELECT tty_techid FROM t_thg_thing_type WHERE
						tty_manufacturer='DRAGINO' AND
						tty_model='LHT65'
		),
		'Available DRAGINO LHT65 2',
		
		'a8404152318446ea');

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_naturalid)
	VALUES (
		(SELECT gtw_techid FROM t_thg_gateway WHERE gtw_naturalid='RAK7249-finca-las-rosas'),
		(SELECT tty_techid FROM t_thg_thing_type WHERE
						tty_manufacturer='DRAGINO' AND
						tty_model='LHT65'
		),
		'Sonda temp placa solares',
		
		'a8404111118446ed');

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_naturalid)
	VALUES (
		(SELECT gtw_techid FROM t_thg_gateway WHERE gtw_naturalid='RAK7249-finca-las-rosas'),
		(SELECT tty_techid FROM t_thg_thing_type WHERE
						tty_manufacturer='DRAGINO' AND
						tty_model='LHT65'
		),
		'Sonda temp camara fria',
		
		'a84041ecf18446ee');

/* Digital Twin deposito de agua */

INSERT INTO t_dtw_digital_twin(
	twi_discriminator, twi_name, twi_fk_twt_type)
	VALUES (
		'wat', 
		'Deposito de agua', 
		(SELECT twt_techid FROM t_dtw_digital_twin_type WHERE twt_name='WaterTank')
	);

INSERT INTO t_dtw_water_tank(twi_techid, wat_length, wat_radius, wat_sensor_alt, wat_max_water_flow)
	VALUES (
		(SELECT twi_techid FROM t_dtw_digital_twin WHERE twi_name='Deposito de agua'),
		6.0,
		1.25,
		0.34,
		7
	);

INSERT INTO t_dtw_reactor_receiver(
	rre_discriminator, rre_readable, rre_sensor, rre_fk_twi_twin, rre_fk_rrt_type, rvt_fk_thg_thing)
	VALUES (
		'thg',
		'Nivel del deposito de agua',
		null,
		(SELECT twi_techid FROM t_dtw_digital_twin WHERE twi_name = 'Deposito de agua'),
		(
			SELECT rrt_techid FROM t_dtw_reactor_receiver_type
		 	JOIN t_dtw_reactor_type on t_dtw_reactor_receiver_type.rrt_fk_rat_reactor_type=t_dtw_reactor_type.rat_techid
			WHERE rat_bean='WaterTankReactor'
		),
		(SELECT thg_techid FROM t_thg_thing WHERE thg_readable = 'Sonda de nivel del deposito de agua')
	);

/* Digital Twin camara fria */
INSERT INTO t_dtw_digital_twin(
	twi_discriminator, twi_name, twi_fk_twt_type)
	VALUES (
		'fri', 
		'Camara fria',
		(SELECT twt_techid FROM t_dtw_digital_twin_type WHERE twt_name='Fridge')
	);

INSERT INTO t_dtw_fridge(twi_techid, fri_width, fri_height, fri_length, fri_inside_temp_min, fri_inside_temp_max)
VALUES (
		(SELECT twi_techid FROM t_dtw_digital_twin WHERE twi_name='Camara fria'),
		2,
		2.5,
		3,
		0.0,
		10.0
	);

INSERT INTO t_dtw_reactor_receiver(
	rre_discriminator, rre_readable, rre_sensor, rre_fk_twi_twin, rre_fk_rrt_type, rvt_fk_thg_thing)
	VALUES (
		'thg',
		'Camara fria temperature data',
		null,
		(
			SELECT twi_techid FROM t_dtw_digital_twin WHERE twi_name = 'Camara fria'
		),
		(
			SELECT rrt_techid FROM t_dtw_reactor_receiver_type
		 	JOIN t_dtw_reactor_type on t_dtw_reactor_receiver_type.rrt_fk_rat_reactor_type=t_dtw_reactor_type.rat_techid
			WHERE rat_bean='FridgeReactor'
		),
		(
			SELECT thg_techid FROM t_thg_thing WHERE thg_readable = 'Sonda temp camara fria'
		)
	);

/* Digital Twin multiswitch */
INSERT INTO t_dtw_digital_twin(
	twi_discriminator, twi_name, twi_fk_twt_type)
	VALUES (
		'msw', 
		'Multiswitch for testing',
		(SELECT twt_techid FROM t_dtw_digital_twin_type WHERE twt_name='MultiSwitch')
	);

INSERT INTO t_dtw_multiswitch(twi_techid, msw_expected_state, msw_state_when_connect)
VALUES (
		(SELECT twi_techid FROM t_dtw_digital_twin WHERE twi_name='Multiswitch for testing'),
		1,
		0
	);

INSERT INTO t_dtw_reactor_receiver(
	rre_discriminator, rre_readable, rre_sensor, rre_fk_twi_twin, rre_fk_rrt_type, rvt_fk_thg_thing)
	VALUES (
		'thg',
		'Multiswitch input for testing',
		null,
		(
			SELECT twi_techid FROM t_dtw_digital_twin WHERE twi_name = 'Multiswitch for testing'
		),
		(
			SELECT rrt_techid FROM t_dtw_reactor_receiver_type
		 	JOIN t_dtw_reactor_type on t_dtw_reactor_receiver_type.rrt_fk_rat_reactor_type=t_dtw_reactor_type.rat_techid
			WHERE rat_bean='MultiSwitchReactor'
		),
		(
			SELECT thg_techid FROM t_thg_thing WHERE thg_readable = 'Multiswitch sensor for testing'
		)
	);
