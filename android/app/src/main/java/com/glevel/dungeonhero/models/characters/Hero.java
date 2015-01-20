package com.glevel.dungeonhero.models.characters;


import com.glevel.dungeonhero.data.items.PotionFactory;
import com.glevel.dungeonhero.data.items.WeaponFactory;
import com.glevel.dungeonhero.game.GameConstants;
import com.glevel.dungeonhero.models.items.Characteristics;
import com.glevel.dungeonhero.models.items.Item;
import com.glevel.dungeonhero.models.items.equipments.Equipment;
import com.glevel.dungeonhero.models.items.equipments.Ring;
import com.glevel.dungeonhero.models.items.requirements.HeroRequirement;
import com.glevel.dungeonhero.models.items.requirements.Requirement;
import com.glevel.dungeonhero.models.items.requirements.StatRequirement;
import com.glevel.dungeonhero.models.skills.ActiveSkill;
import com.glevel.dungeonhero.models.skills.Skill;
import com.glevel.dungeonhero.utils.billing.InAppProduct;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guillaume ON 10/2/14.
 */
public class Hero extends Unit implements InAppProduct {

    private static final long serialVersionUID = -5970005172767341685L;

    private final String productId;
    private final HeroTypes heroType;
    protected boolean mHasBeenBought = false;
    protected List<String> frags = new ArrayList<>();
    private int xp;
    private int level;
    private String heroName;
    private int skillPoints;

    public Hero(String identifier, Ranks ranks, int hp, int currentHP, int strength, int dexterity, int spirit, int movement, String productId, int xp, int level, HeroTypes heroType) {
        super(identifier, ranks, hp, currentHP, strength, dexterity, spirit, movement);
        this.productId = productId;
        this.xp = xp;
        this.level = level;
        this.heroType = heroType;
        this.skillPoints = getInitialSkillPoints();

        // add some healing potions
        addItem(PotionFactory.buildHealingPotion());
        addItem(PotionFactory.buildHealingPotion());
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

    public List<String> getFrags() {
        return frags;
    }

    public void addFrag(String type) {
        frags.add(type);
    }

    public void addGold(int goldAmount) {
        gold += goldAmount;
    }

    public boolean addXP(int xpAmount) {
        if (level == GameConstants.HERO_LEVEL_MAX) {
            return false;
        }

        xp += xpAmount;
        if (xp >= getNextLevelXPAmount()) {
            increaseLevel();
            return true;
        }
        return false;
    }

    private void increaseLevel() {
        level++;

        // increase characteristics and skill points
        switch (heroType) {
            case STR:
                strength += 2;
                dexterity += 1;
                spirit += 1;
                skillPoints = 1;
                break;
            case DEX:
                strength += 1;
                dexterity += 2;
                spirit += 1;
                skillPoints = 2;
                break;
            case SPI:
                strength += 1;
                dexterity += 1;
                spirit += 2;
                skillPoints = 3;
                break;
            case STR_DEX:
                strength += 2;
                dexterity += 2;
                spirit += 1;
                skillPoints = 1;
                break;
            case STR_SPI:
                strength += 2;
                dexterity += 1;
                spirit += 2;
                skillPoints = 2;
                break;
            case DEX_SPI:
                strength += 1;
                dexterity += 2;
                spirit += 2;
                skillPoints = 2;
                break;
        }

        hp += strength / 2;
        currentHP = hp;
    }

    private int getInitialSkillPoints() {
        switch (heroType) {
            case STR:
                return 1;
            case DEX:
                return 2;
            case SPI:
                return 3;
            case STR_DEX:
                return 1;
            case STR_SPI:
                return 2;
            default:
                return 2;
        }
    }

    public int getNextLevelXPAmount() {
        return (int) (100 * (Math.pow(2, level + 1) - 1));
    }

    public void drop(Item item) {
        items.remove(item);
    }

    public String getHeroName() {
        return heroName;
    }

    public void setHeroName(String heroName) {
        this.heroName = heroName;
    }

    public void useSkillPoint() {
        skillPoints--;
    }

    public int getSkillPoints() {
        return skillPoints;
    }

    public void reset() {
        for (Skill skill : skills) {
            if (skill instanceof ActiveSkill) {
                ((ActiveSkill) skill).reset();
            }
        }
        currentHP = hp;
        buffs = new ArrayList<>();
    }

    public boolean canEquipItem(Equipment equipment) {
        boolean canEquip = isEquipmentSuitable(equipment);
        if (!canEquip) {
            return false;
        }

        for (Requirement requirement : equipment.getRequirements()) {
            if (requirement instanceof StatRequirement) {
                StatRequirement statRequirement = (StatRequirement) requirement;
                if (statRequirement.getTarget() == Characteristics.STRENGTH && getStrength() < statRequirement.getValue()) {
                    return false;
                } else if (statRequirement.getTarget() == Characteristics.DEXTERITY && getDexterity() < statRequirement.getValue()) {
                    return false;
                } else if (statRequirement.getTarget() == Characteristics.SPIRIT && getSpirit() < statRequirement.getValue()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isEquipmentSuitable(Equipment equipment) {
        if (equipment instanceof Ring) {
            return true;
        }
        for (Requirement requirement : equipment.getRequirements()) {
            if (requirement instanceof HeroRequirement && heroType == ((HeroRequirement) requirement).getHeroType()) {
                return true;
            }
        }
        return false;
    }

    public enum HeroTypes {
        STR, DEX, SPI, STR_DEX, STR_SPI, DEX_SPI
    }

}
