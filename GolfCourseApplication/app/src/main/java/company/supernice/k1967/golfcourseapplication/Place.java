package company.supernice.k1967.golfcourseapplication;

import android.content.Context;

/**
 * Created by Valtteri on 25.9.2017.
 */

public class Place {

    public String name;
    public String imageName;

    public int getImageResourceId(Context context){
        return context.getResources().getIdentifier(this.imageName, "drawable", context.getPackageName());
    }
}
