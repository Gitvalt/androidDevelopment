package company.supernice.listviewexampleprogram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //--populate the listview--

        //get the listview element
        ListView listview = (ListView) findViewById(R.id.mainListView);

        //create dummy data
        String[] phones = new String[]{
                "Android", "Iphone", "WindowsMobile", "Blackberry", "WebOS", "Ubuntu",
                "Android", "Iphone", "WindowsMobile", "Blackberry", "WebOS", "Ubuntu"
        };

        final ArrayList<String> list = new ArrayList<String>();

        //add items from phones array to the arrayList
        for(int i = 0; i < phones.length; ++i){
            list.add(phones[i]);
        }

        //Default -- ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        //test1 -- ArrayAdapter adapter = new ArrayAdapter(this, R.layout.rowlayout, R.id.textView2, list);

        //custom adapter
        PhoneArrayAdapter adapter = new PhoneArrayAdapter(this, list);

        //set adapter to listview
        listview.setAdapter(adapter);
        //--end populating

    }


}
