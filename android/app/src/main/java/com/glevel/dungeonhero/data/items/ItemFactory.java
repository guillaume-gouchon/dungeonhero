package com.glevel.dungeonhero.data.items;

import com.glevel.dungeonhero.models.items.Item;

import java.util.Random;

/**
 * Created by guillaume ON 10/6/14.
 */
public class ItemFactory {

    public static Item getRandomItem(int threatLevel) {
        int level = (int) Math.max(0, Math.min(6, threatLevel - 2 + Math.random() * 4));
        Random r = new Random();
        int random = r.nextInt(8);
        switch (random) {
            case 0:
            case 1:
            case 2:
                return null;
            case 3:
            case 4:
                return PotionFactory.getRandomPotion();
            case 5:
                return RingFactory.getRandomRing(level);
            case 6:
                return ArmorFactory.getRandomArmor(level);
            default:
                return WeaponFactory.getRandomWeapon(level);
        }
    }

}
