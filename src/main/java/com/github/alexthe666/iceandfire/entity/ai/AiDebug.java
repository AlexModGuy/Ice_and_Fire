package com.github.alexthe666.iceandfire.entity.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.MobEntity;


/**
 * Class to easily list the current tasks/goals of entities and identify potential issues
 */
public class AiDebug {
    private static List<MobEntity> entities = new ArrayList<>();
    private static Logger LOGGER = LogManager.getLogger();

    private AiDebug() {
        //Hides default constructor.
    }

    public static boolean isEnabled(){
        return false;
    }

    public static void logData(){
        List<MobEntity> entitiesCopy = new ArrayList<>(entities);
        for (MobEntity entity : entitiesCopy) {
            if (!entity.isAlive()){
                entities.remove(entity);
                continue;
            }
            if (entity.goalSelector != null) {
                List<String> goals = entity.goalSelector.getRunningGoals().map(goal -> goal.getGoal().toString()).collect(Collectors.toList());
                if (!goals.isEmpty())
                    LOGGER.debug("{} - GOALS: {}", entity, goals);
            }
            if (entity.targetSelector != null) {
                List<String> targets = entity.targetSelector.getRunningGoals().map(goal -> goal.getGoal().toString()).collect(Collectors.toList());
                if (!targets.isEmpty())
                    LOGGER.debug("{} - TARGET: {}", entity, targets);
            }

        }
    }

    public static boolean contains(MobEntity entity){
        return entities.contains(entity);
    }

    public static void addEntity(MobEntity entity){
        if (entities.contains(entity)){
            entities.remove(entity);
        }
        else {
            entities.add(entity);
        }
    }


}
