package com.projects.w.kevin.artcalculator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnFocusChangeListener;

import java.math.BigInteger;

import static android.R.attr.button;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;


/**
 * Created by kw on 11/29/2016.
 */



public class PopUpCountActivity extends Activity {
    private final String TAG = "artCalc";
    private int valueIndex; //the index of the row called
    private EditText etAmount;
    private final double WIDTH_PERCENT = .9;
    private final double HEIGHT_PERCENT = .3;
    private final double WIDTH_PERCENT_LANDSCAPE = .5;
    private final double HEIGHT_PERCENT_LANDSCAPE = .5;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupcount_activity);
        Log.i(TAG, "popUP, orientation: "+getResources().getConfiguration().orientation);
        sizePopup();
        valueIndex = getIntent().getIntExtra("valueIndex", 0);
        count = MainActivity.countsArray[valueIndex];
        etAmount = (EditText)findViewById(R.id.etPopUpItem);
        etAmount.setInputType(InputType.TYPE_CLASS_NUMBER);
        MainActivity.indexChanged = valueIndex;

        setTitle(MainActivity.itemsArray[valueIndex]);
        setAmountText();
        plusButtonInitializer();
        minusButtonInitializer();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event){
        Log.i(TAG, "dispatchKeyEvent event: "+event.toString());
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
            if((etAmount.getText()).length() > 0) {
                count = Integer.parseInt(etAmount.getText() + "");
                if (count < 0) {
                    count = 0;
                }
            }else{
                count = 0;
            }

            Log.i(TAG, "dispatchKeyEvent, count is: "+count);
            MainActivity.countsArray[valueIndex] = count;

            Intent intIntent = new Intent();
            intIntent.putExtra("countIndex", valueIndex);
            setResult(RESULT_OK, intIntent);
            finish();
        }
        return super.dispatchKeyEvent(event);
    }

    private void setAmountText(){

        if(count == 0){
            etAmount.setText("");
        }else {
            etAmount.setText(count + "");
        }
    }

    private void sizePopup(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double width = dm.widthPixels;
        int height = dm.heightPixels;
        if(getResources().getConfiguration().orientation == 1) {
            getWindow().setLayout((int) (width * WIDTH_PERCENT), (int) (height * HEIGHT_PERCENT));
        }else{
            getWindow().setLayout((int) (width * WIDTH_PERCENT_LANDSCAPE), (int) (height * HEIGHT_PERCENT_LANDSCAPE));
        }
    }

    private void plusButtonInitializer(){
        Button buttonName = (Button) findViewById(R.id.plusButton);
        attachListener(buttonName, 1);
    }
    private void minusButtonInitializer(){
        Button buttonName = (Button) findViewById(R.id.minusButton);
        attachListener(buttonName, -1);
    }


    private void attachListener(Button button, final int change){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TextView tvAmount = (TextView)findViewById(R.id.tvPopUpItem);

                Log.i("artCalc","got TextView in attachListener");
                count += change;
                Log.i(TAG, "count changed, "+count);
                if(count < 0){
                    count = 0;
                }
                MainActivity.countsArray[valueIndex] = count;

                Log.i("artCalc", "new value is made: "+count);
              //  EditText etAmount = (EditText)findViewById(R.id.etPopUpItem);
                etAmount.setText(count+"");
                //tvAmount.setText(count+"");
            }
        });
    }
}
