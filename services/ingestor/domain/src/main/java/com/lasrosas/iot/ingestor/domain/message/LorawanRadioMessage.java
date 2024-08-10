package com.lasrosas.iot.ingestor.domain.message;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * This message contains the LORAWAN radio transmission indicators.
 */
@Getter
@Setter
@SuperBuilder
public class LorawanRadioMessage extends BaseMessage {
    private int fCnt;
    private int fPort;
    private Float loRaSNR;
    private Integer rssi;
    private Long frequency;
    private Integer dr;
}
