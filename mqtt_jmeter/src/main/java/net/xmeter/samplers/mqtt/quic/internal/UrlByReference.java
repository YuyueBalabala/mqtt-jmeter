package net.xmeter.samplers.mqtt.quic.internal;

import com.sun.jna.Structure;

public class UrlByReference extends NngPointerByReference {

    public UrlStruct getUrl() {
        return Structure.newInstance(UrlStruct.class, getValue());
    }
}
