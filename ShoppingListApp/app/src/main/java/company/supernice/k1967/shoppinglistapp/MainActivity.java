package company.supernice.k1967.shoppinglistapp;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.SQLData;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    private ListView listView;
    private SQLiteDatabase db;
    private String sqlTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.contentList);

        /*
        Old dummy data
        Product[] products = new Product[]
        {
            new Product("ONEPLUS ONE", 5, 399.0f),
            new Product("ONEPLUS TWO", 2, 499.0f)
        };
        */

        //Creating Database helper and the database itself
        DatabaseHelper helper = new DatabaseHelper(this);
        db = (helper.getReadableDatabase());

        //fetch the table name
        sqlTable = helper.DataTable;

        //if the datatable contains no products --> insert dummy data
        if(getProducts(helper.DataTable).size() <= 0){

            //clear table
            db.delete(helper.DataTable, null, null);

            //write dummydata
            writeData();
        }

        //read products from the database
        List<Product> products = getProducts(helper.DataTable);

        final ArrayList<Product> prod_List = new ArrayList<Product>();

        int prodSize = products.size();
        for(int i = 0; i < prodSize; i++){
            prod_List.add(products.get(i));
        }

        productList adapter = new productList(this, prod_List);
        listView.setAdapter(adapter);

        // item listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "Hello long clicker", Toast.LENGTH_SHORT).show();
                return false;
            }
        });


    }

    //Writing dummy data
    public void writeData()
    {
        writeProduct("ONEPLUS ONE", 5, 299.0f);
        writeProduct("ONEPLUS TWO", 9, 399.0f);
        writeProduct("ONEPLUS THREE", 15, 599.0f);
    }

    //Insert method
    public void writeProduct(String Name, int Quantity, double Price)
    {
        ContentValues inserts = new ContentValues();
        inserts.put("Name", Name);
        inserts.put("Quantity", Quantity);
        inserts.put("Price", Price);

        //execute insert
        db.insert(sqlTable, null, inserts);
    }

    public void updateProduct(String targetName, String NewName, int Quantity, double Price)
    {
        ContentValues values = new ContentValues();

        values.put("Name", NewName);
        values.put("Quantity", Quantity);
        values.put("Price", Price);

        db.update(sqlTable, values ,"Name=" + "'" + targetName + "'", null);
    }

    public Product findProduct(String name)
    {

        //what we are looking from the database
        String[] resultColumns = new String[]{"_id", "Name", "Quantity", "Price"};

        Cursor finder = db.query(sqlTable, resultColumns, null, null, null, null, "Name ASC", null);

        int wasFound = finder.getCount();

        if(wasFound <= 0)
        {
            //nothing found
            return null;
        }
        else if(wasFound > 1)
        {
            //too many items found
            return null;
        }
        else
        {
            //one item found
            return new Product(finder.getString(1), finder.getInt(2), finder.getDouble(3));
        }

    }

    //Select method
    public List<Product> getProducts(String tableName)
    {

        //what we are looking from the database
        String[] resultColumns = new String[]{"_id", "Name", "Quantity", "Price"};

        //table, columns[], selection, selectionARGS, groupby, having, orderBy, limit
        Cursor reader = db.query(tableName, resultColumns, null, null, null, null, "Name ASC", null);

        int foundItems = reader.getCount();

        //if no items were found
        if(foundItems == 0){
            return null;
        }

        //list of found products
        List<Product> products = new ArrayList<Product>();

        int index = 0;
        while(reader.moveToNext()){
            //0 => _id
            products.add(index, new Product(reader.getString(1), reader.getInt(2), reader.getDouble(3)));
            index++;
        }

        reader.close();

        //returns results
        return products;
    }

}
