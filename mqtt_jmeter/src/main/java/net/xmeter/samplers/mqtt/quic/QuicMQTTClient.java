package net.xmeter.samplers.mqtt.quic;

import net.xmeter.samplers.mqtt.ConnectionParameters;
import net.xmeter.samplers.mqtt.MQTTClient;
import net.xmeter.samplers.mqtt.MQTTConnection;
import net.xmeter.samplers.mqtt.quic.mqtt.MqttQuicClientSocket;
import net.xmeter.samplers.mqtt.quic.mqtt.callback.QuicCallback;
import net.xmeter.samplers.mqtt.quic.mqtt.msg.ConnectMsg;
import net.xmeter.samplers.mqtt.quic.nng.Message;
import net.xmeter.samplers.mqtt.quic.nng.NngException;
import net.xmeter.samplers.mqtt.quic.nng.Socket;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.logging.Logger;

/**
 * @author ：yuyue
 * @date ：Created in 2023/7/7 下午5:46
 * @description：
 */

public class QuicMQTTClient  implements MQTTClient {

    private static final Logger logger = Logger.getLogger(QuicMQTTClient.class.getCanonicalName());

    private MqttQuicClientSocket sock;

    private boolean isConn = false;

    final private Semaphore connLock =new Semaphore(0);

    private ConnectionParameters parameters;

    private QuicCallback connCb;


    public QuicMQTTClient(ConnectionParameters parameters) {
        this.parameters = parameters;
        this.connCb=new QuicCallback(connectHandler);

    }

    private ConnectMsg createConnMsg() throws NngException {
        ConnectMsg connMsg = new ConnectMsg();
        connMsg.setClientId(parameters.getClientId());

        if(!StringUtils.isEmpty(parameters.getUsername()) && !StringUtils.isEmpty(parameters.getPassword())){
            connMsg.setPassword(parameters.getPassword());
            connMsg.setUserName(parameters.getUsername());
        }
        connMsg.setCleanSession(parameters.isCleanSession());
        connMsg.setKeepAlive(parameters.getKeepAlive());
        connMsg.setProtoVersion(4);

        return connMsg;
    }



    private String createHostAddress(ConnectionParameters parameters) {
        return "mqtt-quic" + "://" + parameters.getHost() + ":" + parameters.getPort();
    }


    @Override
    public String getClientId() {
        return parameters.getClientId();
    }

    @Override
    public MQTTConnection connect() throws Exception {
        ConnectMsg connMsg =  createConnMsg();
        String url =createHostAddress(parameters);
        this.sock = new MqttQuicClientSocket(url);
        logger.info(() -> "Created mqtt quic socket: " + parameters.getClientId() +" url: "+url);
        this.sock.sendMessage(connMsg);
        this.sock.setConnectCallback(connCb, this.sock);
        int timeout = parameters.getConnectTimeout() > 0? parameters.getConnectTimeout() : 10;
        return new QuicMQTTConnection(this.sock,parameters.getClientId(),connLock.tryAcquire(timeout, TimeUnit.SECONDS));

    }

    final BiFunction<Message, Socket, Integer> connectHandler = (msg, sock) -> {
        logger.info(" Callback: connect");
        isConn = true;
        connLock.release();
        return 0;
    };
}
