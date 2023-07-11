package net.xmeter.samplers.mqtt.quic;

import net.xmeter.samplers.mqtt.ConnectionParameters;
import net.xmeter.samplers.mqtt.MQTTClient;
import net.xmeter.samplers.mqtt.MQTTConnection;
import net.xmeter.samplers.mqtt.quic.mqtt.MqttQuicClientSocket;
import net.xmeter.samplers.mqtt.quic.mqtt.msg.ConnectMsg;
import net.xmeter.samplers.mqtt.quic.nng.NngException;

import java.util.logging.Logger;

/**
 * @author ：yuyue
 * @date ：Created in 2023/7/7 下午5:46
 * @description：
 */

public class QuicMQTTClient  implements MQTTClient {

    private static final Logger logger = Logger.getLogger(QuicMQTTClient.class.getCanonicalName());


    private MqttQuicClientSocket sock;
    private String clientId;
    private String url;


    public QuicMQTTClient(ConnectionParameters parameters) {
        this.clientId = parameters.getClientId();
        this.url = createHostAddress(parameters);
    }


    private ConnectMsg createConnMsg(String clientId) throws NngException {
        ConnectMsg connMsg = new ConnectMsg();
        connMsg.setCleanSession(true);
        connMsg.setKeepAlive((short) 60);
        connMsg.setClientId(clientId);
        connMsg.setProtoVersion(4);
        return connMsg;
    }


    private String createHostAddress(ConnectionParameters parameters) {
        return "mqtt-quic" + "://" + parameters.getHost() + ":" + parameters.getPort();
    }


    @Override
    public String getClientId() {
        return this.clientId;
    }

    @Override
    public MQTTConnection connect() throws Exception {
        ConnectMsg connMsg = createConnMsg(this.clientId);
        this.sock = new MqttQuicClientSocket(this.url);
        logger.info(() -> "Created mqtt quic socket: " + this.clientId +" url:"+this.url);
        this.sock.sendMessage(connMsg);
        return new QuicMQTTConnection(this.sock,this.clientId,connMsg);
    }
}