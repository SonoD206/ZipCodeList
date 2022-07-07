package com.gmail.tkyws505.zipcodelist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class ListActivity extends AppCompatActivity implements LifecycleOwner {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list);
    initialize();
  }

  /**
   * 使用可能の状態にする
   */
  private void initialize() {
    Toolbar toolbar = findViewById(R.id.toolbar);
    RecyclerView recyclerZipCode = findViewById(R.id.recycler_zip_code);
    setSupportActionBar(toolbar);
    ZipCodeListAdapter zipCodeListAdapter = new ZipCodeListAdapter();
    ZipCodeViewModel viewModel = new ViewModelProvider(this).get(ZipCodeViewModel.class);
    CsvParser parser = new CsvParser(this, this);
    recyclerZipCode.setAdapter(zipCodeListAdapter);
    viewModel.getLiveDataZipCode().observe(this, zipCodeListAdapter::updateModels);
    try {
      parser.readCsv();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}