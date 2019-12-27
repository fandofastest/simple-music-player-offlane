package com.tutu.offlinemusic;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private RecyclerView rvHeroes;
    public String title,banner,inter,appid;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;



    private ArrayList<Hero> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


          title = getString(R.string.app_name);

        setActionBarTitle(title);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

         appid = getIntent().getStringExtra("appid");
         inter = getIntent().getStringExtra("inter");

         banner = getIntent().getStringExtra("banner");


        rvHeroes = findViewById(R.id.rv_hero);
        rvHeroes.setHasFixedSize(true);
        list.addAll(getListData());
        showRecyclerList();





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setMode(item.getItemId());
        return super.onOptionsItemSelected(item);
    }
    public void setMode(int selectedMode) {
        switch (selectedMode) {
            case R.id.action_list:
                title = getString(R.string.app_name);

                showRecyclerList();

                break;
            case R.id.action_grid:
                title = getString(R.string.app_name);

                break;
            case R.id.action_cardview:
                title = getString(R.string.app_name);


                break;
        }
    }
    private void showRecyclerList(){
        rvHeroes.setLayoutManager(new LinearLayoutManager(this));
        ListHeroAdapter listHeroAdapter = new ListHeroAdapter(list);
        rvHeroes.setAdapter(listHeroAdapter);

        listHeroAdapter.setOnItemClickCallback(new ListHeroAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(final Hero data) {



                showSelectedHero(data);



            }
        });
    }



    private void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    private void showSelectedHero(Hero hero) {
        Intent pindah = new Intent(MainActivity.this,Player.class);
        pindah.putExtra("judul",hero.getJudul());


        pindah.putExtra("namafile",hero.getNamafile());
        pindah.putExtra("foto",hero.getFoto());
        pindah.putExtra("banner",banner);
        startActivity(pindah);



    }


    public void loadiklan( String inter){

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(inter);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());







    }


    public void loadbanner(String banner){

        AdView mAdView = new AdView(this);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(banner);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        if(mAdView.getAdSize() != null || mAdView.getAdUnitId() != null)
            mAdView.loadAd(adRequest);
        // else Log state of adsize/adunit
        ((LinearLayout)findViewById(R.id.adView)).addView(mAdView);
    }

    public void showinters(){

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            loadiklan(inter);
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Rate Us:");
        alert.setMessage("You want to Rate App ?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id="
                                    + getPackageName())));
                }

                dialog.dismiss();
                finish();
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finishAffinity();

                finish();
            }
        });
        alert.create();
        alert.show();

    }



    public ArrayList<Hero> getListData(){
        final AssetManager assetManager = getAssets();

        ArrayList<Hero> list = new ArrayList<>();
        try {
            // for assets folder add empty string
            String[] filelist = assetManager.list("music");
            // for assets/subFolderInAssets add only subfolder name
            if (filelist == null) {
                // dir does not exist or is not a directory
            } else {



                for (int i=0; i<filelist.length; i++) {
                    // Get filename of file or directory
                    String filename = filelist[i];


                    Hero hero = new Hero();
                    hero.setNamafile(filelist[i]);
                    hero.setDeskripsi(filelist[i]);
                    String tmpjudul= filelist[i].replace(".mp3","");
                    String tmpjudul1=tmpjudul.replace("_"," ");


                    String upperString = tmpjudul1.substring(0,1).toUpperCase() + tmpjudul1.substring(1);

                    hero.setJudul(upperString);




                    list.add(hero);
                }
            }

            // if(filelistInSubfolder == null) ............

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }




}
