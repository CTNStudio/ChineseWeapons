package net.mirrorloong.chineseweapons.spi;

/**
 * Direction a custom packet travels. Directions are declared by the shared
 * code; the loader target decides how to route them.
 */
public enum PacketDirection {
    SERVERBOUND,
    CLIENTBOUND
}
