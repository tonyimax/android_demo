package com.metrox.demo61;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RegionPicker {

    public interface DataInitCallback {
        void onInitComplete(int count);
        void onInitError(String error);
    }

    private Context context;
    private RegionDbHelper dbHelper;

    public RegionPicker(Context context) {
        this.context = context;
        dbHelper = RegionDbHelper.getInstance(context);
    }

    // 显示地区选择对话框
    public void showPicker(RegionPickerDialog.OnRegionSelectedListener listener) {
        showPicker(0, 0, 0, 0, 0, listener);
    }

    public void showPicker(int provinceId, int cityId, int districtId,
                           int townId, int communityId,
                           RegionPickerDialog.OnRegionSelectedListener listener) {
        if (!hasData()) {
            Toast.makeText(context, "请先初始化地区数据", Toast.LENGTH_LONG).show();
            return;
        }

        RegionPickerDialog dialog = new RegionPickerDialog(context, listener);
        dialog.setSelectedRegions(provinceId, cityId, districtId, townId, communityId);
        dialog.show();
    }

    // 从JSON文件初始化数据
    public void initDataFromJson(final DataInitCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    dbHelper.clearAllRegions();

                    InputStream is = context.getAssets().open("regions.json");
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();

                    String json = new String(buffer, "UTF-8");
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray provinces = jsonObject.getJSONArray("provinces");

                    List<Region> allRegions = new ArrayList<>();

                    for (int i = 0; i < provinces.length(); i++) {
                        JSONObject province = provinces.getJSONObject(i);
                        Region provinceRegion = new Region(
                                province.getInt("id"),
                                province.getString("name"),
                                0, 0
                        );
                        allRegions.add(provinceRegion);

                        JSONArray cities = province.getJSONArray("cities");
                        for (int j = 0; j < cities.length(); j++) {
                            JSONObject city = cities.getJSONObject(j);
                            Region cityRegion = new Region(
                                    city.getInt("id"),
                                    city.getString("name"),
                                    provinceRegion.getId(), 1
                            );
                            allRegions.add(cityRegion);

                            JSONArray districts = city.getJSONArray("districts");
                            for (int k = 0; k < districts.length(); k++) {
                                JSONObject district = districts.getJSONObject(k);
                                Region districtRegion = new Region(
                                        district.getInt("id"),
                                        district.getString("name"),
                                        cityRegion.getId(), 2
                                );
                                allRegions.add(districtRegion);

                                JSONArray towns = district.getJSONArray("towns");
                                for (int l = 0; l < towns.length(); l++) {
                                    JSONObject town = towns.getJSONObject(l);
                                    Region townRegion = new Region(
                                            town.getInt("id"),
                                            town.getString("name"),
                                            districtRegion.getId(), 3
                                    );
                                    allRegions.add(townRegion);

                                    JSONArray communities = town.getJSONArray("communities");
                                    for (int m = 0; m < communities.length(); m++) {
                                        JSONObject community = communities.getJSONObject(m);
                                        Region communityRegion = new Region(
                                                community.getInt("id"),
                                                community.getString("name"),
                                                townRegion.getId(), 4
                                        );
                                        allRegions.add(communityRegion);
                                    }
                                }
                            }
                        }
                    }

                    dbHelper.batchInsertRegions(allRegions);

                    if (callback != null) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onInitComplete(allRegions.size());
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    if (callback != null) {
                        final String errorMsg = e.getMessage();
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onInitError(errorMsg);
                            }
                        });
                    }
                }
            }
        }).start();
    }

    // 初始化测试数据
    public void initTestData(final DataInitCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    dbHelper.clearAllRegions();

                    List<Region> testRegions = new ArrayList<>();

                    // 添加省份
                    testRegions.add(new Region(1, "北京市", 0, 0));
                    testRegions.add(new Region(2, "上海市", 0, 0));
                    testRegions.add(new Region(3, "广东省", 0, 0));
                    testRegions.add(new Region(4, "江苏省", 0, 0));

                    // 添加北京市的城市
                    testRegions.add(new Region(101, "北京市", 1, 1));

                    // 添加上海市的城市
                    testRegions.add(new Region(201, "上海市", 2, 1));

                    // 添加广东省的城市
                    testRegions.add(new Region(301, "广州市", 3, 1));
                    testRegions.add(new Region(302, "深圳市", 3, 1));
                    testRegions.add(new Region(303, "珠海市", 3, 1));

                    // 添加江苏省的城市
                    testRegions.add(new Region(401, "南京市", 4, 1));
                    testRegions.add(new Region(402, "苏州市", 4, 1));

                    // 添加广州市的区
                    testRegions.add(new Region(30101, "天河区", 301, 2));
                    testRegions.add(new Region(30102, "越秀区", 301, 2));
                    testRegions.add(new Region(30103, "海珠区", 301, 2));

                    // 添加深圳市的区
                    testRegions.add(new Region(30201, "福田区", 302, 2));
                    testRegions.add(new Region(30202, "南山区", 302, 2));

                    // 添加天河区的街道
                    testRegions.add(new Region(3010101, "天河南街道", 30101, 3));
                    testRegions.add(new Region(3010102, "石牌街道", 30101, 3));
                    testRegions.add(new Region(3010103, "林和街道", 30101, 3));

                    // 添加福田区的街道
                    testRegions.add(new Region(3020101, "福田街道", 30201, 3));
                    testRegions.add(new Region(3020102, "华富街道", 30201, 3));

                    // 添加天河南街道的社区
                    testRegions.add(new Region(301010101, "体育东社区", 3010101, 4));
                    testRegions.add(new Region(301010102, "体育西社区", 3010101, 4));
                    testRegions.add(new Region(301010103, "六运小区社区", 3010101, 4));

                    // 添加福田街道的社区
                    testRegions.add(new Region(302010101, "福安社区", 3020101, 4));
                    testRegions.add(new Region(302010102, "福华社区", 3020101, 4));

                    dbHelper.batchInsertRegions(testRegions);

                    if (callback != null) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onInitComplete(testRegions.size());
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    if (callback != null) {
                        final String errorMsg = e.getMessage();
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onInitError(errorMsg);
                            }
                        });
                    }
                }
            }
        }).start();
    }

    public boolean hasData() {
        return dbHelper.hasData();
    }

    // 获取完整的地区字符串
    public String getFullRegionString(Region province, Region city,
                                      Region district, Region town, Region community) {
        StringBuilder sb = new StringBuilder();
        if (province != null) sb.append(province.getName());
        if (city != null) sb.append(" ").append(city.getName());
        if (district != null) sb.append(" ").append(district.getName());
        if (town != null) sb.append(" ").append(town.getName());
        if (community != null) sb.append(" ").append(community.getName());
        return sb.toString();
    }
}