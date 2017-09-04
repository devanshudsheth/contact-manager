
/*****************************************************
 This is a contact manager application for android.
 It stores the first name, last name, phone number, email and birthdate

 BY DEVANSHU D. SHETH
 dds160030
 CS6326.001

 It can add a new contact from the add button, update a contact or delete a contact.

 This is the second activity where all the fields are present.
 This activity is the used for creating the contact as well as seeing details of stored contacts

 ******************************************************/
package com.example.devan.asg5_dds160030;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class Main2Activity extends AppCompatActivity {

    //define EditText for all the items - 4 TextViews
    EditText fnametxt, lnametxt, phonetxt, emailtxt, bdaytxt;

    //define key and position
    //these are variables which were passed with intent from MainActivity
    int key, position;

    //SAVE_INTENT ,UPDATE_INTENT or DELETE_INTENT keys
    int SAVE_INTENT = 1, UPDATE_INTENT = 2, DELETE_INTENT = 3;

    //OnCreate method for second activity
    //here we define all items in the activity that are created
    //includes buttons, EditTexts
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Buttons save and Delete are defined
        final Button btnSave = (Button) findViewById(R.id.btnSave);
        final Button btnDelete = (Button)findViewById(R.id.btnDelete) ;

        //first name textview is define
        fnametxt = (EditText) findViewById(R.id.txtFname);

        //when initially on opening, length of first name is 0, disable the btnSave
        if(fnametxt.length() == 0)
        {
            btnSave.setEnabled(false);
        }

        //add a textChangedListener for when the text changed in first name
        fnametxt.addTextChangedListener(new TextWatcher() {

            //when the text is changed, do the following
            public void onTextChanged(CharSequence s, int start, int before,int count)
            {
                //if text length goes back down to 0, disable the btnSave again
                if(fnametxt.length() == 0)
                    btnSave.setEnabled(false);
                //if the text length is more than zero, enable
                else
                    btnSave.setEnabled(true);

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });



        //define all the EditTexts for the text fields, last name, phone, email and bdate
        lnametxt = (EditText) findViewById(R.id.txtLname);
        phonetxt = (EditText) findViewById(R.id.txtPhone);
        emailtxt = (EditText) findViewById(R.id.txtEmail);
        bdaytxt = (EditText) findViewById(R.id.dateBday);

        //when there are no extras, that is no data was passed with intent, the delete button is removed.
        //this is so because, this is triggered by the Add button where we will create a new entry
        //this only has Save button
        btnDelete.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        //if extras is not null, that is some data was passed with intent
        //this is triggered on intention on update or delete
        //enable delete button
        //change save button text to show update instead of save
        if (extras != null) {

            //get the data which was passed from MainActivity
            String fname = extras.getString("FirstName");
            String lname = extras.getString("LastName");
            String phone = extras.getString("Phone");
            String email = extras.getString("Email");
            String bday = extras.getString("Bdate");
            //key is used to figure out if INTENT was for update or delete later, here it is just as an extra data
            key = Integer.valueOf(extras.getString("key"));

            // whether update or delete intent, we take the details of the contact like first name, last name etc. and fill in the text fields
            //position is later used to send back to main activity, to tell which position the given Myobject was
            if(key == UPDATE_INTENT || key == DELETE_INTENT)
            {
                position = Integer.valueOf(extras.getString("position"));

                fnametxt.setText(fname);
                lnametxt.setText(lname);
                phonetxt.setText(phone);
                emailtxt.setText(email);
                bdaytxt.setText(bday);

                //change btnSave text to Update
                btnSave.setText("Update");

                //make delete button visible
                btnDelete.setVisibility(View.VISIBLE);

                //set an OnClickListener on btnSave
                btnSave.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        //if key is equal to UPDATE_CONTENT use same method as for addContact
                        if(key == UPDATE_INTENT)
                        {
                            addContact(view);
                        }

                    }
                });
            }
        }




    }

    //addContact method
    //used for adding as well as updating a contact
    //called when onClick event of btnSave is triggered
    public void addContact(View view)
    {

        if(key == SAVE_INTENT )
        {
            //if intent was to save, we will take all text field values and send them back as data with return intent
        String FirstName = fnametxt.getText().toString();
        String LastName = lnametxt.getText().toString();
        String Phone = phonetxt.getText().toString();
        String Email = emailtxt.getText().toString();
        String BDate = bdaytxt.getText().toString();
            //create new return intent
        Intent returnIntent = new Intent();
        returnIntent.putExtra( "FirstName", FirstName);
        returnIntent.putExtra( "LastName", LastName);
        returnIntent.putExtra( "Phone", Phone);
        returnIntent.putExtra( "Email", Email);
        returnIntent.putExtra( "BDate", BDate);

            //show result code as Result_OK
        setResult(RESULT_OK , returnIntent);
        finish();
        }

        //if intent was to update, we pass back the text fields as data so MainActivity can update the fields in myobjects

        if(key == UPDATE_INTENT )
        {
            String FirstName = fnametxt.getText().toString();
            String LastName = lnametxt.getText().toString();
            String Phone = phonetxt.getText().toString();
            String Email = emailtxt.getText().toString();
            String BDate = bdaytxt.getText().toString();

            //create new return intent
            Intent returnIntent = new Intent();
            returnIntent.putExtra( "FirstName", FirstName);
            returnIntent.putExtra( "LastName", LastName);
            returnIntent.putExtra( "Phone", Phone);
            returnIntent.putExtra( "Email", Email);
            returnIntent.putExtra( "BDate", BDate);

            //position is also passed, so we know which position to update
            returnIntent.putExtra( "position", Integer.toString(position));

            //key is passed as well, so we know that the return is for update not delete
            returnIntent.putExtra( "key", Integer.toString(key));

            //we pass Result_OK to show the program executed as expected
            setResult(RESULT_OK , returnIntent);
            finish();
        }

    }

    //Delete method
    //called when btnDelete onClick event is triggered
    //used to delete a contact
    public void Delete(View view)
    {
        //pass intent as delete
        key = DELETE_INTENT;

        //create new return intent
        Intent deleteIntent = new Intent();
        deleteIntent.putExtra( "position", Integer.toString(position));
        deleteIntent.putExtra( "key", Integer.toString(key));
        setResult(RESULT_OK , deleteIntent);
        finish();
    }

    //if item on Menu is selected i.e. the button on Action bar, it is triggered
// Handle action bar item clicks here.
    public boolean onOptionsItemSelected(MenuItem item) {
        //if item clicked was btnCancel, sendExitMessage is called which is for canceling
        switch (item.getItemId()) {
            case R.id.btnCancel:
                sendExitMessage();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds btnCancel to the action bar.
        //layout is in menu_2.xml
        getMenuInflater().inflate(R.menu.menu_2, menu);
        return true;
    }

    //sendExitMessage method
    //called when cancel button on action bar is pressed
    //sends an exit message
    //does nothing on main activity if clicked

    public void sendExitMessage()
    {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

}
