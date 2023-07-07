package net.xmeter.samplers.mqtt.quic.mqtt.msg;


import net.xmeter.samplers.mqtt.quic.internal.mqtt.PropertyPointer;
import net.xmeter.samplers.mqtt.quic.nng.Nng;

public class MqttProperty implements AutoCloseable {

    private PropertyPointer property;

    public MqttProperty() {
        this.property = Nng.lib().mqtt_property_alloc();
    }

    //TODO append property

    @Override
    public void close() throws Exception {
        Nng.lib().mqtt_property_free(this.property);
    }
}
