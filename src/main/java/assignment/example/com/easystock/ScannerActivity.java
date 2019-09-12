package assignment.example.com.easystock;

/**
 * Created by Víctor Gonzalez.
 * This class uses the sanner Zxing library
 * * Reference code of the barcode scanner: https://www.youtube.com/watch?v=Fe7F4Jx7rwo
 */
import android.content.Intent;
import android.database.Cursor;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;


import com.google.zxing.Result;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    private static final int MY_PERMISSIONS_REQUEST_READ_CAMERA = 0;
    private ZXingScannerView zXingScannerView;
    private String name,desc,barcode;
    private Integer pid,stock,catid,supid,qtyRequired;
    private Float price;
      Database db;
    ArrayList<Product> products = new ArrayList<Product>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        openDB();
        start();
    }
    public void openDB(){
        db = new Database(this);
        db.open();


    }

    public void start(){
        zXingScannerView =new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);

        zXingScannerView.startCamera();

        //Event Listener like onClick but triggered when a view is touched
        zXingScannerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!zXingScannerView.getFlash()) {//If the flash is off
                    zXingScannerView.setFlash(true);//turn on flash
                }else{
                    zXingScannerView.setFlash(false);//turn off flash
                }
                return false;
            }

        });
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        zXingScannerView.startCamera();


    }
    protected void onResume() {
        super.onResume();
        products.clear();


    }

    @Override
    public void handleResult(Result result) {

        if(!result.getText().matches("")){
            String pBarcode;
            pBarcode=result.getText();
            zXingScannerView.stopCamera();

            //Class to make the app vibrate when a barcode is detected
            Vibrator vibe = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
            vibe.vibrate(500);
            populateProducts(pBarcode);
            sendToProductDetails();
            finish();

        }
        zXingScannerView.resumeCameraPreview(this);


    }
    public void populateProducts(String b){
        Cursor pcursor;
        pcursor=  db.getProductByBarcode(b);
        pcursor.moveToFirst();

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


    }

    /*Once a barcode is scanned this method is used to get the details about a product and by
     using intent will send the user to the display activity*/
    public void sendToProductDetails(){

        Integer pid = products.get(0).getId();
        String pbarcode = products.get(0).getBarcode();
        String pname = products.get(0).getName();
        String pdesc = products.get(0).getDescription();
        Integer pcatid = products.get(0).getCategoryID();
        Integer psuppid = products.get(0).getSupplierID();
        Float pPrice= products.get(0).getPrice();
        Integer pstock = products.get(0).getStock();
        Integer pqtyrequired = products.get(0).getQtyRequired();

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
        startActivity(displayDetails);
    }
}