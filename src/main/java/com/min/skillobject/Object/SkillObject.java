package com.min.skillobject.Object;

import com.min.skillobject.Interface.SkillBase;
import com.min.skillobject.Interface.SkillEffect;
import com.min.skillobject.Runnable.SkillObjectRunnable;
import com.min.skillobject.SkillManager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import static java.lang.Math.*;

abstract public class SkillObject {
	protected Player owner = null;

	protected Location location;

	protected boolean closed = false;

	protected int closeTimer = 10;

	public SkillObject(Location location) {
		this.location = location;
	}

	public final void setOwner(Player owner) {
		this.owner = owner;
	}

	public final Player getOwner() {
		if (owner == null) {
			return null;
		}
		return owner;
	}

	public final void teleport(Location position) {
		this.location = position;
	}

	public final void teleport(Vector position) {
		this.location = new Location(location.getWorld(), position.getX(), position.getY(), position.getZ(), location.getYaw(), location.getPitch());
	}

	public final Location getLocation() {
		return location;
	}

	public final boolean isClosed() {
		return closed;
	}

	public void close() {
		closed = true;
	}

	public final void spawn() {
		SkillObjectRunnable.addObject(this);
	}

	public final void setCloseTimer(int tick) {
		closeTimer = tick;
	}

	public final Location getDirectionLocation() {
		return getDirectionVector().toLocation(location.getWorld());
	}

	public final Vector getDirectionVector() {
		Location location = this.location;
		double y = -sin(toRadians(location.getPitch()));
		double xz = cos(toRadians(location.getPitch()));
		double x = -xz * sin(toRadians(location.getYaw()));
		double z = xz * cos(toRadians(location.getYaw()));
		float length = (float) sqrt(x * x + y * y + z * z);
		if(length == 0) return new Vector(0, 0, 0);
		return new Vector(x / length, y / length, z / length);
	}

	public void skillTick() {
		this.closeTimer--;
		if (this.closeTimer <= 0) {
			this.close();
			return;
		}
		Player owner = this.owner;
		if (owner == null) {
			close();
			return;
		}
		if (this instanceof SkillEffect) {
			((SkillEffect) this).skillEffect();
		}
		if (this instanceof SkillBase) {
			Location location = this.location;
			World world = location.getWorld();
			for (Entity entity : world.getEntities()) {
				if (location.distance(entity.getLocation()) > ((SkillBase) this).getDistance()) continue;
				if(entity.getType().getEntityClass() != null) {
					if (SkillManager.canTarget.contains(entity.getType().getEntityClass().getName())) {
						((SkillBase) this).skillAttack((LivingEntity) entity);
						continue;
					}
				}
				if (SkillManager.pvpWorlds.contains(world.getName())) {
					if (entity instanceof Player && (getOwner() == null || owner.getUniqueId() != entity.getUniqueId())) {
						if (((Player) entity).getGameMode() == GameMode.SURVIVAL) {
							((SkillBase) this).skillAttack((LivingEntity) entity);
						}
					}
				}
			}
		}
	}
}
