package com.glevel.dungeonhero.activities.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.models.Game;

import java.util.List;

public class LoadGamesAdapter extends ArrayAdapter<Game> {

    private final LayoutInflater mInflater;
    private List<Game> mSavedGames;

    public LoadGamesAdapter(FragmentActivity activity, List<Game> savedCampaigns) {
        super(activity, R.layout.load_game_list_item);
        mSavedGames = savedCampaigns;
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mSavedGames.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.load_game_list_item, null);
        }

        Game game = mSavedGames.get(position);

        TextView title = (TextView) convertView.findViewById(R.id.text);
        title.setText(game.getHero().getHeroName());
        title.setCompoundDrawablesWithIntrinsicBounds(game.getHero().getImage(), 0, 0, 0);
        return convertView;
    }

}
