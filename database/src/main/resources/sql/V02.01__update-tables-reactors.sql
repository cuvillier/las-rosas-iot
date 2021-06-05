
CREATE TABLE t_dtw_reactor_type
(
	rat_techid INT UNSIGNED AUTO_INCREMENT,
	rat_bean VARCHAR(50) NOT NULL,
    rat_fk_twt_twin_type INT UNSIGNED NOT NULL,
	
	PRIMARY KEY (rat_techid)
);

CREATE TABLE t_dtw_reactor_receiver_type
(
	rrt_techid INT UNSIGNED AUTO_INCREMENT,
	rrt_role VARCHAR(50) NOT NULL,
	rrt_schema VARCHAR(128) NOT NULL,
    rrt_fk_rat_reactor_type INT UNSIGNED NOT NULL,

	PRIMARY KEY (rrt_techid),
	CONSTRAINT fk_rrt_fk_rat_reactor_type FOREIGN KEY (rrt_fk_rat_reactor_type) REFERENCES t_dtw_reactor_type (rat_techid)
);

CREATE TABLE t_dtw_reactor_receiver
(
	rre_techid INT UNSIGNED AUTO_INCREMENT,
	rre_discriminator VARCHAR(3) NOT NULL,
    rre_fk_twi_twin INT UNSIGNED NOT NULL,
    rre_fk_rrt_type INT UNSIGNED NOT NULL,
    rvt_fk_thg_thing INT UNSIGNED DEFAULT NULL,
    rvd_fk_twi_twin INT UNSIGNED DEFAULT NULL,

	PRIMARY KEY (rre_techid),
	CONSTRAINT fk_rre_fk_twi_twin FOREIGN KEY (rre_fk_twi_twin) REFERENCES t_dtw_digital_twin (twi_techid),
	CONSTRAINT fk_rvt_fk_thg_thing FOREIGN KEY (rvt_fk_thg_thing) REFERENCES t_thg_thing(thg_techid),
	CONSTRAINT fk_rvd_fk_twi_twin FOREIGN KEY (rvd_fk_twi_twin) REFERENCES t_dtw_digital_twin (twi_techid),
	CONSTRAINT fk_rre_fk_rrt_type FOREIGN KEY (rre_fk_rrt_type) REFERENCES t_dtw_reactor_receiver_type (rrt_techid)
);
