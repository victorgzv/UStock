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
 * * Created by Víctor on 19/11/2017. This class uses a SimpleCursorAdapter to display all the categories stored in the DB.

 */

public class tab1Fragment extends ListFragment {

    Database db;
    FloatingActionButton addCategory;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.tab1_fragment,container,false);
        addCategory= (FloatingActionButton) view.findViewById(R.id.btnAddCategory);
        Cursor categories;
        db = new Database(getContext());
        db.open();
        categories=db.getCategories();
        // array containing columns names from your cursor that you want display on your list
        String[] columns = new String[] {"Category_name"};

        // array of the ids from your category_row.xmly_row.xml that will be populated from the cursor columns
        int[] to = new int [] {R.id.txtCname};

        // Now, create the appropriate adapter that holds your cursor, the category_row layout you will display each category_row on your cursor,
        SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(getContext(), R.layout.category_row, categories, columns, to,1);

        this.setListAdapter(mAdapter);


        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addProductIntent=  new Intent(getContext(),AddCategory.class);
                startActivity(addProductIntent);

            }
        });
        return view;
    }
}
