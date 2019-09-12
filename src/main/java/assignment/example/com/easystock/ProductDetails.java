package assignment.example.com.easystock;
/**
 * Created by Víctor Gonzalez.
 * This class displays the data stored about a selected product.
 */
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class ProductDetails extends AppCompatActivity {
    String name,desc,barcode;
    Integer pid,stock,catid,supid,qtyRequired;
    Float price;
    TextView tstock,tsrequired;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        //Connection to database
        db= new Database(getApplicationContext());
        db.open();

        TextView tid = (TextView) findViewById(R.id.txtID);
        TextView txtName = (TextView) findViewById(R.id.txtName);
        TextView tbarcode = (TextView) findViewById(R.id.txtBarcode);
        TextView tdesc = (TextView) findViewById(R.id.txtDesc);
        TextView tprice = (TextView) findViewById(R.id.txtPrice);
        TextView tcategory = (TextView) findViewById(R.id.txtCategory);
        TextView tsupplier = (TextView) findViewById(R.id.txtSupplier);
        tstock=(TextView)findViewById(R.id.txtStock);
        tsrequired=(TextView)findViewById(R.id.txtQtyRequired) ;
        final SeekBar tsbstcok=(SeekBar) findViewById(R.id.sbStock);

        //Intent to retreive data from activity
        Intent intent = getIntent();
        pid = intent.getIntExtra("ID",0);
        barcode = intent.getStringExtra("Barcode");
        name = intent.getStringExtra("Name");
        desc = intent.getStringExtra("Desc");
        catid = intent.getIntExtra("Catid",0);
        supid= intent.getIntExtra("Suppid",0);
        price = intent.getFloatExtra("Price",0);
        qtyRequired = intent.getIntExtra("QtyRequired",0);
        stock = intent.getIntExtra("InStock",0);

        //Set info on each textview
        tid.setText(pid.toString());
        txtName.setText(name);
        tprice.setText("€ "+price);
        tdesc.setText(desc);
        //If there is no barcode for an item provide a message
        if(barcode.matches("")){
            tbarcode.setText("Not provided");
        }else {
            tbarcode.setText(barcode);
        }
        tsrequired.setText(qtyRequired.toString());
        tstock.setText("" +stock);
        //Slider to set quantity of stock
        tsbstcok.setMax(qtyRequired);
        tsbstcok.setProgress(stock);

        //Get the name of the category and supplier
        String cname=db.getCategoryName(catid);
        tcategory.setText(cname);

        String sname=db.getSupplierName(supid);
        tsupplier.setText(sname);

        //Adding adapater for spinner
        tsbstcok.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                stock=progress;
                tstock.setText("" +stock);//Update the textview as the spinner is move
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            //When the user releases the spinnner
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Get the current value of stock
                int newStock= Integer.parseInt(tstock.getText().toString());
                db.updateStock(pid,newStock);//Update the value of the product stock
                Toast.makeText(getApplicationContext(),"Stock quantity updated", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Add options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater findMenuItems = getMenuInflater();
        findMenuItems.inflate(R.menu.menu_manager, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Delete menu option is selected.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_product:
                showDialog(this,"Remove product","Permanently remove product");//Call to the Dialog function

                break;

        }
        return super.onOptionsItemSelected(item);
    }
    /*Reference: This code is from  https://developer.android.com/guide/topics/ui/dialogs.html*/
    public void showDialog(Activity activity, String title, CharSequence message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);//Create alert Dialog pop out screen

        if (title != null) builder.setTitle(title);

        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                db.deleteProduct(pid);//Call to the database function to delete the selected product
                finish();

            }
        });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();//Cancel dialog screen
                    }
                });
        builder.show();
    }
}
