package com.glevel.dungeonhero.game.models.orders;


public class MoveOrder extends Order {

    /**
     * 
     */
    private static final long serialVersionUID = -2227867419405002169L;
    private float xDestination;
    private float yDestination;

    public MoveOrder(float xDestination, float yDestination) {
        this.xDestination = xDestination;
        this.yDestination = yDestination;
    }

    public float getXDestination() {
        return xDestination;
    }

    public void setxDestination(int xDestination) {
        this.xDestination = xDestination;
    }

    public float getYDestination() {
        return yDestination;
    }

    public void setyDestination(int yDestination) {
        this.yDestination = yDestination;
    }

}
