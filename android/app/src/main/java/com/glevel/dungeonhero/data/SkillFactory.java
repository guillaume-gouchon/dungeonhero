package com.glevel.dungeonhero.data;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.effects.BuffEffect;
import com.glevel.dungeonhero.models.effects.DamageEffect;
import com.glevel.dungeonhero.models.effects.Effect;
import com.glevel.dungeonhero.models.effects.HeroicEffect;
import com.glevel.dungeonhero.models.effects.PermanentEffect;
import com.glevel.dungeonhero.models.effects.StunEffect;
import com.glevel.dungeonhero.models.items.Characteristics;
import com.glevel.dungeonhero.models.skills.ActiveSkill;
import com.glevel.dungeonhero.models.skills.PassiveSkill;
import com.glevel.dungeonhero.models.skills.Skill;

/**
 * Created by guillaume ON 10/6/14.
 */
public class SkillFactory {

    public static Skill buildRage() {
        BuffEffect extra = new BuffEffect(null, Characteristics.PROTECTION, -5, 3, null);
        Effect effect = new BuffEffect("blood.png", Characteristics.DAMAGE, 10, 3, extra);
        return new ActiveSkill(R.string.about_title, R.string.about_title, R.drawable.ic_rage, 0, true, 0, effect);
    }

    public static Skill buildSwirlOfSwords() {
        Effect effect = new DamageEffect("blood.png", 12);
        return new ActiveSkill(R.string.about_title, R.string.about_title, R.drawable.ic_swirl, 0, true, 1, effect);
    }

    public static Skill buildWarcry() {
        Effect effect = new StunEffect("blood.png", Characteristics.STRENGTH, 12);
        return new ActiveSkill(R.string.about_title, R.string.about_title, R.drawable.ic_cry, 0, true, 7, effect);
    }

    public static Skill buildCritics() {
        Effect effect = new PermanentEffect(Characteristics.CRITICAL, 15, null);
        return new PassiveSkill(R.string.about_title, R.string.about_title, R.drawable.ic_critics, 0, effect);
    }

    public static Skill buildDodge() {
        Effect effect = new PermanentEffect(Characteristics.DODGE, 20, null);
        return new PassiveSkill(R.string.about_title, R.string.about_title, R.drawable.ic_key, 0, effect);
    }

    public static Skill buildProtection() {
        Effect effect = new BuffEffect("blood.png", Characteristics.PROTECTION, 8, 5, null);
        return new ActiveSkill(R.string.about_title, R.string.about_title, R.drawable.ic_key, 0, true, 0, effect);
    }

    public static Skill buildHeroic() {
        Effect effect = new HeroicEffect("blood.png");
        return new ActiveSkill(R.string.about_title, R.string.about_title, R.drawable.ic_key, 0, true, 0, effect);
    }

}
