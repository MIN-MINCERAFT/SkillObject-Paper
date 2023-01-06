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

	protected int closeTick = 10;

	protected int tick = 0;

	public SkillObject(Location location) {
		this.location = location;
	}

	public final Player getOwner() {
		if (owner == null) {
			return null;
		}
		return owner;
	}

	public final void setOwner(Player owner) {
		this.owner = owner;
	}

	public final void teleport(Location position) {
		location = position;
	}

	public final void teleport(Vector position) {
		location = new Location(location.getWorld(), position.getX(), position.getY(), position.getZ(), location.getYaw(), location.getPitch());
	}

	public final Location getLocation() {
		return location;
	}

	public final Vector getDirectionVector() {
		Location location = this.location;
		double y = -sin(toRadians(location.getPitch()));
		double xz = cos(toRadians(location.getPitch()));
		double x = -xz * sin(toRadians(location.getYaw()));
		double z = xz * cos(toRadians(location.getYaw()));
		float length = (float) sqrt(x * x + y * y + z * z);
		if (length == 0) return new Vector(0, 0, 0);
		return new Vector(x / length, y / length, z / length);
	}

	public final int getTick() {
		return tick;
	}

	public final boolean isClosed() {
		return closed;
	}

	public void close() {
		closed = true;
	}

	public final void setCloseTick(int closeTick) {
		this.closeTick = closeTick;
	}

	public final void spawn() {
		SkillObjectRunnable.addObject(this);
	}

	public void skillTick() {
		if (closeTick <= 0) {
			this.close();
			return;
		}
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
				if (entity.getType().getEntityClass() != null) {
					if (SkillManager.canTarget.contains(entity.getType().getEntityClass().getName())) {
						((SkillBase) this).skillAttack((LivingEntity) entity);
						continue;
					}
				}
				if (SkillManager.pvpWorlds.contains(world.getName())) {
					if (entity instanceof Player && (owner == null || owner.getUniqueId() != entity.getUniqueId())) {
						if (((Player) entity).getGameMode() == GameMode.SURVIVAL) {
							((SkillBase) this).skillAttack((LivingEntity) entity);
						}
					}
				}
			}
		}
		closeTick--;
		tick++;
	}
}
