package net.mirrorloong.chineseweapons.client.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

/**
 * 26.1 needs a real {@code EntityModel} for {@code submitModel}, but the
 * geometry is shared; this adapter only supplies the version's base class.
 */
public final class SpecEntityModel extends EntityModel<EntityRenderState> {
    public SpecEntityModel(ModelPart root) {
        super(root);
    }
}
