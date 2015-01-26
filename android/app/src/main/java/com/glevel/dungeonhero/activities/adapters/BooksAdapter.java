package com.glevel.dungeonhero.activities.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.data.characters.MonsterFactory;
import com.glevel.dungeonhero.game.base.GraphicsManager;
import com.glevel.dungeonhero.models.Book;
import com.glevel.dungeonhero.models.characters.Monster;
import com.glevel.dungeonhero.views.CustomCarousel;
import com.glevel.dungeonhero.views.SpriteView;
import com.glevel.dungeonhero.views.StarRatingView;

import java.util.List;

public class BooksAdapter extends CustomCarousel.Adapter<Book> {

    private Resources mResources;
    private View.OnClickListener mItemClickedListener;

    public BooksAdapter(final Context context, int layoutResource, List<Book> dataList, View.OnClickListener itemClickedListener) {
        super(context, layoutResource, dataList, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View enterQuestLayout = v.findViewById(R.id.enter_quest_layout);
                enterQuestLayout.startAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_in));
                enterQuestLayout.setVisibility(View.VISIBLE);
            }
        });
        mResources = context.getResources();
        mItemClickedListener = itemClickedListener;
    }

    private static Monster getBookMonsterSprite(int bookLevel) {
        switch (bookLevel) {
            case 0:
                return MonsterFactory.buildGoblin();
            case 1:
                return MonsterFactory.buildOrc();
            case 2:
                return MonsterFactory.buildTroll();
            case 3:
                return MonsterFactory.buildChaosWarrior();
            default:
                return MonsterFactory.buildDemonKing();
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View layout = (View) super.instantiateItem(container, position);

        Book book = mDataList.get(position);

        ((TextView) layout.findViewById(R.id.name)).setText(book.getName(mResources));
        if (book.getIntroText(mResources) > 0) {
            ((TextView) layout.findViewById(R.id.summary)).setText(book.getIntroText(mResources));
        } else {
            layout.findViewById(R.id.summary).setVisibility(View.INVISIBLE);
        }
        layout.findViewById(R.id.lock).setVisibility(book.isAvailable() ? View.GONE : View.VISIBLE);

        // star rating
        int rating = book.getBestScore();
        if (rating >= 0) {
            StarRatingView ratingView = (StarRatingView) layout.findViewById(R.id.rating);
            ratingView.updateRating(rating);
        }

        // enter quest layout
        View enterQuestButton = layout.findViewById(R.id.enter_quest_btn);
        enterQuestButton.setOnClickListener(mItemClickedListener);
        enterQuestButton.setTag(R.string.id, position);

        Monster monster = getBookMonsterSprite(book.getLevel());
        SpriteView monsterSprite1 = (SpriteView) layout.findViewById(R.id.monster1);
        SpriteView monsterSprite2 = (SpriteView) layout.findViewById(R.id.monster2);
        monsterSprite1.setImageResource(monster.getImage(mResources));
        monsterSprite1.setSpriteName(GraphicsManager.ASSETS_PATH + monster.getIdentifier() + ".png");
        monsterSprite2.setImageResource(monster.getImage(mResources));
        monsterSprite2.setSpriteName(GraphicsManager.ASSETS_PATH + monster.getIdentifier() + ".png");

        return layout;
    }

}
