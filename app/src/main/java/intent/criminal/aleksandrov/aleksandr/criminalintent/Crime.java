package intent.criminal.aleksandrov.aleksandr.criminalintent;

import java.util.UUID;

/**
 * Created by aleksandr on 1/9/17.
 */

public class Crime {

    private UUID mId;
    private String mTitle;

    public Crime() {
        // Генерирование уникального идентификатора
        mId = UUID.randomUUID();
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
}
