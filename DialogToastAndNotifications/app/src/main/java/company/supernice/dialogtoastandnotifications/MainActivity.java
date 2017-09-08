package company.supernice.dialogtoastandnotifications;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //hard coded list items
        String[] items = new String[]{"test", "tester", "testeri"};

        //listview items
        final ArrayList<String> list = new ArrayList<String>();

        //add hard coded items into the list
        for (int i = 0; i < items.length; i++) {
            list.add(items[i]);
        }

        //get the list
        ListView listView = (ListView) findViewById(R.id.pollSelection);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // get list row data (now String as a phone name)
                String phone = list.get(position);



            }
        });

    }
}
