package net.xmeter.samplers.mqtt.quic.internal.mqtt;

import com.sun.jna.Pointer;
import net.xmeter.samplers.mqtt.quic.internal.NngPointerByReference;

public class BytesPointerByReference extends NngPointerByReference {
    public BytesPointerByReference() {
    }

    public BytesPointerByReference(Pointer p) {
        super(p);
    }

    public BytesPointer getBytesPointer() {
        BytesPointer bytePtr = new BytesPointer();
        bytePtr.setPointer(getValue());
        return bytePtr;
    }
}
