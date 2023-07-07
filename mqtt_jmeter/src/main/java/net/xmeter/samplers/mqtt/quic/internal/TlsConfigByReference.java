package net.xmeter.samplers.mqtt.quic.internal;

public class TlsConfigByReference extends NngPointerByReference {
    public TlsConfigPointer getTlsConfig() {
        return new TlsConfigPointer(getPointer().getPointer(0));
    }
}
