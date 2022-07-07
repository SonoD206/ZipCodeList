package com.gmail.tkyws505.zipcodelist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;


public class ListActivity extends AppCompatActivity implements LifecycleOwner {

  private ZipCodeViewModel viewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list);
    initialize();
    try {
      readCsv();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * 使用可能の状態にする
   */
  private void initialize() {
    Toolbar toolbar = findViewById(R.id.toolbar);
    RecyclerView recyclerZipCode = findViewById(R.id.recycler_zip_code);
    setSupportActionBar(toolbar);
    ZipCodeListAdapter zipCodeListAdapter = new ZipCodeListAdapter();
    viewModel = new ViewModelProvider(this).get(ZipCodeViewModel.class);
    recyclerZipCode.setAdapter(zipCodeListAdapter);
    viewModel.getLiveDataZipCode().observe(this, models -> {
      Log.i("###", "initialize: observe");
      zipCodeListAdapter.updateModels(models);
    });
  }

  public void readCsv() throws InterruptedException {
    AssetManager assetManager = this.getResources().getAssets();
    AtomicReference<List<ZipCodeModel>> atomicZipCodes = new AtomicReference<>();
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    executorService.submit(() -> {
      try {
        InputStream inputStream = assetManager.open("13TOKYO.csv");
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        List<ZipCodeModel> models = new ArrayList<>();
        while ((line = bufferedReader.readLine()) != null){
          ZipCodeModel model = new ZipCodeModel();
          String[] rowData = line.split(",");
          model.setZipCode(rowData[2]);
          model.setWardName(rowData[7]);
          if (rowData[8].equals("以下に掲載がない場合")){
            model.setStreetName("");
          } else {
            model.setStreetName(rowData[8]);
          }
          models.add(model);
        }
        bufferedReader.close();
        atomicZipCodes.set(models);
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    executorService.shutdown();
    if (!executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)){
      executorService.shutdownNow();
    }
    viewModel.getLiveDataZipCode().setValue(atomicZipCodes.get());
  }
}