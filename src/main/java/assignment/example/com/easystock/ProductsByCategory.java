package assignment.example.com.easystock;
/**
 * Created by Víctor Gonzalez.
 * This class displays the products sorted by category.
 */
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class ProductsByCategory extends ListActivity {
    private int cid;
    private String name,desc,barcode;
    private Integer pid,stock,catid,supid,qtyRequired;
    private Float price;
    ArrayList<Product> products = new ArrayList<Product>();

    Database db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_by_category);
        //Intent to retreive data from activity
        Intent intent = getIntent();
        cid = intent.getIntExtra("ID",0);//get the id of the product clicked from the source activity
        openDB();

    }
    public void openDB(){
        db = new Database(this);
        db.open();
        populateProducts();
        if(products.isEmpty()){
            Toast.makeText(this,"There are no products for this category",Toast.LENGTH_SHORT).show();
            finish();
        }
        MyCustomAdapter myAdapter=new MyCustomAdapter(this,R.layout.activity_products_by_category,products);
        setListAdapter(myAdapter);

    }
    //Function that get all the products sorted by the selected category.
    public void populateProducts(){
        Cursor pcursor;
        pcursor=db.getProductsByCategory(cid);//function that return a cursos with the products
        pcursor.moveToFirst();//Move to the first row of the cursor
        while (!pcursor.isAfterLast()) {//Loop thru all the rows all the cursor

            //Getting each column of the table product
            pid=pcursor.getInt(pcursor.getColumnIndex("_id"));
            barcode=pcursor.getString(pcursor.getColumnIndex("Product_barcode"));
            name=pcursor.getString(pcursor.getColumnIndex("Product_name"));
            String formatedName = name.substring(0, 1).toUpperCase() + name.substring(1);
            desc=pcursor.getString(pcursor.getColumnIndex("Product_desc"));
            catid=pcursor.getInt(pcursor.getColumnIndex("Product_categoryid"));
            supid=pcursor.getInt(pcursor.getColumnIndex("Product_supplierid"));
            price=pcursor.getFloat(pcursor.getColumnIndex("Product_price"));
            stock=pcursor.getInt(pcursor.getColumnIndex("Stock_Qty"));
            qtyRequired=pcursor.getInt(pcursor.getColumnIndex("Stock_required"));
            Product p= new Product(pid,barcode,formatedName,desc,catid,supid,price,stock,qtyRequired);//Create a new product
            products.add(p);//Add product to the arraylist
            pcursor.moveToNext();//move to the next row
        }
    }

    //Custom Adapter class to display the products
    public class MyCustomAdapter extends ArrayAdapter<Product> {


        public MyCustomAdapter(Context context, int rowLayoutId, ArrayList<Product> products) {

            super(context, rowLayoutId, products);
        }
        @Override
        public View getView(int position, View convertView,
                            ViewGroup parent)
        {
            View row;
            TextView txtID,txtName,txtStock;

            LayoutInflater inflater=getLayoutInflater();
            //Using custom row.xml file
            row=inflater.inflate(R.layout.product_row, parent, false);
            //Then, format the parts of your row layout that need formatting; Note – to connect to a widget ID in your row layout, use row.findViewByID

            //Add the data to the textviews  we want to show in the listview about the products
            txtID= row.findViewById(R.id.txtPID);
            txtID.setText(String.valueOf(products.get(position).getId()));

            txtName= row.findViewById(R.id.txtPname);
            txtName.setText(products.get(position).getName());

            txtStock= row.findViewById(R.id.txtPstock);
            txtStock.setText(String.valueOf(products.get(position).getStock()));

            return row;
        }


    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        //Get data from the products arraylist thata contains the products objects
        Integer pid = products.get(position).getId();
        String pbarcode = products.get(position).getBarcode();
        String pname = products.get(position).getName();
        String pdesc = products.get(position).getDescription();
        Integer pcatid = products.get(position).getCategoryID();
        Integer psuppid = products.get(position).getSupplierID();
        Float pPrice= products.get(position).getPrice();
        Integer pstock = products.get(position).getStock();
        Integer pqtyrequired = products.get(position).getQtyRequired();

        //Create and intent and attach all  details about a product that will be use in the ProductDetails activity
        Intent displayDetails= new Intent(getApplicationContext(),ProductDetails.class);
        displayDetails.putExtra("ID",pid);
        displayDetails.putExtra("Barcode",pbarcode);
        displayDetails.putExtra("Name",pname);
        displayDetails.putExtra("Desc",pdesc);
        displayDetails.putExtra("Catid",pcatid);
        displayDetails.putExtra("Suppid",psuppid);
        displayDetails.putExtra("Price",pPrice);
        displayDetails.putExtra("InStock",pstock);
        displayDetails.putExtra("QtyRequired",pqtyrequired);
        startActivity(displayDetails);//starts next activity


    }
    /*I came up with this method which is an alternative to refresh the values of the database into the list every time the activity is resumed*/
    @Override
    public void onResume() {
        super.onResume();
        products.clear();
        populateProducts();
        MyCustomAdapter myAdapter=new MyCustomAdapter(getApplicationContext(),R.layout.product_row,products);
        setListAdapter(myAdapter);
    }
}
