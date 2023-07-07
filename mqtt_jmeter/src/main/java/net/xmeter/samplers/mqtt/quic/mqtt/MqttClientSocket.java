package net.xmeter.samplers.mqtt.quic.mqtt;


import net.xmeter.samplers.mqtt.quic.nng.Nng;
import net.xmeter.samplers.mqtt.quic.nng.NngException;
import net.xmeter.samplers.mqtt.quic.nng.Socket;

public class MqttClientSocket extends Socket {
    public MqttClientSocket() throws NngException {
        super(Nng.lib()::nng_mqtt_client_open);
    }
}
