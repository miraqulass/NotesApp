package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

import java.util.Date;
import java.util.Calendar;
import java.sql.Timestamp;

public class NoteDetailsActivity extends AppCompatActivity {

    EditText titleEditText, contentEditText;
    ImageButton saveNoteBtn, downlaodFileBtn;
    TextView pageTitleTextView;
    String title, content, docId;
    boolean isDownloadMode = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        titleEditText = findViewById(R.id.notes_title_text);
        contentEditText = findViewById(R.id.notes_content_text);
        saveNoteBtn = findViewById(R.id.save_note_btn);
        pageTitleTextView = findViewById(R.id.page_title);
        downlaodFileBtn = findViewById(R.id.download_file_btn);

//        recieve data
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        if (docId != null && !docId.isEmpty()){
            isDownloadMode = true;

        }

        titleEditText.setText(title);


        if (isDownloadMode){
            pageTitleTextView.setText("Download Notes");
            downlaodFileBtn.setVisibility(View.VISIBLE);
            saveNoteBtn.setVisibility(View.GONE);
        }else {
            downlaodFileBtn.setVisibility(View.GONE);
            saveNoteBtn.setVisibility(View.VISIBLE);
        }

        downlaodFileBtn.setOnClickListener((v)->mpesaMenu());


        saveNoteBtn.setOnClickListener((v)-> saveNote());


    }

    void saveNote(){
      String noteTitle = titleEditText.getText().toString();
      String noteContent = titleEditText.getText().toString();

      if (noteTitle == null || noteTitle.isEmpty()){
          titleEditText.setError("Title is required!");
          return;
      }
      Note note = new Note();
      note.setTitle(noteTitle);
      note.setContent(noteContent);
//      note.setTimestamp(Timestamp.now());

      saveNoteToFirebase(note);
      
    }

    void saveNoteToFirebase(Note note){

        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForNotes().document();

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
//                    note is added
                    Utility.showToast(NoteDetailsActivity.this, "Note added successfully");
                    finish();
                }else {
                    Utility.showToast(NoteDetailsActivity.this, "Failed while adding note");
                }
            }
        });

    }

    void mpesaMenu(){
        Utility.showToast(NoteDetailsActivity.this,"Pay to download the notes");
        startActivity(new Intent(NoteDetailsActivity.this, MpesaActivity.class));
    }
}