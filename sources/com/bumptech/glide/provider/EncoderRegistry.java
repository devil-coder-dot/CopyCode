package com.bumptech.glide.provider;

import com.bumptech.glide.load.Encoder;
import java.util.ArrayList;
import java.util.List;

public class EncoderRegistry {
    private final List<Entry<?>> encoders = new ArrayList();

    private static final class Entry<T> {
        private final Class<T> dataClass;
        final Encoder<T> encoder;

        Entry(Class<T> cls, Encoder<T> encoder2) {
            this.dataClass = cls;
            this.encoder = encoder2;
        }

        /* access modifiers changed from: 0000 */
        public boolean handles(Class<?> cls) {
            return this.dataClass.isAssignableFrom(cls);
        }
    }

    public synchronized <T> Encoder<T> getEncoder(Class<T> cls) {
        for (Entry entry : this.encoders) {
            if (entry.handles(cls)) {
                return entry.encoder;
            }
        }
        return null;
    }

    public synchronized <T> void append(Class<T> cls, Encoder<T> encoder) {
        this.encoders.add(new Entry(cls, encoder));
    }

    public synchronized <T> void prepend(Class<T> cls, Encoder<T> encoder) {
        this.encoders.add(0, new Entry(cls, encoder));
    }
}
