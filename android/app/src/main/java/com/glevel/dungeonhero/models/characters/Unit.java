package com.glevel.dungeonhero.models.characters;

import com.glevel.dungeonhero.game.base.GameElement;
import com.glevel.dungeonhero.game.graphics.UnitSprite;
import com.glevel.dungeonhero.models.Buff;
import com.glevel.dungeonhero.models.FightResult;
import com.glevel.dungeonhero.models.dungeons.Tile;
import com.glevel.dungeonhero.models.items.Characteristics;
import com.glevel.dungeonhero.models.items.Consumable;
import com.glevel.dungeonhero.models.items.Equipment;
import com.glevel.dungeonhero.models.items.Item;
import com.glevel.dungeonhero.models.items.Requirement;
import com.glevel.dungeonhero.models.items.equipments.Armor;
import com.glevel.dungeonhero.models.items.equipments.Ring;
import com.glevel.dungeonhero.models.items.equipments.Weapon;
import com.glevel.dungeonhero.models.skills.ActiveSkill;
import com.glevel.dungeonhero.models.skills.PassiveSkill;
import com.glevel.dungeonhero.utils.pathfinding.MovingElement;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guillaume ON 10/2/14.
 */
public abstract class Unit extends GameElement implements MovingElement<Tile>, Serializable {

    private static final long serialVersionUID = 1683650465351973413L;

    private static final int NB_ITEMS_MAX_IN_BAG = 15;

    protected final List<Item> items = new ArrayList<Item>();
    // SKills
    protected final List<PassiveSkill> passive = new ArrayList<PassiveSkill>();
    protected final List<ActiveSkill> active = new ArrayList<ActiveSkill>();
    protected final List<Buff> buffs = new ArrayList<Buff>();
    protected final Equipment[] equipments = new Equipment[5];
    // Images
    private final int image;
    // RP
    private final int description;
    // Possessions
    protected int gold;
    // Characteristics
    private int hp;
    private int currentHP;
    private int strength;
    private int dexterity;
    private int spirit;
    private int attack;
    private int block;
    private int movement;

    public Unit(Ranks rank, int image, String spriteName, int hp, int currentHP, int strength, int dexterity, int spirit, int attack, int block, int movement, int name, int description, int coins) {
        super(name, spriteName, rank, 210, 400, 3, 4);
        this.image = image;
        this.hp = hp;
        this.currentHP = currentHP;
        this.strength = strength;
        this.dexterity = dexterity;
        this.spirit = spirit;
        this.attack = attack;
        this.block = block;
        this.movement = movement;
        this.description = description;
        this.gold = coins;
    }

    public int getImage() {
        return image;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public int getSpirit() {
        return spirit;
    }

    public void setSpirit(int spirit) {
        this.spirit = spirit;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        this.block = block;
    }

    public int getDescription() {
        return description;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public List<Item> getItems() {
        return items;
    }

    public List<PassiveSkill> getPassive() {
        return passive;
    }

    public List<ActiveSkill> getActive() {
        return active;
    }

    @Override
    public boolean canMoveIn(Tile tile) {
        return tile.getGround() != null && tile.getContent() == null;
    }

    @Override
    public void createSprite(VertexBufferObjectManager vertexBufferObjectManager) {
        sprite = new UnitSprite(this, vertexBufferObjectManager);
    }

    public float getLifeRatio() {
        return (float) currentHP / (float) hp;
    }

    public FightResult attack(Unit target) {
        FightResult fightResult;

        int dice = (int) (Math.random() * 100);
        int damage = (equipments[0] != null ? calculateDamage((Weapon) equipments[0]) : 0) + (equipments[1] != null ? calculateDamage((Weapon) equipments[1]) / 2 : 0);

        int critical = calculateCritical();

        if (dice < critical) {
            fightResult = new FightResult(FightResult.States.CRITICAL, damage * 2 - target.calculateProtection());
        } else if (dice < attack * 10 - target.getBlock() * 5) {
            if (Math.random() * 100 < calculateDodge()) {
                fightResult = new FightResult(FightResult.States.DODGE, 0);
            } else {
                fightResult = new FightResult(FightResult.States.DAMAGE, damage - target.calculateProtection());
            }
        } else {
            fightResult = new FightResult(FightResult.States.BLOCK, 0);
        }

        target.takeDamage(fightResult.getDamage());

        return fightResult;
    }

    public String getReadableDamage() {
        int weapon1Min = (equipments[0] != null ? ((Weapon) equipments[0]).getMinDamage() : 0);
        int weapon2Min = (equipments[1] != null ? ((Weapon) equipments[1]).getMinDamage() / 2 : 0);
        int weapon1Delta = (equipments[0] != null ? ((Weapon) equipments[0]).getDeltaDamage() : 0);
        int weapon2Delta = (equipments[1] != null ? ((Weapon) equipments[1]).getDeltaDamage() / 2 : 0);
        return (weapon1Min + weapon2Min) + " - " + (weapon1Min + weapon1Delta + weapon2Min + weapon2Delta);
    }

    public void takeDamage(int damage) {
        currentHP -= damage;
    }

    public boolean isDead() {
        return currentHP <= 0;
    }

    private int calculateDamageBonus() {
        return Math.max(0, strength - 10);
    }

    public List<Buff> getBuffs() {
        return buffs;
    }

    public int calculateDamage(Weapon weapon) {
        return calculateDamageBonus() + weapon.getMinDamage() + (int) (Math.random() * weapon.getDeltaDamage());
    }

    public int calculateProtection() {
        return getBonusFromBuffsAndEquipments(Characteristics.PROTECTION);
    }

    public int calculateInitiative() {
        return dexterity + spirit + getBonusFromBuffsAndEquipments(Characteristics.INITIATIVE);
    }

    public int calculateMovement() {
        return movement + getBonusFromBuffsAndEquipments(Characteristics.MOVEMENT);
    }

    public int calculateDodge() {
        return Math.max(0, Math.max(0, (dexterity - 10) * 5) + getBonusFromBuffsAndEquipments(Characteristics.DODGE));
    }

    public int calculateCritical() {
        return Math.max(0, 5 + getBonusFromBuffsAndEquipments(Characteristics.CRITICAL));
    }

    private int getBonusFromBuffsAndEquipments(Characteristics characteristic) {
        int bonus = 0;
        for (Buff buff : buffs) {
            if (buff.getTarget() == characteristic) {
                bonus += buff.getValue();
            }
        }

        Equipment equipment;
        for (int n = 0; n < equipments.length; n++) {
            if (equipments[n] != null) {
                equipment = equipments[n];
                for (Buff buff : equipment.getBuffs()) {
                    if (buff.getTarget() == characteristic) {
                        bonus += buff.getValue();
                    }
                }
            }
        }

        return bonus;
    }

    public void initNewTurn() {
        // consume and remove ended buffs
        List<Buff> copy = new ArrayList<Buff>(buffs);
        for (Buff buff : copy) {
            boolean isOver = buff.consume();
            if (isOver) {
                buffs.remove(buff);
            }
        }
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
        boolean success = addItem(equipments[index]);
        if (success) {
            equipments[index] = null;
        }
    }

    public boolean addItem(Item item) {
        if (items.size() < NB_ITEMS_MAX_IN_BAG) {
            items.add(item);
            return true;
        } else {
            return false;
        }
    }

    public void use(Consumable consumable) {
        items.remove(consumable);
        consumable.use(this);
    }

    public boolean canEquipItem(Equipment equipment) {
        for (Requirement requirement : equipment.getRequirements()) {
            if (requirement.getTarget() == Characteristics.STRENGTH && strength < requirement.getValue()) {
                return false;
            } else if (requirement.getTarget() == Characteristics.DEXTERITY && dexterity < requirement.getValue()) {
                return false;
            } else if (requirement.getTarget() == Characteristics.SPIRIT && spirit < requirement.getValue()) {
                return false;
            }
        }
        return true;
    }

}
