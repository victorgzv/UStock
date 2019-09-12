package assignment.example.com.easystock;
/**
 * Created by Víctor Gonzalez.
 * This class handles the user input. It adds a new category entry to the sqlite DB.
 */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddCategory extends AppCompatActivity {
EditText cat_name;
Button btnAddC;
    Database db = new Database(this);//Initialise an instance of Database class
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        cat_name=findViewById(R.id.etCategoryName);
        btnAddC= (Button) findViewById(R.id.btnAddNewC);
        db.open();//call database function that reads the data from the database

        //On click event fired when the Add button is clicked
        btnAddC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String cname= cat_name.getText().toString();//Get the text from input field
                    //Using the substring method,transform the first letter to uppercase
                    String formatedName = cname.substring(0, 1).toUpperCase() + cname.substring(1);
                    long insertCategory= db.insertCategory(formatedName);//Call db function tha inserts a new category passing the name
                //The function above returns 1 if the a row has been inserted.
                if(insertCategory>0){
                    //Using toast the user is informed that a new category has been added
                    Toast.makeText(getApplicationContext(),"New Category Added",Toast.LENGTH_SHORT).show();
                    //Intent class to move to another activity
                    Intent exit_screen= new Intent(AddCategory.this,tab1Fragment.class);
                    startActivity(exit_screen);
                }
            }
        });

    }
}
