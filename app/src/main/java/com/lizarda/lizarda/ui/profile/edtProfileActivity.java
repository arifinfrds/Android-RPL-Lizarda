package com.lizarda.lizarda.ui.profile;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lizarda.lizarda.R;
import com.lizarda.lizarda.model.User;
import com.lizarda.lizarda.ui.add_product.AddProductActivity;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.lizarda.lizarda.Const.TAG.DOWNLOAD_IMAGE;
import static com.lizarda.lizarda.Const.TAG.URI;

public class edtProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText input_nama;
    private EditText input_alamat;
    private EditText input_deskripsi;
    private Button btn_edtUser;
    private Button btn_browseImage;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private StorageReference mStorageRef1;

    private String mImageDownloadUrl1;
    private ImageView mIvProduct;
    private String mCurrentPhotoPath1;

    public static final int REQUEST_PICK_IMAGE = 1;
    public static final int REQUEST_IMAGE_CAPTURE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edt_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        input_nama = (EditText) findViewById(R.id.input_nama);
        input_alamat = (EditText) findViewById(R.id.input_alamat);
        input_deskripsi = (EditText) findViewById(R.id.input_deskripsi);

        btn_edtUser = (Button) findViewById(R.id.btn_submit);
        btn_browseImage = (Button) findViewById(R.id.btn_browseEdtImage);
        btn_edtUser.setOnClickListener(this);
        btn_browseImage.setOnClickListener(this);

        mIvProduct = (ImageView) findViewById(R.id.img_user);
    }
    private void updateUser(){
        String nama = input_nama.getText().toString();
        String alamat = input_alamat.getText().toString();
        String deskripsi = input_deskripsi.getText().toString();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        User userUpdate = new User();
        userUpdate.setNama(nama);
        userUpdate.setAlamat(alamat);
        userUpdate.setDeskripsi(deskripsi);

        databaseReference.child("User").child(user.getUid()).setValue(userUpdate);

        Toast.makeText(edtProfileActivity.this, "Saved", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(edtProfileActivity.this, ProfileActivity.class));

    }

    //Gambar
    private void showPopupMenuPhoto() {
        PopupMenu popup = new PopupMenu(edtProfileActivity.this, btn_browseImage);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.image_popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.popup_open_gallery) {
                    presentImagePicker();
                }
                if (id == R.id.popup_open_camera) {
                    dispatchTakePictureIntent();
                }
                return true;
            }
        });
        popup.show();
    }

    private void presentImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_PICK_IMAGE);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    // deprecated
    private void uploadImageToFirebaseStorage(Uri fileUri) {
        StorageReference imagesRef = mStorageRef1.child("images/" + fileUri.getLastPathSegment());
        UploadTask uploadTask = mStorageRef1.putFile(fileUri);
        uploadTask = imagesRef.putFile(fileUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Log.d(DOWNLOAD_IMAGE, "onSuccess: downloadUrl + " + downloadUrl);
            }
        });
    }

    private void uploadImageToFirebaseStorage(byte[] data, Uri fileUri) {
        StorageReference imagesRef = mStorageRef1.child("images/" + fileUri.getLastPathSegment());
        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                mImageDownloadUrl1 = downloadUrl.toString();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            Log.d(URI, "onActivityResult: uri.toString() : " + uri.toString());
            Log.d(URI, "onActivityResult: uri.getPath() : " + uri.getPath());

            // update UI
            Picasso.with(this).load(uri).into(mIvProduct);
            try {
                File productImageFile = createImageFile();
                Bundle extras = data.getExtras();
                Bitmap imageBitmap;
                if (data.getData() == null) {
                    imageBitmap = (Bitmap) data.getExtras().get("data");
                } else {
                    imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                }
                int nh = (int) (imageBitmap.getHeight() * (512.0 / imageBitmap.getWidth()));
                Bitmap scaledImageBitmap = Bitmap.createScaledBitmap(imageBitmap, 512, nh, false);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                scaledImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                Bitmap.createScaledBitmap(scaledImageBitmap, mIvProduct.getWidth(), mIvProduct.getHeight(), false);
                byte[] baosByte = baos.toByteArray();
                uploadImageToFirebaseStorage(baosByte, Uri.fromFile(productImageFile));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            // update UI
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mIvProduct.setImageBitmap(imageBitmap);

            Log.d(URI, "onActivityResult: data : " + data);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] baosByte = baos.toByteArray();

            Uri uri;
            Cursor cursor = getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{
                            MediaStore.Images.Media.DATA,
                            MediaStore.Images.Media.DATE_ADDED,
                            MediaStore.Images.ImageColumns.ORIENTATION
                    },
                    MediaStore.Images.Media.DATE_ADDED,
                    null,
                    "date_added ASC"
            );
            // set image URI
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    uri = Uri.parse(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                } while (cursor.moveToNext());
                cursor.close();
                try {
                    File imageFile = createImageFile();
                    uploadImageToFirebaseStorage(baosByte, Uri.fromFile(imageFile));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // MARK: - Model & Logic
    private File createImageFile() throws IOException {
        // Create an image file name

        FirebaseUser user = firebaseAuth.getCurrentUser();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = user.getUid() + "_JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath1 = image.getAbsolutePath();
        return image;
    }
    //Gambar



    @Override
    public void onClick(View view) {
        if(view == btn_edtUser) {
            updateUser();
        }
        if(view == btn_browseImage){
            showPopupMenuPhoto();
        }

    }
}
