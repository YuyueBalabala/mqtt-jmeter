package net.xmeter.samplers.mqtt.quic;

import net.xmeter.Constants;
import net.xmeter.Util;
import net.xmeter.samplers.SubSampler;
import net.xmeter.samplers.mqtt.*;
import org.junit.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

import static net.xmeter.samplers.mqtt.MQTTQoS.AT_LEAST_ONCE;

/**
 * @author ：yuyue
 * @date ：Created in 2023/7/11 下午5:22
 * @description：
 */

public class QuicSample {

    private static final Logger logger = Logger.getLogger(QuicSample.class.getCanonicalName());

    @Test
    public void test() throws Exception {
        ConnectionParameters parameters = new ConnectionParameters();
        parameters.setHost("10.42.3.130");
        parameters.setPort(14567);
        String clientId = Util.generateClientId("QUIC");
        parameters.setClientId(clientId);
        MQTTClient client = MQTT.getInstance(Constants.QUIC_MQTT_CLIENT_NAME).createClient(parameters);
        MQTTConnection connection = client.connect();
        connection.publish("123-topic","111111".getBytes(),AT_LEAST_ONCE,true);
    }

    @Test
    public void testSub() throws Exception {
        ConnectionParameters parameters = new ConnectionParameters();
        parameters.setHost("10.42.3.130");
        parameters.setPort(14567);
        String clientId = Util.generateClientId("QUIC");
        parameters.setClientId(clientId);
        MQTTClient client = MQTT.getInstance(Constants.QUIC_MQTT_CLIENT_NAME).createClient(parameters);
        MQTTConnection connection = client.connect();
        String[] topicNames =new String[1];
        topicNames[0]="test-topic";
        connection.subscribe(topicNames, AT_LEAST_ONCE, () -> {
            logger.info(() -> "sub successful, topic length is " + topicNames.length);
        }, error -> {
            logger.log(Level.INFO, "subscribe failed", error);
        });
    }
}