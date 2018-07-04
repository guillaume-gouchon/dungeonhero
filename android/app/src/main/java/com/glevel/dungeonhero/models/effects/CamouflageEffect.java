package com.glevel.dungeonhero.models.effects;

public class CamouflageEffect extends Effect {

    private static final long serialVersionUID = 5321695335925128052L;

    public CamouflageEffect(String spriteName, int value, int level) {
        super(spriteName, null, value, INFINITE_EFFECT, null, level);
    }

}
