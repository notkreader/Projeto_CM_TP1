package com.example.projeto_cm.ui.Response;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projeto_cm.MainActivity;
import com.example.projeto_cm.R;
import com.example.projeto_cm.Requests;
import com.example.projeto_cm.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class Response extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;

    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;

    private EditText descriptionET;
    private Button uploadImgBtn, sendBtn;
    String[] cameraPermissions;
    String[] storagePermissions;
    String isVisit;
    String filePathAndName;
    String userEmail;
    ArrayList<Uri> images_rui;
    String title;
    int countImages;
    String uidRequest;

    public Response(String userEmail, String title, String uidRequest) {
       this.userEmail=userEmail;
       this.title=title;
       this.uidRequest=uidRequest;
    }
    public Response(){

    }

    public static Response newInstance(String param1, String param2) {
        Response fragment = new Response();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_response, container, false);
        sendBtn = (Button) view.findViewById(R.id.response_send);
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        images_rui = new ArrayList<>();
        countImages=0;

        descriptionET = (EditText) view.findViewById(R.id.response_text);
        uploadImgBtn = (Button) view.findViewById(R.id.response_upload);

        uploadImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickDialog();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = descriptionET.getText().toString().trim();

                if (TextUtils.isEmpty(description)) {
                    Toast.makeText(view.getContext(), "Missing Description!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (images_rui.isEmpty()) {
                    ArrayList<String> imageList = new ArrayList<>();
                    imageList.add("noImage");
                    uploadData(title, description,imageList);
                } else {
                    ArrayList<String> imageList = new ArrayList<>();
                    for (Uri uri : images_rui) {
                        imageList.add(String.valueOf(uri));
                    }
                    uploadData(title, description,imageList);
                }

            }
        });
    return view;
    }

    private void uploadData(String title, String description, ArrayList<String> uriList){

        String timeStamp = String.valueOf(System.currentTimeMillis());
        filePathAndName +=  timeStamp;
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
                                if(uriTask.isSuccessful()){
                                    imgDownload.add(downloadUri);

                                    Requests response = new Requests(title, description, timeStamp, imgDownload);
                                    MainActivity.mDataBase.child("Users").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                            for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                                                User user = dataSnapshot.getValue(User.class);
                                                if(user.getEmail().equals(userEmail)){
                                                    MainActivity.mDataBase.child("Users").child(user.getUid()).child("messages").child(timeStamp).setValue(response).
                                                            addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            MainActivity.mDataBase.child("Requests").child(uidRequest).removeValue();
                                                        }
                                                    });

                                                }
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                        }
                                    });

                                }
                                descriptionET.setText("");
                                images_rui = null;
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                countImages--;
            }
        }else{
           Requests response = new Requests(title, description, timeStamp, "noImage");
            MainActivity.mDataBase.child("Users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        User user = dataSnapshot.getValue(User.class);
                        if(user.getEmail().equals(userEmail)){
                            MainActivity.mDataBase.child("Users").child(user.getUid()).child("messages").child(timeStamp).setValue(response)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            MainActivity.mDataBase.child("Requests").child(uidRequest).removeValue();
                                        }
                                    });
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                }
            });
            descriptionET.setText("");
            images_rui = null;
        }
        ((MainActivity)getActivity()).setVisitsOff();
    }

    private void showImagePickDialog() {
        String[] options = {"Camera", "Galery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Choose images from:");

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
        Uri img = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
        images_rui.add(img);
        countImages++;
        /*
        TextView tv = (TextView) getActivity().findViewById(R.id.textView4);
        tv.setText(countImages + " images uploaded");*/

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
                        Toast.makeText(this.getContext(), "Camera & Storage permissions needed...", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(this.getContext(), " Storage permission needed...", Toast.LENGTH_SHORT).show();
                    }
                } else {
                }
            }
            break;
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
                countImages++;
                /*
                TextView tv = (TextView) getActivity().findViewById(R.id.textView4);
                tv.setText(countImages + " images uploaded");*/
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }






}