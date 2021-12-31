CREATE TABLE t_dtw_switch (
  twi_techid int(10) unsigned NOT NULL,
  swi_state varchar(3) NOT NULL DEFAULT "Off",
  swi_expectedState varchar(3) NOT NULL DEFAULT "Off",
  swi_connected int(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (twi_techid),
  CONSTRAINT fk_swi_twi_techid FOREIGN KEY (twi_techid) REFERENCES t_dtw_digital_twin (twi_techid)
);
