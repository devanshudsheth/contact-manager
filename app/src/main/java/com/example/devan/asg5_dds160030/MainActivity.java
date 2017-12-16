
/*****************************************************
 This is a contact manager application for android.
 It stores the first name, last name, phone number, email and birthdate

 BY DEVANSHU D. SHETH
 dds160030
 CS6326.001

It can add a new contact from the add button, update a contact or delete a contact.
 This is the main activity where all the contacts are visible as a list in ascending order of first name.

 ******************************************************/
package com.example.devan.asg5_dds160030;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    //list of custom MyObjects
    List<MyObject> myObjects = new ArrayList<>();

    //keys for intents - 1 = save, 2 = update, 3 = delete
    int SAVE_INTENT = 1, UPDATE_INTENT = 2, DELETE_INTENT = 3;

    //our ListView
    ListView contactListView;

    //the Save button
    Button btnSave;

    //MyObject record
    MyObject readRecord = new MyObject();

    //make separate folder on the internal storage (database)
    public String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/database";

    //this is the File path - it is in the database folder
    File file = new File(path + "/CS6326Asg5.txt");


    public MainActivity() throws FileNotFoundException {
    }

    //OnCreate method, this is where we define the elements like buttons and ListView
    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //instantiate the ListView
        contactListView = (ListView) findViewById(R.id.listNames);

        //Instantiate the Save button
        btnSave = (Button) findViewById(R.id.btnSave);

        //Directory Path
        File dir = new File(path);

        //if directory does not exist, create
        if(!dir.exists())
        {
            dir.mkdirs();
        }
        //otherwise read from the database text file
        else
        {
            //our method where we read the previous entries on start
            //parameter file
            readfromfile(file);
        }
        //if ListView item is clicked we set a Listener. If it is clicked, transfer the Second activity (Main2Activity)
        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long id)
            {
                //initiate Intent on pressing an item in ListView
                //this is will move to the Update/Delete page
                //we pass the details of the MyObject record from our myobjects list
                //It sends all parameters of the index "position"
                //Extra data is passed as key to show it is for UPDATE/DELETE
                Intent viewIntent = new Intent(MainActivity.this, Main2Activity.class);
                viewIntent.putExtra("FirstName", myObjects.get(position).FirstName);
                viewIntent.putExtra("LastName", myObjects.get(position).LastName);
                viewIntent.putExtra("Phone", myObjects.get(position).Phone);
                viewIntent.putExtra("Email", myObjects.get(position).Email);
                viewIntent.putExtra("Bdate", myObjects.get(position).BDate);

                //we pass key to determine when it comes back if it was update/delete
                viewIntent.putExtra("key", Integer.toString(UPDATE_INTENT));

                //we use this to determine the position for when it comes back
                viewIntent.putExtra("position", Integer.toString(position));
                //start activity with intention to get result with key UPDATE_INTENT - used both for update as well as delete
                startActivityForResult(viewIntent, UPDATE_INTENT);
            }
        });
    }


    //used to send Intent incase Add button is pressed
    //extra data key is passed to signify it is for SAVE
    public void sendMessage()
    {
        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
        intent.putExtra("key", Integer.toString(SAVE_INTENT));
        startActivityForResult(intent,SAVE_INTENT);
    }

    //when we get a Result this method is invoked
    //it gives us the returning intent, the request code - RESULT_OK or RESUlT_CANCELED in our case
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //if result code is RESULT_OK, it is one of the three cases - SAVE, UPDATE, DELETE
        if(resultCode == RESULT_OK)

        {
            //if requestCode was SAVE_INTENT, the returning intent is sending back data to add to the myobjects
            //it will get the strings for first and last name, phone etc.
            if (requestCode == SAVE_INTENT  )
            {
                String FirstName = data.getStringExtra("FirstName");
                String LastName = data.getStringExtra("LastName");
                String Phone = data.getStringExtra("Phone");
                String Email = data.getStringExtra("Email");
                String BDate = data.getStringExtra("BDate");

                //set them in MyObject
                MyObject currObject = new MyObject();
                currObject.FirstName = (FirstName);
                currObject.LastName = (LastName);
                currObject.Phone = (Phone);
                currObject.Email = (Email);
                currObject.BDate = (BDate);

                //Add to myobjects, MyObject currObject
                myObjects.add(currObject);

            }
            else
            {
                //in case of UPDATE/DELETE since we sent only one intent, we must differentiate based on key
                int key = Integer.valueOf(data.getStringExtra("key"));

                //if the key was UPDATE_INTENT, update
                if (requestCode == UPDATE_INTENT && key == UPDATE_INTENT)
                    {
                    //in case of update, we just update values in myobjects
                    String FirstName = data.getStringExtra("FirstName");
                    String LastName = data.getStringExtra("LastName");
                    String Phone = data.getStringExtra("Phone");
                    String Email = data.getStringExtra("Email");
                    String BDate = data.getStringExtra("BDate");

                    //get position we sent with our initial intent so we know which position in myobjects to update
                    int position = Integer.valueOf(data.getStringExtra("position"));

                    //get myobjects index i, and change the values with values got from return intent
                    MyObject currObject = myObjects.get(position);
                    currObject.FirstName = (FirstName);
                    currObject.LastName = (LastName);
                    currObject.Phone = (Phone);
                    currObject.Email = (Email);
                    currObject.BDate = (BDate);

                    }
                    //if key was DELETE_INTENT then we delete
                if (requestCode == UPDATE_INTENT && key == DELETE_INTENT)
                    {
                        //get the position to delete from our initial intent
                    int position = Integer.valueOf(data.getStringExtra("position"));
                   //remove MyObject at that position
                    myObjects.remove(position);

                    }

                }
            //call populateListView custom function
            populateListView();


            //call saveTofile
            //parameter myobjects
            //checks IO Exception
            try {
                saveTofile(myObjects);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }//onActivityResult

    //saveTofile method
    //throws IO Exception
    //used to write to the text file
     public void saveTofile(List<MyObject> myObjects) throws IOException {

        //initialize fos as our FileWriter
        FileWriter fw = null;

        //try with catch block for FileNotFoundException
         //pass the absolute path of our database file
        try {
            fw = new FileWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //prints line by line all MyObject in myobjects
        try {
            try {
                for (int i = 0; i < myObjects.size(); i++)
                {
                    fw.write(myObjects.get(i).toString() );

                }

            }
         catch (IOException e) {
            e.printStackTrace();
        }

    }
    finally
    {   //close the FileWriter
        try
        {
            fw.close();
        }
        catch(IOException e )
        {
            e.printStackTrace();
        }

    }
    }

    //readfromfile method which reads the file line by line
    //takes input file
    public void readfromfile(File file)
    {
        //initiate FileInputStream
        FileInputStream fis = null;

        //try with catch block checks FileNotFoundException
        try
        {
            fis = new FileInputStream(file);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        //Initiate new Input StreamReader
        InputStreamReader isr = new InputStreamReader(fis);

        //Initiate new BufferedReader
        BufferedReader br = new BufferedReader(isr);

        //String line which is a temp variable for lines
        String line;
        try
        {
            //while there are more lines, read line
            while ((line=br.readLine()) != null)
            {
                //parses the line line
                //returns string array
                //parameters 1. delimiter 2. for passing null values as well we put negative integer
                String[] data = line.split("\t", -1);

                //read into readRecord which is a MyObject
                readRecord = new MyObject();
                readRecord.FirstName = data[0] ;
                readRecord.LastName = data[1];
                readRecord.Phone = data[2];
                readRecord.Email =data[3];
                readRecord.BDate = data[4];

                //add to myobjects
                myObjects.add(readRecord);

                //populate ListView with the MyObject (s) in myobjects list
                populateListView();

            }
        }
        catch (IOException e) {e.printStackTrace();}
        //gets current channel
        try
        {
            fis.getChannel().position(0);
        }
        catch (IOException e) {e.printStackTrace();}



    }

    //populateListView method where we set the adapter to display the ListView
    //We also use the sort method of the adapter to sort according to ascending a-z order by First Name

    //Create custom Comparator for MyObject
    //it is done by overwriting the Comparator class' compare method.
    //compare the first names of the two MyObjects
    public void populateListView()
    {
        ArrayAdapter<MyObject> adapter = new ContactListAdapter();

        adapter.sort(new Comparator<MyObject>()
        {
            @Override
            public int compare(MyObject lhs, MyObject rhs) {
                return (lhs.getFirstName()).compareTo(rhs.getFirstName());
            }
        });

        //sets adapter to ListView
        contactListView.setAdapter(adapter);

    }

    //our custom adapter created to show the ListView
    //the layout is developed in listview_item.xml

    public class ContactListAdapter extends ArrayAdapter<MyObject>
    {
        //constructor for the adapter
        public ContactListAdapter()
        {
            super(MainActivity.this,R.layout.listview_item, myObjects);
        }

        //getItem method is overriden
        public MyObject getItem(int position){
            return myObjects.get(position);
        }
        //Override the getView method.
        //needs to be done to create custom adapter
        @Override
        public View getView(int position, View view, ViewGroup parent)
        {
            //if the view == null
            if(view == null)
            {
                view = getLayoutInflater().inflate(R.layout.listview_item, parent, false);
            }

            //define currentObject as MyObject with index position
            MyObject currentObject = myObjects.get(position);

            //put first TextView for the name
            //gets first name and last name and sets the text
            TextView name = (TextView)view.findViewById(R.id.textName);
            name.setText(currentObject.getFirstName() + " " + currentObject.getLastName());

            //put second TextView for the phone
            //gets phone number and sets the text
            TextView phone = (TextView)view.findViewById(R.id.textPhone);
            phone.setText(currentObject.getPhone());

            return view;
        }
    }
    //Override menu to show button on action bar
    //set to custom menu menu_main
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds item button to the action bar .
        //layout is in menu_main
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //if item on Menu is selected i.e. the button on Action bar, it is triggered
// Handle action bar item clicks here.
    public boolean onOptionsItemSelected(MenuItem item) {
       //if item clicked was btnCreate, sendMessage which is for creating a new contact
        switch (item.getItemId())
        {
            case R.id.btnCreate:
                sendMessage();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
