package com.mcg.mcemil.cubbacubba.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mcg.mcemil.cubbacubba.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        final TextView tUsername = (TextView) findViewById(R.id.tUsername);
        final TextView tPassword = (TextView) findViewById(R.id.tPassword);
        Button bLogin = (Button) findViewById(R.id.bLogin);

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("username", tUsername.getText().toString());
                intent.putExtra("password", tPassword.getText().toString());

                startActivity(intent);
            }
        });

    }
}
