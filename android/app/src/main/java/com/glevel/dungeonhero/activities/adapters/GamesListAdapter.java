package com.glevel.dungeonhero.activities.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.activities.fragments.GameChooserFragment;
import com.glevel.dungeonhero.models.Game;

import java.util.List;

public class GamesListAdapter extends BaseExpandableListAdapter {

    private final LayoutInflater mInflater;
    private List<Game> mSavedGames;
    private int[] mHeaderLabels;

    public GamesListAdapter(FragmentActivity activity, List<Game> savedCampaigns, int[] headerLabels) {
        mSavedGames = savedCampaigns;
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mHeaderLabels = headerLabels;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (groupPosition == GameChooserFragment.NEW_GAME_CATEGORY_ID) {
            return new Game();
        } else if (groupPosition == GameChooserFragment.LOAD_GAME_CATEGORY_ID
                && childPosition < mSavedGames.size()) {
            return mSavedGames.get(childPosition);
        }
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.game_list_child, null);
        }

        TextView title = (TextView) convertView.findViewById(R.id.text);

        Object child = getChild(groupPosition, childPosition);
        if (getChild(groupPosition, childPosition) == null) {
            // if empty view
            convertView.setEnabled(false);
            title.setText(R.string.no_saved_games);
            title.setBackgroundColor(Color.BLACK);
            title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else {
            convertView.setEnabled(true);
            Game game = (Game) child;
            // TODO
//            title.setText(game.getName());
//            title.setCompoundDrawablesWithIntrinsicBounds(game.getArmy().getFlagImage(), 0, 0, 0);
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupPosition == GameChooserFragment.NEW_GAME_CATEGORY_ID) {
            return 1;
        } else {
            return Math.max(1, mSavedGames.size());
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mHeaderLabels[groupPosition];
    }

    @Override
    public int getGroupCount() {
        return mHeaderLabels.length;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.games_list_header, null);
        }

        // set header title
        TextView headerTitle = (TextView) convertView.findViewById(R.id.text);
        headerTitle.setText(mHeaderLabels[groupPosition]);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
