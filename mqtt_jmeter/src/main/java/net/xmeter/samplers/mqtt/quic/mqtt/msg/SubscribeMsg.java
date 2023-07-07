package net.xmeter.samplers.mqtt.quic.mqtt.msg;


import net.xmeter.samplers.mqtt.quic.internal.jna.Size;
import net.xmeter.samplers.mqtt.quic.internal.jna.UInt32;
import net.xmeter.samplers.mqtt.quic.internal.mqtt.TopicQosPointer;
import net.xmeter.samplers.mqtt.quic.internal.mqtt.constants.MqttPacketType;
import net.xmeter.samplers.mqtt.quic.mqtt.data.TopicQos;
import net.xmeter.samplers.mqtt.quic.nng.Message;
import net.xmeter.samplers.mqtt.quic.nng.Nng;
import net.xmeter.samplers.mqtt.quic.nng.NngException;

import java.util.List;

public class SubscribeMsg extends MqttMessage {

    private List<TopicQos> topicQosList;

    public SubscribeMsg() throws NngException {
        super(MqttPacketType.NNG_MQTT_SUBSCRIBE);
    }

    public SubscribeMsg(Message msg) throws NngException {
        super(msg.getMessagePointer());
    }

    public SubscribeMsg(List<TopicQos> topicQosList) throws NngException {
        super(MqttPacketType.NNG_MQTT_SUBSCRIBE);
        this.topicQosList = topicQosList;
        setTopicQosList(topicQosList);
    }

    private TopicQosPointer createTopicQosPointer(List<TopicQos> topicQosList) {
        TopicQosPointer topicQosPointer = Nng.lib().nng_mqtt_topic_qos_array_create(new Size(topicQosList.size()));
        for (int i = 0; i < topicQosList.size(); i++) {
            Nng.lib().nng_mqtt_topic_qos_array_set(topicQosPointer, new Size(i), topicQosList.get(i).getTopic(), topicQosList.get(i).getQos());
        }
        return topicQosPointer;
    }

    public void setTopicQosList(List<TopicQos> topicQosList) {
        this.topicQosList = topicQosList;
        TopicQosPointer topicQosPointer = createTopicQosPointer(topicQosList);
        Nng.lib().nng_mqtt_msg_set_subscribe_topics(super.msg, topicQosPointer, new UInt32(topicQosList.size()));
        Nng.lib().nng_mqtt_topic_qos_array_free(topicQosPointer, new Size(topicQosList.size()));
    }

    public List<TopicQos> getTopicQosList() {
        return this.topicQosList;
    }

}
