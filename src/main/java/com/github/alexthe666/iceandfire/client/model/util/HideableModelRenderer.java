package com.github.alexthe666.iceandfire.client.model.util;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.iceandfire.client.model.ModelDreadThrall;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

public class HideableModelRenderer extends AdvancedModelBox {

    public boolean invisible;
    private int displayList;
    private boolean compiled;
    public HideableModelRenderer(AdvancedEntityModel model, String name) {
        super(model, name);
    }

    public HideableModelRenderer(AdvancedEntityModel model, int i, int i1) {
        super(model, i, i1);
    }

    @Override
    public void render(MatrixStack p_228309_1_, IVertexBuilder p_228309_2_, int p_228309_3_, int p_228309_4_, float p_228309_5_, float p_228309_6_, float p_228309_7_, float p_228309_8_) {
        if (!invisible) {
            if (this.showModel) {
               super.render(p_228309_1_, p_228309_2_, p_228309_3_, p_228309_4_, p_228309_5_, p_228309_6_, p_228309_7_, p_228309_8_);
            }
        }
    }
}
