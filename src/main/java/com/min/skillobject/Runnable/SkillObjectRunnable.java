package com.min.skillobject.Runnable;

import com.min.skillobject.Object.SkillObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SkillObjectRunnable implements Runnable {
	private static List<SkillObject> objects;

	public SkillObjectRunnable() {
		objects = new ArrayList<>();
	}

	public static void addObject(SkillObject object) {
		objects.add(object);
	}

	@Override
	public void run() {
		if (objects == null) return;
		for (Iterator<SkillObject> iterator = objects.iterator(); iterator.hasNext(); ) {
			SkillObject item = iterator.next();
			if (item.isClosed()) {
				iterator.remove();
				continue;
			}
			item.skillTick();
		}
	}
}
