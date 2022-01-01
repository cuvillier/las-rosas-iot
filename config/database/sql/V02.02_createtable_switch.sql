CREATE TABLE t_dtw_multiswitch (
  twi_techid int(10) unsigned NOT NULL,
  msw_state int(2) NOT NULL DEFAULT 0,
  msw_expectedState int(2) NOT NULL DEFAULT 0,
  msw_connected int(1) NOT NULL DEFAULT 0,
  msw_stateWhenConnect int(2) DEFAULT NULL,
  PRIMARY KEY (twi_techid),
  CONSTRAINT fk_msw_twi_techid FOREIGN KEY (twi_techid) REFERENCES t_dtw_digital_twin (twi_techid)
);
