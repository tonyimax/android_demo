package com.metrox.demo68;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity
        implements FragmentCommunication.OnMessageListener,
        FragmentA.OnFragmentInteractionListener {

    private static final String TAG_FRAGMENT_A = "fragment_a";
    private static final String TAG_FRAGMENT_B = "fragment_b";
    private static final String TAG_COMM_FRAGMENT = "communication_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        if (savedInstanceState == null) {
            addFragmentA();
        }
    }

    private void initViews() {
        findViewById(R.id.btnAdd).setOnClickListener(v -> addFragmentA());
        findViewById(R.id.btnReplace).setOnClickListener(v -> replaceWithFragmentB());
        findViewById(R.id.btnRemove).setOnClickListener(v -> removeCurrentFragment());
        findViewById(R.id.btnShow).setOnClickListener(v -> showFragmentA());
        findViewById(R.id.btnHide).setOnClickListener(v -> hideFragmentA());
        findViewById(R.id.btnAttach).setOnClickListener(v -> attachDetachExample());
        findViewById(R.id.btnCommFragment).setOnClickListener(v -> addCommunicationFragment());
    }

    // 实现 FragmentA.OnFragmentInteractionListener 接口的方法
    @Override
    public void onSwitchToFragmentB() {
        switchToFragmentB();
    }

    @Override
    public void onFragmentAction(String action, Bundle data) {
        if (action.equals("navigation")) {
            String fragment = data.getString("fragment", "");
            Toast.makeText(this, "Action from " + fragment, Toast.LENGTH_SHORT).show();
        }
    }

    // 实现 FragmentCommunication.OnMessageListener 接口的方法
    @Override
    public void onMessageSent(String message) {
        Toast.makeText(this, "Message received: " + message, Toast.LENGTH_SHORT).show();

        FragmentManager fragmentManager = getSupportFragmentManager();

        // 传递给 Fragment A
        Fragment fragmentA = fragmentManager.findFragmentByTag(TAG_FRAGMENT_A);
        if (fragmentA instanceof FragmentA) {
            ((FragmentA) fragmentA).updateMessage(message);
        }

        // 传递给 Fragment B
        Fragment fragmentB = fragmentManager.findFragmentByTag(TAG_FRAGMENT_B);
        if (fragmentB instanceof FragmentB) {
            ((FragmentB) fragmentB).updateMessage(message);
        }

        // 传递给通信 Fragment
        Fragment commFragment = fragmentManager.findFragmentByTag(TAG_COMM_FRAGMENT);
        if (commFragment instanceof FragmentCommunication) {
            ((FragmentCommunication) commFragment).receiveMessage("Processed: " + message);
        }
    }

    /**
     * 添加 Fragment A
     */
    private void addFragmentA() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment existingFragment = fragmentManager.findFragmentByTag(TAG_FRAGMENT_A);
        if (existingFragment == null) {
            FragmentA fragmentA = FragmentA.newInstance("Hello from Activity!");
            transaction.add(R.id.fragmentContainer, fragmentA, TAG_FRAGMENT_A);
            transaction.addToBackStack("add_fragment_a");
            transaction.commit();
        }
    }

    /**
     * 从 Fragment A 切换到 Fragment B
     */
    public void switchToFragmentB() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // 添加过渡动画
        transaction.setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
        );

        FragmentB fragmentB = new FragmentB();
        transaction.replace(R.id.fragmentContainer, fragmentB, TAG_FRAGMENT_B);
        transaction.addToBackStack("switch_to_b");
        transaction.commit();
    }

    /**
     * 替换为 Fragment B
     */
    private void replaceWithFragmentB() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        FragmentB fragmentB = new FragmentB();
        transaction.replace(R.id.fragmentContainer, fragmentB, TAG_FRAGMENT_B);
        transaction.addToBackStack("replace_with_b");
        transaction.commit();
    }

    /**
     * 返回到 Fragment A
     */
    public void goBackToFragmentA() {
        getSupportFragmentManager().popBackStack();
    }

    /**
     * 移除 Fragment B
     */
    public void removeFragmentB() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragmentB = fragmentManager.findFragmentByTag(TAG_FRAGMENT_B);

        if (fragmentB != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.remove(fragmentB);
            transaction.addToBackStack("remove_b");
            transaction.commit();
        }
    }

    /**
     * 移除当前 Fragment
     */
    private void removeCurrentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);

        if (fragment != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.remove(fragment);
            transaction.addToBackStack("remove_fragment");
            transaction.commit();
        }
    }

    /**
     * 显示 Fragment A
     */
    private void showFragmentA() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragmentA = fragmentManager.findFragmentByTag(TAG_FRAGMENT_A);

        if (fragmentA != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.show(fragmentA);
            transaction.addToBackStack("show_fragment_a");
            transaction.commit();
        }
    }

    /**
     * 隐藏 Fragment A
     */
    private void hideFragmentA() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragmentA = fragmentManager.findFragmentByTag(TAG_FRAGMENT_A);

        if (fragmentA != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.hide(fragmentA);
            transaction.addToBackStack("hide_fragment_a");
            transaction.commit();
        }
    }

    /**
     * 分离和附加示例
     */
    private void attachDetachExample() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragmentA = fragmentManager.findFragmentByTag(TAG_FRAGMENT_A);

        if (fragmentA != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            if (fragmentA.isDetached()) {
                transaction.attach(fragmentA);
            } else {
                transaction.detach(fragmentA);
            }

            transaction.addToBackStack("attach_detach");
            transaction.commit();
        }
    }

    /**
     * 添加通信 Fragment
     */
    private void addCommunicationFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment existingFragment = fragmentManager.findFragmentByTag(TAG_COMM_FRAGMENT);

        if (existingFragment == null) {
            FragmentCommunication fragment = new FragmentCommunication();
            fragment.setOnMessageListener(this);

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.fragmentContainer2, fragment, TAG_COMM_FRAGMENT);
            transaction.addToBackStack("add_comm_fragment");
            transaction.commit();
        }
    }

    /**
     * 处理返回键
     */
    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}