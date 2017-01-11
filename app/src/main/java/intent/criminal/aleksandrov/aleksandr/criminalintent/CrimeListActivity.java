package intent.criminal.aleksandrov.aleksandr.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by aleksandr on 1/11/17.
 */

public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
