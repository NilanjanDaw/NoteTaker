package com.nilanjan.notetaker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nilanjan on 13-Jun-17.
 * Project NoteTaker
 */

public class RegistrationActivity extends AppCompatActivity {

    @BindView(R.id.email) EditText emailId;
    @BindView(R.id.password) EditText password;
    @BindView(R.id.password_confirm) EditText passwordConfirm;
    @BindView(R.id.first_name) EditText firstName;
    @BindView(R.id.last_name) EditText lastName;
    @BindView(R.id.register) Button register;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);
        sharedPreferences = this.getSharedPreferences(getString(R.string.shared_preference_file), MODE_PRIVATE);
        progressDialog = new ProgressDialog(RegistrationActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Registering...");

    }

    @OnClick(R.id.register)
    public void registerAccount(View view) {
        progressDialog.show();
        if (validateCredentials()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("user_id", emailId.getText().toString());
            editor.putString("password", password.getText().toString());
            editor.putString("first_name", firstName.getText().toString());
            editor.putString("last_name", lastName.getText().toString());
            editor.putString("logged", "1");
            editor.apply();
            progressDialog.dismiss();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            progressDialog.dismiss();
            Toast.makeText(this, "Please enter valid details", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validateCredentials() {
        int ctr = 0;
        if(!emailValidator(emailId.getText().toString())) {
            emailId.setError(getString(R.string.invalid_field));
            ctr = 1;
        }
        if(!nameValidator(firstName.getText().toString())) {
            firstName.setError(getString(R.string.invalid_field));
            ctr = 1;
        }
        if(!nameValidator(lastName.getText().toString())) {
            lastName.setError(getString(R.string.invalid_field));
            ctr = 1;
        }

        if(!passwordValidator(password.getText().toString(), passwordConfirm.getText().toString())) {
            password.setError(getString(R.string.invalid_field));
            password.setText("");
            passwordConfirm.setText("");
            ctr = 1;
        }
        return (ctr == 0);
    }

    private boolean emailValidator(String emailId)
    {
        return !Objects.equals(emailId, "") && android.util.Patterns.EMAIL_ADDRESS.matcher(emailId).matches();

    }

    private boolean nameValidator(String name)
    {
        return !Objects.equals(name, "") && name.matches("[a-zA-Z ]+");
    }

    private boolean passwordValidator(String password, String confirmPassword)
    {
        return !Objects.equals(password, "") && password.length() > 5 && confirmPassword.equals(password);
    }

}
