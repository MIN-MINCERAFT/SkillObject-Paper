package com.min.skillobject;

import com.min.skillobject.Runnable.SkillObjectRunnable;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class SkillManager extends JavaPlugin {
	public static List<String> pvpWorlds;

	public static List<String> canTarget;

	public void addPvpWorld(String world) {
		if (!pvpWorlds.contains(world)) {
			pvpWorlds.add(world);
		}
	}

	public void addPvpWorld(World world) {
		if (!pvpWorlds.contains(world.getName())) {
			pvpWorlds.add(world.getName());
		}
	}

	public void removePvpWorld(String world) {
		pvpWorlds.removeIf(pvpWorld -> pvpWorld.equals(world));
	}

	public void removePvpWorld(World world) {
		pvpWorlds.removeIf(pvpWorld -> pvpWorld.equals(world.getName()));
	}

	public void registerMob(EntityType entity) {
		if (entity.getEntityClass() != null) {
			if (!canTarget.contains(entity.getEntityClass().getName())) {
				canTarget.add(entity.getEntityClass().getName());
			}
		}
	}

	public void registerMob(String entity) {
		if (!canTarget.contains(entity)) {
			canTarget.add(entity);
		}
	}

	@Override
	public void onEnable() {
		pvpWorlds = new ArrayList<>();
		canTarget = new ArrayList<>();
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new SkillObjectRunnable(), 0, 1);
	}
}
