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
import java.util.concurrent.Semaphore;
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

    private final static String disconnInfo = "Callback: Disconnected";
    private final static String sendInfo = "Callback: Sent";
    private final static String receivedInfo = "Callback: Received";

    private static final Logger logger = Logger.getLogger(ConnectSampler.class.getCanonicalName());

    private final MqttQuicClientSocket sock;
    private MQTTSubListener listener;

    private final String clientId;
    private boolean isConn = false;

    public QuicMQTTConnection(MqttQuicClientSocket sock,String clientId,boolean isConn ) {
        this.sock = sock;
        this.clientId = clientId;
        this.isConn = isConn;
        this.sock.setSendCallback(new QuicCallback(handler), sendInfo);
        this.sock.setReceiveCallback(new QuicCallback(recvHandler), receivedInfo);
    }

    final static BiFunction<Message, String, Integer> handler = (msg, arg) -> {
        logger.info(arg);
        return 0;
    };

    final static BiFunction<Message, String, Integer> recvHandler = (msg, arg) -> {
        logger.info(arg);
        try {
            PublishMsg publishMsg = new PublishMsg(msg);
            logger.info("Recieved =>"+
                            "\nTopic: " + publishMsg.getTopic() +
                            "\nQos: " + publishMsg.getQos()+
                            "\nPayload: " + StandardCharsets.UTF_8.decode(publishMsg.getPayload()));
        } catch (NngException e) {
            logger.log(Level.SEVERE, "recv failed ", e);
            throw new RuntimeException(e);
        }


        return 0;
    };




    @Override
    public boolean isConnectionSucc() {
        return isConn;
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
        if(!isConn){
            return new MQTTPubResult(false);
        }
        try {

            PublishMsg pubMsg = new PublishMsg();
            pubMsg.setPayload(new String(message));
            byte qosVal = QuicUtil.qosVal(qos);
            pubMsg.setQos(qosVal);
            pubMsg.setTopic(topicName);
            pubMsg.setRetain(retained);
            sock.sendMessage(pubMsg);
            logger.info("clientId=>"+this.clientId+" payload=>"+new String(message)+" topic=>"+topicName +" qos=>"+qosVal);

            return new MQTTPubResult(true);

        } catch (Exception exception) {
            logger.log(Level.SEVERE, "Publish failed :" + clientId, exception);
            return new MQTTPubResult(false, exception.getMessage());
        }
    }

    @Override
    public void subscribe(String[] topicNames, MQTTQoS qos, Runnable onSuccess, Consumer<Throwable> onFailure) {
        if(!isConn){
            return;
        }
        byte qosVal = QuicUtil.qosVal(qos);
        for(String topic:topicNames){
            List<TopicQos> topicQosList = Collections.singletonList(new TopicQos(topic, qosVal));
            SubscribeMsg subMsg = null;
            try {
                subMsg = new SubscribeMsg(topicQosList);
                sock.sendMessage(subMsg);
            } catch (NngException e) {
                throw new RuntimeException(e);
            }
            logger.info("clientId=>"+this.clientId+" topic=>"+topic);
        }
    }

    @Override
    public void setSubListener(MQTTSubListener listener) {
        this.listener = listener;
    }



    @Override
    public String toString() {
        return "QuicMQTTConnection{" +
                "clientId='" + clientId + '\'' +
                '}';
    }
}
