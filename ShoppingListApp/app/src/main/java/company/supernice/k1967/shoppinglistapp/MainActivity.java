package company.supernice.k1967.shoppinglistapp;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.SQLData;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private ListView listView;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.contentList);

        Product[] products = new Product[]
        {
            new Product("ONEPLUS ONE", 5, 399.0f),
            new Product("ONEPLUS TWO", 2, 499.0f)
        };

        db = (new DatabaseHelper(this)).getWritableDatabase();



        final ArrayList<Product> prod_List = new ArrayList<Product>();

        for(int i = 0; i < products.length; i++){
            prod_List.add(products[i]);
        }

        productList adapter = new productList(this, prod_List);
        listView.setAdapter(adapter);

        // item listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

    public Product[] getProductsFromDB(){

        return null;
    }
}
