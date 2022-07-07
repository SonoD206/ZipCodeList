package com.gmail.tkyws505.zipcodelist;

import android.content.Context;
import android.content.res.AssetManager;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

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

public class CsvParser {
  private final Context context;
  private final ZipCodeViewModel viewModel;

  public CsvParser(Context context, LifecycleOwner owner) {
    this.context = context;
    viewModel = new ViewModelProvider((ViewModelStoreOwner) owner).get(ZipCodeViewModel.class);
  }

  public void readCsv() throws InterruptedException {
    AssetManager assetManager = context.getResources().getAssets();
    AtomicReference<List<ZipCodeModel>> atomicZipCodes = new AtomicReference<>();
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    executorService.submit(() -> {
      try {
        InputStream inputStream = assetManager.open("13TOKYO.csv");
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        List<ZipCodeModel> models = new ArrayList<>();
        while ((line = bufferedReader.readLine()) != null) {
          ZipCodeModel model = new ZipCodeModel();
          String[] rowData = line.split(",");
          model.setZipCode(rowData[2]);
          model.setWardName(rowData[7]);
          if (rowData[8].equals("以下に掲載がない場合")) {
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
    if (!executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)) {
      executorService.shutdownNow();
    }
    viewModel.getLiveDataZipCode().setValue(atomicZipCodes.get());
  }
}
