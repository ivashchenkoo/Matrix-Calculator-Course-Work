package com.example.matrixcalculatorcw;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import Jama.Matrix;
import me.relex.circleindicator.CircleIndicator;

public class MatrixActivity extends AppCompatActivity {
    private TextView[] textViewsDimension = new TextView[2];
    private SeekBar[] seekBarsDimension = new SeekBar[2];
    private ItemOperation itemOperation;
    private String[] stringsDimension;
    private AdapterTabLayout adapter;
    private Button confirmButton;
    private ViewPager viewPager;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ThemeDark);
        setContentView(R.layout.activity_matrix);

        final Bundle bundle = getIntent().getExtras();
        itemOperation = bundle.getParcelable("itemOperation");

        editText = findViewById(R.id.matrix_edittext);
        viewPager = findViewById(R.id.matrix_viewpager);
        confirmButton = findViewById(R.id.matrix_button);
        seekBarsDimension[0] = findViewById(R.id.matrix_seekbar_row);
        seekBarsDimension[1] = findViewById(R.id.matrix_seekbar_column);
        textViewsDimension[0] = findViewById(R.id.matrix_textview_row);
        textViewsDimension[1] = findViewById(R.id.matrix_textview_column);
        stringsDimension = getResources().getStringArray(R.array.display_dimension);

        initViewPager(bundle.<ItemMatrix>getParcelableArrayList("bundleMatrix"));
        SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                switch (seekBar.getId()) {
                    case R.id.matrix_seekbar_row:
                        seekBarChangeListener(0, progress);
                        break;

                    case R.id.matrix_seekbar_column:
                        seekBarChangeListener(1, progress);
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        };
        seekBarsDimension[0].setOnSeekBarChangeListener(onSeekBarChangeListener);
        seekBarsDimension[1].setOnSeekBarChangeListener(onSeekBarChangeListener);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
                    String text = editText.getText().toString();
                    if (!text.matches("")) {
                        getFragment().setCell(Double.parseDouble(text));
                        editText.setText("");
                    }
                    if (!getFragment().stepPointer())
                        nextPage();
                    return true;
                }
                return false;
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();
                if (!text.matches("")) {
                    getFragment().setCell(Double.parseDouble(text));
                    editText.setText("");
                }
                nextPage();
            }
        });
    }

    private void initViewPager(ArrayList<ItemMatrix> bundleArrayList) {
        ArrayList<ItemMatrix> arrayList = (bundleArrayList == null) ? new ArrayList<ItemMatrix>() : bundleArrayList;
        adapter = new AdapterTabLayout(getSupportFragmentManager());
        String titleFormat = getString(R.string.placeholder_matrix_name);
        String matrixName = getString(R.string.display_matrix_name);
        String matrixLetter = getString(R.string.display_matrix_letter);
        int i;

        if (arrayList.size() != itemOperation.getMatrixNumber()) {
            int[] dimensionDefault = new Gson().fromJson(PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                    .getString(getString(R.string.key_default_matrix_dimension), null), int[].class);

            for (i = arrayList.size(); i < itemOperation.getMatrixNumber(); ++i)
                arrayList.add(new ItemMatrix(String.format(titleFormat, matrixName, matrixLetter.charAt(i)),
                        new Matrix(dimensionDefault[0], dimensionDefault[1])));
        }

        for (i = 0; i < itemOperation.getMatrixNumber(); ++i) {
            FragmentMatrix fragmentMatrix = new FragmentMatrix();
            Bundle bundle = new Bundle();

            bundle.putParcelable("matrix", arrayList.get(i));
            fragmentMatrix.setArguments(bundle);
            adapter.addFragment(fragmentMatrix, "");
        }

        updateSeekBars(arrayList.get(0));
        viewPager.setAdapter(adapter);

        if (adapter.getCount() > 1)
            ((CircleIndicator) findViewById(R.id.matrix_circleindicator)).setViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                updateSeekBars(null);
            }
        });
    }

    public void migrateData() {
        Intent intent = new Intent(this, ResultActivity.class);

        intent.putExtra("bundleMatrix", getMatrixArray());
        intent.putExtra("itemOperation", itemOperation);
        startActivity(intent);
    }

    public void seekBarChangeListener(int dimension, int progress) {
        getFragment().updateMatrixDimension(dimension, progress);
        textViewsDimension[dimension].setText(String.format(getString(R.string.placeholder_seekbar),
                stringsDimension[dimension], getMatrix().getDimensionArray()[dimension]));
    }


    public void updateSeekBars(ItemMatrix itemMatrix) {
        int[] newDimension = ((itemMatrix == null) ? getMatrix().getDimensionArray() : itemMatrix.getDimensionArray());

        for (int i = 0; i < 2; ++i) {
            seekBarsDimension[i].setProgress(newDimension[i] - 1);
            textViewsDimension[i].setText(String.format(getString(R.string.placeholder_seekbar), stringsDimension[i], newDimension[i]));
        }

        if (itemOperation.getMatrixNumber() > 1) {
            if (viewPager.getCurrentItem() == 0)
                confirmButton.setText(getString(R.string.display_button_next));
            else
                confirmButton.setText(getString(R.string.display_button_solve));
        }
    }

    private void nextPage() {
        if (itemOperation.getMatrixNumber() == 1 || viewPager.getCurrentItem() == 1)
            migrateData();
        else
            viewPager.setCurrentItem(1);
    }

    public ArrayList<ItemMatrix> getMatrixArray() {
        ArrayList<ItemMatrix> auxArray = new ArrayList<>();

        for (int i = 0; i < itemOperation.getMatrixNumber(); ++i)
            auxArray.add(getMatrix(i));
        return auxArray;
    }

    private ItemMatrix getMatrix() {
        return getFragment().getItemMatrix();
    }

    private ItemMatrix getMatrix(int matrix) {
        return getFragment(matrix).getItemMatrix();
    }

    private FragmentMatrix getFragment() {
        return getFragment(viewPager.getCurrentItem());
    }

    private FragmentMatrix getFragment(int fragment) {
        return ((FragmentMatrix) adapter.getItem(fragment));
    }
}