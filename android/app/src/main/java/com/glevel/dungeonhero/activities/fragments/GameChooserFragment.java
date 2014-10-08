package com.glevel.dungeonhero.activities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.glevel.dungeonhero.MyDatabase;
import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.activities.GameActivity;
import com.glevel.dungeonhero.activities.adapters.GamesListAdapter;
import com.glevel.dungeonhero.models.Game;

import java.util.List;

public class GameChooserFragment extends DialogFragment {

    public static final int LOAD_GAME_CATEGORY_ID = 1;

    private ExpandableListView mGamesListView;
    private GamesListAdapter mAdapter;
    private MyDatabase mDbHelper;
    private List<com.glevel.dungeonhero.models.Game> mSavedGamesList;


    /**
     * Callbacks
     */
    private final ExpandableListView.OnChildClickListener mOnItemClickedListener = new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {
            if (view.isEnabled()) {
                launchGame(mSavedGamesList.get(childPosition));
            }
            return false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0); // remove title from dialog fragment

        mDbHelper = new MyDatabase(getActivity());
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() == null)
            return;

        // set the animations to use ON showing and hiding the dialog
        getDialog().getWindow().setWindowAnimations(R.style.DialogAnimation);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSavedGamesList = mDbHelper.getRepository(MyDatabase.Repositories.GAME.name()).getAll();
        mAdapter = new GamesListAdapter(getActivity(), mSavedGamesList, new int[]{R.string.load_game});
        mGamesListView.setAdapter(mAdapter);

        // the most dirty hack ON earth in order to expand all the groups (buggy
        // when called at the same time (!))
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mGamesListView.expandGroup(0);
            }
        }, 100);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mGamesListView.expandGroup(1);
            }
        }, 200);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.game_chooser_fragment, container, false);

        // expandable friends list view
        mGamesListView = (ExpandableListView) layout.findViewById(R.id.gamesList);
        mGamesListView.setChoiceMode(ExpandableListView.CHOICE_MODE_SINGLE);
        mGamesListView.setOnChildClickListener(mOnItemClickedListener);
        // disable group collapsing
        mGamesListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // do nothing
                return true;
            }
        });
        // remove group icons
        mGamesListView.setGroupIndicator(null);

        return layout;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // null out the group cursor. This will cause the group cursor and all
        // of the child cursors to be closed.
        mAdapter = null;
    }

    private void launchGame(Game game) {
        Intent intent = new Intent(getActivity(), Game.class);
        intent.putExtra(GameActivity.EXTRA_GAME_ID, game.getId());
        startActivity(intent);
        getActivity().finish();
    }

}
