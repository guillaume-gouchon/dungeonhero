package com.glevel.dungeonhero.activities.adapters;

import android.content.Context;
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
        if (groupPosition == GameChooserFragment.LOAD_GAME_CATEGORY_ID && childPosition < mSavedGames.size()) {
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
            convertView = mInflater.inflate(R.layout.game_chooser_list_child, null);
        }

        Game game = (Game) getChild(groupPosition, childPosition);

        TextView title = (TextView) convertView.findViewById(R.id.text);
        title.setText(game.getHero().getName());
        title.setCompoundDrawablesWithIntrinsicBounds(game.getHero().getImage(), 0, 0, 0);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupPosition == GameChooserFragment.LOAD_GAME_CATEGORY_ID) {
            return Math.max(1, mSavedGames.size());
        }
        return 0;
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
            convertView = mInflater.inflate(R.layout.game_chooser_list_child, null);
        }
        convertView.setVisibility(View.GONE);
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
