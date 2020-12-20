package com.example.matrixcalculatorcw;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import Jama.Matrix;

public class ResultActivity extends AppCompatActivity {
    private ArrayList<ItemMatrix> bundleMatrix;
    private ArrayList<ItemMatrix> finalMatrix;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private boolean isBasic = false;
    private String exception;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ThemeDark);
        setContentView(R.layout.activity_result);

        Bundle bundle = getIntent().getExtras();
        bundleMatrix = bundle.getParcelableArrayList("bundleMatrix");
        ItemOperation itemOperation = bundle.getParcelable("itemOperation");

        String matrixLetter = getString(R.string.display_matrix_letter);
        String titleFormat = getString(R.string.placeholder_matrix_name);
        String matrixName = getString(R.string.display_matrix_name);
        ArrayList<Matrix> auxArray = new ArrayList<>();
        finalMatrix = new ArrayList<>();

        viewPager = findViewById(R.id.activity_result_viewpager);

        // TableLayout для відображення результуючої матриці
        tabLayout = findViewById(R.id.activity_result_tablayout);

        switch (itemOperation.getId()) {
            //Характеристика матриці
            case 0:
                isBasic = true;
                break;
            //Обернена матриця
            case 1:
                auxArray.add(bundleMatrix.get(0).inverse());
                break;
            //Додавання матриць
            case 2:
                auxArray.add(bundleMatrix.get(0).plus(bundleMatrix.get(1)));
                break;
            //Віднімання матриць
            case 3:
                auxArray.add(bundleMatrix.get(0).minus(bundleMatrix.get(1)));
                break;
            //Множення матриць
            case 4:
                auxArray.add(bundleMatrix.get(0).times(bundleMatrix.get(1)));
                break;
        }

        for (int i = 0; i < auxArray.size(); ++i)
            finalMatrix.add(new ItemMatrix(String.format(titleFormat, matrixName,
                    matrixLetter.charAt(i + bundleMatrix.size())), auxArray.get(i)));

        initLayout();
    }

    private void initLayout() {
        String titleArray = getResources().getString(R.string.display_result_title);
        Fragment fragment = new FragmentResult();
        AdapterTabLayout adapter = new AdapterTabLayout(getSupportFragmentManager());
        Bundle bundle = new Bundle();
        ArrayList<ItemMatrix> arrayList = new ArrayList<>();

        if (!isBasic) {
            bundle.putParcelableArrayList("bundleMatrix", finalMatrix);
            arrayList.addAll(finalMatrix);
        } else {
            bundle.putParcelableArrayList("bundleMatrix", bundleMatrix);
            arrayList.addAll(bundleMatrix);
        }
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, titleArray);

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem((isBasic) ? 0 : 1);
    }
}