package assignment.example.com.easystock;
/**
 * Created by Víctor Gonzalez.
 * This class handles the user input. It adds a new product into the database.
 * This class implements a barcode scanner library called ZXing which is been added to build.gradle as a dependency.
 * Reference code of the barcode scanner: https://www.youtube.com/watch?v=Fe7F4Jx7rwo
 */
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.zxing.Result;
import java.util.List;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class AddProduct extends AppCompatActivity implements ZXingScannerView.ResultHandler {
EditText pName,etBarcode,pDesc,pPrice,pQtyRequired;
Spinner spCategory,spSupplier;
Integer cat_id,sup_id;

Button addNew,btnScan;
Database db = new Database(this);//Initaliase a db instance of Database class

private ZXingScannerView zXingScannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);//set layout
        //Variables that store a reference to the views in the layout
        pName=(EditText) findViewById(R.id.etName);
        pDesc=(EditText) findViewById(R.id.etDesc);
        etBarcode=(EditText) findViewById(R.id.etBarcode);
        pPrice=(EditText) findViewById(R.id.etPrice);
        pQtyRequired=(EditText) findViewById(R.id.etQtyRequired);
        addNew=(Button) findViewById(R.id.btnAddNewP);
        btnScan=(Button) findViewById(R.id.btnScan);
        spCategory=(Spinner) findViewById(R.id.spCategory);
        spSupplier=(Spinner) findViewById(R.id.spSupplier);
        db.open();//Read data from database

        Intent intent= getIntent();  //Intent to retrieve the data sent from one activity to another
        String bc=intent.getStringExtra("barcode");//Get the data sent
        etBarcode.setText(bc);//Set the barcode field with the barcode number after scanning a product

        //Set spinner listener
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //Override method from Adapter view class to handle the click event of a spinner
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // On selecting a spinner item
                String label = parentView.getItemAtPosition(position).toString();//Get position of the item in the spinner
                cat_id= db.getCategoryId(label);//Database function that returns the id of the selected item
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        //Set spinner listener
        spSupplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //Override method from Adapter view class to handle the click event of a spinner
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                String label = parent.getItemAtPosition(position).toString();//Get position of the item in the spinner
                sup_id= db.getSupplierId(label);//Database function that returns the id of the selected item
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        loadCategorySpinnerData();//Call to method that loads the categories into the spinner
        loadSupplierSpinnerData();//Call to method that loads the suppliers into the spinner

        //Add listener to add button
        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pBarcodeAdd="";
                //Get all text from the fields before doing the insert
                String pNameAdd=  pName.getText().toString();
                pBarcodeAdd= etBarcode.getText().toString();
                String pDescAdd=pDesc.getText().toString();
                Float pPriceAdd=  Float.valueOf(pPrice.getText().toString());//Parse from string to float
                Integer pQtyReqAdd  =Integer.parseInt(pQtyRequired.getText().toString());//Parse from string to integer

                //If the following fields are empty it shows the appropriate message
                if(pNameAdd.matches("") ||pDescAdd.matches("")
                        || pQtyReqAdd.toString().matches("")) {
                    Toast.makeText(getApplicationContext(),"Complete fields",Toast.LENGTH_SHORT).show();

                }else{
                    //If all the required fields are completed, perform to add a new product into the DB.
                    long insertProduct= db.insertProduct(
                            pBarcodeAdd,
                            pNameAdd,
                            pDescAdd,
                            cat_id,
                            sup_id,
                            pPriceAdd ,
                            0,//Stock set to 0. Initial value.
                            pQtyReqAdd
                            );
                    if(insertProduct>0){
                        Toast.makeText(getApplicationContext(),"New Product Added",Toast.LENGTH_SHORT).show();
                }
                }
            }


        });

        //Add listener to button scan
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan(v);//Call to method scan

            }
        });
    }//end onCreate method
    /* Reference: https://developer.android.com/reference/android/widget/Spinner.html*/
    //Load the categories stored in the database to the spinner
    private void loadCategorySpinnerData() {
        // Spinner Drop down elements are stored in a List
        List<String> categories = db.getAllCategories();//DB  function that returns all categories

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, categories);

        // Drop down layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spCategory.setAdapter(dataAdapter);
    }
    //Load the suppliers stored in the database to the spinner
    private void loadSupplierSpinnerData() {
        // Spinner Drop down elements
        List<String> suppliers = db.getAllSuppliers();//DB  function that returns all suppliers

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, suppliers);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spSupplier.setAdapter(dataAdapter);
    }

    //Reference code of the barcode scanner: https://www.youtube.com/watch?v=Fe7F4Jx7rwo
    public void scan(View view){
        zXingScannerView =new ZXingScannerView(getApplicationContext());//Initialise the class
        setContentView(zXingScannerView);//Starts a new activity to scan a barcode
        zXingScannerView.setResultHandler(this);//se the result handles described at the end of this file.

        zXingScannerView.startCamera();//starts the camera


        /*Event Listener like onClick but triggered when a view is touched
        As this is not part of the ZXing library I used the android documentation in order to use a touch listener
        that enables the flash when the screen is clicked.*/
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


    //If the app was left running on the background while scanning a product. OnRestart method start the camera again and makes sure the scanner works.
    @Override
    protected void onRestart() {
        super.onRestart();
        zXingScannerView.startCamera();
    }


//This method is override from
    @Override
    public void handleResult(Result result) {

        //If the result is not empty
        if(!result.getText().matches("")){
            //Barcode has been scanned
            String pBarcode;
            pBarcode=result.getText();//Store barcode into a string variable
            zXingScannerView.stopCamera();//Stop the camera

            /*Reference : The following code is from https://developer.android.com/reference/android/os/Vibrator.html
            Class to make the app vibrate when a barcode is detected.
            I also needed to add vibration permission in the manifest file.*/
            Vibrator vibe = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
            vibe.vibrate(500);//It vibrates for 500 milliseconds

            //Intent to exit the camera activity and move to the addProduct
            Intent goBack= new Intent(this,AddProduct.class);
            goBack.putExtra("barcode",pBarcode);//Save the barcode and send it through the activity
            startActivity(goBack);
            finish();//finish this activity

        }
        zXingScannerView.resumeCameraPreview(this);


    }


}
