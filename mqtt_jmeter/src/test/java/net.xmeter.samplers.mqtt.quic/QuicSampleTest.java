package net.xmeter.samplers.mqtt.quic;

import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import net.xmeter.Constants;
import net.xmeter.Util;
import net.xmeter.samplers.mqtt.*;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static net.xmeter.samplers.mqtt.MQTTQoS.AT_LEAST_ONCE;

/**
 * @author ：yuyue
 * @date ：Created in 2023/7/11 下午5:22
 * @description：
 */
@Ignore
public class QuicSampleTest {

    private static final Logger logger = Logger.getLogger(QuicSampleTest.class.getCanonicalName());

    @Test
    public void testPub() throws Exception {
        ConnectionParameters parameters = new ConnectionParameters();
        parameters.setHost("10.42.3.130");
        parameters.setPort(14567);
        String clientId = Util.generateClientId("QUIC");
        parameters.setClientId(clientId);
        MQTTClient client = MQTT.getInstance(Constants.QUIC_MQTT_CLIENT_NAME).createClient(parameters);
        MQTTConnection connection = client.connect();
//        Problem with pub under multithreading
        for(int i=0;i<10;i++){
            MQTTPubResult result = connection.publish("test-topic","111111".getBytes(),AT_LEAST_ONCE,true);
            logger.info(""+result.isSuccessful());
        }
        TimeUnit.SECONDS.sleep(30);

    }

    @Test
    public void testPubs() throws Exception {
        TimeUnit.SECONDS.sleep(5);

//        ExecutorService service = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 2; i++) {
//            service.submit(() -> {
                try {
                    ConnectionParameters parameters = new ConnectionParameters();
                    parameters.setHost("10.42.3.130");
                    parameters.setPort(14567);
                    String clientId = Util.generateClientId("QUIC");
                    parameters.setClientId(clientId);
                    MQTTClient client = MQTT.getInstance(Constants.QUIC_MQTT_CLIENT_NAME).createClient(parameters);
                    MQTTConnection connection = client.connect();
                    if(!connection.isConnectionSucc()){
                        throw new Exception("fail to connect ~");
                    }
                    System.out.println("conn --->" + connection.isConnectionSucc());
//        Problem with pub under multithreading
                    for (int j = 0; j < 100; j++) {
                        MQTTPubResult result = connection.publish("test-topic", "111111".getBytes(), AT_LEAST_ONCE, true);
                        System.out.println("pub --->" + result.isSuccessful());
                    }
                } catch (Exception e) {
                    logger.info(e.getMessage());
                    e.printStackTrace();
                }

//            });
        }

        TimeUnit.MINUTES.sleep(60);


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
        connection.setSubListener(((topic, message, ack) -> {
            ack.run();
            logger.info("pub sampler success !");
        }));

        connection.subscribe(topicNames, AT_LEAST_ONCE, () -> {
            logger.info(() -> "sub successful, topic length is " + topicNames.length);
        }, error -> {
            logger.log(Level.INFO, "subscribe failed", error);
        });

        TimeUnit.SECONDS.sleep(30);

    }

    @Test
    public void wrongUrl() throws Exception {
        TimeUnit.SECONDS.sleep(15);

        ConnectionParameters parameters = new ConnectionParameters();
        parameters.setHost("10.42.3.134");
        parameters.setPort(14567);
//            TimeUnit.SECONDS.sleep(5);
        String clientId = Util.generateClientId("QUI-");
        parameters.setClientId(clientId);
        MQTTClient client = MQTT.getInstance(Constants.QUIC_MQTT_CLIENT_NAME).createClient(parameters);
        MQTTConnection connection = client.connect();
        System.out.println(connection.isConnectionSucc());
    }


    @Test
    public void testConn() throws Exception {
        for(int i=0;i<10;i++){
            ConnectionParameters parameters = new ConnectionParameters();
            parameters.setHost("10.42.3.130");
            parameters.setPort(14567);
//            TimeUnit.SECONDS.sleep(5);
            String clientId = Util.generateClientId("QUI-");
            parameters.setClientId(clientId);
            MQTTClient client = MQTT.getInstance(Constants.QUIC_MQTT_CLIENT_NAME).createClient(parameters);
            MQTTConnection connection = client.connect();
            System.out.println(connection.isConnectionSucc());
        }

        TimeUnit.SECONDS.sleep(60);
    }


    @Test
    public void testConns() throws Exception {
//        ExecutorService service = Executors.newFixedThreadPool(2);
        for(int i=0;i<3;i++){
            ConnectionParameters parameters = new ConnectionParameters();
            parameters.setHost("10.42.3.130");
            parameters.setPort(14567);
            String clientId = Util.generateClientId("QUI-");
            parameters.setClientId(clientId);
            MQTTClient client = MQTT.getInstance(Constants.QUIC_MQTT_CLIENT_NAME).createClient(parameters);

//            service.submit(() -> {
                try {
                    logger.info("client----"+client.toString());
                    MQTTConnection connection = client.connect();
                    logger.info("--------"+connection.isConnectionSucc());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
//            });
        }


        TimeUnit.SECONDS.sleep(60);
    }
}
