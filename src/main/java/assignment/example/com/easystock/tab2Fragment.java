package assignment.example.com.easystock;

import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Víctor on 19/11/2017.
 */

public class tab2Fragment extends ListFragment {

    Database db;
    FloatingActionButton addProduct;
    String name,desc,barcode;
    Integer pid,stock,catid,supid,qtyRequired;
    Float price;

    ArrayList<Product> products = new ArrayList<Product>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.tab2_fragment,container,false);
        addProduct= (FloatingActionButton) view.findViewById(R.id.btnAddProduct);

            openDB();//Read data from db

            populateProducts();//function to query the products table

        //adding listener to button for adding a new product
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent addProductIntent=  new Intent(getContext(),AddProduct.class);
                startActivity(addProductIntent);



            }
        });

        return view;
    }

    //When teh activity is resumed I call the necessary methods to refresh the listview
    @Override
    public void onResume() {
        super.onResume();
        products.clear();//empty the arraylist
        populateProducts();//populate listview
        //Set adapter
        MyCustomAdapter myAdapter=new MyCustomAdapter(getContext(),R.layout.product_row,products);
        setListAdapter(myAdapter);
    }
    //Initialise db
    public void openDB(){
        db = new Database(getContext());
        db.open();

    }


//This methos handles a cursor returned from the db query that select all the columns of table products
    public void populateProducts(){
        Cursor pcursor;
        pcursor=db.getProducts();
        pcursor.moveToFirst();
        while (!pcursor.isAfterLast()) {
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
            Product p= new Product(pid,barcode,formatedName,desc,catid,supid,price,stock,qtyRequired);
            products.add(p);
            pcursor.moveToNext();
        }
    }

/*By clicking an item of the listview we send the data of the selected product to the next activity
    that will process and display its details*/
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Integer pid = products.get(position).getId();
        String pbarcode = products.get(position).getBarcode();
        String pname = products.get(position).getName();
        String pdesc = products.get(position).getDescription();
        Integer pcatid = products.get(position).getCategoryID();
        Integer psuppid = products.get(position).getSupplierID();
        Float pPrice= products.get(position).getPrice();
        Integer pstock = products.get(position).getStock();
        Integer pqtyrequired = products.get(position).getQtyRequired();

        Intent displayDetails= new Intent(getContext(),ProductDetails.class);
        displayDetails.putExtra("ID",pid);
        displayDetails.putExtra("Barcode",pbarcode);
        displayDetails.putExtra("Name",pname);
        displayDetails.putExtra("Desc",pdesc);
        displayDetails.putExtra("Catid",pcatid);
        displayDetails.putExtra("Suppid",psuppid);
        displayDetails.putExtra("Price",pPrice);
        displayDetails.putExtra("InStock",pstock);
        displayDetails.putExtra("QtyRequired",pqtyrequired);
        startActivity(displayDetails);


    }



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
            row=inflater.inflate(R.layout.product_row, parent, false);

            //Place all the values in the textviews of the layout

            txtID= row.findViewById(R.id.txtPID);
            txtID.setText(String.valueOf(products.get(position).getId()));

            txtName= row.findViewById(R.id.txtPname);
            txtName.setText(products.get(position).getName());

            txtStock= row.findViewById(R.id.txtPstock);
            String in_stock=String.valueOf(products.get(position).getStock());
            Integer qtyStock,qtyReq;
            qtyStock=products.get(position).getStock();
            qtyReq=products.get(position).getQtyRequired();
            //If the stock is less than half of the required amount that should be in stock
            if(qtyStock<(qtyReq/2)){
                txtStock.setTextColor(getResources().getColor(R.color.lowStock));//red message display in the listview
            }else if(qtyStock>(qtyReq/2)){
                txtStock.setTextColor(getResources().getColor(R.color.highStock));//green message display in the listview
            }else{
                txtStock.setTextColor(getResources().getColor(R.color.mediumStock));//yellow message display in the listview
            }
            txtStock.setText(in_stock);

            return row;
        }










    }


}

