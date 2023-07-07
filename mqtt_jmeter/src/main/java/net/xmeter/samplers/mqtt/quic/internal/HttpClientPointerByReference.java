package net.xmeter.samplers.mqtt.quic.internal;

public class HttpClientPointerByReference extends NngPointerByReference {

    public HttpClientPointer getHttpClientPointer() {
        HttpClientPointer client = new HttpClientPointer();
        client.setPointer(getValue());
        return client;
    }
}
