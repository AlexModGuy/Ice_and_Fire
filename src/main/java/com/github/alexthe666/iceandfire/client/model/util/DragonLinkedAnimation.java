package com.github.alexthe666.iceandfire.client.model.util;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.ilexiconn.llibrary.server.animation.Animation;

import java.util.ArrayList;
import java.util.List;

public class DragonLinkedAnimation {

    public List<EnumDragonAnimations> animations = new ArrayList<EnumDragonAnimations>();
    public Animation linked_animation;

    public DragonLinkedAnimation(Animation linked_animation, EnumDragonAnimations... animations) {
        for(EnumDragonAnimations anim : animations){
            this.animations.add(anim);
        }
        this.linked_animation = linked_animation;
    }

    public EnumDragonAnimations getCurrentPose(EntityDragonBase dragon){
        float progress = dragon.getAnimationTick() / linked_animation.getDuration();
        int count = this.animations.size();
        return this.animations.get(count * Math.round(progress));
    }
}
