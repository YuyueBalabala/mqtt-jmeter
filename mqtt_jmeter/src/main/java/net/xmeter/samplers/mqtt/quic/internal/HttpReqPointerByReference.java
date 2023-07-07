package net.xmeter.samplers.mqtt.quic.internal;

public class HttpReqPointerByReference extends NngPointerByReference {

    public HttpReqPointer getHttpReqPointer() {
        HttpReqPointer req = new HttpReqPointer();
        req.setPointer(getPointer().getPointer(0));
        return req;
    }
}
