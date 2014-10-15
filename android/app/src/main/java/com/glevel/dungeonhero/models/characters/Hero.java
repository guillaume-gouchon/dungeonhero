package com.glevel.dungeonhero.models.characters;


import com.glevel.dungeonhero.models.FightResult;
import com.glevel.dungeonhero.models.items.Consumable;
import com.glevel.dungeonhero.models.items.Equipment;
import com.glevel.dungeonhero.models.items.Item;
import com.glevel.dungeonhero.models.items.equipments.Armor;
import com.glevel.dungeonhero.models.items.equipments.Ring;
import com.glevel.dungeonhero.models.items.equipments.Weapon;
import com.glevel.dungeonhero.utils.billing.InAppProduct;

/**
 * Created by guillaume ON 10/2/14.
 */
public class Hero extends Unit implements InAppProduct {

    private static final long serialVersionUID = -2887616275513777101L;

    protected boolean mHasBeenBought = false;

    private final String productId;
    private int xp;
    private int level;
    protected int frags = 0;

    private final Equipment[] equipments = new Equipment[5];

    public Hero(Ranks ranks, int image, String spriteName, int hp, int currentHP, int strength, int dexterity, int spirit, int attack, int block, int name, int description, int coins, String productId, int xp, int level) {
        super(ranks, image, spriteName, hp, currentHP, strength, dexterity, spirit, attack, block, name, description, coins);
        this.productId = productId;
        this.xp = xp;
        this.level = level;
    }

    @Override
    public String getProductId() {
        return productId;
    }

    @Override
    public void setHasBeenBought(boolean hasBeenBought) {
        mHasBeenBought = hasBeenBought;
    }

    @Override
    public boolean isAvailable() {
        return isFree() || mHasBeenBought;
    }

    @Override
    public boolean isFree() {
        return productId == null;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void setFrags(int frags) {
        this.frags = frags;
    }

    public int getFrags() {
        return frags;
    }

    public void addGold(int goldAmount) {
        gold += goldAmount;
    }

    public Equipment[] getEquipments() {
        return equipments;
    }

    public void equip(Equipment equipment) {
        if (equipment instanceof Armor) {
            if (equipments[2] != null) {
                removeEquipment(2);
            }
            equipments[2] = equipment;
        } else if (equipment instanceof Weapon) {
            if (equipments[0] == null) {
                equipments[0] = equipment;
            } else if (equipments[1] == null) {
                equipments[1] = equipment;
            } else {
                removeEquipment(1);
                equipments[1] = equipment;
            }
        } else if (equipment instanceof Ring) {
            if (equipments[3] == null) {
                equipments[3] = equipment;
            } else if (equipments[4] == null) {
                equipments[4] = equipment;
            } else {
                removeEquipment(4);
                equipments[4] = equipment;
            }
        }
        items.remove(equipment);
    }

    public boolean isEquipped(Equipment equipment) {
        Equipment e;
        for (int n = 0; n < equipments.length; n++) {
            e = equipments[n];
            if (e == equipment) {
                return true;
            }
        }
        return false;
    }

    public void removeEquipment(Equipment equipment) {
        Equipment e;
        for (int n = 0; n < equipments.length; n++) {
            e = equipments[n];
            if (e == equipment) {
                removeEquipment(n);
                return;
            }
        }
    }

    private void removeEquipment(int index) {
        items.add(equipments[index]);
        equipments[index] = null;
    }

    public void addXP(int xpAmount) {
        xp += xpAmount;
        checkLevelChange();
    }

    private void checkLevelChange() {
        // TODO
        if (xp > 1000) {
            xp -= 1000;
            level++;
        }
    }

    @Override
    public FightResult attack(Unit target) {
        FightResult fightResult = super.attack(target);
        if (target.isDead()) {
            frags++;
        }
        return fightResult;
    }

    public void drop(Item item) {
        items.remove(item);
    }

    public void use(Consumable consumable) {
        items.remove(consumable);
        consumable.use(this);
    }

}
