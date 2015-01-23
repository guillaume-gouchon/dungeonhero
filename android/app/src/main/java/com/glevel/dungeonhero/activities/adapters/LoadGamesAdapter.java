package com.glevel.dungeonhero.activities.adapters;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.Game;

public class LoadGamesAdapter extends CursorAdapter {

    private Context mContext;
    private final LayoutInflater mInflater;

    public LoadGamesAdapter(Context context) {
        super(context, null, 0);
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.load_game_list_item, parent, false);
    }

    private static class ViewHolder {
        TextView title;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ViewHolder viewHolder;
        if (view.getTag(R.string.viewholder) == null) {
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) view.findViewById(R.id.text);
            view.setTag(R.string.viewholder, viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag(R.string.viewholder);
        }

        viewHolder.title.setText(R.string.loading_saved_games);
        viewHolder.title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_loading, 0, 0, 0);

        final int position = cursor.getPosition();
        new AsyncTask<Void, Void, Game>() {
            @Override
            protected Game doInBackground(Void... params) {
                return getElementAt(position);
            }

            @Override
            protected void onPostExecute(Game game) {
                super.onPostExecute(game);
                if (game != null) {
                    viewHolder.title.setText(game.getHero().getHeroName());
                    viewHolder.title.setCompoundDrawablesWithIntrinsicBounds(game.getHero().getImage(mContext.getResources()), 0, 0, 0);
                }
            }
        }.execute();
    }

    public Game getElementAt(int position) {
        Cursor cursor = getCursor();
        if (cursor != null && cursor.moveToPosition(position)) {
            return Game.fromCursor(cursor);
        }
        return null;
    }

}
