package com.example.uurkseolu.androidhomework1;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class MainActivity extends Activity {

    List<nameNumber> persons = new ArrayList<nameNumber>();
    List<nameNumber> persons2 = new ArrayList<nameNumber>();
    List<nameNumber> persons3 = new ArrayList<nameNumber>();
    List<nameNumber> persons4 = new ArrayList<nameNumber>();
    CustomAdapter myAdaptor;
    ListView listemiz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       listemiz = (ListView) findViewById(R.id.listView);


        myAdaptor = new CustomAdapter(this, persons);
        listemiz.setAdapter(myAdaptor);

        RadioButton r = (RadioButton) findViewById(R.id.BtnAll);
        r.setChecked(true);


        accessContact();
    }
    public void accessContact (){

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection    = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};

        Cursor people = getContentResolver().query(uri, projection, null, null, null);

        int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

        people.moveToFirst();
        do {
            String name   = people.getString(indexName);
            String number = people.getString(indexNumber);
            persons.add(new nameNumber(name,number));
            persons2.add(new nameNumber(name,number));
            persons3.add(new nameNumber(name,number));
        } while (people.moveToNext());

        myAdaptor.notifyDataSetChanged();

    }
    public void ClickAll(View v){

        persons.clear();
        int i = 0;
        while(i<persons2.size()){
            persons.add(new nameNumber(persons2.get(i).getName(),persons2.get(i).getNumber()) );
            i++;

        }
        myAdaptor.notifyDataSetChanged();

    }
    public void ClickAvea(View v){
        persons.clear();
        int i = 0;
        while(i<persons2.size()){

            if(persons2.get(i).getNumber().substring(0,3).equals("055") || persons2.get(i).getNumber().substring(0,3).equals("050")
                    || persons2.get(i).getNumber().substring(0,5).equals("+9055") || persons2.get(i).getNumber().substring(0,5).equals("+9050"))
                persons.add(new nameNumber(persons2.get(i).getName(),persons2.get(i).getNumber()) );

            i++;

        }
        myAdaptor.notifyDataSetChanged();

    }
    public void ClickVodafone(View v){
        persons.clear();
        int i = 0;
        while(i<persons2.size()){

            if(persons2.get(i).getNumber().substring(0,3).equals("054")||persons2.get(i).getNumber().substring(0,5).equals("+9054"))
                persons.add(new nameNumber(persons2.get(i).getName(),persons2.get(i).getNumber()) );

            i++;

        }
        myAdaptor.notifyDataSetChanged();

    }

    public void ClickTurkcell(View v){
        persons.clear();
        int i = 0;
        while(i<persons2.size()){

            if(persons2.get(i).getNumber().substring(0,3).equals("053") || persons2.get(i).getNumber().substring(0,5).equals("+9053"))
                persons.add(new nameNumber(persons2.get(i).getName(),persons2.get(i).getNumber()) );

            i++;

        }
        myAdaptor.notifyDataSetChanged();

    }


    public void ClickBackUp(View view) throws IOException {

        File file = getFileStreamPath("backupFile.txt");
        FileOutputStream fileWriter=null;
        String test="abc";



        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }



        }
        try {
            fileWriter  = openFileOutput(file.getName(), Context.MODE_PRIVATE);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            String temp;

            for (int i = 0; i < persons3.size(); i++){

                temp = persons3.get(i).getName()+"\t"+persons3.get(i).getNumber()+"\n";
                fileWriter.write(temp.getBytes());
                fileWriter.flush();


            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        finally {
            fileWriter.close();
        }


    }

    public void ClickRecovery(View view) {

        StringBuffer buffer_string = new StringBuffer();

        try {

            BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                    openFileInput("backupFile.txt")));
            String inputString="";
            String  [] temporary = new String[2];
            while ((inputString = inputReader.readLine()) != null) {
                temporary = inputString.split("\t");
                buffer_string.append(temporary[0]);
                persons4.add(new nameNumber(temporary[0],temporary[1]));

            }

            for (int x = 0; x< persons4.size();x++){
                if(!persons3.contains(persons4.get(x))) {

                    RecoveryContact(persons4.get(x).getName(),persons4.get(x).getNumber());

                }

            }



        } catch (IOException e) {
            e.printStackTrace();


        }


        listemiz.setAdapter(new CustomAdapter(this, persons4));
        listemiz.setAdapter(myAdaptor);
        Toast.makeText(getApplicationContext(),persons4.size()+"",Toast.LENGTH_LONG).show();

    }


    private void RecoveryContact(String name, String number)
    {

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int rawContactInsertIndex = ops.size();

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());


        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number)
                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, "1").build());


        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Contacts.Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
                .build());
        try {
            ContentProviderResult[] res = getContentResolver().applyBatch(
                    ContactsContract.AUTHORITY, ops);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
