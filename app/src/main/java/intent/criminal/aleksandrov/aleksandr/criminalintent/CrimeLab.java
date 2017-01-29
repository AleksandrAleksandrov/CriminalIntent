package intent.criminal.aleksandrov.aleksandr.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import intent.criminal.aleksandrov.aleksandr.criminalintent.database.CrimeBaseHelper;
import intent.criminal.aleksandrov.aleksandr.criminalintent.database.CrimeCursorWrapper;
import intent.criminal.aleksandrov.aleksandr.criminalintent.database.CrimeDbScheme.CrimeTable;

/**
 * Created by aleksandr on 1/11/17.
 */

public class CrimeLab {
    private static CrimeLab sCrimeLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();

    }

    public void addCrime(Crime crime) {
        ContentValues contentValues = getContentValues(crime);

        mDatabase.insert(CrimeTable.NAME, null, contentValues);
    }

    public void updateCrime(Crime crime) {
        String uuidString = crime.getId().toString();
        ContentValues contentValues = getContentValues(crime);

        mDatabase.update(CrimeTable.NAME, contentValues, CrimeTable.Cols.UUID + " = ?", new String[] {uuidString});
    }

    public void deleteCrime(Crime crime) {
        mDatabase.delete(CrimeTable.NAME, CrimeTable.Cols.UUID + " = ?", new String[] {crime.getId().toString()});
    }

    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper crimeCursorWrapper = queryCrimes(null, null);

        try {
            crimeCursorWrapper.moveToFirst();
            while (!crimeCursorWrapper.isAfterLast()) {
                crimes.add(crimeCursorWrapper.getCrime());
                crimeCursorWrapper.moveToNext();
            }
        } finally {
            crimeCursorWrapper.close();
        }

        return crimes;
    }

    public Crime getCrime(UUID id) {
        CrimeCursorWrapper cursorWrapper = queryCrimes(CrimeTable.Cols.UUID + " = ?", new String[] {id.toString()});
        try {
            if (cursorWrapper.getCount() == 0) {
                return null;
            }

            cursorWrapper.moveToFirst();
            return cursorWrapper.getCrime();
        } finally {
            cursorWrapper.close();
        }
    }

    private static ContentValues getContentValues(Crime crime) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CrimeTable.Cols.UUID, crime.getId().toString());
        contentValues.put(CrimeTable.Cols.TITLE, crime.getTitle());
        contentValues.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        contentValues.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);

        return contentValues;
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null, // Columns - null выбирает все столбцы
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null //orderBy
        );
        return new CrimeCursorWrapper(cursor);
    }
}
