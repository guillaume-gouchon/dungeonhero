package com.glevel.dungeonhero.game.data;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.game.models.weapons.Bazooka;
import com.glevel.dungeonhero.game.models.weapons.HMG;
import com.glevel.dungeonhero.game.models.weapons.HandGrenade;
import com.glevel.dungeonhero.game.models.weapons.Mortar;
import com.glevel.dungeonhero.game.models.weapons.PM;
import com.glevel.dungeonhero.game.models.weapons.Rifle;
import com.glevel.dungeonhero.game.models.weapons.Turret;
import com.glevel.dungeonhero.game.models.weapons.categories.DeflectionWeapon;
import com.glevel.dungeonhero.game.models.weapons.categories.Weapon;

public class WeaponsData {

	public static Weapon buildGarandM1() {
		return new Rifle(R.string.garandM1, R.drawable.ic_garand, 2, 0, 400, 8, 1, 8, 2, 2, new int[] { 40, 30, 20, 13 });
	}

	public static Weapon buildMauserG98() {
		return new Rifle(R.string.mauser, R.drawable.ic_mauser, 2, 0, 400, 8, 1, 5, 3, 2, new int[] { 40, 30, 20, 13 });
	}

	public static Weapon buildThompson() {
		return new PM(R.string.thompson, R.drawable.ic_thompson, 3, 0, 50, 6, 5, 30, 2, 10, new int[] { 30, 25, 10, 5 });
	}

	public static Weapon buildMP40() {
		return new PM(R.string.mp40, R.drawable.ic_mp40, 4, 0, 100, 6, 4, 32, 2, 10, new int[] { 30, 25, 10, 5 });
	}

	public static Weapon buildBrowningM2() {
		return new HMG(R.string.browningM2, R.drawable.ic_browning_m2, 4, 1, 1200, 5, 10, 100, 5, 10, new int[] { 35, 30, 30, 20 });
	}

	public static Weapon buildMG42() {
		return new HMG(R.string.mg42, R.drawable.ic_mg42, 5, 1, 1200, 8, 8, 80, 5, 10, new int[] { 40, 35, 35, 25 });
	}

	public static DeflectionWeapon buildMortar50() {
		return new Mortar(R.string.mortar50, R.drawable.ic_mortar, 3, 1, 1500, 24, 1, 1, 2, 1, 2);
	}

	public static DeflectionWeapon buildMortar81() {
		return new Mortar(R.string.mortar81, R.drawable.ic_mortar, 4, 2, 1500, 24, 1, 1, 2, 1, 4);
	}

	public static Weapon buildBazooka() {
		return new Bazooka(R.string.bazookaM1A, R.drawable.ic_bazooka, 2, 4, 140, 16, 1, 1, 4, 1, 2);
	}

	public static Weapon buildPanzerschreck() {
		return new Bazooka(R.string.panzerschreck, R.drawable.ic_panzerschreck, 2, 5, 220, 16, 1, 1, 4, 1, 2);
	}

	public static Weapon buildPanzerfaust() {
		return new Bazooka(R.string.panzerfaust, R.drawable.ic_panzerfaust, 1, 3, 60, 2, 1, 1, 4, 1, 2);
	}

	public static DeflectionWeapon buildCannon75(int rotationSpeed) {
		return new Turret(R.string.cannon75, R.drawable.ic_cannon, 3, 5, 800, 30, 1, 1, 3, 1, 3, rotationSpeed);
	}

	public static DeflectionWeapon buildPak43() {
		return new Turret(R.string.pak43, R.drawable.ic_cannon, 3, 5, 800, 30, 1, 1, 3, 1, 4, 10);
	}

	public static DeflectionWeapon buildHandGrenades(ArmiesData army) {
		int image;
		if (army == ArmiesData.GERMANY) {
			image = R.drawable.ic_grenade_ger;
		} else {
			image = R.drawable.ic_grenade_usa;
		}
		return new HandGrenade(R.string.grenade, image, 5, 1, 25, 2, 1, 1, 1, 1, 2);
	}

}
