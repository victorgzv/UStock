package assignment.example.com.easystock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Víctor on 20/11/2017.
 */

public class Database {


    // database column NAMES e.g. rowID
    private static final String KEY_PRODUCTID 	    = "_id";
    private static final String KEY_PRODUCTBARCODE ="Product_barcode";
    private static final String  KEY_PRODUCTNAME= "Product_name";
    private static final String  KEY_PRODUCTDESC= "Product_desc";
    private static final String  KEY_PRODUCTCAT= "Product_categoryid";
    private static final String  KEY_PRODUCTSUPPLIER= "Product_supplierid";
    private static final String  KEY_PRODUCTPRICE= "Product_price";
    private static final String  KEY_PRODUCTQTY= "Stock_Qty";
    private static final String  KEY_PRODUCTQTYREQUIRED= "Stock_required";

    private static final String KEY_CATEGORYID="_id";
    private static final String KEY_CATEGORYNAME="Category_name";

    private static final String KEY_SUPPLIERID="_id";
    private static final String KEY_SUPPLIERNAME="Supplier_name";
    private static final String KEY_SUPPLIERADDRESS="Supplier_address";
    private static final String KEY_SUPPLIEREMAIL="Supplier_email";

    private static final String  KEY_STATUS= "Status";

    private static final String PRODUCT_TABLE 	= "PRODUCT";
    private static final String CATEGORY_TABLE 	= "CATEGORY";
    private static final String SUPPLIER_TABLE 	= "SUPPLIER";
    private static final String DATABASE_NAME 	= "STOCK";
    private static final int DATABASE_VERSION 	= 1; // since it is the first version of the dB


    // SQL statement to create the database
    private static final String CATEGORY_CREATE =
            "CREATE TABLE CATEGORY (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "Category_name TEXT)";

    private static final String SUPPLIER_CREATE =
            "CREATE TABLE SUPPLIER (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "Supplier_name TEXT ," +
                    "Supplier_address TEXT ," +
                    "Supplier_email TEXT)";

    private static final String PRODUCTS_CREATE =
            "CREATE TABLE PRODUCT (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "Product_barcode TEXT ," +
                    "Product_name TEXT ," +
                    "Product_desc TEXT ," +
                    "Product_categoryid INTEGER ," +
                    "Product_supplierid INTEGER ," +
                    "Product_price REAL ," +
                    "Stock_Qty INTEGER ," +
                    "Stock_required INTEGER ," +
                    "FOREIGN KEY(Product_categoryid) REFERENCES CATEGORY (_id),"+
                    "FOREIGN KEY(Product_supplierid) REFERENCES SUPPLIER (_id))";

    private static final String CATEGORY_INSERTS =
    "INSERT INTO CATEGORY (CATEGORY_NAME)VALUES ('Coffee')";


    private static final String SUPPLIER_INSERTS =
            "INSERT INTO SUPPLIER (Supplier_name,Supplier_address,Supplier_email) VALUES ('Pallas Ltd','Tallaght','pallas@gmail.com')";


    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    // Constructor
    public Database(Context ctx)
    {
        //
        this.context 	= ctx;
        DBHelper 		= new DatabaseHelper(context);
    }

    public Database open() throws SQLException
    {
        db     = DBHelper.getWritableDatabase();
        return this;

    }

    // nested dB helper class
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        //
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        //
        public void onCreate(SQLiteDatabase db)
        {

            // Execute SQL to create your tables (call the execSQL method of the SQLLiteDatabase class, passing in your create table(s) SQL)
            db.execSQL(CATEGORY_CREATE);
            db.execSQL(SUPPLIER_CREATE);
            db.execSQL(PRODUCTS_CREATE);
            db.execSQL(CATEGORY_INSERTS);
            db.execSQL(SUPPLIER_INSERTS);
        }

        @Override
        //
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion)
        {
            // dB structure change..

        }
    }   // end nested class



    // remainder of the Database Example methods to "use" the database
    public void close()

    {
        DBHelper.close();
    }

    // Insert method to create store new product rows into the database
    public long insertProduct(String pBarcode,String pName, String pDescription, Integer pCategory,Integer pSupplier,Float pPrice,Integer pStock, Integer pStockRequired)
    {
        ContentValues initialValues = new ContentValues();
        // put your own column/ values onto the Context Values object
        initialValues.put(KEY_PRODUCTBARCODE, pBarcode);
        initialValues.put(KEY_PRODUCTNAME, pName);
        initialValues.put(KEY_PRODUCTDESC, pDescription);
        initialValues.put(KEY_PRODUCTCAT, pCategory);
        initialValues.put(KEY_PRODUCTSUPPLIER, pSupplier);
        initialValues.put(KEY_PRODUCTPRICE, pPrice);
        initialValues.put(KEY_PRODUCTQTY, pStock);
        initialValues.put(KEY_PRODUCTQTYREQUIRED, pStockRequired);


        return db.insert(PRODUCT_TABLE, null, initialValues);
    }

    //Function to inset a ne category. It only takes the name because the id is auto increment.
    public long insertCategory(String cName)
    {
        ContentValues initialValues = new ContentValues();
        // put your own column/ values onto the Context Values object
        initialValues.put(KEY_CATEGORYNAME, cName);
        return db.insert(CATEGORY_TABLE, null, initialValues);
    }
    //Function to insert new suppliers.It takes the name, address and email of the supplier wish to be add.
    public long insertSupplier(String sName,String sAddress,String sEmail)
    {
        ContentValues initialValues = new ContentValues();
        // put your own column/ values onto the Context Values object
        initialValues.put(KEY_SUPPLIERNAME, sName);
        initialValues.put(KEY_SUPPLIERADDRESS, sAddress);
        initialValues.put(KEY_SUPPLIEREMAIL, sEmail);
        return db.insert(SUPPLIER_TABLE, null, initialValues);
    }
    //This method return a cursor with all the rows of the table products.
    public Cursor getProducts() throws SQLException
    {
        // The query method from SQLLiteDatabase class has various parameters that define the query: the database table, the string of columns names to be returned and
        // the last set of parameters allow you to specify "where" conditions for the query.  In this case, there is just one "where" clause. The others are unused.

        Cursor mCursor =   db.query(true, PRODUCT_TABLE, new String[]
                        {
                                // this String array is the 2nd paramter to the query method - and is the list of columns you want to return
                                KEY_PRODUCTID,
                                KEY_PRODUCTBARCODE,
                                KEY_PRODUCTNAME,
                                KEY_PRODUCTDESC,
                                KEY_PRODUCTCAT,
                                KEY_PRODUCTSUPPLIER,
                                KEY_PRODUCTPRICE,
                                KEY_PRODUCTQTY,
                                KEY_PRODUCTQTYREQUIRED

                        },
                null,  null, null, null, null, null);

        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    //This function takes the category id and returns a cursor that contains all the products of certain category.
    public Cursor getProductsByCategory(Integer cid) throws SQLException
    {
        // The query method from SQLLiteDatabase class has various parameters that define the query: the database table, the string of columns names to be returned and
        // the last set of parameters allow you to specify "where" conditions for the query.  In this case, there is just one "where" clause. The others are unused.

        Cursor mCursor =   db.query(true, PRODUCT_TABLE, new String[]
                        {
                                // this String array is the 2nd paramter to the query method - and is the list of columns you want to return
                                KEY_PRODUCTID,
                                KEY_PRODUCTBARCODE,
                                KEY_PRODUCTNAME,
                                KEY_PRODUCTDESC,
                                KEY_PRODUCTCAT,
                                KEY_PRODUCTSUPPLIER,
                                KEY_PRODUCTPRICE,
                                KEY_PRODUCTQTY,
                                KEY_PRODUCTQTYREQUIRED

                        },
                KEY_PRODUCTCAT +" ='"+ cid+"'",  null, null, null, null, null);

        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    //This functin takes the barcode of a product and return a cursor containing all details about it.
    public Cursor getProductByBarcode(String barcode) throws SQLException
    {
        // The query method from SQLLiteDatabase class has various parameters that define the query: the database table, the string of columns names to be returned and
        // the last set of parameters allow you to specify "where" conditions for the query.  In this case, there is just one "where" clause. The others are unused.

        Cursor mCursor =   db.query(true, PRODUCT_TABLE, new String[]
                        {
                                // this String array is the 2nd paramter to the query method - and is the list of columns you want to return
                                KEY_PRODUCTID,
                                KEY_PRODUCTBARCODE,
                                KEY_PRODUCTNAME,
                                KEY_PRODUCTDESC,
                                KEY_PRODUCTCAT,
                                KEY_PRODUCTSUPPLIER,
                                KEY_PRODUCTPRICE,
                                KEY_PRODUCTQTY,
                                KEY_PRODUCTQTYREQUIRED

                        },
                KEY_PRODUCTBARCODE +" ='"+ barcode+"'",  null, null, null, null, null);

        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    //Update the stock a product.It takes its id and thew new stock quantity.
    public int updateStock(Integer rowId, Integer newStock)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_PRODUCTQTY, newStock);
        return db.update(PRODUCT_TABLE, args,
                KEY_PRODUCTID + "=" + rowId, null) ;
    }

    //Function that deletes a product given its id
    public boolean deleteProduct(Integer rowId)
    {
    // delete statement. If any rows deleted (i.e. >0), returns true
        return db.delete(PRODUCT_TABLE, KEY_PRODUCTID +
                "=" + rowId, null) > 0;
    }

    //Function that return a cursor with all categories
    public Cursor getCategories() throws SQLException
    {
        // The query method from SQLLiteDatabase class has various parameters that define the query: the database table, the string of columns names to be returned and
        // the last set of parameters allow you to specify "where" conditions for the query.  In this case, there is just one "where" clause. The others are unused.

        Cursor mCursor =   db.query(true, CATEGORY_TABLE, new String[]
                        {
                                // this String array is the 2nd paramter to the query method - and is the list of columns you want to return
                                KEY_CATEGORYID,
                                KEY_CATEGORYNAME
                        },
                null,  null, null, null, null, null);

        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    //This function returns a string value tha contains the name of the category selected. It used for the categories spinner.
    public String getCategoryName(Integer cid) throws SQLException
    {
        // The query method from SQLLiteDatabase class has various parameters that define the query: the database table, the string of columns names to be returned and
        // the last set of parameters allow you to specify "where" conditions for the query.  In this case, there is just one "where" clause. The others are unused.
        String cname="";
        Cursor mCursor =   db.query(true, CATEGORY_TABLE, new String[]
                        {
                                // this String array is the 2nd paramter to the query method - and is the list of columns you want to return
                                KEY_CATEGORYNAME
                        },
                KEY_CATEGORYID+" ='"+ cid+"'",  null, null, null, null, null);

        if (mCursor != null)
        {
            mCursor.moveToFirst();
            cname= mCursor.getString(0);

        }
        return cname;

    }
    //This function returns a string value tha contains the name of the supplier selected. It used for the supplier spinner.
    public String getSupplierName(Integer sid) throws SQLException
    {
        // The query method from SQLLiteDatabase class has various parameters that define the query: the database table, the string of columns names to be returned and
        // the last set of parameters allow you to specify "where" conditions for the query.  In this case, there is just one "where" clause. The others are unused.
        String sname="";
        Cursor mCursor =   db.query(true, SUPPLIER_TABLE, new String[]
                        {
                                // this String array is the 2nd paramter to the query method - and is the list of columns you want to return
                                KEY_SUPPLIERNAME
                        },
                KEY_SUPPLIERID+" ='"+ sid+"'",  null, null, null, null, null);

        if (mCursor != null)
        {
            mCursor.moveToFirst();
            sname= mCursor.getString(0);

        }
        return sname;

    }
    //Function that return a cursor with all the rows of suppliers
    public Cursor getSuppliers() throws SQLException
    {
        // The query method from SQLLiteDatabase class has various parameters that define the query: the database table, the string of columns names to be returned and
        // the last set of parameters allow you to specify "where" conditions for the query.  In this case, there is just one "where" clause. The others are unused.

        Cursor mCursor =   db.query(true, SUPPLIER_TABLE, new String[]
                        {
                                // this String array is the 2nd paramter to the query method - and is the list of columns you want to return
                                KEY_SUPPLIERID,
                                KEY_SUPPLIERNAME,
                                KEY_SUPPLIERADDRESS,
                                KEY_SUPPLIEREMAIL
                        },
                null,  null, null, null, null, null);

        if (mCursor != null)
        {

            mCursor.moveToFirst();
        }
        return mCursor;

    }

    //This function returns all categories in an List. It used to populate the category spinner.
    public List<String> getAllCategories(){
        List<String> categories = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM CATEGORY";
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                categories.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();


        // returning lables
        return categories;
    }
    //Returns the id of a category by passing its name.
    public Integer getCategoryId(String cname){
        Cursor mCursor =   db.query(true, CATEGORY_TABLE, new String[]
                        {
                                // this String array is the 2nd paramter to the query method - and is the list of columns you want to return
                                KEY_CATEGORYID,
                                KEY_CATEGORYNAME
                        },
                KEY_CATEGORYNAME +" ='"+ cname+"'",  null, null, null, null, null);
        mCursor.moveToFirst();
        String id=mCursor.getString(0);
        return Integer.parseInt(id);
    }
    //This function returns all suppliers in an List. It used to populate the supplier spinner.
    public List<String> getAllSuppliers(){
        List<String> suppliers = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM SUPPLIER";
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                suppliers.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();


        // returning lables
        return suppliers;
    }
    //Returns the id of a supplier y by passing its name.
    public Integer getSupplierId(String sname){
        Cursor mCursor =   db.query(true, SUPPLIER_TABLE, new String[]
                        {
                                // this String array is the 2nd paramter to the query method - and is the list of columns you want to return
                                KEY_SUPPLIERID,
                                KEY_SUPPLIERNAME
                        },
                KEY_SUPPLIERNAME +" ='"+ sname+"'",  null, null, null, null, null);
        mCursor.moveToFirst();
        String id=mCursor.getString(0);
        return Integer.parseInt(id);
    }
}
