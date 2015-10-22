package it.jaschke.alexandria;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import it.jaschke.alexandria.api.Callback;


public class MainActivity extends AppCompatActivity implements Callback, FragmentBase.FragmentBaseListener {
    
    private Toolbar mTtoolbar;
    private FloatingActionButton mFab;

    public static boolean IS_TABLET = false;
    private BroadcastReceiver messageReciever;

    public static final String MESSAGE_EVENT = "MESSAGE_EVENT";
    public static final String MESSAGE_KEY = "MESSAGE_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IS_TABLET = isTablet();
        if (IS_TABLET) {
            setContentView(R.layout.activity_main_tablet);
        } else {
            setContentView(R.layout.activity_main);
        }

        mTtoolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mTtoolbar != null) {
            setSupportActionBar(mTtoolbar);
        }


        mFab = (FloatingActionButton) findViewById(R.id.fab);
        if (mFab != null) {
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToFragment(new AddBook());
                }
            });
        }

        navigateToFragment(new ListOfBooks());

        messageReciever = new MessageReciever();
        IntentFilter filter = new IntentFilter(MESSAGE_EVENT);
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReciever, filter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReciever);
        super.onDestroy();
    }

    private void navigateToFragment(Fragment fragment) {
        boolean shouldBackStack = !(fragment instanceof ListOfBooks);

        int id = R.id.container;

        if (fragment instanceof BookDetail && findViewById(R.id.right_container) != null) {
            id = R.id.right_container;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(id, fragment);
        if (shouldBackStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    @Override
    public void onItemSelected(String ean) {
        Bundle args = new Bundle();
        args.putString(BookDetail.EAN_KEY, ean);

        BookDetail fragment = new BookDetail();
        fragment.setArguments(args);

        navigateToFragment(fragment);
    }

    @Override
    public void setToolBar(CharSequence title) {
        if (!IS_TABLET && mTtoolbar != null) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                if (title != null) {
                    if (title.equals(getString(R.string.list_books_name))) {
                        actionBar.setDisplayHomeAsUpEnabled(false);
                        mFab.setVisibility(View.VISIBLE);
                    }
                    else {
                        actionBar.setDisplayHomeAsUpEnabled(true);
                        mFab.setVisibility(View.INVISIBLE);
                    }
                    actionBar.setTitle(title);
                } else {
                    actionBar.setDisplayHomeAsUpEnabled(false);

                    actionBar.setTitle(getString(R.string.app_name));
                }
            }
        }
    }

    private class MessageReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra(MESSAGE_KEY) != null) {
                Toast.makeText(MainActivity.this, intent.getStringExtra(MESSAGE_KEY), Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean isTablet() {
        return (getApplicationContext().getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}