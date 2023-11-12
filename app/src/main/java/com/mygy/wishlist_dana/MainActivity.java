package com.mygy.wishlist_dana;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private ArrayList<WishList> lists = new ArrayList<>();
    private static WishList currentList = null;
    private static TextView listName = null;
    private static TextView listDate = null;
    public static WishRecyclerAdapter wishesAdapter;
    private static WishListRecyclerAdapter listsAdapter;
    private ImageView tempIco;
    private Uri newImageUri;
    private ArrayList<Wish> searchRes = new ArrayList<>();
    private static final String file_name = "info.dat";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readSaved();

        if(lists.size() == 0)
            lists.add(new WishList("список",new Date()));

        listName = findViewById(R.id.main_listName);
        listDate = findViewById(R.id.main_listDate);

        RecyclerView listsRecycler = findViewById(R.id.main_listsRecycler);
        listsAdapter = new WishListRecyclerAdapter(this,lists);
        listsRecycler.setAdapter(listsAdapter);

        FloatingActionButton addListBtn = findViewById(R.id.main_addListBtn);
        addListBtn.setOnClickListener(v -> {
            showAddListWindow();
        });

        currentList = lists.get(0);
        RecyclerView wishesRecycler = findViewById(R.id.main_wishesRecycler);
        wishesAdapter = new WishRecyclerAdapter(this,currentList,this);
        wishesRecycler.setAdapter(wishesAdapter);

        FloatingActionButton addWishBtn = findViewById(R.id.main_addWishBtn);
        addWishBtn.setOnClickListener(v -> {
            if(currentList != null){
                showAddWishWindow();
            }
        });

        ImageButton searchBtn = findViewById(R.id.main_searchBtn);
        EditText searchET = findViewById(R.id.main_search);
        searchBtn.setOnClickListener(v -> {
            String text = searchET.getText().toString();
            if(text.length() == 0){
                wishesAdapter.setWishList(currentList);
            }
            else{
                searchRes.clear();
                for(Wish w:currentList.getList()){
                    if(w.getName().contains(text) || w.getDescription().contains(text)){
                        searchRes.add(w);
                    }
                }
                if(searchRes.size() == 0){
                    Toast.makeText(this,"Ничего не найдено",Toast.LENGTH_SHORT).show();
                }
                wishesAdapter.setWishList(searchRes);
            }
        });

        setCurrentList(currentList);
    }

    public void saveToFile(){
        try(ObjectOutputStream oos = new ObjectOutputStream(this.openFileOutput(file_name,Context.MODE_PRIVATE));
            ObjectInputStream ois = new ObjectInputStream(this.openFileInput(file_name))){
            //ois.readObject();
            oos.writeObject(lists);
        }catch (Exception ex){
            Toast.makeText(this,"ex.getMessage()",Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
    void readSaved(){
        try(ObjectInputStream ois = new ObjectInputStream(this.openFileInput(file_name))){
            Object o = ois.readObject();
            if(o == null) lists = new ArrayList<>();
            else{
                lists = (ArrayList<WishList>) o;
            }
        }catch (FileNotFoundException ex){

        }
        catch (Exception ex){
            Toast.makeText(this,"ex.getMessage()",Toast.LENGTH_LONG).show();
            lists = new ArrayList<>();
            ex.printStackTrace();
        }
    }

    public static void setCurrentList(WishList currentList) {
        MainActivity.currentList = currentList;
        if(listName != null){
            listName.setText(currentList.getName());
        }
        if(listDate != null){
            listDate.setText(currentList.getFormattedDate());
        }
        wishesAdapter.setWishList(currentList);
    }

    private void showAddListWindow() {
        AlertDialog.Builder a_builder = new AlertDialog.Builder(this);
        final View addListView = getLayoutInflater().inflate(R.layout.popup_add_list, null);
        MainActivity activity = this;

        EditText nameET = addListView.findViewById(R.id.addList_name);
        ImageView ico = addListView.findViewById(R.id.addList_ico);
        ico.setOnClickListener(v -> {
            tempIco = ico;
            ImagePicker.with(activity)
                    .crop(1,1)	    			//Crop image(Optional), Check Customization for more option
                    .compress(512)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(512, 512)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });

        final Date[] selectedDate = new Date[1];
        Button dateBtn = addListView.findViewById(R.id.addList_dateBtn);
        dateBtn.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    selectedDate[0] = new Date(year - 1900, month, dayOfMonth);
                    dateBtn.setText(dayOfMonth + "/" + month + "/" + year);
                }
            }, year, month, day);
            dpd.show();
        });

        a_builder.setView(addListView);
        a_builder.setPositiveButton("Создать", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(selectedDate[0] == null){
                    Toast.makeText(activity,"Выберите дату!!!",Toast.LENGTH_SHORT).show();
                    return;
                }
                String name = nameET.getText().toString();
                if(name.length() == 0){
                    Toast.makeText(activity,"Введите имя!!!",Toast.LENGTH_SHORT).show();
                    return;
                }
                WishList list = new WishList(name,selectedDate[0]);
                list.setIcoURI(newImageUri);
                newImageUri = null;
                lists.add(list);
                saveToFile();
                listsAdapter.notifyDataSetChanged();
                Toast.makeText(activity,"Создан список",Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = a_builder.create();
        dialog.show();
    }

    private void showAddWishWindow(){
        AlertDialog.Builder a_builder = new AlertDialog.Builder(this);
        final View addWishView = getLayoutInflater().inflate(R.layout.popup_add_wish, null);
        MainActivity activity = this;

        EditText nameET = addWishView.findViewById(R.id.addWish_name);
        EditText linkET = addWishView.findViewById(R.id.addWish_link);
        EditText priceET = addWishView.findViewById(R.id.addWish_price);
        EditText descriptionET = addWishView.findViewById(R.id.addWish_description);
        ImageView ico = addWishView.findViewById(R.id.addWish_ico);
        ImageButton copyLinkBtn = addWishView.findViewById(R.id.addWish_copyLinkBtn);

        ico.setOnClickListener(v -> {
            tempIco = ico;
            ImagePicker.with(activity)
                    .crop(1,1)	    			//Crop image(Optional), Check Customization for more option
                    .compress(512)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(512, 512)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });

        copyLinkBtn.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("ссылка",linkET.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(activity,"Скопировано",Toast.LENGTH_SHORT).show();
        });

        a_builder.setView(addWishView);
        a_builder.setPositiveButton("Создать", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = nameET.getText().toString();
                if(name.length() == 0){
                    Toast.makeText(activity,"Введите имя!!!",Toast.LENGTH_SHORT).show();
                    return;
                }
                double price;
                try{
                    price = Double.parseDouble(priceET.getText().toString());
                }catch (NumberFormatException ex){
                    price = 0.0;
                }
                Wish wish = new Wish(name,linkET.getText().toString(),price,descriptionET.getText().toString());
                wish.setIcoUri(newImageUri);
                wish.setPinned(false);
                newImageUri = null;
                currentList.getList().add(wish);
                currentList.sort();
                saveToFile();
                wishesAdapter.notifyDataSetChanged();
                Toast.makeText(activity,"Создано желание",Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = a_builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            tempIco.setImageURI(uri);
            newImageUri = uri;
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }


}