package com.metrox.demo61;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnSelectRegion;
    private Button btnInitData;
    private Button btnInitTestData;
    private TextView tvSelectedRegion;
    private TextView tvRegionDetail;
    private RegionPicker regionPicker;

    private Region selectedProvince;
    private Region selectedCity;
    private Region selectedDistrict;
    private Region selectedTown;
    private Region selectedCommunity;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initRegionPicker();
        setupClickListeners();

        // 检查数据状态
        checkDataStatus();
    }

    private void initViews() {
        btnSelectRegion = findViewById(R.id.btn_select_region);
        tvSelectedRegion = findViewById(R.id.tv_selected_region);
        tvRegionDetail = findViewById(R.id.tv_region_detail);
        btnInitData = findViewById(R.id.btn_init_data);
        btnInitTestData = findViewById(R.id.btn_init_test_data);

        // 默认禁用选择按钮，直到数据初始化完成
        btnSelectRegion.setEnabled(false);
    }

    private void initRegionPicker() {
        regionPicker = new RegionPicker(this);
    }

    private void setupClickListeners() {
        btnSelectRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegionPicker();
            }
        });

        btnInitData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initRegionDataFromJson();
            }
        });

        btnInitTestData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initRegionTestData();
            }
        });
    }

    private void checkDataStatus() {
        if (regionPicker.hasData()) {
            btnSelectRegion.setEnabled(true);
            btnInitData.setVisibility(View.GONE);
            btnInitTestData.setVisibility(View.GONE);
            Toast.makeText(this, "地区数据已加载，可以开始选择", Toast.LENGTH_SHORT).show();
        } else {
            btnSelectRegion.setEnabled(false);
            btnInitData.setVisibility(View.VISIBLE);
            btnInitTestData.setVisibility(View.VISIBLE);
            Toast.makeText(this, "请先初始化地区数据", Toast.LENGTH_LONG).show();
        }
    }

    private void showRegionPicker() {
        // 设置默认选中的地区（如果有的话）
        int provinceId = selectedProvince != null ? selectedProvince.getId() : 0;
        int cityId = selectedCity != null ? selectedCity.getId() : 0;
        int districtId = selectedDistrict != null ? selectedDistrict.getId() : 0;
        int townId = selectedTown != null ? selectedTown.getId() : 0;
        int communityId = selectedCommunity != null ? selectedCommunity.getId() : 0;

        regionPicker.showPicker(provinceId, cityId, districtId, townId, communityId,
                new RegionPickerDialog.OnRegionSelectedListener() {
                    @Override
                    public void onRegionSelected(Region province, Region city,
                                                 Region district, Region town, Region community) {
                        // 保存选中的地区
                        selectedProvince = province;
                        selectedCity = city;
                        selectedDistrict = district;
                        selectedTown = town;
                        selectedCommunity = community;

                        // 显示完整的地区字符串
                        String fullRegion = regionPicker.getFullRegionString(
                                province, city, district, town, community);
                        tvSelectedRegion.setText("已选择: " + fullRegion);

                        // 显示详细的地区信息
                        showRegionDetails();
                    }
                });
    }

    private void showRegionDetails() {
        StringBuilder details = new StringBuilder("选中地区详情：\n");

        if (selectedProvince != null) {
            details.append("省：").append(selectedProvince.getName())
                    .append(" (ID: ").append(selectedProvince.getId()).append(")\n");
        }

        if (selectedCity != null) {
            details.append("市：").append(selectedCity.getName())
                    .append(" (ID: ").append(selectedCity.getId()).append(")\n");
        }

        if (selectedDistrict != null) {
            details.append("区：").append(selectedDistrict.getName())
                    .append(" (ID: ").append(selectedDistrict.getId()).append(")\n");
        }

        if (selectedTown != null) {
            details.append("镇：").append(selectedTown.getName())
                    .append(" (ID: ").append(selectedTown.getId()).append(")\n");
        }

        if (selectedCommunity != null) {
            details.append("社区：").append(selectedCommunity.getName())
                    .append(" (ID: ").append(selectedCommunity.getId()).append(")\n");
        }

        tvRegionDetail.setText(details.toString());
    }

    private void initRegionDataFromJson() {
        // 显示进度对话框
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在从JSON文件初始化地区数据...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // 初始化地区数据（从JSON文件加载）
        regionPicker.initDataFromJson(new RegionPicker.DataInitCallback() {
            @Override
            public void onInitComplete(int count) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(MainActivity.this,
                                "地区数据初始化完成！共加载" + count + "条数据",
                                Toast.LENGTH_SHORT).show();
                        checkDataStatus();
                    }
                });
            }

            @Override
            public void onInitError(String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(MainActivity.this,
                                "数据初始化失败: " + error,
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void initRegionTestData() {
        // 显示进度对话框
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在初始化测试数据...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // 初始化测试数据
        regionPicker.initTestData(new RegionPicker.DataInitCallback() {
            @Override
            public void onInitComplete(int count) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(MainActivity.this,
                                "测试数据初始化完成！共加载" + count + "条数据",
                                Toast.LENGTH_SHORT).show();
                        checkDataStatus();
                    }
                });
            }

            @Override
            public void onInitError(String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(MainActivity.this,
                                "测试数据初始化失败: " + error,
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}