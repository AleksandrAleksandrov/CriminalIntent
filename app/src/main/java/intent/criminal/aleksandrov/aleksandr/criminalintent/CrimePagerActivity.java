package intent.criminal.aleksandrov.aleksandr.criminalintent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.List;
import java.util.UUID;

/**
 * Created by aleksandr on 1/13/17.
 */

public class CrimePagerActivity extends FragmentActivity {

    private static final String EXTRA_CRIME_ID = "intent.criminal.aleksandrov.aleksandr.criminalintent.crime_id";
    private static final String EXTRA_LIST_POSITION = "intent.criminal.aleksandrov.aleksandr.criminalintent.list_position";

    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    private int mItemPosition;

    public static Intent newIntent(Context packageContext, UUID crimeId, int position) {
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        intent.putExtra(EXTRA_LIST_POSITION, position);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        mItemPosition = getIntent().getIntExtra(EXTRA_LIST_POSITION, 0);

        mViewPager = (ViewPager) findViewById(R.id.activity_crime_pager_view_pager);
        mCrimes = CrimeLab.get(this).getCrimes();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId(), position);
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        for (int i = 0; i < mCrimes.size();i++) {
            if (mCrimes.get(i).getId().equals(crimeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
        returnResult();
    }

    public void returnResult() {
        setResult(Activity.RESULT_OK, new Intent().putExtra(CrimeListFragment.TAG_REQUEST_ITEM_POSITION, mItemPosition));
    }
}
