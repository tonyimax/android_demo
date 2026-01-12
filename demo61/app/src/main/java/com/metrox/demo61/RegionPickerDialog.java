package com.metrox.demo61;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class RegionPickerDialog extends Dialog {

    public interface OnRegionSelectedListener {
        void onRegionSelected(Region province, Region city,
                              Region district, Region town, Region community);
    }

    private Context context;
    private OnRegionSelectedListener listener;

    private LinearLayout tabContainer;
    private ListView listView;
    private Button btnConfirm, btnCancel;

    private List<Region> provinces = new ArrayList<>();
    private List<Region> cities = new ArrayList<>();
    private List<Region> districts = new ArrayList<>();
    private List<Region> towns = new ArrayList<>();
    private List<Region> communities = new ArrayList<>();

    private Region selectedProvince;
    private Region selectedCity;
    private Region selectedDistrict;
    private Region selectedTown;
    private Region selectedCommunity;

    private int currentLevel = 0;
    private RegionDbHelper dbHelper;

    public RegionPickerDialog(Context context, OnRegionSelectedListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
        dbHelper = RegionDbHelper.getInstance(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_region_picker);

        initViews();
        loadProvinces();
        setupTabs();
    }

    private void initViews() {
        tabContainer = findViewById(R.id.tab_container);
        listView = findViewById(R.id.list_view);
        btnConfirm = findViewById(R.id.btn_confirm);
        btnCancel = findViewById(R.id.btn_cancel);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                handleRegionClick(position);
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onRegionSelected(selectedProvince, selectedCity,
                            selectedDistrict, selectedTown, selectedCommunity);
                }
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void setupTabs() {
        String[] tabTitles = {"省", "市", "区", "镇", "社区"};
        tabContainer.removeAllViews();

        for (int i = 0; i < 5; i++) {
            TextView tabView = new TextView(context);
            tabView.setLayoutParams(new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            tabView.setText(tabTitles[i]);
            tabView.setPadding(16, 16, 16, 16);
            tabView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tabView.setBackgroundResource(i == currentLevel ?
                    R.drawable.bg_tab_selected : R.drawable.bg_tab_normal);
            tabView.setTextColor(context.getResources().getColor(
                    i == currentLevel ? android.R.color.white : android.R.color.black));

            final int level = i;
            tabView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchToLevel(level);
                }
            });

            tabContainer.addView(tabView);
        }
    }

    private void loadProvinces() {
        provinces = dbHelper.getRegionsByParent(0, 0);
        Log.d("RegionPickerDialog", "Loaded " + provinces.size() + " provinces");
        updateListView(provinces);
    }

    private void handleRegionClick(int position) {
        switch (currentLevel) {
            case 0: // 省
                selectedProvince = provinces.get(position);
                selectedCity = null;
                selectedDistrict = null;
                selectedTown = null;
                selectedCommunity = null;
                cities = dbHelper.getRegionsByParent(selectedProvince.getId(), 1);
                Log.d("RegionPickerDialog", "Loaded " + cities.size() + " cities for province " + selectedProvince.getName());
                switchToLevel(1);
                break;

            case 1: // 市
                selectedCity = cities.get(position);
                selectedDistrict = null;
                selectedTown = null;
                selectedCommunity = null;
                districts = dbHelper.getRegionsByParent(selectedCity.getId(), 2);
                Log.d("RegionPickerDialog", "Loaded " + districts.size() + " districts for city " + selectedCity.getName());
                switchToLevel(2);
                break;

            case 2: // 区
                selectedDistrict = districts.get(position);
                selectedTown = null;
                selectedCommunity = null;
                towns = dbHelper.getRegionsByParent(selectedDistrict.getId(), 3);
                Log.d("RegionPickerDialog", "Loaded " + towns.size() + " towns for district " + selectedDistrict.getName());
                switchToLevel(3);
                break;

            case 3: // 镇
                selectedTown = towns.get(position);
                selectedCommunity = null;
                communities = dbHelper.getRegionsByParent(selectedTown.getId(), 4);
                Log.d("RegionPickerDialog", "Loaded " + communities.size() + " communities for town " + selectedTown.getName());
                switchToLevel(4);
                break;

            case 4: // 社区
                selectedCommunity = communities.get(position);
                updateTabs();
                btnConfirm.setEnabled(true);
                break;
        }
        updateTabs();
    }

    private void switchToLevel(int level) {
        currentLevel = level;
        updateTabs();

        switch (level) {
            case 0:
                updateListView(provinces);
                break;
            case 1:
                if (selectedProvince != null && cities != null) {
                    updateListView(cities);
                }
                break;
            case 2:
                if (selectedCity != null && districts != null) {
                    updateListView(districts);
                }
                break;
            case 3:
                if (selectedDistrict != null && towns != null) {
                    updateListView(towns);
                }
                break;
            case 4:
                if (selectedTown != null && communities != null) {
                    updateListView(communities);
                }
                break;
        }
    }

    private void updateTabs() {
        for (int i = 0; i < tabContainer.getChildCount(); i++) {
            TextView tabView = (TextView) tabContainer.getChildAt(i);
            tabView.setBackgroundResource(i == currentLevel ?
                    R.drawable.bg_tab_selected : R.drawable.bg_tab_normal);
            tabView.setTextColor(context.getResources().getColor(
                    i == currentLevel ? android.R.color.white : android.R.color.black));

            // 更新标签文本，显示已选内容
            if (i == 0 && selectedProvince != null) {
                tabView.setText(selectedProvince.getName());
            } else if (i == 1 && selectedCity != null) {
                tabView.setText(selectedCity.getName());
            } else if (i == 2 && selectedDistrict != null) {
                tabView.setText(selectedDistrict.getName());
            } else if (i == 3 && selectedTown != null) {
                tabView.setText(selectedTown.getName());
            } else if (i == 4 && selectedCommunity != null) {
                tabView.setText(selectedCommunity.getName());
            } else {
                String[] tabTitles = {"省", "市", "区", "镇", "社区"};
                tabView.setText(tabTitles[i]);
            }
        }
    }

    private void updateListView(List<Region> regions) {
        List<String> names = new ArrayList<>();
        if (regions != null) {
            for (Region region : regions) {
                names.add(region.getName());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1, names);
        listView.setAdapter(adapter);
    }

    public void setSelectedRegions(int provinceId, int cityId, int districtId,
                                   int townId, int communityId) {
        if (provinceId > 0) {
            selectedProvince = dbHelper.getRegionById(provinceId);
            if (selectedProvince != null) {
                cities = dbHelper.getRegionsByParent(provinceId, 1);
            }
        }
        if (cityId > 0) {
            selectedCity = dbHelper.getRegionById(cityId);
            if (selectedCity != null) {
                districts = dbHelper.getRegionsByParent(cityId, 2);
            }
        }
        if (districtId > 0) {
            selectedDistrict = dbHelper.getRegionById(districtId);
            if (selectedDistrict != null) {
                towns = dbHelper.getRegionsByParent(districtId, 3);
            }
        }
        if (townId > 0) {
            selectedTown = dbHelper.getRegionById(townId);
            if (selectedTown != null) {
                communities = dbHelper.getRegionsByParent(townId, 4);
            }
        }
        if (communityId > 0) {
            selectedCommunity = dbHelper.getRegionById(communityId);
        }
    }
}