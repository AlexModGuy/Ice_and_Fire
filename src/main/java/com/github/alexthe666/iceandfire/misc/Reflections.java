package com.github.alexthe666.iceandfire.misc;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class Reflections {

	public void reflectField(Class clazz, String[] obv, Object replacedField, Object replacer){
        Field field = ReflectionHelper.findField(clazz, ObfuscationReflectionHelper.remapFieldNames(clazz.getName(), obv));
        try
        {
            Field modifier = Field.class.getDeclaredField("modifiers");
            modifier.setAccessible(true);
            modifier.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(replacer, replacedField);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
	}
}
