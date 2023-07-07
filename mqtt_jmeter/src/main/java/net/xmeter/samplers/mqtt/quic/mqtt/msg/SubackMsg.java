package net.xmeter.samplers.mqtt.quic.mqtt.msg;



import net.xmeter.samplers.mqtt.quic.internal.jna.UInt32ByReference;
import net.xmeter.samplers.mqtt.quic.internal.mqtt.BytesPointer;
import net.xmeter.samplers.mqtt.quic.internal.mqtt.PropertyPointer;
import net.xmeter.samplers.mqtt.quic.internal.mqtt.constants.MqttPacketType;
import net.xmeter.samplers.mqtt.quic.nng.Message;
import net.xmeter.samplers.mqtt.quic.nng.Nng;
import net.xmeter.samplers.mqtt.quic.nng.NngException;

import java.nio.ByteBuffer;

public class SubackMsg extends MqttMessage {
    public SubackMsg() throws NngException {
        super(MqttPacketType.NNG_MQTT_SUBACK);
    }

    public SubackMsg(Message msg) throws NngException {
        super(msg.getMessagePointer());
    }

    public ByteBuffer getReturnCodes() {
        UInt32ByReference u32 = new UInt32ByReference();
        BytesPointer bytes = Nng.lib().nng_mqtt_msg_get_suback_return_codes(super.msg, u32);
        int count = u32.getUInt32().intValue();
        if (count == 0) {
            return null;
        }
        return bytes.getPointer().getByteBuffer(0, count);
    }

    public PropertyPointer getProperty() {
        return Nng.lib().nng_mqtt_msg_get_suback_property(super.msg);
    }
}
