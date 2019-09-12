package assignment.example.com.easystock;
/**
 * Created by Víctor Gonzalez.
 * This class is teh Login Activity. It presents teh user with the app logo and a login screen
 * where the user can be staff or manager. By selecting from a spinner and entering the correct
 * password we get to the next activity. This class does not use any database,however it use 2
 * object of the user class.
 */
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;


public class LoginActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_CAMERA = 0;
    private EditText user, password;
    private Button login;
    private Spinner spUser;
    User floor_staff,manager;
    String selectedUser;
    ArrayList<User> users= new ArrayList<User>();//It stores teh users
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user= (EditText) findViewById(R.id.etName);
        password= (EditText) findViewById(R.id.etPassword);
        login = (Button) findViewById(R.id.btnLogin);
        spUser=(Spinner)findViewById(R.id.spUser);
        //Create two types of users
        floor_staff = new User("Staff","1234");
        manager = new User("Manager","12345");
        users.add(floor_staff);//Add user to the arraylist
        users.add(manager);//Add user to the arraylist
        loadUsers();//This method populates the spinner with the two users
        //Listener for the spinner
        spUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                 selectedUser = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //On click listener for login button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(selectedUser,password.getText().toString());
            }
        });
        /*Reference. This code is from : https://developer.android.com/training/permissions/requesting.html
        * Requesting permissions at the start of the app. In this case the app will use the camera to scan barcode.*/
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_READ_CAMERA);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }
    //Methods that performs the login operations
    private void login(String userName,String userPassword){
        //If user and password are correct for each type of user a new activity is started
        if((userName.equals(floor_staff.getName())) && (userPassword.equals(floor_staff.getPassword()))){
            Intent staffIntent= new Intent(this,StaffActivity.class);
            startActivity(staffIntent);
        }else if((userName.equals(manager.getName())) && (userPassword.equals(manager.getPassword()))){
            Intent managerIntent= new Intent(this,ManagerActivity.class);
            startActivity(managerIntent);
        }else{
            Toast.makeText(this,"Invalid username or password",Toast.LENGTH_SHORT).show();
        }
    }

    //ArrayAdapter loading users into spinner
    private void loadUsers() {
        ArrayAdapter<User> dataAdapter = new ArrayAdapter<User>(this, android.R.layout.simple_spinner_dropdown_item, users);
        spUser.setAdapter(dataAdapter);
    }
}
