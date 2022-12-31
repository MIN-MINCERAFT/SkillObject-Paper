<h1>SkillObject</h1>

<img src="https://img.shields.io/badge/JAVA-F80000?style=flat&logo=ORACLE&logoColor=white" alt="">

<hr/>

### How to bind SkillObject to your plugin

```java
SkillManager skillManager=(SkillManager)this.getServer().getPluginManager().getPlugin("SkillObject");
if(skillManager==null)return;
```

### How to register entity that can take damage

<span style="color:red">Don't register player entity</span>

```java
SkillManager skillManager=(SkillManager)this.getServer().getPluginManager().getPlugin("SkillObject");
skillManager.registerMob(EntityType.VILLAGER);
```

### How to add world that can damage players

```java
SkillManager skillManager=(SkillManager)this.getServer().getPluginManager().getPlugin("SkillObject");
skillManager.addWorld("world name");
```

### How to make SkillObject

#### 1. Create class that extends SkillObject and implement Skill

#### (Example)

```java
public class RoastSkillObject extends SkillObject implements SkillEffect {
	public RoastSkillObject(Location location) {
		super(location);
	}

	@Override
	public void skillEffect() {
	  /**
	   * Here is skill effect
	   * You can decide here what the skill object will do per tick.
	   */
		teleport(getDirectionVector().multiply(1.5).add(
				new Vector(
						location.getX(),
						location.getY(),
						location.getZ()
				)
		));
		Location location = getLocation();
		location.getWorld().spawnParticle(Particle.REDSTONE, location, 1, (new Particle.DustOptions(Color.BLUE, 3)));
	}
}
```

#### if you want to make skill object that can damage , you should implement SkillBase

```java
public class RoastSkillObject extends SkillObject implements SkillBase {
	public RoastSkillObject(Location location) {
		super(location);
	}

	@Override
	public float getDistance() {
		// Here is distance that skill object can damage
		return 2;
	}

	@Override
	public void skillAttack(LivingEntity target) {
		// Here is skill effect when skill object can damage
		// You can decide here what the skill object will do when it can damage
		target.damage(6);
		target.setFireTicks(40);
	}

	@Override
	public void skillEffect() {
		/**
		 * Here is skill effect
		 * You can decide here what the skill object will do per tick.
		 */
		teleport(getDirectionVector().multiply(1.5).add(
				new Vector(
						location.getX(),
						location.getY(),
						location.getZ()
				)
		));
		Location location = getLocation();
		location.getWorld().spawnParticle(Particle.REDSTONE, location, 1, (new Particle.DustOptions(Color.BLUE, 3)));
	}
}
```

#### 2. Spawn skill object

```java
// Put Location
RoastSkillObject object = new RoastSkillObject(player.getLocation().add(0, player.getEyeHeight(), 0));
// Put Tick
object.setCloseTimer(20);
// Put Player
object.setOwner(player);
// Spawn
object.spawn();
```