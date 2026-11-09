package net.mirrorloong.chineseweapons.spi;

import java.util.function.Supplier;

/**
 * Carries the registry id currently being constructed. Loaders that require
 * ids during object construction (Minecraft 26.1) read {@link #current()} from
 * content factories; older versions ignore it.
 */
public final class ContentIds {
    private static final ThreadLocal<String> CURRENT = new ThreadLocal<>();

    private ContentIds() {
    }

    public static <T> T with(String id, Supplier<T> supplier) {
        String previous = CURRENT.get();
        CURRENT.set(id);
        try {
            return supplier.get();
        } finally {
            if (previous == null) {
                CURRENT.remove();
            } else {
                CURRENT.set(previous);
            }
        }
    }

    public static String current() {
        return CURRENT.get();
    }
}
