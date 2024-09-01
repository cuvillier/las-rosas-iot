package com.lasrosas.iot.ingestor.domain.model.digitalTwin.twins;

import com.lasrosas.iot.ingestor.domain.model.digitalTwin.DigitalTwin;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuperBuilder
@Getter
@Setter
public class Fridge extends DigitalTwin {
	public static final Logger log = LoggerFactory.getLogger(Fridge.class);
	private Double length;
	private Double width;
	private Double height;
	private Double insideHumidity;
	private Double insideTempMax;
	private Double insideTempMin;
	private Double insideTemp;
	private Double outsideTemp;
	private FridgeStatus status;

	private void updateStatus() {

		if(insideTemp != null) {
			if(insideTempMax != null && insideTemp >= insideTempMax)
				status = FridgeStatus.ALARM_OVERHEAT;
			else if(insideTempMax != null && insideTemp <= insideTempMin)
				status = FridgeStatus.ALARM_TOO_COLD;
			else
				status = FridgeStatus.NORMAL;
		}
	}

	public Double getVolume() {
		if( length != null && width != null && height != null)
			return length * width * height;
		return null;
	}

	public void setInsideTempMax(Double insideTempMax) {
		this.insideTempMax = insideTempMax;
		updateStatus();
	}

	public void setInsideTempMin(Double insideTempMin) {
		this.insideTempMin = insideTempMin;
		updateStatus();
	}

	public void setInsideTemp(Double insideTemp) {
		this.insideTemp = insideTemp;
		updateStatus();
	}
}
