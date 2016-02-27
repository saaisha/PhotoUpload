package com.example.saaishasingh.photoupload;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;


public class MainActivity extends Activity {
    Button createAccount;
    Button signIn;
    EditText userName,password;
    String uname,pass;
    Firebase myFirebaseRef;
    CoordinatorLayout coordinatorLayout;
    String id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://glaring-torch-5788.firebaseio.com/");
        createAccount=(Button) findViewById(R.id.buttonCreateAccount);
        signIn=(Button) findViewById(R.id.buttonSignIn);
        userName=(EditText) findViewById(R.id.editTextUserName);
        password=(EditText) findViewById(R.id.editTextPassword);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        final Intent intent = new Intent(this,Home.class);


        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uname = userName.getText().toString();
                pass = password.getText().toString();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, " Creating your account ..", Snackbar.LENGTH_LONG);
                snackbar.show();
                myFirebaseRef.createUser(uname, pass, new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        id = result.get("uid").toString();
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "Successfully created user account with uid: " + result.get("uid"), Snackbar.LENGTH_LONG);

                        snackbar.show();
                        Log.d("signUp", "Successfully created user account with uid: " + result.get("uid"));
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "Could not sign you up !" + firebaseError.toString(), Snackbar.LENGTH_LONG);

                        snackbar.show();
                        Log.d("signUp", firebaseError.toString());
                    }
                });




            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uname = userName.getText().toString();
                pass = password.getText().toString();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, " Signing in ..", Snackbar.LENGTH_LONG);
                snackbar.show();

                myFirebaseRef.authWithPassword(uname, pass, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        id=authData.getUid().toString();
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "User ID: " + authData.getUid() + ", Provider: " + authData.getProvider() , Snackbar.LENGTH_LONG);

                        snackbar.show();
                        Log.d("signIn", "User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                        intent.putExtra("id",id);
                        startActivity(intent);

                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "Error: "+firebaseError.toString() , Snackbar.LENGTH_LONG);

                        snackbar.show();
                        Log.d("signIn",firebaseError.toString());
                    }
                });
            }
        });
    }



}

