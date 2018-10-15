package com.erhanozler.Magzam;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    FrameLayout frameLayout;
    ListView listView;
    ArrayList<String> usernamesFromParse;
    ArrayList<String> userCommentsFromParse;
    ArrayList<Bitmap> userImageFromParse;
    PostClass postClass;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.add_post) {
            //intent
            Intent intent = new Intent(getApplicationContext(),UploadActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.logout) {

            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if (e!=null) {
                        Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

         frameLayout=findViewById(R.id.fragment_container);

        //---------------------------------------------------

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

       listView = findViewById(R.id.listView);

        usernamesFromParse = new ArrayList<>();
        userCommentsFromParse = new ArrayList<>();
        userImageFromParse = new ArrayList<>();

        postClass = new PostClass(usernamesFromParse,userCommentsFromParse,userImageFromParse,this);

        listView.setAdapter(postClass);

        download();

    }

    public void download() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Posts");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e != null) {
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                } else {

                    if (objects.size() > 0) {

                        for (final ParseObject object : objects) {

                            ParseFile parseFile = (ParseFile) object.get("image");

                            parseFile.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {

                                    if (e == null && data != null) {

                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);

                                        userImageFromParse.add(bitmap);
                                        usernamesFromParse.add(object.getString("username"));
                                        userCommentsFromParse.add("#"+object.getString("comment"));

                                        postClass.notifyDataSetChanged();


                                    }

                                }
                            });

                        }

                    }

                }

            }
        });



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.navigation_home:
                listView.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),"Fragment 1",Toast.LENGTH_LONG).show();
                break;

            case R.id.navigation_dashboard:
                listView.setVisibility(View.INVISIBLE);
                frameLayout.setVisibility(View.VISIBLE);
                 Toast.makeText(getApplicationContext(),"Fragment 2",Toast.LENGTH_LONG).show();
                Fragment fragment2 = new BlankFragment2();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment2, fragment2.getClass().getSimpleName()).addToBackStack(null).commit();
                break;

            case R.id.navigation_notifications:
                listView.setVisibility(View.INVISIBLE);
                frameLayout.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(),"Fragment 3",Toast.LENGTH_LONG).show();
                Fragment fragment3 = new BlankFragment3();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment3, fragment3.getClass().getSimpleName()).addToBackStack(null).commit();
                break;

        }

        return false;
    }
    public void denem(ListView listView){
        postClass = new PostClass(usernamesFromParse,userCommentsFromParse,userImageFromParse, getApplicationContext());
        listView.setAdapter(postClass);
    }
}
