package assignment.example.com.easystock;
/**
 * Created by Víctor Gonzalez.
 * This class handles the user input of three fields. It adds a new supplier  to the sqlite DB.
 */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddSupplier extends AppCompatActivity {
    EditText sname,saddress,semail;
    Button addNewS;
    Database db = new Database(this);//Initialise db instance of Databas class
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_supplier);
        sname=(EditText) findViewById(R.id.etNameS);
        saddress=(EditText) findViewById(R.id.etAddressS);
        semail=(EditText) findViewById(R.id.etEmailS);
        addNewS=findViewById(R.id.btnAddNewS);
        db.open();//Read data from database
        //Add button on click listener
        addNewS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If the fields are empty ask the user to complete them
                if(sname.getText().toString().matches("") ||saddress.getText().toString().matches("") || semail.getText().toString().matches("")){
                    Toast.makeText(getApplicationContext(),"Complete all fields",Toast.LENGTH_SHORT).show();//Show message
                }else{
                    //Insert new supplier into the database
                    long insertS= db.insertSupplier(sname.getText().toString(),saddress.getText().toString(),semail.getText().toString());
                    if(insertS>0){
                        //One it is add successfully we go back to the activity that list all suppliers
                        Intent exit_screen= new Intent(AddSupplier.this,tab3Fragment.class);
                        startActivity(exit_screen);
                        Toast.makeText(getApplicationContext(),"New Supplier Added",Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
    }
}
