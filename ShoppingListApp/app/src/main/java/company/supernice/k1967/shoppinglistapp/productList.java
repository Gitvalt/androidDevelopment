package company.supernice.k1967.shoppinglistapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Valtteri on 9.9.2017.
 * Creates the list shown in the main activity
 */

public class ProductList extends ArrayAdapter<Product> {

    //application context
    private Context context;

    private ArrayList<Product> items;

    public ProductList(Context context, ArrayList<Product> products){
        super(context, R.layout.product_display_list, products);
        this.context = context;
        this.items = products;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //get the custom display
        View rowView = inflater.inflate(R.layout.product_display_list, parent, false);

        //get the current product
        Product prod = items.get(position);

        //get the textviews from the custom display
        TextView itemView = (TextView)rowView.findViewById(R.id.Item);
        TextView quantityView = (TextView)rowView.findViewById(R.id.Quantity);
        TextView priceView = (TextView)rowView.findViewById(R.id.Price);

        double priceCheck = prod.getPrice();

        itemView.setText(prod.getName());
        quantityView.setText(Integer.toString(prod.getQuantity()));
        priceView.setText("$" + String.format("%.2f", priceCheck));

        return rowView;
    }
}
