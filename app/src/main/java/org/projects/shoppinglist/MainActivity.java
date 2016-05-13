package org.projects.shoppinglist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.view.View.OnClickListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //This is the event handler for the show button
    //This is specified in the xml file that this should
    //be the event handler


    ArrayAdapter<Product> adapter;
    ListView listView;
    ArrayList<Product> bag = new ArrayList<Product>();
    Firebase mref;
    FirebaseListAdapter<Product> tuzike;
    public FirebaseListAdapter<Product> getMyAdapter() {return tuzike;}
    public Product getItem (int index) {return getMyAdapter().getItem(index);}

   // public ArrayAdapter getMyAdapter() {
     //   return adapter;
    //}


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    Product lastDeletedProduct;
    int lastDeletedPosition;

    public void nagyi() {
        lastDeletedPosition = listView.getCheckedItemPosition();
        if (lastDeletedPosition != ListView.INVALID_POSITION) {
            //get it
            Log.v("deleted", String.valueOf(lastDeletedPosition));

           lastDeletedProduct = tuzike.getItem(lastDeletedPosition);
            //lastDeletedProduct = bag.get(lastDeletedPosition);
        } else {
            //if there was no item selection, display a toast to the user
            Toast.makeText(
                    this,
                    "Please select an item to delete!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {

            bag = savedInstanceState.getParcelableArrayList("Product");
            if (bag == null)
                bag = new ArrayList<Product>();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mref = new Firebase("https://sweltering-torch-9535.firebaseio.com/items");

        tuzike = new FirebaseListAdapter<Product>(this, Product.class, android.R.layout.simple_list_item_checked, mref){
            @Override
            protected void populateView(View v, Product product, int i){
                TextView text = (TextView) v.findViewById(android.R.id.text1);
                text.setText(product.toString());
            }
        };
        //getting our listiew - you can check the ID in the xml to see that it
        //is indeed specified as "list"
        listView = (ListView) findViewById(R.id.list);
        //here we create a new adapter linking the bag and the
        //listview


        /*if (bag == null) {
            System.out.println("bag is null");
            bag = new ArrayList<>();
        } else
            System.out.println("bag is not null");
        adapter = new ArrayAdapter<Product>(this,
                android.R.layout.simple_list_item_checked, bag);*/
        //setting the adapter on the listview
        listView.setAdapter(tuzike); //Atirom tuzikere az adapter-t
        //here we set the choice mode - meaning in this case we can
        //only select one item at a time.
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText anyad = (EditText) findViewById(R.id.addItem);
                String szoveg = anyad.getText().toString();
                EditText apad = (EditText) findViewById(R.id.addquantity);
                String darab = apad.getText().toString();
                Spinner amount = (Spinner) findViewById(R.id.spinner1);
                String quan = (String) amount.getSelectedItem();

                int tasak = Integer.parseInt(darab);

              //  bag.add(new Product(tasak, szoveg, quan));

                Product p = new Product(tasak, szoveg, quan);
                Log.v("lemez","valami");
                mref.push().setValue(p);

                //anyad.setText("");
                //apad.setText("");

                //The next line is needed in order to say to the ListView
                //that the data has changed - we have added stuff now!
                getMyAdapter().notifyDataSetChanged();


            }
        });
        Button dltButton = (Button) findViewById(R.id.delete);

        dltButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
                nagyi();
                for (int i = tuzike.getCount() - 1; i >= 0; i--) {
                    if (checkedItems.get(i)) {
                        // This item is checked and can be removed
                        Log.v("valami", String.valueOf(tuzike.getItem(i)));
                        getMyAdapter().getRef(i).setValue(null);
                        //bag.remove(adapter.getItem(i));

                    }
                }
                //The next line is needed in order to say to the ListView
                //that the data has changed - we have added stuff now!
                getMyAdapter().notifyDataSetChanged();

                final View parent = listView;
                Snackbar snackbar = Snackbar
                        .make(parent, "Item Deleted", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                               mref.push().setValue(lastDeletedProduct);
                                // bag.add(lastDeletedPosition, lastDeletedProduct);
                                getMyAdapter().notifyDataSetChanged();
                                Snackbar snackbar = Snackbar.make(parent, "Item restored!", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }
                        });

                snackbar.show();
                getMyAdapter().notifyDataSetChanged();

            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    //Share things
    public String convertListToString()
    {
        String result = "";
        for (int i = 0; i<tuzike.getCount();i++)
        {
            Product p = (Product) tuzike.getItem(i);
            //TODO....add the product string to the result
            result = result + "\n" + p;
            //to insert newline you can do something like
            // result = result + "\n";

        }
        return result;
    }

    /*public void showDialog(View v) {
        //showing our dialog.
        MyDialogFragment dialog = new MyDialogFragment() {
            @Override
            protected void positiveClick() {
                //Here we override the methods and can now
                //do something
                Toast toast = Toast.makeText(getApplicationContext(),
                        "positive button clicked", Toast.LENGTH_LONG);

                bag.clear();
                adapter.notifyDataSetChanged();

            }

            @Override
            protected void negativeClick() {
                //Here we override the method and can now do something
                Toast toast = Toast.makeText(getApplicationContext(),
                        "negative button clicked", Toast.LENGTH_SHORT);
                toast.show();
            }
        };

        //Here we show the dialog
        //The tag "MyFragement" is not important for us.
        dialog.show(getFragmentManager(), "MyFragment");
    }*/


    //public void onClickClearItems(View v) {
    //   bag.clear();
    //  adapter.notifyDataSetChanged();
    //}

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        //savedInstanceState.putStringArrayList("bag", bag);
        savedInstanceState.putParcelableArrayList("Product", bag);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==1) //exited our preference screen
        {
            Toast toast =
                    Toast.makeText(getApplicationContext(), "back from preferences", Toast.LENGTH_LONG);
            toast.setText("back from our preferences");
            toast.show();
            //here you could put code to do something.......
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setPreferences() {
        //Here we create a new activity and we instruct the
        //Android system to start it
        Intent intent = new Intent(this, SettingsActivity.class);
        //startActivity(intent); //this we can use if we DONT CARE ABOUT RESULT

        //we can use this, if we need to know when the user exists our preference screens
        startActivityForResult(intent, 1);
    }

    public void getPreferences() {

        //We read the shared preferences from the

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //int id = item.getItemId();
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        System.out.println("on optionsitem selected");
        switch (item.getItemId()) {

            case R.id.clearAll:
                System.out.println("clear selected");
                // all shall be deleted
                MyDialogFragment dialog = new MyDialogFragment() {
                    @Override
                    protected void positiveClick() {
                        //Here we override the methods and can now
                        //do something;
                        mref.setValue(null);
                        getMyAdapter().notifyDataSetChanged();

                    }

                    @Override
                    protected void negativeClick() {
                        //Here we override the method and can now do something
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "negative button clicked", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                };

                //Here we show the dialog
                //The tag "MyFragement" is not important for us.
                dialog.show(getFragmentManager(), "MyFragment");
                return true;

            //noinspection SimplifiableIfStatement
            case R.id.action_settings:
                setPreferences();
                return true;

            case R.id.action_share:

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, convertListToString());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

        }
        return super.onOptionsItemSelected(item);
    }
}

