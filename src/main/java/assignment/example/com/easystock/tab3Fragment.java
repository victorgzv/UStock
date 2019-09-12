package assignment.example.com.easystock;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Víctor on 19/11/2017.This class uses a SimpleCursorAdapter to display all the suppliers of the DB.
 */

public class tab3Fragment extends ListFragment {
    Database db;
    FloatingActionButton addSupplier;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.tab3_fragment,container,false);
        addSupplier= (FloatingActionButton) view.findViewById(R.id.btnAddS);
        Cursor suppliers;
        db = new Database(getContext());
        db.open();
        suppliers=db.getSuppliers();
        // array containing columns names from your cursor that you want display on your list
        String[] columns = new String[] {"_id","Supplier_name","Supplier_address","Supplier_email"};

        // array of the ids from your supplier_row.xml
        int[] to = new int [] {R.id.txtSID,R.id.txtSname,R.id.txtSaddress,R.id.txtSemail};

        // Now, create the appropriate adapter that holds your cursor, the category_row layout you will display each category_row on your cursor,
        SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(getContext(), R.layout.supplier_row, suppliers, columns, to,1);

        this.setListAdapter(mAdapter);

        addSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addProductIntent=  new Intent(getContext(),AddSupplier.class);
                startActivity(addProductIntent);

            }
        });



        return view;
    }


}
