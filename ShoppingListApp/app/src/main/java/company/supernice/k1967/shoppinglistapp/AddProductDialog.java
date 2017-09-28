package company.supernice.k1967.shoppinglistapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Valtteri on 9.9.2017.
 * Dialog for adding new items to database
 */

public class AddProductDialog extends DialogFragment {

    //create interfaces
    public interface AddProductDialogListener {
        void onDialogPositiveClick(String name, int quantity, double price);
        void onDialogNegativeClick();
    }

    AddProductDialogListener mListerner;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.add_product, null);

        mListerner = (AddProductDialogListener)getActivity();

        builder.setView(dialogView);
        builder.setTitle("Add new product");

        final EditText nameBox = (EditText)dialogView.findViewById(R.id.nameBox);
        final EditText quantityBox = (EditText)dialogView.findViewById(R.id.QuantityBox);
        final EditText priceBox = (EditText)dialogView.findViewById(R.id.priceBox);

        //when user tries to submit results
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try
                {
                    String name = nameBox.getText().toString();
                    int quantity = Integer.parseInt(quantityBox.getText().toString());
                    Double price = Double.parseDouble(priceBox.getText().toString());

                    //reply with response
                    mListerner.onDialogPositiveClick(name, quantity, price);
                }
                catch (ClassCastException e)
                {
                    Toast.makeText(getActivity().getApplicationContext(), "Creating a product failed. Incorrect value format", Toast.LENGTH_LONG).show();
                    mListerner.onDialogNegativeClick();
                }

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity().getApplicationContext(), "Adding new product has been canceled", Toast.LENGTH_LONG).show();
                mListerner.onDialogNegativeClick();
            }
        });

        return builder.create();
    }
}
