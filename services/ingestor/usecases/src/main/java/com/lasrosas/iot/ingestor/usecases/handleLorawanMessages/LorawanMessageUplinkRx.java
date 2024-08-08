package com.lasrosas.iot.ingestor.usecases.handleLorawanMessages;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/*
    {
        "applicationID": "3",
        "applicationName": "las-rosas-iot",
        "devEUI": "0018b2200000093c",
        "deviceName": "Adenuis/ARF8180BA/0018B2200000093C",
        "timestamp": 1620078842,
        "fCnt": 6694,
        "fPort": 1,
        "data": "Q6ABAP8CAPw=",
        "data_encode": "base64",
        "adr": true,
        "rxInfo": [
            {
                "gatewayID": "60c5a8fffe76f8b2",
                "loRaSNR": 8.3,
                "rssi": -70,
                "location": {
                    "latitude": 36.825600,
                    "longitude": -5.579390,
                    "altitude": 279
                },
                "time": "2021-07-18T07:50:17.653651Z"
            }
        ],
        "txInfo": {
            "frequency": 868500000,
            "dr": 0
        }
    }
*/
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class LorawanMessageUplinkRx extends LorawanMessage {
	public static final String BASE64 = "base64";

	private String data;

	@JsonProperty("data_encode")
	private String dataEncode;

	@JsonProperty("fCnt")
	private int fCnt;

	@JsonProperty("fPort")
	private int fPort;

	private Boolean adr;


	@Getter
	@Setter
	@SuperBuilder
	@NoArgsConstructor
	public static class Location {
		private double longitude;
		private double latitude;
		private double altitude;
	}

    @Getter
	@Setter
	@SuperBuilder
	@NoArgsConstructor
	public static class RxInfo {
		private String gatewayID;
		private Float loRaSNR;
		private Integer rssi;
		private Location location;
		private LocalDateTime time;
	}

	@Builder.Default
	private List<RxInfo> rxInfo = new ArrayList<>();

	@Getter
	@Setter
	@SuperBuilder
	@NoArgsConstructor
	public static class TxInfo {
		private Long frequency;
		private Integer dr;
	}

	private TxInfo txInfo;

	public byte[] decodeData() {
		if(this.data == null) return null;

		if (!this.dataEncode.equalsIgnoreCase(BASE64))
			throw new RuntimeException("Unknown data encoding encoding=" + this.dataEncode);

		return Base64.getDecoder().decode(this.data);
	}
}
