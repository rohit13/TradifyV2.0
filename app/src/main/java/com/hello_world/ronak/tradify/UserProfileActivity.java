package com.hello_world.ronak.tradify;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Fragment userProfileFragment;
    Toolbar mtoolBar;
    Intent intent;
    ActionBar mActionbar;
    DrawerLayout mdrawerLayout;
    NavigationView mnavigationView;
    static Firebase refNotif = new Firebase("https://tradify.firebaseio.com/Notifications");
    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mtoolBar = (Toolbar) findViewById(R.id.uftoolbar);
        mtoolBar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(mtoolBar);
        mActionbar = getSupportActionBar();
        mActionbar.setDisplayHomeAsUpEnabled(true);

        mtoolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        mnavigationView = (NavigationView) findViewById(R.id.navigation_view_uf);
        Bitmap b = StringToBitMap(UserContext.USERPROFILEURL);
        ((ImageView)mnavigationView.getHeaderView(0).findViewById(R.id.profile_image)).setImageBitmap(b);
        ((TextView)mnavigationView.getHeaderView(0).findViewById(R.id.nav_profile_name)).setText(UserContext.USERNAME);
        mnavigationView.setNavigationItemSelectedListener(this);

        mdrawerLayout = (DrawerLayout) findViewById(R.id.drawerUF);
        ActionBarDrawerToggle actionBarDrawerToggle =new ActionBarDrawerToggle(this,mdrawerLayout,mtoolBar,R.string.abt_od, R.string.abt_cd) {
            @Override
            public  void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }

            @Override
            public  void onDrawerOpened(View v){
                super.onDrawerOpened(v);
            }

        };

        mdrawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        Bundle extra = getIntent().getExtras();
        final String userId = extra.getString("username");
        NotificationsLocal.notification.clear();
        if(userId!=null && !userId.equals(UserContext.USERID)) {
            String notif = "Can't show other user's notifications.";
            NotificationsLocal.notification.add(notif);
        }else {
            refNotif.child(UserContext.USERID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    NotificationsLocal.notification.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String notif = NotificationsLocal.createNotification(ds);
                        NotificationsLocal.notification.add(notif);
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
        userProfileFragment = UserProfileFragment.newInstance();
        userProfileFragment.setArguments(extra);
        getSupportFragmentManager().beginTransaction().replace(R.id.userProfileContainer,userProfileFragment).commitAllowingStateLoss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.register_prod_act_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.editProfile:
                Intent intent = new Intent(UserProfileActivity.this,AccountSettingsActivity.class);
                startActivity(intent);
        }
        return true;
    }

    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.drw_home:
                intent = new Intent(this,Activity_HomeScreen.class);
                startActivity(intent);
                break;
            case R.id.drw_settings:
                intent = new Intent(this,AccountSettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.drw_my_profile:
                //Toast.makeText(getApplicationContext(),"My profile clicked",Toast.LENGTH_SHORT).show();
                intent = new Intent(this,UserProfileActivity.class);
                intent.putExtra("username", UserContext.USERID);
                startActivity(intent);
                break;
            case R.id.drw_abtus:
                intent = new Intent(this,AboutUsActivity.class);
                startActivity(intent);
                break;
            case R.id.drw_logoff:
                LoginActivity.firebaseRef.unauth();
                intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        mdrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}

