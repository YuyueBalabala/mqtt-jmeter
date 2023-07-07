package net.xmeter.samplers.mqtt.quic.internal.mqtt;

import com.sun.jna.Pointer;
import net.xmeter.samplers.mqtt.quic.internal.NngPointerByReference;

public class TopicQosPointerByReference extends NngPointerByReference {
    public TopicQosPointerByReference() {
    }

    public TopicQosPointerByReference(Pointer p) {
        super(p);
    }

    public TopicQosPointer getTopicQosPointer() {
        TopicQosPointer topicQosPointer = new TopicQosPointer();
        topicQosPointer.setPointer(getValue());
        return topicQosPointer;
    }
}
