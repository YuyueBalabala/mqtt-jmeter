package net.xmeter.samplers.mqtt.quic;

import net.xmeter.Constants;
import net.xmeter.Util;
import net.xmeter.samplers.AbstractMQTTSampler;
import net.xmeter.samplers.mqtt.ConnectionParameters;
import net.xmeter.samplers.mqtt.MQTTFactory;
import net.xmeter.samplers.mqtt.MQTTSsl;

import java.util.List;

/**
 * @author ：yuyue
 * @date ：Created in 2023/7/7 下午5:41
 * @description：
 */

public class QuicMQTTFactory implements MQTTFactory {
    @Override
    public String getName() {
        return Constants.QUIC_MQTT_CLIENT_NAME;
    }

    @Override
    public List<String> getSupportedProtocols() {
        return QuicUtil.ALLOWED_PROTOCOLS;
    }

    @Override
    public QuicMQTTClient createClient(ConnectionParameters parameters) throws Exception {
        return new QuicMQTTClient(parameters);
    }

    @Override
    public MQTTSsl createSsl(AbstractMQTTSampler sampler) throws Exception {
        return new QuicMQTTSsl(Util.getContext(sampler));

    }
}
