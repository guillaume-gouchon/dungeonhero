package com.glevel.dungeonhero.models.characters;

import android.util.Log;

import com.glevel.dungeonhero.game.GameConstants;
import com.glevel.dungeonhero.game.base.GameElement;
import com.glevel.dungeonhero.game.graphics.UnitSprite;
import com.glevel.dungeonhero.models.FightResult;
import com.glevel.dungeonhero.models.Reward;
import com.glevel.dungeonhero.models.dungeons.Tile;
import com.glevel.dungeonhero.models.effects.BuffEffect;
import com.glevel.dungeonhero.models.effects.CamouflageEffect;
import com.glevel.dungeonhero.models.effects.Effect;
import com.glevel.dungeonhero.models.effects.PermanentEffect;
import com.glevel.dungeonhero.models.items.Characteristics;
import com.glevel.dungeonhero.models.items.Item;
import com.glevel.dungeonhero.models.items.consumables.Consumable;
import com.glevel.dungeonhero.models.items.equipments.Armor;
import com.glevel.dungeonhero.models.items.equipments.Equipment;
import com.glevel.dungeonhero.models.items.equipments.Ring;
import com.glevel.dungeonhero.models.items.equipments.weapons.RangeWeapon;
import com.glevel.dungeonhero.models.items.equipments.weapons.TwoHandedWeapon;
import com.glevel.dungeonhero.models.items.equipments.weapons.Weapon;
import com.glevel.dungeonhero.models.skills.ActiveSkill;
import com.glevel.dungeonhero.models.skills.PassiveSkill;
import com.glevel.dungeonhero.models.skills.Skill;
import com.glevel.dungeonhero.utils.pathfinding.MovingElement;

import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by guillaume ON 10/2/14.
 */
public abstract class Unit extends GameElement implements MovingElement<Tile> {

    private static final long serialVersionUID = 9185541600463971808L;
    private static final String TAG = "Unit";

    // Skills
    protected final List<Skill> skills = new ArrayList<>();
    protected final List<Item> items = new ArrayList<>();
    protected final Equipment[] equipments = new Equipment[5];
    protected List<Effect> buffs = new ArrayList<>();
    // Possessions
    protected int gold;
    // Characteristics
    protected int hp;
    protected int currentHP;
    protected int strength;
    protected int dexterity;
    protected int spirit;
    protected int movement;
    protected Reward reward;

    public Unit(String identifier, Ranks rank, int hp, int currentHP, int strength, int dexterity, int spirit, int movement) {
        super(identifier, rank, 210, 400, UnitSprite.SPRITE_ANIM_X, UnitSprite.SPRITE_ANIM_Y);
        this.hp = hp;
        this.currentHP = currentHP;
        this.strength = strength;
        this.dexterity = dexterity;
        this.spirit = spirit;
        this.movement = movement;
        this.gold = 0;
    }

    private static void applyWeaponEffect(Equipment equipment, Unit target) {
        for (Effect effect : equipment.getEffects()) {
            if (!(effect instanceof PermanentEffect)) {
                target.getBuffs().add(effect);
            }
        }
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
        return Math.max(0, strength + getBonusesFromBuffsAndEquipments(Characteristics.STRENGTH));
    }

    public int getDexterity() {
        return Math.max(0, dexterity + getBonusesFromBuffsAndEquipments(Characteristics.DEXTERITY));
    }

    public int getSpirit() {
        return Math.max(0, spirit + getBonusesFromBuffsAndEquipments(Characteristics.SPIRIT));
    }

    public int getGold() {
        return gold;
    }

    public List<Item> getItems() {
        return items;
    }

    public List<Skill> getSkills() {
        return skills;
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

    public Equipment[] getEquipments() {
        return equipments;
    }

    public FightResult attack(Unit target) {
        FightResult fightResult;

        // remove invisibility
        boolean isInvisible = false;
        for (Effect buff : buffs) {
            if (buff instanceof CamouflageEffect) {
                buffs.remove(buff);
                updateSprite();
                isInvisible = true;
                break;
            }
        }

        int dice = (int) (Math.random() * 100);

        if (isRangeAttack()) {
            if (isNextTo(target.getTilePosition())) {
                // malus for range weapon in close combat
                dice = Math.min(99, dice + 20);
            } else {
                // range attack bonus or malus depending on dexterity
                dice = Math.max(0, dice - (dexterity - 12) * 3);
            }
        }

        Log.d(TAG, "attack dice = " + dice);

        int critical = calculateCritical();

        if (isInvisible || dice < critical) {
            int criticalDamage = Math.max(0, calculateDamageNaturalBonus() + getBonusesFromBuffsAndEquipments(Characteristics.DAMAGE)
                    + (equipments[0] != null ? calculateCriticalDamage((Weapon) equipments[0]) : 0) + (equipments[1] != null ? calculateCriticalDamage((Weapon) equipments[1]) / 2 : 0));
            fightResult = new FightResult(FightResult.States.CRITICAL, (int) Math.max(0, criticalDamage * 1.3f - target.calculateProtection()));
        } else if (dice >= 95) {
            fightResult = new FightResult(FightResult.States.MISS, 0);
        } else if (dice > 95 - target.calculateBlock()) {
            fightResult = new FightResult(FightResult.States.BLOCK, 0);
        } else {
            int dodgeDice = (int) (Math.random() * 100);
            Log.d(TAG, "dodge dice = " + dodgeDice);
            if (dodgeDice < target.calculateDodge()) {
                fightResult = new FightResult(FightResult.States.DODGE, 0);
            } else {
                int damage = Math.max(0, calculateDamageNaturalBonus() + getBonusesFromBuffsAndEquipments(Characteristics.DAMAGE)
                        + (equipments[0] != null ? calculateDamage((Weapon) equipments[0]) : 0) + (equipments[1] != null ? calculateDamage((Weapon) equipments[1]) / 2 : 0));
                fightResult = new FightResult(FightResult.States.DAMAGE, Math.max(0, damage - target.calculateProtection()));
            }
        }

        Log.d(TAG, "dealing " + fightResult.getDamage() + " damage");
        target.takeDamage(fightResult.getDamage());

        // add weapons special effects
        if (fightResult.getDamage() > 0) {
            if (equipments[0] != null) {
                applyWeaponEffect(equipments[0], target);
            }
            if (equipments[1] != null) {
                applyWeaponEffect(equipments[1], target);
            }
        }

        return fightResult;
    }

    public String getReadableDamage() {
        int weapon1Min = (equipments[0] != null ? ((Weapon) equipments[0]).getMinDamage() : 0);
        int weapon2Min = (equipments[1] != null ? ((Weapon) equipments[1]).getMinDamage() / 2 : 0);
        int weapon1Delta = (equipments[0] != null ? ((Weapon) equipments[0]).getDeltaDamage() : 0);
        int weapon2Delta = (equipments[1] != null ? ((Weapon) equipments[1]).getDeltaDamage() / 2 : 0);
        int minDamage = calculateDamageNaturalBonus() + getBonusesFromBuffsAndEquipments(Characteristics.DAMAGE) + weapon1Min + weapon2Min;
        int maxDamage = minDamage + weapon1Delta + weapon2Delta;
        return (minDamage + " - " + maxDamage);
    }

    public void takeDamage(int damage) {
        currentHP -= damage;
    }

    public boolean isDead() {
        return currentHP <= 0;
    }

    private int calculateDamageNaturalBonus() {
        return Math.max(0, (getStrength() - 10) / 2);
    }

    public List<Effect> getBuffs() {
        return buffs;
    }

    public int calculateDamage(Weapon weapon) {
        return weapon.getMinDamage() + (int) (Math.random() * (weapon.getDeltaDamage() + 1));
    }

    public int calculateCriticalDamage(Weapon weapon) {
        return weapon.getMinDamage() + weapon.getDeltaDamage();
    }

    public int calculateProtection() {
        return Math.max(0, getBonusesFromBuffsAndEquipments(Characteristics.PROTECTION));
    }

    public int calculateInitiative() {
        return dexterity + spirit + getBonusesFromBuffsAndEquipments(Characteristics.INITIATIVE);
    }

    public int calculateBlock() {
        return Math.max(0, Math.min(80, getBonusesFromBuffsAndEquipments(Characteristics.BLOCK)));
    }

    public int calculateMovement() {
        return Math.max(1, movement + getBonusesFromBuffsAndEquipments(Characteristics.MOVEMENT));
    }

    public int calculateDodge() {
        return Math.max(5, Math.min(80, (getDexterity() - 10) * 4) + getBonusesFromBuffsAndEquipments(Characteristics.DODGE));
    }

    public int calculateCritical() {
        return Math.max(0, 5 + getBonusesFromBuffsAndEquipments(Characteristics.CRITICAL));
    }

    private int getBonusesFromBuffsAndEquipments(Characteristics characteristic) {
        int bonus = 0;
        for (Effect buff : buffs) {
            if (buff.getTarget() == characteristic) {
                bonus += buff.getValue();
            }
        }

        for (Skill skill : skills) {
            if (skill.getLevel() > 0 && skill instanceof PassiveSkill && ((PassiveSkill) skill).getEffect().getTarget() == characteristic) {
                Effect effect = ((PassiveSkill) skill).getEffect();
                bonus += effect.getValue();
            }
        }

        Equipment equipment;
        for (int n = 0; n < equipments.length; n++) {
            if (equipments[n] != null) {
                equipment = equipments[n];
                if (characteristic == Characteristics.PROTECTION && equipment instanceof Armor) {
                    bonus += ((Armor) equipment).getProtection();
                }
                for (Effect buff : equipment.getEffects()) {
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
        List<Effect> copy = new ArrayList<>(buffs);
        for (Effect buff : copy) {
            boolean isOver = buff.consume();
            if (isOver) {
                Log.d(TAG, "buff is over =" + buff.getTarget());
                buffs.remove(buff);
            }
        }
        updateSprite();
    }

    public void updateSprite() {
        Log.d(TAG, "update sprite with buffs");
        boolean isInvisible = false;
        boolean isColored = false;
        for (Effect effect : buffs) {
            if (effect instanceof CamouflageEffect) {
                isInvisible = true;
            } else if (!isColored && effect instanceof BuffEffect && ((BuffEffect) effect).getBuffColor() != null) {
                isColored = true;
                sprite.setColor(((BuffEffect) effect).getBuffColor());
            }
        }

        sprite.setAlpha(isInvisible ? 0.3f : 1);
        if (!isColored) {
            sprite.setColor(new Color(1, 1, 1, sprite.getAlpha()));
        }
    }

    /**
     * @param equipment
     * @return item to drop
     */
    public Item equip(Equipment equipment) {
        Item itemToDrop = null;
        items.remove(equipment);
        if (equipment instanceof Armor) {
            if (equipments[2] != null) {
                removeEquipment(2);
            }
            equipments[2] = equipment;
        } else if (equipment instanceof TwoHandedWeapon) {
            if (equipments[0] != null) {
                removeEquipment(0);
            }

            Equipment equipment1 = equipments[1];
            if (equipment1 != null) {
                boolean success = removeEquipment(1);
                if (!success) {
                    itemToDrop = equipment1;
                }
            }
            equipments[0] = equipment;
        } else if (equipment instanceof Weapon) {
            if (equipments[0] == null) {
                equipments[0] = equipment;
            } else if (!(equipments[0] instanceof TwoHandedWeapon) && equipments[1] == null) {
                equipments[1] = equipment;
            } else {
                removeEquipment(0);
                equipments[0] = equipment;
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
        return itemToDrop;
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

    public boolean removeEquipment(Equipment equipment) {
        Equipment e;
        for (int n = 0; n < equipments.length; n++) {
            e = equipments[n];
            if (e == equipment) {
                return removeEquipment(n);
            }
        }
        return true;
    }

    private boolean removeEquipment(int index) {
        boolean success = addItem(equipments[index]);
        equipments[index] = null;
        return success;
    }

    public boolean addItem(Item item) {
        if (items.size() < GameConstants.NB_ITEMS_MAX_IN_BAG) {
            items.add(item);
            return true;
        } else {
            return false;
        }
    }

    public void use(Consumable consumable) {
        items.remove(consumable);
    }

    public boolean testCharacteristic(Characteristics target, int value) {
        int dice = (int) (Math.random() * 20);
        Log.d(TAG, "characteristic dice result = " + dice);
        return dice == 1 || dice < getCharacteristic(target) - value;
    }

    public boolean isRangeAttack() {
        return equipments[0] != null && equipments[0] instanceof RangeWeapon;
    }

    public int getCharacteristic(Characteristics target) {
        switch (target) {
            case STRENGTH:
                return getStrength();
            case DEXTERITY:
                return getDexterity();
            case SPIRIT:
                return getSpirit();
        }
        return 0;
    }

    public ActiveSkill getAvailableSkill() {
        Collections.shuffle(skills);
        for (Skill skill : skills) {
            if (skill instanceof ActiveSkill && !((ActiveSkill) skill).isUsed()) {
                return (ActiveSkill) skill;
            }
        }
        return null;
    }

    public Reward getReward() {
        return reward;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }

}
