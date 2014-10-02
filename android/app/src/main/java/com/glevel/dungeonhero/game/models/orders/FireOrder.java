package com.glevel.dungeonhero.game.models.orders;

import com.glevel.dungeonhero.game.models.units.categories.Unit;

public class FireOrder extends Order {

    /**
     * 
     */
    private static final long serialVersionUID = -5452279368421461793L;
    private float xDestination;
    private float yDestination;
    private Unit target;

    public FireOrder(Unit target) {
        this.target = target;
    }

    public FireOrder(float xDestination, float yDestination) {
        this.xDestination = xDestination;
        this.yDestination = yDestination;
    }

    public float getXDestination() {
        return target != null ? target.getSprite().getX() : xDestination;
    }

    public void setXDestination(float xDestination) {
        this.xDestination = xDestination;
    }

    public float getYDestination() {
        return target != null ? target.getSprite().getY() : yDestination;
    }

    public void setYDestination(float yDestination) {
        this.yDestination = yDestination;
    }

    public Unit getTarget() {
        return target;
    }

    public void setTarget(Unit target) {
        this.target = target;
    }

}
