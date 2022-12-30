DO
$$
DECLARE
BEGIN

DELETE FROM t_thg_thing;

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_discriminator, lor_dev_eui) VALUES
((SELECT gtw_techid FROM t_thg_gateway WHERE gtw_natural_id='GatewayTechoFinca'), (SELECT tty_techid FROM t_thg_thing_type WHERE tty_manufacturer='Elsys' AND tty_model='MB7389'), 'Test Elsys MB7389','lor','a81758fffe053159');

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_discriminator, lor_dev_eui) VALUES
((SELECT gtw_techid FROM t_thg_gateway WHERE gtw_natural_id='GatewayTechoFinca'), (SELECT tty_techid FROM t_thg_thing_type WHERE tty_manufacturer='Elsys' AND tty_model='ERS'), 'Test ELSYS ERS','lor','a81758fffe0346aa');

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_discriminator, lor_dev_eui) VALUES
((SELECT gtw_techid FROM t_thg_gateway WHERE gtw_natural_id='GatewayTechoFinca'), (SELECT tty_techid FROM t_thg_thing_type WHERE tty_manufacturer='MFC88' AND tty_model='LW13IO'), 'Test MFC88 LW13IO','lor','70b3d58ff10184df');

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_discriminator, lor_dev_eui) VALUES
((SELECT gtw_techid FROM t_thg_gateway WHERE gtw_natural_id='GatewayTechoFinca'), (SELECT tty_techid FROM t_thg_thing_type WHERE tty_manufacturer='Adeunis' AND tty_model='ARF8180BA'), 'Test Adeunis ARF8180BA','lor','0018b2200000093c');

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_discriminator, lor_dev_eui) VALUES
((SELECT gtw_techid FROM t_thg_gateway WHERE gtw_natural_id='GatewayTechoFinca'), (SELECT tty_techid FROM t_thg_thing_type WHERE tty_manufacturer='Adeunis' AND tty_model='ARF8170BA'), 'Test Adeunis ARF8170BA','lor','0018b21000003d4f');

INSERT INTO t_thg_thing (thg_fk_gtw_gateway,thg_fk_tty_type, thg_readable, thg_discriminator, lor_dev_eui) VALUES
((SELECT gtw_techid FROM t_thg_gateway WHERE gtw_natural_id='GatewayTechoFinca'), (SELECT tty_techid FROM t_thg_thing_type WHERE tty_manufacturer='DRAGINO' AND tty_model='LHT65'), 'Test DRAGINO LHT65','lor','a8404114e18446ec');

END 
$$
