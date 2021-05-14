package com.example.projeto_cm.ui.Requests;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.projeto_cm.MainActivity;
import com.example.projeto_cm.R;
import com.example.projeto_cm.User;
import com.example.projeto_cm.Visits;
import com.example.projeto_cm.ui.Map.MapFragment;
import com.example.projeto_cm.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class RequestsFragment extends Fragment {
    private EditText locationET, voiceET, titleET, descriptionET;
    private Button uploadImgBtn, sendBtn;
    SpeechRecognizer speechRecognizer;
    ArrayList<Uri> images_rui;
    ProgressDialog pd;
    int countImages;

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;

    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;

    private static final int RECORD_AUDIO_REQUEST_CODE = 1;

    private View view;
    String[] cameraPermissions;
    String[] storagePermissions;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_requests, container, false);
        sendBtn = (Button) view.findViewById(R.id.button_submit);
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        pd = new ProgressDialog(view.getContext());
        images_rui = new ArrayList<>();
        countImages=0;

        MainActivity.mDataBase.child("Users").child(MainActivity.mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user.getIsGuide()) {
                    TextView request = (TextView) view.findViewById(R.id.text_aboutUs);
                    request.setText("Guide Visit");
                    sendBtn.setText("Publish");
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });

        locationET = (EditText) view.findViewById(R.id.input_location);
        Bundle bundleLatlng = this.getArguments();
        if (bundleLatlng == null || bundleLatlng.isEmpty()) {
            locationET.setText("");
        } else {
            locationET.setText(bundleLatlng.getString("LAT_LNG"));
        }

        Button backBtn = (Button) view.findViewById(R.id.back_button);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment, new HomeFragment());
                ft.commit();
            }
        });

        Button mapsBtn = (Button) view.findViewById(R.id.button_maps);
        mapsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment, new MapFragment());
                ft.commit();
            }
        });


        voiceET = (EditText) view.findViewById(R.id.input_description);

        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            checkVoicePermission();
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> array = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                voiceET.setText(array.get(0));
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });

        Button voiceBtn = (Button) view.findViewById(R.id.button_mic);
        voiceBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        speechRecognizer.stopListening();
                        voiceET.setHint("Descrição");
                        break;
                    case MotionEvent.ACTION_DOWN:
                        voiceET.setText("");
                        voiceET.setHint("A ouvir...");
                        speechRecognizer.startListening(speechRecognizerIntent);
                        break;
                }
                return false;
            }
        });


        titleET = (EditText) view.findViewById(R.id.requestTitle);
        descriptionET = (EditText) view.findViewById(R.id.input_description);
        uploadImgBtn = (Button) view.findViewById(R.id.button_upload);


        uploadImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickDialog();
            }
        });


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleET.getText().toString().trim();
                String description = descriptionET.getText().toString().trim();
                String location = locationET.getText().toString().trim();


                if (TextUtils.isEmpty(title)) {
                    Toast.makeText(view.getContext(), "Titulo necessário!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(description)) {
                    Toast.makeText(view.getContext(), "Introduza uma descrição!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (images_rui.isEmpty()) {
                    ArrayList<String> imageList = new ArrayList<>();
                    imageList.add("noImage");
                    uploadData(title, description,imageList, location);
                } else {
                    ArrayList<String> imageList = new ArrayList<>();
                    for (Uri uri : images_rui) {
                        imageList.add(String.valueOf(uri));
                    }
                    uploadData(title, description, imageList, location);
                }
            }
        });


        return view;
    }

    private void uploadData(String title, String description, ArrayList<String> uriList, String location) {
        pd.setMessage("A enviar...");
        pd.show();

        String timeStamp = String.valueOf(System.currentTimeMillis());
        String filePathAndName = "Visits/" + "visit_" + timeStamp;
        ArrayList<String> imgDownload = new ArrayList<>();

        if (!uriList.get(0).equals("noImage")) {
            for (String uri:uriList){
                StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName+countImages);
                ref.putFile(Uri.parse(uri))
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                while (!uriTask.isSuccessful()) ;
                                String downloadUri = uriTask.getResult().toString();
                                if (uriTask.isSuccessful()) {
                                    imgDownload.add(downloadUri);
                                    DatabaseReference dbRef = MainActivity.mDataBase.child("Visits");
                                    Visits visit = new Visits(title, description, timeStamp,imgDownload, location);
                                    dbRef.child(timeStamp).setValue(visit);
                                }
                                pd.dismiss();
                                Toast.makeText(view.getContext(), "Visita publicada", Toast.LENGTH_SHORT).show();
                                titleET.setText("");
                                descriptionET.setText("");
                                images_rui = null;
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(view.getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                countImages++;
            }

        } else {
            Visits visit = new Visits(title, description, timeStamp, "noImage", location);

            DatabaseReference dbRef = MainActivity.mDataBase.child("Visits");
            dbRef.child(timeStamp).setValue(visit).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    pd.dismiss();
                    Toast.makeText(view.getContext(), "Visita publicada", Toast.LENGTH_SHORT).show();

                    titleET.setText("");
                    descriptionET.setText("");
                    images_rui = null;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(view.getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void showImagePickDialog() {
        String[] options = {"Camera", "Galeria"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Escolher imagens de:");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }
                }
                if (which == 1) {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }
            }
        });
        builder.create().show();
    }

    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE, "Temp Pick");
        cv.put(MediaStore.Images.Media.DESCRIPTION, "Temp Descr");
        Uri img = view.getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
        images_rui.add(img);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, img);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);

    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this.requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this.requireActivity(), storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this.getActivity(), cameraPermissions, CAMERA_REQUEST_CODE);
    }

    private void checkVoicePermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(this.getContext(), "Permissões de camera e armazenamento necessárias...", Toast.LENGTH_SHORT).show();
                    }
                } else {

                }
            }
            break;
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this.getContext(), "Permissão de armazenamento necessária...", Toast.LENGTH_SHORT).show();
                    }
                } else {
                }
            }
            break;
            case RECORD_AUDIO_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this.getContext(), "Permissão garantida", Toast.LENGTH_LONG).show();
                }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if (resultCode == RESULT_OK) {

            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                if(images_rui==null){
                    images_rui= new ArrayList<>();
                }
                images_rui.add(data.getData());
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
