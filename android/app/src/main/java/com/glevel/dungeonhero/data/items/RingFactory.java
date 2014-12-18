package com.glevel.dungeonhero.data.items;

import com.glevel.dungeonhero.models.effects.PermanentEffect;
import com.glevel.dungeonhero.models.items.Characteristics;
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
                ring = new Ring("dodge_ring", level);
                ring.addEffect(new PermanentEffect(Characteristics.DODGE, 10, null, level));
                return ring;
            case 1:
                ring = new Ring("speed_ring", level);
                ring.addEffect(new PermanentEffect(Characteristics.MOVEMENT, 2, null, level));
                return ring;
            case 2:
                ring = new Ring("protection_ring", level);
                ring.addEffect(new PermanentEffect(Characteristics.PROTECTION, 3, null, level));
                return ring;
            case 3:
                ring = new Ring("critical_ring", level);
                ring.addEffect(new PermanentEffect(Characteristics.CRITICAL, 10, null, level));
                return ring;
            case 4:
                ring = new Ring("damage_ring", level);
                ring.addEffect(new PermanentEffect(Characteristics.DAMAGE, 5, null, level));
                ring.addEffect(new PermanentEffect(Characteristics.SPIRIT, -3, null, level));
                return ring;
            case 5:
                ring = new Ring("initiative_ring", level);
                ring.addEffect(new PermanentEffect(Characteristics.INITIATIVE, 5, null, level));
                return ring;
            case 6:
                ring = new Ring("strength_ring", level);
                ring.addEffect(new PermanentEffect(Characteristics.STRENGTH, 3, null, level));
                return ring;
            case 7:
                ring = new Ring("dexterity_ring", level);
                ring.addEffect(new PermanentEffect(Characteristics.DEXTERITY, 3, null, level));
                return ring;
            case 8:
                ring = new Ring("spirit_ring", level);
                ring.addEffect(new PermanentEffect(Characteristics.SPIRIT, 3, null, level));
                return ring;
            default:
                ring = new Ring("king_ring", level);
                ring.addEffect(new PermanentEffect(Characteristics.STRENGTH, 2, null, level));
                ring.addEffect(new PermanentEffect(Characteristics.DEXTERITY, 2, null, level));
                ring.addEffect(new PermanentEffect(Characteristics.SPIRIT, 2, null, level));
                return ring;
        }
    }

}
