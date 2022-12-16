CREATE TABLE t_dtw_fridge (
  fri_techid int(10) unsigned NOT NULL,
  fri_width double,
  fri_height double,
  fri_length double,
  fri_inside_temp double,
  fri_inside_temp_max double,
  fri_inside_temp_min double,
  fri_inside_humidity double,
  fri_outside_temp double,
  PRIMARY KEY (fri_techid),
  CONSTRAINT fk_fri_twi_techid FOREIGN KEY (fri_techid) REFERENCES t_dtw_digital_twin (twi_techid)
);
