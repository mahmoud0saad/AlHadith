package com.mahmoud.hadith.ui.activities.azkar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mahmoud.hadith.R;
import com.mahmoud.hadith.databinding.ActivityAzkarBinding;
import com.mahmoud.hadith.model.utils.Utils;
import com.mahmoud.hadith.model.viewmodel.azkar.AzkarViewModel;
import com.mahmoud.hadith.ui.activities.base.BaseActivity;
import com.mahmoud.hadith.ui.adapter.AzkarRecyclerAdapter;

import java.util.ArrayList;

public class AzkarActivity extends BaseActivity {
    private static final String TAG = "AzkarActivity";

    private ActivityAzkarBinding mAzkarBinding;
    private AzkarViewModel mAzkarViewModel;
    private AzkarRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAzkarBinding = DataBindingUtil.setContentView(this, R.layout.activity_azkar);

        mAzkarViewModel = ViewModelProviders.of(this).get(AzkarViewModel.class);


        initViews();
    }

    private void initViews() {

        mAdapter = new AzkarRecyclerAdapter();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        mAzkarBinding.azkarRecyclerView.setLayoutManager(linearLayoutManager);

        mAzkarBinding.azkarRecyclerView.setAdapter(mAdapter);


        mAdapter.setmItemList(mAzkarViewModel.getAzkarList());


        mAzkarBinding.addAzkarFabButton.setOnClickListener(this::appearAzkarAdd);

        mAzkarBinding.azkarAddButton.setOnClickListener(this::addAzkarToList);

        initAppBar();


    }

    private void addAzkarToList(View view) {
        disappearAzkarAdd();
        String azkarText = mAzkarBinding.azkarEditText.getText().toString();
        if (!TextUtils.isEmpty(azkarText)) {
            mAdapter.addItem(azkarText);
            mAzkarBinding.azkarEditText.getText().clear();
        }
        Utils.hideSoftKeyboard(mAzkarBinding.azkarEditText);
    }

    private void appearAzkarAdd(View view) {

        mAzkarBinding.inputAzkarCardView.setVisibility(View.VISIBLE);
    }

    private void disappearAzkarAdd() {
        mAzkarBinding.inputAzkarCardView.setVisibility(View.GONE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.azkar_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.restart_azkar) {
            mAdapter.setmItemList(new ArrayList<>(Utils.getDefaultAzkar()));
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAzkarViewModel.saveAzkarList(mAdapter.getSetItem());
    }

    /**
     * set title for action bar by text view and delete the default title toolbar
     */
    private void initAppBar() {
        TextView textView = mAzkarBinding.includeToolbar.myToolbar.findViewById(R.id.title_toolbar_textview);
        textView.setText(getResources().getString(R.string.title_azkar));

        setSupportActionBar(mAzkarBinding.includeToolbar.myToolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");

        }

    }


}
