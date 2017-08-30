package company.supernice.listviewexampleprogram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Valtteri on 30.8.2017.
 */

public class PhoneArrayAdapter extends ArrayAdapter<String > {

    //application context
    private Context context;

    //phone data (names)
    private ArrayList<String> phones;

    //get application context and phones data to adapter
    public PhoneArrayAdapter(Context context, ArrayList<String> phones){
        super(context, R.layout.rowlayout, R.id.textView2, phones);
        this.context = context;
        this.phones = phones;
    }

    //populate every row in ListView
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        //get row
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);

        //show phone name
        TextView textView = (TextView)rowView.findViewById(R.id.textView2);
        textView.setText(phones.get(position));

        //show phone icon/image
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView);

        switch (phones.get(position)){
            case "Android": imageView.setImageResource(R.drawable.android); break;
            case "Iphone": imageView.setImageResource(R.drawable.ios); break;
            case "WindowsMobile": imageView.setImageResource(R.drawable.windows); break;
            case "BlackBerry": imageView.setImageResource(R.drawable.blackberry); break;
            case "WebOS": imageView.setImageResource(R.drawable.webos); break;
            case "Ubuntu": imageView.setImageResource(R.drawable.ubuntu); break;
        }

        //return row view
        return  rowView;
    }

}