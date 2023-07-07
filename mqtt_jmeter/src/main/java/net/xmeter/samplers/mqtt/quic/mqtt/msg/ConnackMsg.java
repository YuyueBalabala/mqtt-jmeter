package net.xmeter.samplers.mqtt.quic.mqtt.msg;


import net.xmeter.samplers.mqtt.quic.internal.mqtt.PropertyPointer;
import net.xmeter.samplers.mqtt.quic.internal.mqtt.constants.MqttPacketType;
import net.xmeter.samplers.mqtt.quic.nng.Message;
import net.xmeter.samplers.mqtt.quic.nng.Nng;
import net.xmeter.samplers.mqtt.quic.nng.NngException;

public class ConnackMsg extends MqttMessage{

    public ConnackMsg() throws NngException {
        super(MqttPacketType.NNG_MQTT_CONNACK);
    }

    public ConnackMsg(Message message) throws NngException {
        super(message.getMessagePointer());
    }

    public byte getReturnCode() {
        return Nng.lib().nng_mqtt_msg_get_connack_return_code(super.msg);
    }

    public byte getFlags() {
        return Nng.lib().nng_mqtt_msg_get_connack_flags(super.msg);
    }

    public PropertyPointer getProperty() {
        return Nng.lib().nng_mqtt_msg_get_connect_property(super.msg);
    }

}
