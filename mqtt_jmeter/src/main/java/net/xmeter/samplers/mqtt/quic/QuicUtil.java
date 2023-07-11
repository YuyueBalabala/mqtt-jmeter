package net.xmeter.samplers.mqtt.quic;

import net.xmeter.Constants;
import net.xmeter.samplers.mqtt.MQTTQoS;
import org.fusesource.mqtt.client.QoS;

import java.util.ArrayList;
import java.util.List;

class QuicUtil {
    static final List<String> ALLOWED_PROTOCOLS;
    static {
        ALLOWED_PROTOCOLS = new ArrayList<>();
        ALLOWED_PROTOCOLS.add(Constants.QUIC_PROTOCOL);
    }

    static QoS map(MQTTQoS qos) {
        switch (qos) {
            case AT_MOST_ONCE: return QoS.AT_MOST_ONCE;
            case AT_LEAST_ONCE: return QoS.AT_LEAST_ONCE;
            case EXACTLY_ONCE: return QoS.EXACTLY_ONCE;
            default: throw new IllegalArgumentException("Unknown QoS: " + qos);
        }
    }

    static byte qosVal(MQTTQoS qoS){
        byte qos=0;
        switch (qoS){
            case AT_MOST_ONCE:
                qos=0;
                break;
            case AT_LEAST_ONCE:
                qos=1;
                break;
            case EXACTLY_ONCE:
                qos=2;
                break;
            default:
                break;
        }
        return qos;
    }

}
