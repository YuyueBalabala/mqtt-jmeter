package net.xmeter.samplers.mqtt.quic;

import net.xmeter.samplers.mqtt.ConnectionParameters;
import net.xmeter.samplers.mqtt.MQTTClient;
import net.xmeter.samplers.mqtt.MQTTConnection;

/**
 * @author ：yuyue
 * @date ：Created in 2023/7/7 下午5:46
 * @description：
 */

public class QuicMQTTClient  implements MQTTClient {

    public QuicMQTTClient(ConnectionParameters parameters) {

    }

    @Override
    public String getClientId() {
        return null;
    }

    @Override
    public MQTTConnection connect() throws Exception {
        return null;
    }
}
