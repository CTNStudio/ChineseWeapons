package net.mirrorloong.chineseweapons.spi;

import net.mirrorloong.chineseweapons.content.ArmorDefinition;

/**
 * Implemented by the generic armour item so callers can reach its definition.
 */
public interface ArmorItemLike {
    ArmorDefinition definition();
}
