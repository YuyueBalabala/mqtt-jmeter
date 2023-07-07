package net.xmeter.samplers.mqtt.quic.internal;

public class HttpResPointerByReference extends NngPointerByReference {

    public HttpResPointer getHttpReqPointer() {
        HttpResPointer res = new HttpResPointer();
        res.setPointer(getPointer().getPointer(0));
        return res;
    }
}
