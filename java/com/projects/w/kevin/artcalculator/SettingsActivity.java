package com.projects.w.kevin.artcalculator;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;



/**
 * Created by kw on 11/28/2016.
 */


public class SettingsActivity extends Activity {

    private String TAG = "artCalc";
    private int departmentTitle;
    private int departmentYear;
    private int employeeRank;
    private int colorTheme1;
    private int colorTheme2;
    private int[] tvColorIDList = {R.id.tvColor1, R.id.tvColor2, R.id.tvColor3, R.id.tvColor4, R.id.tvColor5, R.id.tvColor6};
    private Deque<Integer> colorChoices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings); //xml from res/layout/activity_name.xml
        initColorsQueue();
        departmentTitle = getIntent().getIntExtra("departmentTitle", 0);
        departmentYear = getIntent().getIntExtra("departmentYear", 0);
        Log.i(TAG, "onCreate, Settings, departmentTitle: "+departmentTitle+" deptYear: "+departmentYear);
        employeeRank = getIntent().getIntExtra("employeeRank", 0);
        //pickYear = "";
     //   pickDepartment = "";
        okayButtonInitializer();
        initColorsQueue();
        initRadioButtons();

    }



    private void initRadioButtons(){
        //find which ones to prefill based on intent extra
        Log.i(TAG, "initRadioButtons, departmentTitle is: "+departmentTitle);
        switch(departmentTitle){
            case R.array.airbrushTitles: ((RadioButton)findViewById(R.id.rbAirbrush)).setChecked(true);
                break;
            case R.array.bodyartTitles: ((RadioButton)findViewById(R.id.rbBodyArt)).setChecked(true);
                break;
            case R.array.portraitTitles: ((RadioButton)findViewById(R.id.rbPortraits)).setChecked(true);
                break;
            case R.array.caricatureTitles: ((RadioButton)findViewById(R.id.rbCaricatures)).setChecked(true);
                break;
        }

        setCheckedYearButton();
    }

    private void initColorsQueue(){
        colorChoices  = new ArrayDeque<>(Arrays.asList(
                getIntent().getIntExtra("colorTheme1", 0),
                getIntent().getIntExtra("colorTheme2", 1)));
        boolean firstColorSet = false;
        for(int tvColor: tvColorIDList){
            attachColorListener((TextView)findViewById(tvColor));

            if(colorChoices.contains(getColorCode(tvColor))){
                ((TextView)findViewById(tvColor)).setText(R.string.colorSelect);
                if(!firstColorSet){
                    colorTheme1 = getColorCode(tvColor);
                }else{
                    colorTheme2 = getColorCode(tvColor);
                }
            }

        }

    }

    private int getColorCode(int tvID){
        return ((ColorDrawable)(findViewById(tvID)).getBackground()).getColor();
    }

    private void setColors(){
        Log.i(TAG, "in setColors");
        boolean color1set = false;
        int i = 0;
        for(int tvColor: tvColorIDList){

            if(((TextView)findViewById(tvColor)).getText().length() > 0){
                if(!color1set){
                    colorTheme1 = getColorCode(tvColor);
                    Log.i(TAG, "color1 set to: "+colorTheme1);
                    color1set = true;
                }else{

                    colorTheme2 = getColorCode(tvColor);
                    Log.i(TAG, "color 2 set to: "+colorTheme2+" index: "+i);
                    break;
                }
            }
            i++;
        }
    }

    private void updateColorQueue(int color){
        if(color != colorChoices.peekFirst()) {
            int popColor = colorChoices.pop();
            for(int tvColor: tvColorIDList){
                if(popColor == getColorCode(tvColor)){
                    ((TextView)findViewById(tvColor)).setText("");
                }
            }
            colorChoices.addLast(color);
        }
    }

    private void attachColorListener(final TextView textView){
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TextView tvAmount = (TextView)findViewById(R.id.tvPopUpItem);

                if(textView.getText().length() == 0) {
                    textView.setText(R.string.colorSelect);
                    updateColorQueue(getColorCode(textView.getId()));
                }
            }
        });
    }

    private void okayButtonInitializer(){
        Button buttonName = (Button) findViewById(R.id.okayButton);


        buttonName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick, OK button, departmentTitle: "+departmentTitle+" year: "+departmentYear);
                Intent returnIntent = new Intent();
                //returnIntent.putExtra("position", pickYear);
                //returnIntent.putExtra("department", pickDepartment);
                setDepartmentAndValue();
                setColors();
                Log.i(TAG, "CLICKED OK, dept value: "+departmentTitle+" dept title: "+departmentTitle);
                returnIntent.putExtra("department", departmentTitle);
                returnIntent.putExtra("position", departmentYear);
                returnIntent.putExtra("colorTheme1", colorTheme1);
                returnIntent.putExtra("colorTheme2", colorTheme2);
                returnIntent.putExtra("employeeYear", employeeRank);
                Log.i(TAG, "added colors: "+colorTheme1+"  color2: "+colorTheme2);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }


    private void setCheckedYearButton(){
        switch(employeeRank){
            case 0:
                employeeRank = 0;
                ((RadioButton)findViewById(R.id.rbFirstYear)).setChecked(true);
                break;
            case 1:
               // pickYear = "secondYear";
                employeeRank = 1;
                ((RadioButton)findViewById(R.id.rbSecondYear)).setChecked(true);
                break;
            case 2:
                //pickYear = "lead";
                employeeRank = 2;
                ((RadioButton)findViewById(R.id.rbLead)).setChecked(true);
                break;
            case 3:
               // pickYear = "supervisor";
                employeeRank = 3;
                ((RadioButton)findViewById(R.id.rbSupervisor)).setChecked(true);
                break;
            case 4:
               // pickYear = "manager";
                employeeRank = 4;
                ((RadioButton)findViewById(R.id.rbManager)).setChecked(true);
        }

    }

    private void setDepartmentAndValue(){
        switch(departmentTitle){
            case R.array.airbrushTitles:
                switch(employeeRank){
                    case 0:
                        departmentYear = R.array.airbrushValues_firstYear;
                        break;
                    case 1:
                        departmentYear = R.array.airbrushValues_secondYear;
                        break;
                    case 2:
                        departmentYear = R.array.airbrushValues_lead;
                        break;
                    case 3:
                        departmentYear = R.array.airbrushValues_supervisor;
                        break;
                    case 4:
                        departmentYear = R.array.airbrushValues_manager;
                }
                break;
            case R.array.bodyartTitles:
                switch(employeeRank) {
                    case 0:
                        departmentYear = R.array.bodyartValues_firstYear;
                        break;
                    case 1:
                        departmentYear = R.array.bodyartValues_secondYear;
                        break;
                    case 2:
                        departmentYear = R.array.bodyartValues_lead;
                        break;
                    case 3:
                        departmentYear = R.array.bodyartValues_supervisor;
                        break;
                    case 4:
                        departmentYear = R.array.bodyartValues_manager;
                }
                break;
            case R.array.caricatureTitles:
                switch(employeeRank) {
                    case 0:
                        departmentYear = R.array.caricatureValues_firstYear;
                        ((RadioButton)findViewById(R.id.rbFirstYear)).setChecked(true);
                        break;
                    case 1:
                        departmentYear = R.array.caricatureValues_secondYear;
                        break;
                    case 2:
                        departmentYear = R.array.caricatureValues_lead;
                        break;
                    case 3:
                        departmentYear = R.array.caricatureValues_supervisor;
                        break;
                    case 4:
                        departmentYear = R.array.caricatureValues_manager;
                }
                break;
            case R.array.portraitTitles:
                switch(employeeRank) {
                    case 0:
                        departmentYear = R.array.portraitValues_firstYear;
                        break;
                    case 1:
                        departmentYear = R.array.portraitValues_secondYear;
                        break;
                    case 2:
                        departmentYear = R.array.portraitValues_lead;
                        break;
                    case 3:
                        departmentYear = R.array.portraitValues_supervisor;
                        break;
                    case 4:
                        departmentYear = R.array.portraitValues_manager;
                }
                break;
        }
    }



    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        //years
        switch(view.getId()) {
            case R.id.rbFirstYear:
                if (checked)
                 //   pickYear = "firstYear";
                employeeRank = 0;
                    break;
            case R.id.rbSecondYear:
                if (checked)
                 //   pickYear = "secondYear";
                employeeRank = 1;
                    break;
            case R.id.rbLead:
                if (checked)
                    //pickYear = "lead";
                employeeRank = 2;
                    break;
            case R.id.rbSupervisor:
                if (checked)
                  //  pickYear = "supervisor";
                employeeRank = 3;
                    break;
            case R.id.rbManager:
                if (checked)
                 //   pickYear = "manager";
                employeeRank = 4;
                    break;

            //departments
            case R.id.rbAirbrush:
                if (checked)
                  //  pickDepartment = "airbrush";
                departmentTitle = R.array.airbrushTitles;
                //pickDepartment = R.array.airbrushTitles;
                break;
            case R.id.rbBodyArt:
                if (checked)
                    departmentTitle = R.array.bodyartTitles;
                break;
            case R.id.rbCaricatures:
                if (checked)
                   // pickDepartment = "caricatures";
                    departmentTitle = R.array.caricatureTitles;
                break;
            case R.id.rbPortraits:
                if (checked)
                   // pickDepartment = "portraits";
                    departmentTitle = R.array.portraitTitles;
                break;
        }
    }






    // Lifecycle callback overrides


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Bundle savedInstanceState= new Bundle();
        onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onRestart() {
        super.onRestart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        //savedInstanceState.putInt("save_key", saveVariable);
        super.onSaveInstanceState(savedInstanceState);
    }
}
