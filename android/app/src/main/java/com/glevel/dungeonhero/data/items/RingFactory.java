package com.glevel.dungeonhero.data.items;

import com.glevel.dungeonhero.models.effects.PermanentEffect;
import com.glevel.dungeonhero.models.items.Characteristics;
import com.glevel.dungeonhero.models.items.Item;
import com.glevel.dungeonhero.models.items.equipments.Ring;

/**
 * Created by guillaume ON 10/6/14.
 */
public class RingFactory {

    public static Ring getRandomRing(int level) {
        Ring ring;
        int random = (int) (Math.random() * 10);
        switch (random) {
            case 0:
                ring = new Ring("dodge_ring", level, 250);
                ring.addEffect(new PermanentEffect(Characteristics.DODGE, 10, null, level));
                return ring;
            case 1:
                ring = new Ring("speed_ring", level, 300);
                ring.addEffect(new PermanentEffect(Characteristics.MOVEMENT, 1, null, level));
                return ring;
            case 2:
                ring = new Ring("protection_ring", level, 200);
                ring.addEffect(new PermanentEffect(Characteristics.PROTECTION, 1, null, level));
                return ring;
            case 3:
                ring = new Ring("critical_ring", level, 180);
                ring.addEffect(new PermanentEffect(Characteristics.CRITICAL, 10, null, level));
                return ring;
            case 4:
                ring = new Ring("damage_ring", level, 200);
                ring.addEffect(new PermanentEffect(Characteristics.DAMAGE, 3, null, level));
                ring.addEffect(new PermanentEffect(Characteristics.SPIRIT, -2, null, level));
                return ring;
            case 5:
                ring = new Ring("initiative_ring", level, 250);
                ring.addEffect(new PermanentEffect(Characteristics.INITIATIVE, 5, null, level));
                return ring;
            case 6:
                ring = new Ring("strength_ring", level, 180);
                ring.addEffect(new PermanentEffect(Characteristics.STRENGTH, 1, null, level));
                return ring;
            case 7:
                ring = new Ring("dexterity_ring", level, 180);
                ring.addEffect(new PermanentEffect(Characteristics.DEXTERITY, 1, null, level));
                return ring;
            case 8:
                ring = new Ring("spirit_ring", level, 180);
                ring.addEffect(new PermanentEffect(Characteristics.SPIRIT, 1, null, level));
                return ring;
            default:
                ring = new Ring("king_ring", level, 350);
                ring.addEffect(new PermanentEffect(Characteristics.STRENGTH, 1, null, level));
                ring.addEffect(new PermanentEffect(Characteristics.DEXTERITY, 1, null, level));
                ring.addEffect(new PermanentEffect(Characteristics.SPIRIT, 1, null, level));
                return ring;
        }
    }

    public static Item buildSOVRing() {
        Ring ring = new Ring("sov_ring", 0, 220);
        ring.addEffect(new PermanentEffect(Characteristics.CRITICAL, 5, null, 0));
        ring.addEffect(new PermanentEffect(Characteristics.DAMAGE, 3, null, 0));
        return ring;
    }
}
