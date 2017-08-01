package com.nilanjan.notetaker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nilanjan on 13-Jun-17.
 * Project NoteTaker
 */
public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.email) AutoCompleteTextView mEmailView;
    @BindView(R.id.password) EditText mPasswordView;
    @BindView(R.id.email_sign_in_button) Button mEmailSignInButton;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;
    public static final String TAG = LoginActivity.class.getCanonicalName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        sharedPreferences = this.getSharedPreferences(getString(R.string.shared_preference_file), MODE_PRIVATE);
        if (sharedPreferences.getString("logged", "").equals("1")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.authenticating));

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    @OnClick(R.id.link_signup)
    public void signUp() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
        finish();
    }

    private void attemptLogin() {

        progressDialog.show();
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            progressDialog.dismiss();
            focusView.requestFocus();
        } else {
            if (sharedPreferences.contains("user_id") && sharedPreferences.contains("password")) {
                String credentialsEmail = sharedPreferences.getString("user_id", "");
                String credentialsPassword = sharedPreferences.getString("password", "");
                if (credentialsEmail.equals(email) && credentialsPassword.equals(password)) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("logged", "1");
                    editor.apply();
                    progressDialog.dismiss();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Invalid Credentials! Please try again", Toast.LENGTH_LONG).show();
                }
            } else {
                progressDialog.dismiss();
                Toast.makeText(this, "Please Register yourself!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 5;
    }

}

