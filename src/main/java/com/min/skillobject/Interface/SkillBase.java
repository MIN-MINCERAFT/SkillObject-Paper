package com.min.skillobject.Interface;

import org.bukkit.entity.LivingEntity;

public interface SkillBase extends SkillEffect{
	float getDistance();

	void skillAttack(LivingEntity target);
}
