package com.example.passwordsnotesmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Passwords extends AppCompatActivity {

    private static final String PREFS_NAME="PasswordPrefs";
    private static final String KEY_NOTE_COUNT="PasswordCount";

    private  LinearLayout passContainer;
    private List<Password> passlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwords);

        passContainer=findViewById(R.id.passContainer);
        Button saveButton=findViewById(R.id.saveButton);

        passlist=new ArrayList<Password>();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePassword();
            }
        });
        loadPasswordsFromPreferences();
        displayPasswords();
    }

    private void displayPasswords() {
        for (Password pass : passlist){
            createPasswordView(pass);
        }
    }

    private void loadPasswordsFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        int passCount= sharedPreferences.getInt(KEY_NOTE_COUNT, 0);

        for (int i=0; i< passCount;i++){
            String title =sharedPreferences.getString("pass_title" + i,"");
            String content =sharedPreferences.getString("pass_content" + i,"");

            Password pass = new Password();
            pass.setTitle(title);
            pass.setContent(content);

            passlist.add(pass);
        }

    }

    private void savePassword() {
        EditText titleEditText=findViewById(R.id.titleEditText);
        EditText contentEditText=findViewById(R.id.contentEditText);

        String title= titleEditText.getText().toString();
        String content= contentEditText.getText().toString();

        if(!title.isEmpty() && !content.isEmpty()) {
            Password pass = new Password();
            pass.setTitle(title);
            pass.setContent(content);

            passlist.add(pass);
            savePasswordsToPreferences();

            createPasswordView(pass);
            clearInputFields();
        }
    }

    private void clearInputFields() {
        EditText titleEditText= findViewById(R.id.titleEditText);
        EditText contentEditText= findViewById(R.id.contentEditText);

        titleEditText.getText().clear();
        contentEditText.getText().clear();

    }

    private void createPasswordView(final Password pass) {
        View passView = getLayoutInflater().inflate(R.layout.pass_item,null);
        TextView titleTextView = passView.findViewById(R.id.titleTextView);
        TextView contentTextView= passView.findViewById(R.id.contentTextView);

        titleTextView.setText(pass.getTitle());
        contentTextView.setText(pass.getContent());

        passView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDeleteDialog(pass);
                return true;
            }
        });

        passContainer.addView(passView);
    }

    private void showDeleteDialog(final Password pass) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete this pass.");
        builder.setMessage("Are you sure you want to delete this pass");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletePasswordAndRefresh(pass);
            }
        });
        builder.setNegativeButton("Cancel",null);
        builder.show();
    }

    private void deletePasswordAndRefresh(Password pass) {
        passlist.remove(pass);
        savePasswordsToPreferences();
        refreshPasswordViews();
    }

    private void refreshPasswordViews() {
        passContainer.removeAllViews();
        displayPasswords();
    }

    private void savePasswordsToPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_NOTE_COUNT,passlist.size());
        for(int i =0; i< passlist.size();i++){
            Password pass = passlist.get(i);
            editor.putString("pass_title"+ i, pass.getTitle());
            editor.putString("desc_pass"+ i,pass.getContent());
        }
        editor.apply();


    }
}