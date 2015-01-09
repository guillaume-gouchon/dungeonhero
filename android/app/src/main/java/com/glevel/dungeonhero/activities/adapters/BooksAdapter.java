package com.glevel.dungeonhero.activities.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.Book;
import com.glevel.dungeonhero.views.CustomCarousel;

import java.util.List;

public class BooksAdapter extends CustomCarousel.Adapter<Book> {

    private Resources mResources;

    public BooksAdapter(Context context, int layoutResource, List<Book> dataList, View.OnClickListener itemClickedListener) {
        super(context, layoutResource, dataList, itemClickedListener);
        mResources = context.getResources();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View layout = (View) super.instantiateItem(container, position);
        layout.setTag(R.string.id, position);

        Book book = mDataList.get(position);

        ((TextView) layout.findViewById(R.id.name)).setText(book.getName(mResources));
        ((TextView) layout.findViewById(R.id.summary)).setText(book.getIntroText(mResources));
        layout.findViewById(R.id.lock).setVisibility(book.isAvailable() ? View.GONE : View.VISIBLE);

        ImageView scoreImage = (ImageView) layout.findViewById(R.id.score);
        scoreImage.setVisibility(book.isDone() ? View.VISIBLE : View.GONE);
        switch (book.getBestScore()) {
            case 1:
                scoreImage.setImageResource(R.drawable.ic_trophy_bronze);
                break;
            case 2:
                scoreImage.setImageResource(R.drawable.ic_trophy_silver);
                break;
            case 3:
                scoreImage.setImageResource(R.drawable.ic_trophy_gold);
                break;
        }

        return layout;
    }

}
