package beastandroidapps.beastweather.activites;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import beastandroidapps.beastweather.R;
import beastandroidapps.beastweather.fragments.ForcastFragment;

public class MainActivity extends BaseFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return ForcastFragment.newInstance();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                startActivity(new Intent(this,SettingActivity.class));
                break;
        }
        return true;
    }
}
