package net.xmeter.samplers.mqtt.quic.internal.mqtt;

import com.sun.jna.Pointer;
import net.xmeter.samplers.mqtt.quic.internal.NngPointerByReference;

public class PropertyPointerByReference extends NngPointerByReference {
    public PropertyPointerByReference() {
    }

    public PropertyPointerByReference(Pointer p) {
        super(p);
    }

    public PropertyPointer getPropertyPointer() {
        PropertyPointer prop = new PropertyPointer();
        prop.setPointer(getValue());
        return prop;
    }
}
