package hr.best.ai.asteroids.bot;

import hr.best.ai.asteroids.GameObject;

public class Utilities {

	public static GameObject move(GameObject object, long steps) {
		GameObject obj = object;
		for (int i = 0; i < steps; i++)
			obj = obj.move();
		return obj;
	}

	public static double angleDiff(double ang1, double ang2) {
		ang1 = minimalPositive(ang1);
		ang2 = minimalPositive(ang2);

		double larger = Math.max(ang1, ang2);
		double smaller = Math.min(ang1, ang2);

		if (larger - smaller > 180) {
			larger -= 180;
		}
		return Math.abs(larger - smaller);

	}

	private static double minimalPositive(double angle) {
		double twoPi = Math.PI * 2;
		return (angle % twoPi + twoPi) % twoPi;
	}
}