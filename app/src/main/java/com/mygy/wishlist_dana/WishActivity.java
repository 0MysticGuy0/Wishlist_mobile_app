package com.mygy.wishlist_dana;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.mygy.wishlist_dana.databinding.ActivityWishBinding;

public class WishActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityWishBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish);

        ImageButton backBtn = findViewById(R.id.wishActivity_backBtn);
        backBtn.setOnClickListener(v -> {
            finish();
        });

        Bundle arguments = getIntent().getExtras();

        Wish wish;
        if(arguments!=null){
            wish = (Wish) arguments.getSerializable(Wish.class.getSimpleName());
            ImageView ico = findViewById(R.id.wishActivity_ico);
            if(wish.getIcoUri() == null) ico.setImageResource(R.drawable.img);
            else ico.setImageURI(wish.getIcoUri());

            TextView name = findViewById(R.id.wishActivity_name);
            name.setText(wish.getName());

            TextView link = findViewById(R.id.wishActivity_link);
            link.setText(wish.getLink());

            TextView price = findViewById(R.id.wishActivity_price);
            price.setText( Double.toString(wish.getPrice()) );

            TextView descr = findViewById(R.id.wishActivity_description);
            descr.setText(wish.getDescription());

            ImageButton copy = findViewById(R.id.wishActivity_copyLinkBtn);
            copy.setOnClickListener(v -> {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("ссылка",link.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this,"Скопировано",Toast.LENGTH_SHORT).show();
            });
        }



    }

}