package io.hawk.clock.state;


/**
 * Created by lan on 2016/4/14.
 */
public interface BaseState {

    void registerEvent(Object recevier);

    void unregisterEvent(Object recevier);

    class BaseArgumentEvent<T> {
        public final T item;

        public BaseArgumentEvent(T item) {
            this.item = item;
        }
    }

}
