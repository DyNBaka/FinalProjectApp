package com.example.a1530630.learningapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a1530630.learningapplication.Database.SQLiteManage;
import com.example.a1530630.learningapplication.Models.User;

public class User_setting extends Main_Menu implements NavigationView.OnNavigationItemSelectedListener {

    SQLiteManage db;
    SharedPreferences sharedPreferences;
    NavigationView nv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        dl = (DrawerLayout) findViewById(R.id.drawer_layout);

        //inputs
        EditText Fullname = findViewById(R.id.UserFull);
        EditText Username = findViewById(R.id.UserName);
        EditText Password = findViewById(R.id.UserPass);
        EditText Email = findViewById(R.id.UserEmail);

        db = new SQLiteManage(this);
        SharedPreferences settings = getSharedPreferences(Login.MyPreferences, Context.MODE_PRIVATE);

        //converted inputs
        String userName = settings.getString("Username",null);
        String fullName = settings.getString("FullName",null);
        String passWord = settings.getString("Password",null);
        String email = settings.getString("Email",null);


        t = new ActionBarDrawerToggle(this, dl,R.string.nav_open, R.string.nav_close);
        dl.addDrawerListener(t);

        t.syncState(); //getActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dl.addDrawerListener(new DrawerLayout.DrawerListener()
        {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) { }
            @Override
            public void onDrawerOpened(@NonNull View view)
            {
                SharedPreferences settings = getSharedPreferences(Login.MyPreferences, Context.MODE_PRIVATE);
                String userName = settings.getString("Username",null);
                TextView user = findViewById(R.id.nav_header_textView);
                user.setText(userName);
            }
            @Override
            public void onDrawerClosed(@NonNull View view) {}
            @Override
            public void onDrawerStateChanged(int i) {}
        });

        nv = findViewById(R.id.nav_view);
        nv.setNavigationItemSelectedListener(this);

        if(!userName.contains("admin"))
        {
            nv.getMenu().findItem(R.id.nav_summary).setVisible(false);
            nv.getMenu().findItem(R.id.nav_detail).setVisible(false);
            nv.getMenu().findItem(R.id.nav_add).setVisible(false);
        }


    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if(t.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent i;
        switch (menuItem.getItemId())
        {

            case R.id.nav_menu: {
                i = new Intent(this, Main_Menu.class);
                startActivity(i);
                return true;
            }
            case R.id.nav_profile: {
                i = new Intent(this, Profile.class);
                startActivity(i);
                return true;
            }

            case R.id.nav_summary: {
                i = new Intent(this, SummaryReport.class);
                startActivity(i);
                return true;
            }

            case R.id.nav_detail:{
                i = new Intent(this, DetailedReport.class);
                startActivity(i);
                return true;
            }

            case R.id.nav_exit: {
                SharedPreferences settings = getSharedPreferences(Login.MyPreferences, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.clear();
                editor.commit();
                i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
                return true;
            }
            case R.id.nav_add: {
                i = new Intent(getApplicationContext(), Store.class);
                startActivity(i);
                return true;
            }
            case R.id.nav_tutorial: {
                i = new Intent(getApplicationContext(), Tutorial.class);
                startActivity(i);
                return true;
            }

        }

        return true;
    }


    public void UpdateUser(View view)
    {
        MD5 hash = new MD5();
        SharedPreferences settings = getSharedPreferences(Login.MyPreferences, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        Integer id = settings.getInt("UserID",0);
        editor.clear();
        editor.commit();

        EditText Fullname = findViewById(R.id.UserFull);
        EditText Username = findViewById(R.id.UserName);
        EditText Password = findViewById(R.id.UserPass);
        EditText Email = findViewById(R.id.UserEmail);

        String userName = Fullname.getText().toString();
        String fullName = Username.getText().toString();
        String passWord = Password.getText().toString();
        String email = Email.getText().toString();
        if(db.User_Exist(email, userName))
        {
            Toast.makeText(this,"Username already used",Toast.LENGTH_SHORT).show();
        }
        else
        {
            try
            {
                User user = new User();
                String oldEmail="";
                String oldName = "";
                String oldUser ="";
                String oldPass = "";
                Cursor cursor = db.getUserInfo(id);
                if(cursor.moveToFirst())
                {
                    oldEmail = cursor.getString(cursor.getColumnIndex(User.COLUMN_EMAIL));
                    oldName = cursor.getString(cursor.getColumnIndex(User.COLUMN_FULL_NAME));
                    oldUser = cursor.getString(cursor.getColumnIndex(User.COLUMN_USERNAME));
                    oldPass = cursor.getString(cursor.getColumnIndex(User.COLUMN_PASSWORD));
                }
                if(email.isEmpty()) { user.setEmail(oldEmail); }
                else { user.setEmail(email); }

                if(fullName.isEmpty()){ user.setFullName( oldName);}
                else { user.setFullName(fullName);}

                if(userName.isEmpty()){ user.setUsername(oldUser);}
                else{user.setUsername(userName);}

                if(passWord.isEmpty()){user.setPassword(oldPass);}
                else {user.setPassword(hash.hashPass(passWord));}

                db.UpdateProfile(user, id);

                editor.putInt("UserID",id);
                editor.putString("Username",user.getUsername());
                editor.putString("Password",user.getPassword());
                editor.putString("Email",user.getEmail());
                editor.putString("FullName",user.getFullName());
                editor.commit();

                Toast.makeText(this, "Profile Updated",Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), Main_Menu.class);
                startActivity(i);
            }
            catch(Exception e)
            {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
}
