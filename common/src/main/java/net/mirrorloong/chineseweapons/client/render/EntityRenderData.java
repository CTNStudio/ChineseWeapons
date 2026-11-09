package net.mirrorloong.chineseweapons.client.render;

/**
 * Texture paths and transform constants shared by the two entity renderers.
 * Values are copied from the former per-target renderers; they are paths
 * relative to the mod namespace so every target can turn them into its own
 * ResourceLocation/Identifier type.
 */
public final class EntityRenderData {
    private EntityRenderData() {
    }

    public static final float JAVELIN_SCALE = 1.0F;
    public static final float JAVELIN_YAW_OFFSET = -90.0F;
    public static final float JAVELIN_PITCH_OFFSET = 90.0F;

    public static final float SHIELD_SHADOW_RADIUS = 0.0F;
    public static final float SHIELD_SCALE_X = -1.0F;
    public static final float SHIELD_SCALE_Y = -1.0F;
    public static final float SHIELD_SCALE_Z = 1.0F;
    public static final double SHIELD_TRANSLATE_Y = -1.5D;

    public static final String SHIELD_TEXTURE = "textures/entity/song_standing_shield.png";
    public static final String SHIELD_TEXTURE_CRACKED_1 = "textures/entity/song_standing_shield_cracked_1.png";
    public static final String SHIELD_TEXTURE_CRACKED_2 = "textures/entity/song_standing_shield_cracked_2.png";
    public static final String SHIELD_TEXTURE_CRACKED_3 = "textures/entity/song_standing_shield_cracked_3.png";
    public static final String SHIELD_TEXTURE_CRACKED_4 = "textures/entity/song_standing_shield_cracked_4.png";

    /**
     * Same thresholds as the former renderers: below 400 HP the most cracked
     * texture is used, then 700, 1000 and 1300 before the pristine texture.
     */
    public static String shieldTexturePath(float health) {
        if (health < 400.0F) {
            return SHIELD_TEXTURE_CRACKED_4;
        }
        if (health < 700.0F) {
            return SHIELD_TEXTURE_CRACKED_3;
        }
        if (health < 1000.0F) {
            return SHIELD_TEXTURE_CRACKED_2;
        }
        if (health < 1300.0F) {
            return SHIELD_TEXTURE_CRACKED_1;
        }
        return SHIELD_TEXTURE;
    }
}
