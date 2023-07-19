package net.xmeter.samplers.mqtt.quic.mqtt.callback;

import com.sun.jna.Pointer;
import net.xmeter.samplers.mqtt.quic.internal.NngMsgCallback;
import net.xmeter.samplers.mqtt.quic.nng.Message;

import java.util.concurrent.Semaphore;
import java.util.function.BiFunction;
import java.util.logging.Logger;


public class QuicCallback<T, U, R> implements NngMsgCallback {
    private static final Logger logger = Logger.getLogger(QuicCallback.class.getCanonicalName());


    private T arg = null;

    private Message msg;
    private BiFunction<Message, T, Integer> function;

    private Semaphore lock;


    public QuicCallback() {

    }

    public QuicCallback(BiFunction<Message, T, Integer> function) {
        this.function = function;
    }
    public QuicCallback(BiFunction<Message, T, Integer> function, Semaphore lock) {
        this.arg = arg;
        this.function = function;
        this.lock = lock;
    }

    public T getArg() {
        return arg;
    }

    public void setArg(T arg) {
        this.arg = arg;
    }

    @Override
    public int callback(Pointer p1, Pointer p2) {
        try {
            if(lock !=null){
                this.lock.release();
                logger.info("lock:"+lock);
            }
            return this.function.apply(p1 == Pointer.NULL ? null : new Message(p1), this.arg);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }
        return 0;
    }
}