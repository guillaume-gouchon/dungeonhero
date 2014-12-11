package com.glevel.dungeonhero.activities.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.glevel.dungeonhero.MyDatabase;
import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.activities.BookChooserActivity;
import com.glevel.dungeonhero.activities.GameActivity;
import com.glevel.dungeonhero.activities.adapters.LoadGamesAdapter;
import com.glevel.dungeonhero.models.Game;

import java.util.List;

public class LoadGameFragment extends DialogFragment {

    /**
     * Callbacks
     */
    private final ListView.OnItemClickListener mOnItemClickedListener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            launchGame(mSavedGamesList.get(position));
        }
    };

    private OnFragmentClosed mListener;
    private ListView mGamesListView;
    private LoadGamesAdapter mAdapter;
    private MyDatabase mDbHelper;
    private List<com.glevel.dungeonhero.models.Game> mSavedGamesList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0); // remove title from dialog fragment
        mDbHelper = new MyDatabase(getActivity());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (OnFragmentClosed) activity;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() == null)
            return;

        // set the animations to use ON showing and hiding the dialog
        getDialog().getWindow().setWindowAnimations(R.style.DialogAnimation);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mListener.OnFragmentClosed();
        super.onDismiss(dialog);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSavedGamesList = mDbHelper.getRepository(MyDatabase.Repositories.GAME.name()).getAll();
        mAdapter = new LoadGamesAdapter(getActivity(), mSavedGamesList);
        mGamesListView.setAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.load_game_fragment, container, false);

        mGamesListView = (ListView) layout.findViewById(R.id.gamesList);
        mGamesListView.setOnItemClickListener(mOnItemClickedListener);

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
        Intent intent;
        if (game.getDungeon() == null) {
            intent = new Intent(getActivity(), BookChooserActivity.class);
        } else {
            intent = new Intent(getActivity(), GameActivity.class);
        }
        intent.putExtra(Game.class.getName(), game);
        startActivity(intent);
        getActivity().finish();
    }

    public static interface OnFragmentClosed {
        public void OnFragmentClosed();
    }

}
