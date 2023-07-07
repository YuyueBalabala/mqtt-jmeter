package net.xmeter.samplers.mqtt.quic.internal;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;

public interface NngMsgCallback extends Callback {
    int callback(Pointer p1, Pointer p2);
}
