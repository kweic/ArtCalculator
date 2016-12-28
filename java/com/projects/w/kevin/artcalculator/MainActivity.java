package com.projects.w.kevin.artcalculator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;


import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.os.Build.ID;
import static android.provider.Contacts.SettingsColumns.KEY;
import static java.security.AccessController.getContext;
//import static com.projects.w.kevin.artcalculator.R.id.textView;


public class MainActivity extends AppCompatActivity {

    private static String TAG = "artCalc";
    private final String SAVE_KEY = "ARTCALC_SAVEFILE";
    private int SETTINGS_ID = 999;
    private final String DEPT_KEY = SAVE_KEY+".department";
    private final String YEAR_KEY = SAVE_KEY+".deptYear";
    private final String RANK_KEY = SAVE_KEY+".empRank";
    private final String COLOR1_KEY = SAVE_KEY+".color1";
    private final String COLOR2_KEY = SAVE_KEY+".color2";
    private final String COUNTS_KEY = SAVE_KEY+".counts";
    private int departmentTitle;
    private int departmentYear;
    //TODO: move textSize to dimens
    private static float textSize = 16;
    private double total = 0;
    private final int ITEM_C = 0;
    private final int COUNT_C = 1;
    private final int FLATRATE_C = 2;
    private final int TOTALS_C = 3;
    static int indexChanged = -1;
    private int employeeRank;
    private int colorTheme1;
    private int colorTheme2;
    private int colorTheme1_darker;
    private int colorTheme2_darker;


    static String[] itemsArray;
    double[] flatrateArray;
    static int[] countsArray;
    double[] totalsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate started");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       // loadSavedFiles();

        //TODO: add this into savedState

       // setDarkerColors();



        //TODO: replace this assignment to airbrush with save load feature

        if (savedInstanceState != null) {
            departmentTitle = savedInstanceState.getInt("departmentTitle");
            departmentYear = savedInstanceState.getInt("departmentYear");
            countsArray = savedInstanceState.getIntArray("countsArray");
            employeeRank = savedInstanceState.getInt("employeeRank");
            colorTheme1 = savedInstanceState.getInt("colorTheme1");
            colorTheme2 = savedInstanceState.getInt("colorTheme2");
           // totalsArray = savedInstanceState.getDoubleArray("totalsArray");
            loadSavedFiles();
            fullInitialization(true);

        } else {
            //no save
            departmentTitle = R.array.airbrushTitles;
            departmentYear = R.array.airbrushValues_firstYear;
            colorTheme1 = ContextCompat.getColor(getBaseContext(), R.color.color_grey);
            colorTheme2 = ContextCompat.getColor(getBaseContext(), R.color.color_white);
            employeeRank = 0;
            loadSavedFiles();
            fullInitialization(false);
        }
       // Log.i(TAG, "COUNTS LENGTH PRE SAVE LOAD: "+countsArray.length);

       // Log.i(TAG, "counts length after loading: "+countsArray.length);
        applyResetListener();

        Log.i(TAG, "finished onCreate");
    }

    private void saveFiles(){
        Log.i(TAG, "doing save");
        //Context context = getBaseContext();
        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences(SAVE_KEY, Context.MODE_PRIVATE);
        Log.i(TAG, "got sharedPreferences");
        sharedPreferences.edit().putString("test", "this is my test save").apply();
        if(departmentTitle != 0)sharedPreferences.edit().putInt(DEPT_KEY, departmentTitle).apply();
        if(departmentYear != 0)sharedPreferences.edit().putInt(YEAR_KEY, departmentYear).apply();
        sharedPreferences.edit().putInt(RANK_KEY, employeeRank).apply();
        sharedPreferences.edit().putInt(COLOR1_KEY, colorTheme1).apply();
        sharedPreferences.edit().putInt(COLOR2_KEY, colorTheme2).apply();
        Log.i(TAG, "colors, rank and dept / year saved, about to do counts");
      //  if(countsArray != null)sharedPreferences.edit().putStringSet(COUNTS_KEY, convertCounts());
        Log.i(TAG, "saves done");


    }

    private Set<String> convertCounts(){
        Set<String> strCounts = new HashSet<>();
        for(int count: countsArray){
            strCounts.add(count+"");
        }
        return strCounts;
    }


    private void loadSavedFiles(){
        //this will init the save
        Context context = getBaseContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences(SAVE_KEY, Context.MODE_PRIVATE);
        Log.i(TAG, "loading save");
       String testLoad = sharedPreferences.getString("test", "no save found");
        departmentTitle = sharedPreferences.getInt(DEPT_KEY, R.array.airbrushTitles);
        departmentYear = sharedPreferences.getInt(YEAR_KEY, R.array.airbrushValues_firstYear);
        employeeRank = sharedPreferences.getInt(RANK_KEY, 0);
        colorTheme1 = sharedPreferences.getInt(COLOR1_KEY, R.color.color_grey);
        colorTheme2 = sharedPreferences.getInt(COLOR2_KEY, R.color.color_white);
      //  countsArray = loadCountsArrayFromSet(sharedPreferences.getStringSet(COUNTS_KEY, new HashSet<String>()));
        Log.i(TAG, testLoad);
    }

    /*
    private int[] loadCountsArrayFromSet(Set<String> loaded){
        if(loaded.isEmpty())return null;
        int[] loadedCounts = new int[loaded.size()];

        int i = 0;
        for(String value: loaded){
        loadedCounts[i] = Integer.parseInt(value);
            i++;
        }
        return loadedCounts;
    }*/


    private void setDarkerColors(){
        float[] hsv = new float[3];
        Color.colorToHSV(colorTheme1, hsv);
        hsv[2] *= 0.8f; // value component
        colorTheme1_darker = Color.HSVToColor(hsv);

        Color.colorToHSV(colorTheme2, hsv);
        hsv[2] *= 0.8f; // value component
        colorTheme2_darker = Color.HSVToColor(hsv);

    }

    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    public void onPause(){
        super.onPause();
        saveFiles();
        Bundle savedInstanceState= new Bundle();
        onSaveInstanceState(savedInstanceState);

        //SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedPref.edit();
       // editor.putInt(getString(R.string.saved_high_score), newHighScore);
       // editor.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {


       // savedInstanceState.putStringArray("itemsArray", itemsArray); //can probably reload this by only saving the department
        //savedInstanceState.putDoubleArray("flatrateArray", flatrateArray); //can probably reload this by only saving the department
        savedInstanceState.putInt("departmentTitle", departmentTitle);
        savedInstanceState.putInt("departmentYear", departmentYear);
        savedInstanceState.putInt("employeeRank", employeeRank);
        savedInstanceState.putIntArray("countsArray", countsArray); //need to save this
        savedInstanceState.putInt("colorTheme1", colorTheme1);
        savedInstanceState.putInt("colorTheme2", colorTheme2);
       // savedInstanceState.putDoubleArray("totalsArray", totalsArray); //need to save this
        super.onSaveInstanceState(savedInstanceState);

    }

    private void fullInitialization(boolean loadSave){
        getSupportActionBar().setTitle(R.string.toolbarTitle);
        total = 0;
        Log.i(TAG, " full init: getting items array");
        itemsArray = getResources().getStringArray(departmentTitle);
        Log.i(TAG, "full init: getting flatRate Array");
        flatrateArray = getFlatRateArray();
        if(!loadSave || countsArray == null) countsArray = new int[itemsArray.length];

        totalsArray = new double[itemsArray.length];
        //initDepartmentAndPosition();
        Log.i(TAG, "in fullInit, about to do colors");
        setDarkerColors();
        Log.i(TAG, "colors done");
        calculateTotals();
        initialColumns();

        updateBottomTotal();
    }


    private void initialColumns(){
        populateColumn(ITEM_C, 0);
        populateColumn(COUNT_C, -1);
        populateColumn(FLATRATE_C, 0);
        populateColumn(TOTALS_C, 0);
       // printTotalsColumn();
    }

    private void calculateTotals(){
        //double total = 0;
        //totalsArray[totalsArray.length-1] = 0;
        total = 0;
        for(int i = 0; i < totalsArray.length; i++){
            totalsArray[i] = flatrateArray[i] * countsArray[i];
            //totalsArray[totalsArray.length-1] += totalsArray[i];
            total += totalsArray[i];
        }
        //totalsArray[totalsArray.length-1] = total;
    }

    private void setZeroCounts(int[] arr){
        for(int i = 0; i < arr.length; i++){
            arr[i] = 0;
        }
    }
    private void setZeroCounts(double[] arr){
        for(int i = 0; i < arr.length; i++){
            arr[i] = 0;
        }
    }


    private double[] getFlatRateArray(){
        flatrateArray = new double[itemsArray.length];
        String[] arr = getResources().getStringArray(departmentYear);

        for(int i = 0; i < flatrateArray.length; i++){
            flatrateArray[i] = Double.parseDouble(arr[i]);
        }

        return flatrateArray;
    }

    private LinearLayout getLinearLayout(int column){
        if(column == ITEM_C)return (LinearLayout) findViewById(R.id.itemColumn);
        if(column == FLATRATE_C) return(LinearLayout) findViewById(R.id.flatRateColumn);
        if(column == COUNT_C) return(LinearLayout) findViewById(R.id.amountColumn);
        if(column == TOTALS_C) return(LinearLayout) findViewById(R.id.totalsColumn);
        return null;
    }

    private void populateColumn(int column, int countIndexChanged){
        LinearLayout linearLayout = getLinearLayout(column);
        linearLayout.removeAllViews();

       // printColumnHeader(column, linearLayout);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //params.topMargin = gap;

            if(column == 3){//totals
                for (int i = 0; i < totalsArray.length; i++) { //double array
                    setTextInLayout(linearLayout, params, i, "", true, totalsArray[i], false);
                }
            }else if(column == 1) { //counts
                for (int i = 0; i < countsArray.length; i++) { //int array
                    setTextInLayout(linearLayout, params, i, countsArray[i] + "", false, 0, i == indexChanged);
                }

            }else if(column == 0) { //items
                for (int i = 0; i < itemsArray.length; i++) { //string array
                    setTextInLayout(linearLayout, params, i, itemsArray[i], false, 0, false);
                }
            }else if(column == 2) { //FlatRate
                for (int i = 0; i < flatrateArray.length; i++) { //double array
                    setTextInLayout(linearLayout, params, i, "", false, flatrateArray[i], false);
                }
            }

    }

    private String formatDollars(double num){
        return NumberFormat.getCurrencyInstance(Locale.US).format(num);
    }

    private void setTextInLayout(LinearLayout layout, LinearLayout.LayoutParams params, int index, String text, boolean totals, double total, boolean highlight) {
        TextView textView = new TextView(this);
        if(totals) textView.setTypeface(null, Typeface.BOLD);
        setTextBackgroundColor(textView, index, totals, highlight);

        textView.setTextSize(textSize);
        applyPopUpListener(textView, index);
        textView.setMinLines(3);
        layout.addView(textView, params);

        setTextViewText(textView, total, text);

    }

    private void setTextViewText(TextView textView, double total, String text){
        if(total != 0){
            textView.setText(" "+formatDollars(total));
        }else {
            textView.setText(" "+text);
        }
    }

    private void setTextBackgroundColor(TextView textView, int index, boolean totals, boolean highlight){
        if(highlight){
            textView.setBackgroundColor(Color.YELLOW);
            textView.setTypeface(null, Typeface.BOLD);
            return;
        }

        if(totals && index % 2 == 0){ //dark row totals
            textView.setBackgroundColor(colorTheme1_darker);
        }else if(totals){
            textView.setBackgroundColor(colorTheme2_darker);
        }else if(index % 2 == 0){
            textView.setBackgroundColor(colorTheme1); //dark row
        }else{
            textView.setBackgroundColor(colorTheme2);
        }
    }

    private void applyResetListener(){
        TextView resetText = (TextView)findViewById(R.id.resetTag);
        resetText.setClickable(true);
        resetText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //TODO: add popup confirming reset and don't complete the rest of this unless confirmed
                setZeroCounts(countsArray);
                setZeroCounts(totalsArray);
                indexChanged = -1;
                total = 0;
                populateColumn(COUNT_C, 0);
                populateColumn(TOTALS_C, 0);
                updateBottomTotal();
            }
        });
    }

    private void applyPopUpListener(TextView textView, final int index){
        textView.setClickable(true);
        textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.i(TAG, "doing onClick to create a popup");
                Intent intent = new Intent(getBaseContext(), PopUpCountActivity.class);
                intent.putExtra("valueIndex", index);
                intent.putExtra("valueValue", index);
                startActivityForResult(intent, index);
            }
        });
    }

    private void updateBottomTotal(){
        ((TextView)findViewById(R.id.bottom_total)).setText(formatDollars(total));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            settingsIntent.putExtra("colorTheme1", colorTheme1);
            settingsIntent.putExtra("colorTheme2", colorTheme2);
            settingsIntent.putExtra("employeeRank", employeeRank);
            settingsIntent.putExtra("departmentTitle", departmentTitle);
            settingsIntent.putExtra("departmentYear", departmentYear);
            startActivityForResult(settingsIntent, SETTINGS_ID);
            Log.i(TAG, "settings button clicked");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void finishedCountAdjust(int indexChanged){
        calculateTotals();
        populateColumn(COUNT_C, indexChanged);
        populateColumn(TOTALS_C, 0);
        updateBottomTotal();
        //TODO: make way to highlight last count changed
       // getSupportActionBar().setTitle("Total: "+formattedTotal);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SETTINGS_ID) {
            if(resultCode == Activity.RESULT_OK){ //Settings changed
                Log.i(TAG, "previous color1: "+colorTheme1);
                colorTheme1 = data.getIntExtra("colorTheme1", colorTheme1);
                colorTheme2 = data.getIntExtra("colorTheme2", colorTheme2);
                Log.i(TAG, "color1 is now: "+colorTheme1);
                Log.i(TAG, "returning from settings, position int: "+data.getIntExtra("department", 0)+" dept value int: "+ data.getIntExtra("position", 0));
               //if(!position.equals(data.getStringExtra("position")) || !department.equals(data.getStringExtra("department"))) {
                if(departmentTitle != data.getIntExtra("department", 0) ||
                        departmentYear != data.getIntExtra("position", 0)){
                    departmentTitle = data.getIntExtra("department", 0);
                    departmentYear = data.getIntExtra("position", 0);
                    employeeRank = data.getIntExtra("employeeRank", 0);
                    indexChanged = -1;
                    //setDepartmentAndValue();
                    Log.i(TAG, "full init from inside if, dept or position changed");
                    fullInitialization(false);
                }else{
                    Log.i(TAG, "full init from outside if, dept and position stayed the same");
                    fullInitialization(true); //init while keeping save
                }
                Log.i(TAG, "done with return from settings");
            }
        }else if(requestCode >= 0){

            finishedCountAdjust(indexChanged);
        }
    }
}
