package intent.criminal.aleksandrov.aleksandr.criminalintent.database;

/**
 * Created by aleksandr on 1/28/17.
 */

public class CrimeDbScheme {
    public static final class CrimeTable {
        public static final String NAME = "crimes";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
            public static final String SUSPECT = "suspect";
        }
    }
}
