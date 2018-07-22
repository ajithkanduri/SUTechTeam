package com.example.android.sutechteam;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    SignInButton btn;
    FirebaseAuth mAuth;
    private static int RC_SIGN_IN =2;
    GoogleSignInClient mGoogleSignInClient;
    private static String TAG = "Main Activity";
    FirebaseAuth.AuthStateListener mAuthListener;
    public static int x;
    public static String username;
    public static String gmail;
    public static String photoUrl;
   // String username = "Ajith";
   // String password ="sutechteam";
    Button login;
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn = (SignInButton) findViewById(R.id.googlebtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        // Build a GoogleSignInClient with the options specified by gso.
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(LoginActivity.this, NavigationActivity.class));
                }
            }
        };
        login = (Button)findViewById(R.id.login);
        final EditText user = (EditText) findViewById(R.id.username);

       final EditText pswrd = (EditText)findViewById(R.id.pswrd);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String u = user.getText().toString();
                final String p = pswrd.getText().toString();
                if(u.equals( "UserName")&& p.equals("Password")){
                    x=1;
                    Intent nav = new Intent(LoginActivity.this,NavigationActivity.class);
                    startActivity(nav);
                }
                else {
                    Toast.makeText(LoginActivity.this, "Invalid UserName or Password",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        LayoutInflater myInflater = LayoutInflater.from(this);
        View view = myInflater.inflate(R.layout.activity_login, null);
        Toast mytoast = new Toast(this);
        mytoast.setView(view);
        mytoast.setDuration(Toast.LENGTH_LONG);
        mytoast.show();
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                           /* photoUrl = null;
                            if(acct.getPhotoUrl()!= null)
                            {
                                photoUrl = acct.getPhotoUrl().toString();
                            }

                             username =    acct.getDisplayName()+ " "+ acct.getFamilyName();
                            gmail =    acct.getEmail();*/
                               // photoUrl,
                                //FirebaseAuth.getInstance().getCurrentUser().getUid()


                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }
   public static int getLogin()
    {
        return x;
    }/*
    public static String getUsername()
    {
        return username;
    }
    public static String getGmail()
    {
        return gmail;
    }
*/



}

