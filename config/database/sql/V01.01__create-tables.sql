use lasrosasiot;

CREATE TABLE t_dtw_space
(
	spa_techid INT UNSIGNED AUTO_INCREMENT,
	spa_name VARCHAR(50) DEFAULT NULL,

	PRIMARY KEY (spa_techid)
);

CREATE TABLE t_dtw_digital_twin_type
(
	twt_techid INT UNSIGNED AUTO_INCREMENT,
	twt_name VARCHAR(50) DEFAULT NULL,
	twt_discriminator VARCHAR(3) NOT NULL,
	twt_may_have_children BIT DEFAULT 0,
	twt_fk_spa_space INT UNSIGNED NOT NULL,
	dtt_fk_dtt_super_type INT UNSIGNED DEFAULT NULL,
	dtt_concrete BIT DEFAULT 1,
	mst_max_state INT UNSIGNED NOT NULL DEFAULT 2,

	PRIMARY KEY (twt_techid),
	CONSTRAINT fk_dtt_fk_dtt_super_type FOREIGN KEY (dtt_fk_dtt_super_type) REFERENCES t_dtw_digital_twin_type (twt_techid),
	CONSTRAINT fk_twt_fk_spa_space FOREIGN KEY (twt_fk_spa_space) REFERENCES t_dtw_space (spa_techid)
);

CREATE TABLE t_dtw_digital_twin
(
	twi_techid INT UNSIGNED AUTO_INCREMENT,
	twi_discriminator VARCHAR(3) NOT NULL,
	twi_name VARCHAR(50),
	twi_fk_twt_type INT UNSIGNED NOT NULL,
	twi_fk_twi_part_of INT UNSIGNED DEFAULT NULL,
	twi_properties VARCHAR(4000) DEFAULT NULL,

	PRIMARY KEY (twi_techid),
	CONSTRAINT fk_twi_fk_twi_part_of FOREIGN KEY (twi_fk_twi_part_of) REFERENCES t_dtw_digital_twin(twi_techid),
	CONSTRAINT fk_twi_fk_twt_type FOREIGN KEY (twi_fk_twt_type) REFERENCES t_dtw_digital_twin_type(twt_techid)
);

CREATE TABLE t_dtw_water_tank (
  twi_techid int(10) unsigned NOT NULL,
  wat_length double NOT NULL,
  wat_radius double NOT NULL,
  wat_sensor_alt double NOT NULL,
  wat_level double DEFAULT NULL,
  wat_volume double DEFAULT NULL,
  wat_percentage double DEFAULT NULL,
  wat_water_flow double  NULL,
  wat_max_water_flow double  NULL,
  PRIMARY KEY (twi_techid),
  CONSTRAINT fk_wat_twi_techid FOREIGN KEY (twi_techid) REFERENCES t_dtw_digital_twin (twi_techid)
);

CREATE TABLE t_dtw_dynamic_twin (
  twi_techid int(10) unsigned NOT NULL,
  PRIMARY KEY (twi_techid),
  CONSTRAINT fk_dtw_twi_techid FOREIGN KEY (twi_techid) REFERENCES t_dtw_digital_twin (twi_techid)
);
/*
   -------------------------------- PROVISIONING ----------------------------------------------
*/

CREATE TABLE t_thg_gateway
(
	gtw_techid INT UNSIGNED AUTO_INCREMENT,
	gtw_natural_id VARCHAR(50),
	gtw_protocol VARCHAR(50) DEFAULT NULL,
	gtw_url VARCHAR(50) DEFAULT NULL,
	gtw_login VARCHAR(50) DEFAULT NULL,
	gtw_password VARCHAR(50) DEFAULT NULL,
	gtw_type_name VARCHAR(50) DEFAULT NULL,
	PRIMARY KEY (gtw_techid)
);

CREATE TABLE t_thg_thing_type
(
	tty_techid INT UNSIGNED AUTO_INCREMENT,
	tty_readable VARCHAR(50) DEFAULT NULL,
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
	thg_techid INT UNSIGNED AUTO_INCREMENT,
	thg_readable VARCHAR(50) DEFAULT NULL,
	thg_connection_timeout INT(6) DEFAULT NULL,
	thg_discriminator VARCHAR(3) NOT NULL,
	thg_fk_tty_type INT UNSIGNED NOT NULL,
	thg_fk_gtw_gateway INT UNSIGNED,
	thg_admin_state VARCHAR(10) DEFAULT 'Enabled' NOT NULL,
	lor_dev_eui VARCHAR(50) DEFAULT NULL,
	lor_app_eui VARCHAR(50) DEFAULT NULL,
	lor_app_key VARCHAR(50) DEFAULT NULL,
	wst_station_id VARCHAR(50) DEFAULT NULL,
	PRIMARY KEY (thg_techid),
	CONSTRAINT fk_thg_fk_tty_type FOREIGN KEY (thg_fk_tty_type) REFERENCES t_thg_thing_type(tty_techid),
	CONSTRAINT fk_thg_fk_gtw_gateway FOREIGN KEY (thg_fk_gtw_gateway) REFERENCES t_thg_gateway (gtw_techid),
	CONSTRAINT fk_thg_admin_state CHECK(thg_admin_state IN ('Enabled', 'Disabled', 'Removed'))
);

CREATE TABLE t_thg_thing_proxy
(
	thp_techid INT UNSIGNED AUTO_INCREMENT,
	thp_battery_state INT UNSIGNED DEFAULT 0,
	thp_connected INT UNSIGNED DEFAULT 0,
	thp_battery_level INT UNSIGNED DEFAULT NULL,
	thp_last_seen TIMESTAMP(6) NULL DEFAULT NULL,
	thp_config VARCHAR(4000) DEFAULT NULL,
	thp_values VARCHAR(4000) DEFAULT NULL,
	thp_fk_thg_thing INT UNSIGNED,

	PRIMARY KEY (thp_techid),
	CONSTRAINT fk_thp_fk_thg_thing FOREIGN KEY (thp_fk_thg_thing) REFERENCES t_thg_thing(thg_techid)
);


/*
   -------------------------------- TIMESERIES ----------------------------------------------
*/

CREATE TABLE t_tsr_time_serie_type
(
	tst_techid INT UNSIGNED AUTO_INCREMENT,
	tst_schema VARCHAR(4000) DEFAULT NULL,
	tst_diffused BOOLEAN DEFAULT FALSE,
	PRIMARY KEY (tst_techid)
);

CREATE TABLE t_tsr_time_serie
(
	tsr_techid INT UNSIGNED AUTO_INCREMENT,
	tsr_sensor VARCHAR(16) DEFAULT NULL,
	tsr_influxdb_measurement VARCHAR(100) DEFAULT NULL,
	tsr_fk_thg_thing INT UNSIGNED DEFAULT NULL,
	tsr_fk_tst_type INT UNSIGNED DEFAULT NULL,
	tsr_fk_twi_twin INT UNSIGNED DEFAULT NULL,
	tsr_fk_poi_current_value INT UNSIGNED DEFAULT NULL,
	PRIMARY KEY (tsr_techid),
	CONSTRAINT fk_tsr_fk_thg_thing FOREIGN KEY (tsr_fk_thg_thing) REFERENCES t_thg_thing(thg_techid),
	CONSTRAINT fk_tsr_fk_tst_type FOREIGN KEY (tsr_fk_tst_type) REFERENCES t_tsr_time_serie_type(tst_techid)
);

CREATE TABLE t_tsr_point
(
	poi_techid INT UNSIGNED AUTO_INCREMENT,
	poi_fk_tsr_time_serie INT UNSIGNED,
	poi_value VARCHAR(2048) DEFAULT NULL,
	poi_time  TIMESTAMP(6),
	PRIMARY KEY (poi_techid),
	CONSTRAINT fk_poi_fk_tsr_time_serie FOREIGN KEY (poi_fk_tsr_time_serie) REFERENCES t_tsr_time_serie(tsr_techid)
);

ALTER TABLE t_tsr_time_serie
ADD	CONSTRAINT fk_tsr_fk_poi_current_value FOREIGN KEY (tsr_fk_poi_current_value) REFERENCES t_tsr_point(poi_techid);

/*--------------- ALARM --------------*/
CREATE TABLE t_alr_alarm_type
(
	alt_techid INT UNSIGNED AUTO_INCREMENT,
	alt_name VARCHAR(50) DEFAULT NULL,

	PRIMARY KEY (alt_techid)
);

CREATE TABLE t_alr_alarm
(
	alr_techid INT UNSIGNED AUTO_INCREMENT,
	alr_discriminator VARCHAR(3) NOT NULL,
	alr_name VARCHAR(50),
	alr_state VARCHAR(10),
	alr_opened_time datetime DEFAULT NULL,
	alr_closed_time datetime DEFAULT NULL,
	alr_fk_alt_type INT UNSIGNED NOT NULL,
	tha_fk_thg_thing INT UNSIGNED DEFAULT NULL,
	twa_fk_twi_twin INT UNSIGNED DEFAULT NULL,
	PRIMARY KEY (alr_techid),
	CONSTRAINT fk_alr_fk_alt_type FOREIGN KEY (alr_fk_alt_type) REFERENCES t_alr_alarm_type(alt_techid),
	CONSTRAINT fk_tha_fk_thg_thing FOREIGN KEY (tha_fk_thg_thing) REFERENCES t_thg_thing(thg_techid),
	CONSTRAINT fk_twa_fk_twi_twin FOREIGN KEY (twa_fk_twi_twin) REFERENCES t_dtw_digital_twin(twi_techid)
);