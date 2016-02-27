package com.example.saaishasingh.photoupload;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.saaishasingh.photoupload.Bean.FirebaseImage;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by saaishasingh on 2/27/16.
 */
public class Home extends Activity {
    private static int RESULT_LOAD_IMG = 1;
    private static int REQUEST_CAMERA = 2;
    String imgDecodableString, cameraImgDecodableString;
    String id = null;
    Firebase myFirebaseRef;
    Bitmap bitmap;
    ImageView imgView;
    ProgressDialog progress;
    File destination = null;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        myFirebaseRef = new Firebase("https://glaring-torch-5788.firebaseio.com/" + id);
        imgView = (ImageView) findViewById(R.id.imageViewDownloaded);
        progress = new ProgressDialog(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                if (requestCode == REQUEST_CAMERA) {
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                    destination = new File(getApplicationContext().getCacheDir(),
                            System.currentTimeMillis() + ".png");
                    FileOutputStream fo;
                    try {
                        destination.createNewFile();
                        fo = new FileOutputStream(destination);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    AlertDialog.Builder imageDialog = new AlertDialog.Builder(this);
                    LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

                    View layout = inflater.inflate(R.layout.custom_fullimage_dialog,
                            (ViewGroup) findViewById(R.id.layout_root));
                    ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
                    image.setImageBitmap(thumbnail);
                    imageDialog.setView(layout);
                    imageDialog.setPositiveButton("Upload to server", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            if (destination != null) {
                                Bitmap bit = BitmapFactory.decodeFile(destination.getAbsolutePath());
                                new UploadAsync().execute(bit);
                            }
                        }

                    });

                    imageDialog.create();
                    imageDialog.show();
                }

                if (requestCode == RESULT_LOAD_IMG && null != data) {

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();

                    int nh = (int) ( BitmapFactory.decodeFile(imgDecodableString).getHeight() * (512.0 / BitmapFactory.decodeFile(imgDecodableString).getWidth()) );
                    final Bitmap scaled = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(imgDecodableString), 512, nh, true);

                    AlertDialog.Builder imageDialog = new AlertDialog.Builder(this);
                    LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

                    View layout = inflater.inflate(R.layout.custom_fullimage_dialog,
                            (ViewGroup) findViewById(R.id.layout_root));
                    ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
                    image.setImageBitmap(scaled);
                    imageDialog.setView(layout);
                    imageDialog.setPositiveButton("Upload to server", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            if (scaled != null) {
                                new UploadAsync().execute(scaled);
                            }
                        }

                    });

                    imageDialog.create();
                    imageDialog.show();

//                    imgView.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString, options));


                }
            } catch (Exception e) {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    public void goToUploadPhoto(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    public void goToCameraPhoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    public void getImageFromFirebase(View view) {
        new async().execute();
    }

    public void logout(View view) {
        id = null;
        Intent intent = new Intent(this,
                MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    public class async extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            progress.setTitle("Downloading");
            progress.setMessage("Your picture is downloading");
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            myFirebaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
//                System.out.println(snapshot.getValue());
                    FirebaseImage img = snapshot.getValue(FirebaseImage.class);
                    if (img != null) {
                        imgDecodableString = img.getImage();
                        byte[] decodedByte = Base64.decode(imgDecodableString, 0);
                        bitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
                        imgView.setImageBitmap(bitmap);
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    progress.dismiss();
//                System.out.println("The read failed: " + firebaseError.getMessage());
                }
            });
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            progress.dismiss();
        }
    }

    public class UploadAsync extends AsyncTask<Bitmap, Void, Void> {

        @Override
        protected Void doInBackground(Bitmap... arg0) {
            Bitmap bmp = arg0[0];
            ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
            bmp.recycle();
            byte[] byteArray = bYtE.toByteArray();
            cameraImgDecodableString = Base64.encodeToString(byteArray, Base64.DEFAULT);
            myFirebaseRef.child("image").setValue(cameraImgDecodableString);
            return null;
        }

        @Override
        protected void onPreExecute() {
            progress.setTitle("Uploading");
            progress.setMessage("Please wait while we upload your image to the server");
            progress.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            progress.dismiss();

        }
    }

}
