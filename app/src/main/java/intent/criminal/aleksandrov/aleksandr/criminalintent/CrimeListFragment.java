package intent.criminal.aleksandrov.aleksandr.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by aleksandr on 1/11/17.
 */

public class CrimeListFragment extends Fragment {

    public static final String TAG_REQUEST_ITEM_POSITION = "request_item_position";

    public static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private static final int REQUEST_CRIME = 1;

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private TextView mTextViewNothingToShow;
    private Button mButtonCreateFirstCrime;

    private int mChangePosition;

    private boolean mSubtitleVisible;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
//        String subtitle = getString(R.string.subtitle_format, crimeCount);
        String subtitle = getResources().getQuantityString(R.plurals.subtitle_plural, crimeCount, crimeCount);

        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setSubtitle(subtitle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mTextViewNothingToShow = (TextView) view.findViewById(R.id.text_view_nothing_yet);
        mButtonCreateFirstCrime = (Button) view.findViewById(R.id.button_create_first_crime);
        mButtonCreateFirstCrime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewCrime();
            }
        });

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        } else {
            mSubtitleVisible = getActivity().getIntent().getBooleanExtra(SAVED_SUBTITLE_VISIBLE, false);
        }

        updateUI();

        return view;
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
//            mAdapter.notifyItemChanged(mChangePosition);
            mAdapter.setCrimes(crimes);
            mAdapter.notifyDataSetChanged();
        }
        if (mAdapter.getItemCount() > 0) {
            mTextViewNothingToShow.setVisibility(View.GONE);
            mButtonCreateFirstCrime.setVisibility(View.GONE);
        } else {
            mTextViewNothingToShow.setVisibility(View.VISIBLE);
            mButtonCreateFirstCrime.setVisibility(View.VISIBLE);
        }
        updateSubtitle();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                createNewCrime();
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return true;
        }
    }

    private void createNewCrime() {
        Crime crime = new Crime();
        CrimeLab.get(getActivity()).addCrime(crime);
        Intent intent = new CrimePagerActivity().newIntent(getActivity(), crime.getId(), mSubtitleVisible);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CRIME) {
            mChangePosition = data.getIntExtra(TAG_REQUEST_ITEM_POSITION, 0);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitleTextView, mDateTextView;
        private CheckBox mSolvedCheckBox;

        private Crime mCrime;
        private int mItemPosition;

        public CrimeHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_crime_solved_check_box);
        }

        /**
         *
         * @param crime
         * @param itemPosition получаем позицию выбранного елемента для того чтобы в слушателе нажатия передать его созданному фрагменту. А потом фрагмент передаст его обратно при
         *                     закрытии чтобы RecyclerView вызывал не notifyDataSetChanged() метод который обновляет весь список, а notifyItemChanged(int) для обновления только измененного
         *                     елемента.
         */
        public void bindCrime(Crime crime, int itemPosition) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedCheckBox.setChecked(mCrime.isSolved());
            mItemPosition = itemPosition;
        }

        @Override
        public void onClick(View view) {
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId(), mSubtitleVisible);
            startActivityForResult(intent, REQUEST_CRIME);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
            return new CrimeHolder(view);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime, position);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }
    }
}
