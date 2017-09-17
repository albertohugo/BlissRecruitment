package hugo.alberto.blissrecruitment.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Alberto on 28/05/2016.
 */
public class Question implements Parcelable {
    public String id;
    public String question;
    public String image_url;
    public String thumb_url;
    public String published_at;
    public List<Choices> choices;
    
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        
    }
}