CREATE TABLE t_dtw_space
(
	spa_techid SERIAL,
	spa_name VARCHAR(50) DEFAULT NULL,

	PRIMARY KEY (spa_techid)
);

/*-------------- DTWIN Core --------------------*/
CREATE TABLE t_dtw_digital_twin_type
(
	twt_techid SERIAL,
	twt_name VARCHAR(50) NOT NULL UNIQUE,
	twt_discriminator VARCHAR(3) NOT NULL,
	twt_may_have_children BIT DEFAULT 0::bit NOT NULL,
	twt_fk_spa_space INTEGER NOT NULL,
	dtt_fk_dtt_super_type INTEGER DEFAULT NULL,
	dtt_concrete BIT DEFAULT 1::bit NOT NULL,
	mst_max_state INTEGER NOT NULL DEFAULT 2,

	PRIMARY KEY (twt_techid),
	CONSTRAINT fk_dtt_fk_dtt_super_type FOREIGN KEY (dtt_fk_dtt_super_type) REFERENCES t_dtw_digital_twin_type (twt_techid),
	CONSTRAINT fk_twt_fk_spa_space FOREIGN KEY (twt_fk_spa_space) REFERENCES t_dtw_space (spa_techid)
);

CREATE TABLE t_dtw_digital_twin
(
	twi_techid SERIAL,
	twi_discriminator VARCHAR(3) NOT NULL,
	twi_name VARCHAR(50) NOT NULL UNIQUE,
	twi_fk_twt_type INTEGER NOT NULL,
	twi_fk_twi_part_of INTEGER DEFAULT NULL,
	twi_properties VARCHAR(4000) DEFAULT NULL,

	PRIMARY KEY (twi_techid),
	CONSTRAINT fk_twi_fk_twi_part_of FOREIGN KEY (twi_fk_twi_part_of) REFERENCES t_dtw_digital_twin(twi_techid),
	CONSTRAINT fk_twi_fk_twt_type FOREIGN KEY (twi_fk_twt_type) REFERENCES t_dtw_digital_twin_type(twt_techid)
);

CREATE TABLE t_dtw_water_tank
(
    twi_techid integer NOT NULL UNIQUE,
    wat_length real NOT NULL,
    wat_radius real NOT NULL,
    wat_sensor_alt real NOT NULL,
    wat_level real,
    wat_volume real,
    wat_percentage real,
    wat_water_flow real,
    wat_max_water_flow real,
    wat_status character varying(32) DEFAULT 'UNKNOWN',
    wat_update_time timestamp(6) DEFAULT NULL,
    CONSTRAINT t_dtw_water_tank_pkey PRIMARY KEY (twi_techid),
    CONSTRAINT fk_wat_twi_techid FOREIGN KEY (twi_techid)
        REFERENCES public.t_dtw_digital_twin (twi_techid),
    CONSTRAINT fk_wat_status CHECK (wat_status= ANY (ARRAY['UNKNOWN'::character varying, 'FULL'::character varying, 'NORMAL'::character varying, 'WARNING'::character varying, 'ALARM'::character varying, 'EMPTY'::character varying]::text[]))
);

CREATE TABLE t_dtw_multiswitch
(
    twi_techid integer NOT NULL UNIQUE,
    msw_state integer NOT NULL DEFAULT 0,
    msw_expected_state integer NOT NULL DEFAULT 0,
    msw_connected integer NOT NULL DEFAULT 0,
    msw_state_when_connect integer,
    CONSTRAINT t_dtw_multiswitch_pkey PRIMARY KEY (twi_techid),
    CONSTRAINT fk_msw_twi_techid FOREIGN KEY (twi_techid)
        REFERENCES public.t_dtw_digital_twin (twi_techid)
);

CREATE TABLE t_dtw_fridge
(
    twi_techid integer NOT NULL UNIQUE,
    fri_width real,
    fri_height real,
    fri_length real,
    fri_inside_temp real,
    fri_inside_temp_max real,
    fri_inside_temp_min real,
    fri_inside_humidity real,
    fri_outside_temp real,
    fri_status character varying(32),
    CONSTRAINT t_dtw_fridge_pkey PRIMARY KEY (twi_techid),
    CONSTRAINT fk_fri_twi_techid FOREIGN KEY (twi_techid)
        REFERENCES public.t_dtw_digital_twin (twi_techid)
);

CREATE TABLE t_dtw_dynamic_twin (
  twi_techid integer  NOT NULL UNIQUE,
  PRIMARY KEY (twi_techid),
  CONSTRAINT fk_dtw_twi_techid FOREIGN KEY (twi_techid) REFERENCES t_dtw_digital_twin (twi_techid)
);
/*
   -------------------------------- PROVISIONING ----------------------------------------------
*/

CREATE TABLE t_thg_gateway
(
	gtw_techid SERIAL,
	gtw_natural_id VARCHAR(50) NOT NULL UNIQUE,
	gtw_protocol VARCHAR(50) DEFAULT NULL,
	gtw_url VARCHAR(50) DEFAULT NULL,
	gtw_login VARCHAR(50) DEFAULT NULL,
	gtw_password VARCHAR(50) DEFAULT NULL,
	gtw_type_name VARCHAR(50) DEFAULT NULL,
	PRIMARY KEY (gtw_techid)
);

CREATE TABLE t_thg_thing_type
(
	tty_techid SERIAL,
	tty_readable VARCHAR(50) DEFAULT NULL UNIQUE,
	tty_manufacturer VARCHAR(32),
	tty_model VARCHAR(32),
	tty_version VARCHAR(16),
	tty_max_hours_invisible REAL DEFAULT NULL,
	tty_battery_min_percentage REAL DEFAULT 25,
	tty_volatile_state INT DEFAULT 0,
	bty_rssi_1_meter INTEGER,

	PRIMARY KEY (tty_techid)
);

CREATE TABLE t_thg_thing
(
	thg_techid SERIAL,
	thg_readable VARCHAR(50) DEFAULT NULL UNIQUE,
	thg_connection_timeout integer DEFAULT NULL,
	thg_discriminator VARCHAR(3) NOT NULL,
	thg_fk_tty_type INTEGER NOT NULL,
	thg_fk_gtw_gateway INTEGER,
	thg_admin_state VARCHAR(32) DEFAULT 'Connected' NOT NULL,
	lor_dev_eui VARCHAR(50) DEFAULT NULL,
	lor_app_eui VARCHAR(50) DEFAULT NULL,
	lor_app_key VARCHAR(50) DEFAULT NULL,
	wst_station_id VARCHAR(50) DEFAULT NULL,
	PRIMARY KEY (thg_techid),
	CONSTRAINT fk_thg_fk_tty_type FOREIGN KEY (thg_fk_tty_type) REFERENCES t_thg_thing_type(tty_techid),
	CONSTRAINT fk_thg_fk_gtw_gateway FOREIGN KEY (thg_fk_gtw_gateway) REFERENCES t_thg_gateway (gtw_techid),
	CONSTRAINT fk_thg_admin_state CHECK(thg_admin_state IN (
		'Connected',
		'Disconnected',
		'Disabled',
		'Removed'))
);

CREATE TABLE t_thg_thing_proxy
(
	thp_techid SERIAL,
	thp_battery_state INTEGER DEFAULT 0,
	thp_connected INTEGER DEFAULT 0,
	thp_battery_level INTEGER DEFAULT NULL,
	thp_last_seen TIMESTAMP(6) NULL DEFAULT NULL,
	thp_config VARCHAR(4000) DEFAULT NULL,
	thp_values VARCHAR(4000) DEFAULT NULL,
	thp_fk_thg_thing INTEGER UNIQUE,

	PRIMARY KEY (thp_techid),
	CONSTRAINT fk_thp_fk_thg_thing FOREIGN KEY (thp_fk_thg_thing) REFERENCES t_thg_thing(thg_techid)
);


/*
   -------------------------------- TIMESERIES ----------------------------------------------
*/

CREATE TABLE t_tsr_time_serie_type
(
	tst_techid SERIAL,
	tst_schema VARCHAR(4000) DEFAULT NULL UNIQUE,
	tst_persistent BOOLEAN DEFAULT FALSE,
	tst_retention INTEGER DEFAULT NULL,
	PRIMARY KEY (tst_techid)
);

CREATE TABLE t_tsr_time_serie
(
	tsr_techid SERIAL,
	tsr_sensor VARCHAR(16) DEFAULT NULL,
	tsr_influxdb_measurement VARCHAR(100) DEFAULT NULL,
	tsr_fk_thg_thing INTEGER DEFAULT NULL,
	tsr_fk_tst_type INTEGER DEFAULT NULL,
	tsr_fk_twi_twin INTEGER DEFAULT NULL,
	tsr_fk_poi_current_value INTEGER DEFAULT NULL,
	PRIMARY KEY (tsr_techid),
	CONSTRAINT fk_tsr_fk_thg_thing FOREIGN KEY (tsr_fk_thg_thing) REFERENCES t_thg_thing(thg_techid),
	CONSTRAINT fk_tsr_fk_tst_type FOREIGN KEY (tsr_fk_tst_type) REFERENCES t_tsr_time_serie_type(tst_techid),
	UNIQUE(tsr_fk_thg_thing, tsr_fk_twi_twin, tsr_fk_tst_type)
);

CREATE TABLE t_tsr_point
(
	poi_techid SERIAL,
	poi_fk_tsr_time_serie INTEGER,
	poi_value VARCHAR(2048) DEFAULT NULL,
	poi_time  TIMESTAMP(6),
	PRIMARY KEY (poi_techid),
	CONSTRAINT fk_poi_fk_tsr_time_serie FOREIGN KEY (poi_fk_tsr_time_serie) REFERENCES t_tsr_time_serie(tsr_techid),
	UNIQUE (poi_fk_tsr_time_serie, poi_time)
);

ALTER TABLE t_tsr_time_serie
ADD	CONSTRAINT fk_tsr_fk_poi_current_value FOREIGN KEY (tsr_fk_poi_current_value) REFERENCES t_tsr_point(poi_techid);

/*---------------- REACTOR --------------*/

CREATE TABLE t_dtw_reactor_type
(
	rat_techid SERIAL,
	rat_bean VARCHAR(50) NOT NULL UNIQUE,
	
	PRIMARY KEY (rat_techid)
);

CREATE TABLE t_dtw_reactor_receiver_type
(
	rrt_techid SERIAL,
	rrt_role VARCHAR(50) NOT NULL,
	rrt_schema VARCHAR(128),
    rrt_fk_rat_reactor_type INTEGER NOT NULL,

	PRIMARY KEY (rrt_techid),
	CONSTRAINT fk_rrt_fk_rat_reactor_type FOREIGN KEY (rrt_fk_rat_reactor_type) REFERENCES t_dtw_reactor_type (rat_techid),
	UNIQUE (rrt_fk_rat_reactor_type, rrt_role)
);

CREATE TABLE t_dtw_reactor_receiver
(
	rre_techid SERIAL,
	rre_discriminator VARCHAR(3) NOT NULL,
	rre_readable  VARCHAR(50),
	rre_sensor  VARCHAR(16),
    rre_fk_twi_twin INTEGER NOT NULL,
    rre_fk_rrt_type INTEGER NOT NULL,
    rvt_fk_thg_thing INTEGER DEFAULT NULL,
    rvd_fk_twi_twin INTEGER DEFAULT NULL,

	PRIMARY KEY (rre_techid),
	CONSTRAINT fk_rre_fk_twi_twin FOREIGN KEY (rre_fk_twi_twin) REFERENCES t_dtw_digital_twin (twi_techid),
	CONSTRAINT fk_rvt_fk_thg_thing FOREIGN KEY (rvt_fk_thg_thing) REFERENCES t_thg_thing(thg_techid),
	CONSTRAINT fk_rvd_fk_twi_twin FOREIGN KEY (rvd_fk_twi_twin) REFERENCES t_dtw_digital_twin (twi_techid),
	CONSTRAINT fk_rre_fk_rrt_type FOREIGN KEY (rre_fk_rrt_type) REFERENCES t_dtw_reactor_receiver_type (rrt_techid)
);

/*--------------- ALARM --------------*/
CREATE TABLE t_alr_alarm_type
(
	alt_techid SERIAL,
	alt_name VARCHAR(50) DEFAULT NULL,

	PRIMARY KEY (alt_techid)
);

CREATE TABLE t_alr_alarm
(
	alr_techid SERIAL,
	alr_discriminator VARCHAR(3) NOT NULL,
	alr_name VARCHAR(50),
	alr_state VARCHAR(10),
	alr_opened_time timestamp DEFAULT NULL,
	alr_closed_time timestamp DEFAULT NULL,
	alr_fk_alt_type INTEGER NOT NULL,
	tha_fk_thg_thing INTEGER DEFAULT NULL,
	twa_fk_twi_twin INTEGER DEFAULT NULL,
	PRIMARY KEY (alr_techid),
	CONSTRAINT fk_alr_fk_alt_type FOREIGN KEY (alr_fk_alt_type) REFERENCES t_alr_alarm_type(alt_techid),
	CONSTRAINT fk_tha_fk_thg_thing FOREIGN KEY (tha_fk_thg_thing) REFERENCES t_thg_thing(thg_techid),
	CONSTRAINT fk_twa_fk_twi_twin FOREIGN KEY (twa_fk_twi_twin) REFERENCES t_dtw_digital_twin(twi_techid)
);