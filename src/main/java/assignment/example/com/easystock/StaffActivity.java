package assignment.example.com.easystock;
/**
 * Created by Víctor Gonzalez.
 * This class displays the categories stored in the database.
 * It also let the staff user scan a barcode and if it
 * exists in the database it will display the details of the product.
 */
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class StaffActivity extends ListActivity {
    Database db;
    ArrayList<Category> categories = new ArrayList<Category>();
    private Integer cid;
    private String cname;
    private FloatingActionButton scan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);
        scan= findViewById(R.id.btnScanProduct);
        openDB();//call function that initialise and read data from the database
        populateCategories();//function to get all categories
        MyCustomAdapter myAdapter=new MyCustomAdapter(getApplicationContext(),R.layout.product_row,categories);
        setListAdapter(myAdapter);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent displayScan= new Intent(StaffActivity.this,ScannerActivity.class);
                startActivity(displayScan);
            }
        });
    }
    //Dtaabase connection
    public void openDB(){
        db = new Database(this);
        db.open();

    }
    //Database query to get all categories
    public void populateCategories(){
        Cursor pcursor;
        pcursor=db.getCategories();
        pcursor.moveToFirst();
        while (!pcursor.isAfterLast()) {//Iterate cursor and get the categories
            cid=pcursor.getInt(pcursor.getColumnIndex("_id"));
            cname=pcursor.getString(pcursor.getColumnIndex("Category_name"));
            Category c= new Category(cid,cname);
            categories.add(c);
            pcursor.moveToNext();
        }
    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Integer pid = categories.get(position).getId();
        String pname = categories.get(position).getName();


        Intent displayProducts= new Intent(this,ProductsByCategory.class);
        displayProducts.putExtra("ID",pid);
        displayProducts.putExtra("Name",pname);

        startActivity(displayProducts);


    }
    //Class to setup the adapter to display the catergories into a listview with a custom row xml
    public class MyCustomAdapter extends ArrayAdapter<Category> {


        public MyCustomAdapter(Context context, int rowLayoutId, ArrayList<Category> c) {

            super(context, rowLayoutId, c);
        }
        @Override
        public View getView(int position, View convertView,
                            ViewGroup parent)
        {
            View row;
            TextView txtName;

            LayoutInflater inflater=getLayoutInflater();
            row=inflater.inflate(R.layout.category_row, parent, false);
            //Then, format the parts of your row layout that need formatting; Note – to connect to a widget ID in your row layout, use row.findViewByID


            txtName= row.findViewById(R.id.txtCname);
            txtName.setText(categories.get(position).getName());



            return row;
        }


    }
}
