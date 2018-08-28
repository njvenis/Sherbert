package nickvenis.sherbert;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;

public class home2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    sqlhelperbench benchdb;
    sqlhelpersquat squatdb;
    sqlhelpercandj candjdb;
    sqlhelperdeadlift dldb;
    TextView benchkg, squatkg, candjkg, deadliftkg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        benchdb = new sqlhelperbench(this);
        squatdb = new sqlhelpersquat(this);
        candjdb = new sqlhelpercandj(this);
        dldb = new sqlhelperdeadlift(this);
        benchkg = (TextView) findViewById(R.id.benchkglabel);
        squatkg = (TextView) findViewById(R.id.squatkg);
        candjkg = (TextView) findViewById(R.id.candjkg);
        deadliftkg = (TextView) findViewById(R.id.deadliftkg);

        loadbenchmax();
        loadsquatmax();
        loadcandjmax();
        loaddlmax();
        loadinfo();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ImageView open = (ImageView) findViewById(R.id.opensettings);
        open.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(home2.this, settings.class);
                startActivity(i);
            }
        });

        LinearLayout benchlin = (LinearLayout) findViewById(R.id.benchclick);
        benchlin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(home2.this, bench.class);
                        startActivity(i);
                    }
                }
        );
        LinearLayout squtatlin = (LinearLayout) findViewById(R.id.squatclick);
        squtatlin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(home2.this, squat.class);
                        startActivity(i);
                    }
                }
        );
        LinearLayout candjlinear = (LinearLayout) findViewById(R.id.candjlin);
        candjlinear.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(home2.this, candj.class);
                        startActivity(i);
                    }
                }
        );
        LinearLayout dllinear = (LinearLayout) findViewById(R.id.dllin);
        dllinear.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(home2.this, deadlift.class);
                        startActivity(i);
                    }
                }
        );

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.homemenu) {
            Intent i = new Intent(this, home2.class);
            startActivity(i);
        } else if (id == R.id.findgymmenu) {
            Intent i = new Intent(this, findgym.class);
            startActivity(i);
        } else if (id == R.id.benchmenu) {
            Intent i = new Intent(this, bench.class);
            startActivity(i);
        } else if (id == R.id.squatmenu) {
            Intent i = new Intent(this, squat.class);
            startActivity(i);
        } else if (id == R.id.cleanmenu) {
            Intent i = new Intent(this, candj.class);
            startActivity(i);
        } else if (id == R.id.deadliftmenu) {
            Intent i = new Intent(this, deadlift.class);
            startActivity(i);
        } else if (id == R.id.settingsmenu) {
            Intent i = new Intent(this, settings.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void loadinfo() {
        TextView username = (TextView) findViewById(R.id.user2);
        ImageView profilePic = (ImageView) findViewById(R.id.profile2);
        try {
            KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
            ks.load(null);
            String alias = "login_key";


            SharedPreferences userInfo = getSharedPreferences("userData", MODE_PRIVATE);
            String eUser = userInfo.getString("user", "");
            String eURL = userInfo.getString("profileurl", "");


            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)ks.getEntry(alias, null);

            Cipher output = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());

            String cipherText = eUser;
            CipherInputStream cipherInputStream = new CipherInputStream(
                    new ByteArrayInputStream(Base64.decode(cipherText, Base64.DEFAULT)), output);
            ArrayList<Byte> values = new ArrayList<>();
            int nextByte;
            while ((nextByte = cipherInputStream.read()) != -1) {
                values.add((byte)nextByte);
            }

            byte[] bytes = new byte[values.size()];
            for(int i = 0; i < bytes.length; i++) {
                bytes[i] = values.get(i).byteValue();
            }

            String finalText = new String(bytes, 0, bytes.length, "UTF-8");
            username.setText(finalText);

            Cipher output2 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            output2.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());

            String cipherText2 = eURL;
            CipherInputStream cipherInputStream2 = new CipherInputStream(
                    new ByteArrayInputStream(Base64.decode(cipherText2, Base64.DEFAULT)), output2);
            ArrayList<Byte> values2 = new ArrayList<>();
            int nextByte2;
            while ((nextByte2 = cipherInputStream2.read()) != -1) {
                values2.add((byte)nextByte2);
            }

            byte[] bytes2 = new byte[values2.size()];
            for(int i = 0; i < bytes2.length; i++) {
                bytes2[i] = values2.get(i).byteValue();
            }

            String finalText2 = new String(bytes2, 0, bytes2.length, "UTF-8");
            Picasso.with(this).load(finalText2).into(profilePic);

        } catch (Exception e) {
            Log.e("DECRYPT", Log.getStackTraceString(e));
        }
    }
    public void loadbenchmax (){
        int max = benchdb.getHighestVal();
        benchkg.setText(Integer.toString(max)+"kg");

    }
    public void loadsquatmax(){
        int max = squatdb.getHighestVal();
        squatkg.setText(Integer.toString(max)+"kg");
    }
    public void loadcandjmax(){
        int max = candjdb.getHighestVal();
        candjkg.setText(Integer.toString(max)+"kg");
    }
    public void loaddlmax(){
        int max = dldb.getHighestVal();
        deadliftkg.setText(Integer.toString(max)+"kg");
    }

}

