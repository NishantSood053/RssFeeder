package com.test.rssfeeder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, FragmentDrawer.FragmentDrawerListener {

    private static String TAG = FragmentDrawer.class.getSimpleName();

    //---------PRIVATE VARIABLES-----------------//
    private Menu menu;
    private ListView listView;
    private RecyclerView rv;
    private DrawerLayout drawerLayout;
    private ProgressBar mProgressBar;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private boolean isListView;
    private Toolbar mToolbar;
    private FragmentDrawer mdrawerFragment;
    //-------------------------------------------//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview);

        mToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        //listView = (ListView) findViewById(R.id.listView);
        rv = (RecyclerView) findViewById(R.id.recyclerview);
        rv.setHasFixedSize(true);
        mProgressBar = (ProgressBar) findViewById(R.id.loadProgressMain);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        rv.setLayoutManager(mStaggeredLayoutManager);

        mdrawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        mdrawerFragment.setUp(R.id.fragment_navigation_drawer, drawerLayout ,mToolbar);
        mdrawerFragment.setDrawerListener(this);


        /*
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        */
        new RssDataController(this).execute("http://feeds.feedburner.com/AndroidPolice?format=xml");

        //Show snack bar if network is not available
        if(!isNetworkAvaialable())
        {
            ShowSnackBar("No Internet Connection");

        }

        isListView = true;


    }

    public void ShowSnackBar(String title)
    {
        Snackbar snackbar = Snackbar.make(drawerLayout,title,Snackbar.LENGTH_LONG)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!isNetworkAvaialable())
                        {
                            ShowSnackBar("No Internet Connection");
                        }
                    }
                });

        snackbar.setActionTextColor(Color.RED);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);

        snackbar.show();

    }

    public void setmAdapter(RssAdapter mAdapter)
    {
       // listView.setAdapter(mAdapter);
       // listView.setOnItemClickListener(this);
    }

    public void setmCardAdapter(RssCardAdapter mCardAdapter)
    {
        rv.setAdapter(mCardAdapter);
        mProgressBar.setVisibility(View.GONE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_toggle) {
            toggle();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void toggle()
    {
        MenuItem item = menu.findItem(R.id.action_toggle);
        if(isListView)
        {
            item.setIcon(R.drawable.ic_action_list);
            mStaggeredLayoutManager.setSpanCount(2);
            isListView = false;
        }else
        {
            item.setIcon(R.drawable.ic_action_grid);
            mStaggeredLayoutManager.setSpanCount(1);
            isListView = true;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        RssDataModel mClicked = (RssDataModel)listView.getItemAtPosition(position);
        Intent mIntent = new Intent(this,RssWebView.class);
        mIntent.putExtra("LINK",mClicked.itemDescription);
        startActivity(mIntent);

    }

    public void itemClicked(RssDataModel mDataModel)
    {

    }

    private boolean isNetworkAvaialable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo!= null && networkInfo.isConnected();
    }


    @Override
    public void onDrawerItemSelected(View view, int position) {

        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position)
        {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                break;
        }

        if(fragment != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body,fragment);
            fragmentTransaction.commit();

            //Set the toolbar title
            getSupportActionBar().setTitle(title);

        }
    }
}
