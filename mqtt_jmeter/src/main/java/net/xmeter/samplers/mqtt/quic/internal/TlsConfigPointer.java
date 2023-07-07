package net.xmeter.samplers.mqtt.quic.internal;

import com.sun.jna.FromNativeContext;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

public class TlsConfigPointer extends PointerType {
    public TlsConfigPointer() {}

    public TlsConfigPointer(Pointer p) {
        setPointer(p);
    }

    @Override
    public Object fromNative(Object nativeValue, FromNativeContext context) {
        return super.fromNative(nativeValue, context);
    }
}
