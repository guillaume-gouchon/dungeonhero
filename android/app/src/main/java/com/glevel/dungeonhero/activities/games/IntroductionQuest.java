package com.glevel.dungeonhero.activities.games;

import com.glevel.dungeonhero.data.characters.PNJFactory;
import com.glevel.dungeonhero.models.characters.Pnj;

public class IntroductionQuest extends GameActivity {

    @Override
    protected void addSpecialGameElements() {
        super.addSpecialGameElements();
        if (mHero != null) {
            if (mGame.getBook().getActiveChapter().isFirst() && mDungeon.isFirstRoom()) {
                // add introduction PNJ
                final Pnj tutorialCharacter = PNJFactory.buildTutorialPNJ();
                mRoom.addGameElement(tutorialCharacter, mRoom.getRandomFreeTile());
            }
        }
    }

}
