package com.glevel.dungeonhero.activities.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.Book;
import com.glevel.dungeonhero.views.CustomCarousel;

import java.util.List;

public class BooksAdapter extends CustomCarousel.Adapter<Book> {

    public BooksAdapter(Context context, int layoutResource, List<Book> dataList, View.OnClickListener itemClickedListener) {
        super(context, layoutResource, dataList, itemClickedListener);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View layout = (View) super.instantiateItem(container, position);
        layout.setTag(R.string.id, position);

        Book book = mDataList.get(position);

        ((TextView) layout.findViewById(R.id.name)).setText(book.getName());
        ((ImageView) layout.findViewById(R.id.image)).setImageResource(book.getImage());

        return layout;
    }

}
