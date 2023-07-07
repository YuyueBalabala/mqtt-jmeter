package net.xmeter.samplers.mqtt.quic.internal;

public class HttpServerPointerByReference extends NngPointerByReference {
    public HttpServerPointer getHttpServerPointer() {
        HttpServerPointer server = new HttpServerPointer();
        server.setPointer(getPointer().getPointer(0));
        return server;
    }
}
