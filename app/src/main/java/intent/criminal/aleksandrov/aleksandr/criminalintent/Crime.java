package intent.criminal.aleksandrov.aleksandr.criminalintent;

import java.util.Date;
import java.util.UUID;

/**
 * Created by aleksandr on 1/9/17.
 */

public class Crime {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;

    public Crime() {
        // Генерирование уникального идентификатора
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public UUID getId() {
        return mId;
    }

    public Date getDate() {
//        return DateFormat.format("EEEE, MMM d, yyyy", mDate);
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }
}
