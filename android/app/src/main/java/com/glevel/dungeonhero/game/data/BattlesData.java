package com.glevel.dungeonhero.game.data;

import com.glevel.dungeonhero.R;

public enum BattlesData {
    OOSTERBEEK(R.string.battle_oosterbeek, 0, "oosterbeck.tmx", 120, 120, 10, 12, true), NIMEGUE(R.string.battle_nimegue,
            0, "nijmegen.tmx", 130, 90, 8, 27, true), ARNHEM_STREETS(R.string.battle_arnhem_streets, 0,
            "arnhem_streets.tmx", 80, 120, 30, 8, true);

    private final int id;
    private final int name;
    private final int image;
    private final String tileMapName;
    private final int alliesRequisition, axisRequisition;
    private final int alliesDeploymentZoneSize, axisDeploymentZoneSize;
    private final boolean isAllyLeftSide;

    BattlesData(int name, int image, String tileMapName, int alliesRequisition, int axisRequisition, int alliesDeploymentZoneSize, int axisDeploymentZoneSize,
                boolean isAllyLeftSide) {
        this.id = this.ordinal();
        this.name = name;
        this.image = image;
        this.tileMapName = tileMapName;
        this.alliesRequisition = alliesRequisition;
        this.axisRequisition = axisRequisition;
        this.alliesDeploymentZoneSize = alliesDeploymentZoneSize;
        this.axisDeploymentZoneSize = axisDeploymentZoneSize;
        this.isAllyLeftSide = isAllyLeftSide;
    }

    public int getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    public int getAlliesRequisition() {
        return alliesRequisition;
    }

    public int getAxisRequisition() {
        return axisRequisition;
    }

    public int getId() {
        return id;
    }

    public String getTileMapName() {
        return tileMapName;
    }

    public int getAlliesDeploymentZoneSize() {
        return alliesDeploymentZoneSize;
    }

    public int getAxisDeploymentZoneSize() {
        return axisDeploymentZoneSize;
    }

    public boolean getIsAllyLeftSide() {
        return isAllyLeftSide;
    }

}
