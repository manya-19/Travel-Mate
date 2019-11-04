package io.github.project_travel_mate.friend;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.OnClick;
import io.github.project_travel_mate.R;

public class Main2Activity extends AppCompatActivity  {

    final int PICK_CONTACT_REQUEST = 1;
    Uri tmp_url;
    public static String x;
    String name; String pn;
    String [] num;
    ArrayList<String> fruits_list;
    ArrayAdapter<String> arrayAdapter;
    AlertDialog.Builder builder;
    int PERMISSION_REQUEST_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main2 );
        int pop = 100;
        final ListView lv = (ListView) findViewById( R.id.listview );

        Button btn = (Button) findViewById( R.id.button3 );
        builder = new AlertDialog.Builder(this);
        String[] fruits = new String[] { "", };

        // Create a List from String Array elements
        fruits_list = new ArrayList<String>(Arrays.asList(fruits));

        // Create an ArrayAdapter from List
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fruits_list);

        // DataBind ListView with items from ArrayAdapter
        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                builder.setMessage("Pick action ")
                        .setCancelable(false)
                        .setPositiveButton("Call ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ActivityCompat.requestPermissions(Main2Activity.this, new String[]
                                        {
                                        Manifest.permission.CALL_PHONE
                                        }, PERMISSION_REQUEST_CODE);
                                String num = fruits_list.get( i );
                                String dial = "tel:" + num;
                                startActivity(new Intent(Intent.ACTION_CALL,

                                        Uri.parse(dial)));

                                //Toast.makeText(getApplicationContext(), "you choose yes action for alertbox",
                                        //Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Message", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                String num = fruits_list.get( i );
                                String dial = "tel:" + num;
                                Intent intent = new Intent(Intent.ACTION_SENDTO);
                                intent.setData(Uri.parse("smsto:" + Uri.encode(dial)));
                                startActivity(intent);
                                //Toast.makeText(getApplicationContext(), "you choose no action for alertbox",
                                        //Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Pick an option");
                alert.show();

            }
        } );

        final int PICK_CONTACT_REQUEST = 1;  // The request code

        btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
                pickContactIntent.setType( ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);

            }
        } );
        /*Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType( ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);*/
    }
    @Override
    protected void onPause() {
        super.onPause();

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to

        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                Uri contactUri = data.getData();

                tmp_url = contactUri;

                // We only need the NUMBER column, because there will be only one row in the result
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};


                Cursor cursor = getContentResolver()
                         .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                // Retrieve the phone number from the NUMBER column
                int column = cursor.getColumnIndex( ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(column);
                Toast.makeText( this, number, Toast.LENGTH_SHORT ).show();
                fruits_list.add(number);

                /*
                    notifyDataSetChanged ()
                        Notifies the attached observers that the underlying
                        data has been changed and any View reflecting the
                        data set should refresh itself.
                 */
                arrayAdapter.notifyDataSetChanged();
                // Do something with the contact here (bigger example below)*/

            }
        }

    }

}


