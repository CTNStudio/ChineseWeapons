package net.mirrorloong.chineseweapons.spi;

import java.util.Objects;

/**
 * Loader- and version-neutral registry id. 26.1 renamed {@code ResourceLocation}
 * to {@code Identifier}; the shared source tree must not name either type.
 */
public record ResourceId(String namespace, String path) {
    public ResourceId {
        Objects.requireNonNull(namespace, "namespace");
        Objects.requireNonNull(path, "path");
    }

    public static ResourceId parse(String value) {
        Objects.requireNonNull(value, "value");
        int separator = value.indexOf(':');
        if (separator < 0) {
            return new ResourceId("minecraft", value);
        }
        return new ResourceId(value.substring(0, separator), value.substring(separator + 1));
    }

    @Override
    public String toString() {
        return namespace + ":" + path;
    }
}
