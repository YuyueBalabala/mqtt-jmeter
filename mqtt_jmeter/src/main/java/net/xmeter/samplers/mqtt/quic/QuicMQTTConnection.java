package net.xmeter.samplers.mqtt.quic;

import net.xmeter.samplers.ConnectSampler;
import net.xmeter.samplers.mqtt.MQTTConnection;
import net.xmeter.samplers.mqtt.MQTTPubResult;
import net.xmeter.samplers.mqtt.MQTTQoS;
import net.xmeter.samplers.mqtt.MQTTSubListener;
import net.xmeter.samplers.mqtt.quic.internal.mqtt.constants.MqttPacketType;
import net.xmeter.samplers.mqtt.quic.mqtt.MqttQuicClientSocket;
import net.xmeter.samplers.mqtt.quic.mqtt.callback.QuicCallback;
import net.xmeter.samplers.mqtt.quic.mqtt.data.TopicQos;
import net.xmeter.samplers.mqtt.quic.mqtt.msg.PublishMsg;
import net.xmeter.samplers.mqtt.quic.mqtt.msg.SubscribeMsg;
import net.xmeter.samplers.mqtt.quic.nng.Message;
import net.xmeter.samplers.mqtt.quic.nng.NngException;
import net.xmeter.samplers.mqtt.quic.nng.Socket;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author ：yuyue
 * @date ：Created in 2023/7/10 上午11:27
 * @description：
 */

public class QuicMQTTConnection implements MQTTConnection {

    private static final Logger logger = Logger.getLogger(ConnectSampler.class.getCanonicalName());


    private String topic;
    private byte qos;
    private String payload;

    private final MqttQuicClientSocket sock;
    private MQTTSubListener listener;

    private final String clientId;
    private boolean retained;

    public MqttPacketType packetType;




    public QuicMQTTConnection(MqttQuicClientSocket sock,String clientId) {
        this.sock = sock;
        this.clientId = clientId;
    }

    private final String disconnInfo = "Callback: Disconnected";
    private final String sendInfo = "Callback: Sent";


    final BiFunction<Message, Socket, Integer> connectHandler = (msg, sock) -> {
        String connInfo = "Callback: Connected";
        logger.info(connInfo);
        try {
            if (this.packetType == MqttPacketType.NNG_MQTT_SUBSCRIBE) {
                List<TopicQos> topicQosList = Collections.singletonList(new TopicQos(this.topic, this.qos));
                SubscribeMsg subMsg = new SubscribeMsg(topicQosList);
                sock.sendMessage(subMsg);
            } else if (this.packetType == MqttPacketType.NNG_MQTT_PUBLISH) {
                PublishMsg pubMsg = new PublishMsg();
                pubMsg.setPayload(this.payload);
                pubMsg.setQos(this.qos);
                pubMsg.setTopic(this.topic);
                sock.sendMessage(pubMsg);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.info(ex.getMessage());
            return -1;
        }
        return 0;
    };


    final BiFunction<Message, String, Integer> handler = (msg, arg) -> {
        logger.info(arg);
        return 0;
    };

    final BiFunction<Message, String, Integer> recvHandler = (msg, arg) -> {
        logger.info("1111"+arg);
        try {
            PublishMsg publishMsg = new PublishMsg(msg);
            logger.info("Topic: " + publishMsg.getTopic());
            logger.info("Qos: " + publishMsg.getQos());
            logger.info("Payload: " + StandardCharsets.UTF_8.decode(publishMsg.getPayload()));
        } catch (NngException e) {
            logger.log(Level.SEVERE, "recv failed ", e);
            throw new RuntimeException(e);
        }


        return 0;
    };




    @Override
    public boolean isConnectionSucc() {
        return true;
    }

    @Override
    public String getClientId() {

        return this.clientId;
    }

    @Override
    public void disconnect() throws Exception {
        this.sock.setDisconnectCallback(new QuicCallback(handler), disconnInfo);
        logger.info("disconnect:"+clientId);

    }

    @Override
    public MQTTPubResult publish(String topicName, byte[] message, MQTTQoS qos, boolean retained) {
        this.payload = new String(message);
        this.qos = QuicUtil.qosVal(qos);
        this.topic = topicName;
        this.retained = retained;
        logger.info("clientId=>"+this.clientId+" payload=>"+this.payload+" topic=>"+topic +" qos=>"+this.qos);
        this.packetType = MqttPacketType.NNG_MQTT_PUBLISH;
        try {
            this.sock.setConnectCallback(new QuicCallback(connectHandler), this.sock);
            this.sock.setSendCallback(new QuicCallback(handler), sendInfo);
            Thread.sleep(3000);
            return new MQTTPubResult(true);
        } catch (Exception exception) {
            return new MQTTPubResult(false, exception.getMessage());
        }
    }

    @Override
    public void subscribe(String[] topicNames, MQTTQoS qos, Runnable onSuccess, Consumer<Throwable> onFailure) {

        this.qos = QuicUtil.qosVal(qos);
        String receivedInfo = "Callback: Received";
        for(String topic:topicNames){
            this.topic = topicNames[0];
            this.packetType = MqttPacketType.NNG_MQTT_SUBSCRIBE;
            this.sock.setConnectCallback(new QuicCallback(connectHandler), this.sock);
            logger.info("clientId=>"+this.clientId+" topic=>"+topic +" receivedInfo=>"+receivedInfo);
            this.sock.setReceiveCallback(new QuicCallback(recvHandler), receivedInfo);
            this.sock.setSendCallback(new QuicCallback(handler), sendInfo);
        }

        for (; ; ) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }

    }

    @Override
    public void setSubListener(MQTTSubListener listener) {
        this.listener = listener;
    }
}
