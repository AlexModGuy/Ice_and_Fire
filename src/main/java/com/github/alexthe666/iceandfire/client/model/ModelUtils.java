package com.github.alexthe666.iceandfire.client.model;

import java.util.Iterator;
import java.util.List;

import net.ilexiconn.llibrary.client.model.modelbase.MowzieModelRenderer;
import net.ilexiconn.llibrary.common.animation.Animator;
import net.minecraft.client.model.ModelRenderer;

public class ModelUtils {
	public static void renderAll(List boxList){
		Iterator itr = boxList.iterator();
		while(itr.hasNext()) {
			Object element = itr.next();
			if(element instanceof MowzieModelRenderer){
				MowzieModelRenderer box = (MowzieModelRenderer)element;
				if(box.getParent() == null){
					box.render(0.0625F);
				}
			}
		}
	}
	public static void doMowzieStuff(List boxList, boolean reset){
		Iterator itr = boxList.iterator();
		while(itr.hasNext()) {
			Object element = itr.next();
			if(element instanceof MowzieModelRenderer){
				MowzieModelRenderer box = (MowzieModelRenderer)element;
				if(reset){
					box.setCurrentPoseToInitValues();
				}else{
					box.setInitValuesToCurrentPose();
				}
			}
		}
	}
	
	public static void animateOrRotate(Animator animator, boolean animate, MowzieModelRenderer box, float x, float y, float z){
		if(animate){
			animator.rotate(box, x == box.rotateAngleX ? 0 : x, y == box.rotateAngleY ? 0 : y, z == box.rotateAngleZ ? 0 : z);
		}else{
			ModelUtils.setRotateAngle(box, x, y, z);
		}
	}
	
	public static void animateOrRotateIgnore(Animator animator, boolean animate, MowzieModelRenderer box, float x, float y, float z, boolean ignoreX, boolean ignoreY, boolean ignoreZ){
		if(animate){
			animator.rotate(box, ignoreX ? 0 : x, ignoreY ? y : 0, ignoreZ ? z : 0);
		}else{
			ModelUtils.setRotateAngle(box, x, y, z);
		}
	}
	
	private static void setRotateAngle(MowzieModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
	
	public static void rotate(Animator animator, ModelRenderer box, float x, float y, float z){
		animator.rotate(box, (float)Math.toRadians(x), (float)Math.toRadians(y), (float)Math.toRadians(z));
	}
	
	public static void rotateFrom(Animator animator, ModelRenderer box, float x, float y, float z){
		animator.rotate(box, (float)Math.toRadians(x) - box.rotateAngleX, (float)Math.toRadians(y) - box.rotateAngleY,(float)Math.toRadians(z) - box.rotateAngleZ);
	}
	public static void rotateFromRadians(Animator animator, ModelRenderer box, float x, float y, float z){
		animator.rotate(box, x - box.rotateAngleX, y - box.rotateAngleY, z - box.rotateAngleZ);
	}
}
