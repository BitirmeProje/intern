package yusufcakal.com.stajtakip;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import com.heinrichreimersoftware.materialdrawer.DrawerActivity;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import yusufcakal.com.stajtakip.fragments.firma.FirmalarFragment;
import yusufcakal.com.stajtakip.fragments.staj.StajlarFragment;
import yusufcakal.com.stajtakip.pojo.Staj;
import yusufcakal.com.stajtakip.webservices.interfaces.FragmentListener;
import yusufcakal.com.stajtakip.webservices.util.SessionUtil;

public class DashboardActivity extends DrawerActivity implements FragmentListener{

    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        toolbar = getSupportActionBar();
        setTitle(getResources().getString(R.string.firmalar));
        openFragment(new FirmalarFragment());

        addItem(
                new DrawerItem()
                        .setImage(getResources().getDrawable(R.drawable.ic_home_black_24dp))
                        .setTextPrimary(getString(R.string.firma))
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, long id, int position) {
                                setTitle(getResources().getString(R.string.firmalar));
                                openFragment(new FirmalarFragment());
                                DashboardActivity.this.closeDrawer();
                            }
                        })
        );

        addItem(
                new DrawerItem()
                        .setImage(getResources().getDrawable(R.drawable.ic_home_black_24dp))
                        .setTextPrimary(getString(R.string.staj))
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, long id, int position) {
                                setTitle(getResources().getString(R.string.stajlar));
                                openFragment(new StajlarFragment());
                                DashboardActivity.this.closeDrawer();
                            }
                        })
        );
        addDivider();
        addItem(
                new DrawerItem()
                        .setImage(getResources().getDrawable(R.drawable.ic_last_page_black_24dp))
                        .setTextPrimary(getString(R.string.logout))
                        .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                            @Override
                            public void onClick(DrawerItem drawerItem, long id, int position) {
                                SessionUtil.stop(DashboardActivity.this);
                                startActivity(new Intent(DashboardActivity.this, MainActivity.class));
                            }
                        })
        );

    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //TODO:App is finish
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }

    private void openFragment(final Fragment fragment)   {
        String backStateName = fragment.getClass().getName();

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped){ //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.container, fragment);
            ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            ft.addToBackStack(backStateName);
            ft.commit();
        }

    }

    private void openFragmentWithStaj(final Fragment fragment, Staj staj)   {
        String backStateName = fragment.getClass().getName();

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped){ //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            Bundle args = new Bundle();
            args.putParcelable("staj", staj);
            fragment.setArguments(args);
            ft.replace(R.id.container, fragment);
            ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            ft.addToBackStack(backStateName);
            ft.commit();
        }

    }

    private void setTitle(String title){
        toolbar.setTitle(title);
    }


    @Override
    public void onStart(Fragment fragment) {
        openFragment(fragment);
    }

    @Override
    public void onStart(Fragment fragment, Staj staj) {
        openFragmentWithStaj(fragment, staj);
    }
}
