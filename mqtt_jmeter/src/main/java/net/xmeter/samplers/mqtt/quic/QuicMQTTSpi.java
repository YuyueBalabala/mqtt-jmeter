package net.xmeter.samplers.mqtt.quic;

import net.xmeter.samplers.mqtt.MQTTFactory;
import net.xmeter.samplers.mqtt.MQTTSpi;

public class QuicMQTTSpi implements MQTTSpi {
    @Override
    public MQTTFactory factory() {
        return new QuicMQTTFactory();
    }
}
