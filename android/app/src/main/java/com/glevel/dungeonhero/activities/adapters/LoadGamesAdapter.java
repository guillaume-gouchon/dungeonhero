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
import com.glevel.dungeonhero.models.characters.Hero;
import com.glevel.dungeonhero.utils.providers.ByteSerializerHelper;

import java.util.HashMap;
import java.util.Map;

public class LoadGamesAdapter extends CursorAdapter {

    private final Context mContext;
    private final LayoutInflater mInflater;
    private final Map<Long, Game> mCache;

    public LoadGamesAdapter(Context context) {
        super(context, null, 0);
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mCache = new HashMap<>();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.load_game_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder;
        if (view.getTag(R.string.view_holder) == null) {
            viewHolder = new ViewHolder(view);
            view.setTag(R.string.view_holder, viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag(R.string.view_holder);
        }

        viewHolder.title.setText(R.string.loading_saved_games);
        viewHolder.title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_loading, 0, 0, 0);

        long gameId = cursor.getLong(cursor.getColumnIndex(Game.COLUMN_ID));
        Game game = mCache.get(gameId);
        if (game == null) {
            // if not in cache, de-serialize game object
            new AsyncTask<Object, Void, Game>() {
                @Override
                protected Game doInBackground(Object... params) {
                    long gameId = (long) params[0];
                    byte[] blob = (byte[]) params[1];
                    Game game = new Game();
                    game.setId(gameId);
                    game.setHero((Hero) ByteSerializerHelper.getObjectFromByte(blob));
                    mCache.put(gameId, game);
                    return game;
                }

                @Override
                protected void onPostExecute(Game game) {
                    super.onPostExecute(game);
                    notifyDataSetChanged();
                }
            }.execute(gameId, cursor.getBlob(cursor.getColumnIndex(Game.Columns.HERO.toString())));
            mCache.put(gameId, new Game());
        } else if (game.getHero() != null) {
            // game has already been retrieved and is in cache
            viewHolder.title.setText(game.getHero().getHeroName());
            viewHolder.title.setCompoundDrawablesWithIntrinsicBounds(game.getHero().getImage(mContext.getResources()), 0, 0, 0);
        }

        view.setTag(R.string.id, gameId);
    }

    private static class ViewHolder {
        public TextView title;

        public ViewHolder(View itemView) {
            title = (TextView) itemView.findViewById(R.id.text);
        }
    }

}
