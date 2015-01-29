package com.glevel.dungeonhero.activities.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.activities.BookChooserActivity;
import com.glevel.dungeonhero.activities.games.GameActivity;
import com.glevel.dungeonhero.activities.adapters.LoadGamesAdapter;
import com.glevel.dungeonhero.models.Game;
import com.glevel.dungeonhero.providers.MyContentProvider;
import com.glevel.dungeonhero.utils.ApplicationUtils;
import com.glevel.dungeonhero.utils.MusicManager;

public class LoadGameFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "LoadGameFragment";
    private static final int GET_LOAD_GAMES = 1;

    /**
     * Callbacks
     */
    private final ListView.OnItemClickListener mOnItemClickedListener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            MusicManager.playSound(getActivity().getApplicationContext(), R.raw.button_sound);
            Game game = Game.fromCursor(getActivity().getContentResolver().query(MyContentProvider.URI_GAMES, null, Game.COLUMN_ID + "=?", new String[]{"" + view.getTag(R.string.id)}, null));
            launchGame(game);
        }
    };

    private ListView mGamesListView;
    private FragmentCallbacks mListener;
    private LoadGamesAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0); // remove title from dialog fragment
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (FragmentCallbacks) activity;
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
        mAdapter = new LoadGamesAdapter(getActivity());
        mGamesListView.setAdapter(mAdapter);

        getLoaderManager().initLoader(GET_LOAD_GAMES, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_load_game, container, false);

        mGamesListView = (ListView) layout.findViewById(R.id.gamesList);
        mGamesListView.setOnItemClickListener(mOnItemClickedListener);

        // add empty header view
        LinearLayout viewHeader = new LinearLayout(getActivity().getApplicationContext());
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ApplicationUtils.convertDpToPixels(getActivity().getApplicationContext(), 20));
        viewHeader.setLayoutParams(lp);
        mGamesListView.addHeaderView(viewHeader, null, false);

        return layout;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case GET_LOAD_GAMES:
                return new CursorLoader(getActivity().getApplicationContext(), MyContentProvider.URI_GAMES, new String[]{Game.COLUMN_ID, Game.Columns.HERO.name()}, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(TAG, "Loader finished");
        if (mAdapter != null && cursor != null) {
            mAdapter.swapCursor(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "Loader reset");
        if (mAdapter != null) {
            mAdapter.swapCursor(null);
        }
    }

    private void launchGame(Game game) {
        Intent intent;
        if (game.getDungeon() == null) {
            intent = new Intent(getActivity(), BookChooserActivity.class);
        } else {
            intent = new Intent(getActivity(), game.getBook() != null ? game.getBook().getActivityClass() : GameActivity.class);
        }
        intent.putExtra(Game.class.getName(), game);
        startActivity(intent);
        getActivity().finish();
    }

    public static interface FragmentCallbacks {

        public void OnFragmentClosed();

    }

}
