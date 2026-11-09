package net.mirrorloong.chineseweapons;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

/**
 * Sample test for the shared source tree. Every target compiles this same file
 * into its own test source set, and `gradlew -p common test` runs it against the
 * standalone common classpath.
 */
class ChineseWeaponsCommonTest {
    @Test
    void sharedModuleIsReachable() {
        assertNotNull(ChineseWeaponsCommon.class.getName());
    }
}
