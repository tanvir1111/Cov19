package com.ece.cov19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ece.cov19.DataModels.UserDataModel;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView aPositive, aNegative, bPositive, bNegative, oPositive, oNegative, abPositive, abNegative, selectedBldGrp;
    private EditText nameEditText, ageEditText, thanaEditText, passwordEditText, confPasswordEditText;
    private String bloodGroup, gender, donorInfo = "na";
    private ImageView genderMale, genderFemale;
    private Button singUp;
    private Spinner divisionSpinner, districtSpinner;
    public int divisionResourceIds[] = {R.array.Dhaka, R.array.Rajshahi, R.array.Rangpur, R.array.Khulna, R.array.Chittagong, R.array.Mymensingh,

            R.array.Barisal, R.array.Sylhet};

    private CheckBox isPlasmaDonor, isDonorBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

//        all editTexts
        nameEditText = findViewById(R.id.reg_name_edittext);
        ageEditText = findViewById(R.id.reg_age_edittext);
        thanaEditText = findViewById(R.id.reg_thana_edittext);
        passwordEditText = findViewById(R.id.reg_password_edittext);
        confPasswordEditText = findViewById(R.id.reg_confirm_password_edittext);


//      buttons
        singUp = findViewById(R.id.reg_sign_up_btn);
        isDonorBtn = findViewById(R.id.reg_donor_checkbox);
        isPlasmaDonor = findViewById(R.id.reg_plasma_checkbox);
//        spinners
        divisionSpinner = findViewById(R.id.reg_division_spinner);
        districtSpinner = findViewById(R.id.reg_district_spinner);


        /*blood group textviews*/
        aPositive = findViewById(R.id.reg_bld_a_positive);
        bPositive = findViewById(R.id.reg_bld_b_positive);
        oPositive = findViewById(R.id.reg_bld_o_positive);
        abPositive = findViewById(R.id.reg_bld_ab_positive);
        aNegative = findViewById(R.id.reg_bld_a_negative);
        bNegative = findViewById(R.id.reg_bld_b_negative);
        oNegative = findViewById(R.id.reg_bld_o_negative);
        abNegative = findViewById(R.id.reg_bld_ab_negative);

        /*        Gender Imageviews*/
        genderMale = findViewById(R.id.reg_male_icon);
        genderFemale = findViewById(R.id.reg_female_icon);

        /* just a random one to avoid nullPoint Exception*/
        selectedBldGrp = aPositive;


//        all OnclickListeners
        aPositive.setOnClickListener(this);
        bPositive.setOnClickListener(this);
        oPositive.setOnClickListener(this);
        abPositive.setOnClickListener(this);
        aNegative.setOnClickListener(this);
        bNegative.setOnClickListener(this);
        oNegative.setOnClickListener(this);
        abNegative.setOnClickListener(this);
        genderMale.setOnClickListener(this);
        genderFemale.setOnClickListener(this);
        singUp.setOnClickListener(this);


//      Districts spinner as per selected division

        divisionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String> adpter = new ArrayAdapter<String>(
                        RegistrationActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        getResources().getStringArray(divisionResourceIds[position]));
                districtSpinner.setAdapter(adpter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


//        donorcheckbox setting donor info blood/plasma/na
        isDonorBtn.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            donorInfo = "Blood";
                            isPlasmaDonor.setVisibility(View.VISIBLE);
                            isPlasmaDonor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if (isChecked) {
                                        donorInfo = "Plasma";

                                    } else {
                                        donorInfo = "Blood";
                                    }
                                    Toast.makeText(RegistrationActivity.this, donorInfo, Toast.LENGTH_SHORT).show();


                                }
                            });

                        } else {
                            donorInfo = "na";
                            isPlasmaDonor.setVisibility(View.INVISIBLE);
                        }
                        Toast.makeText(RegistrationActivity.this, donorInfo, Toast.LENGTH_SHORT).show();
                    }
                });


    }

    //  onclickListening
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reg_male_icon:
                genderFemale.setImageResource(R.drawable.female_icon);
                genderMale.setImageResource(R.drawable.male_icon_selected);
                gender = "male";
                break;
            case R.id.reg_female_icon:
                genderFemale.setImageResource(R.drawable.female_icon_selected);
                genderMale.setImageResource(R.drawable.male_icon);
                gender = "female";
                break;
            case R.id.reg_bld_a_positive:
            case R.id.reg_bld_b_positive:
            case R.id.reg_bld_o_positive:
            case R.id.reg_bld_ab_positive:
            case R.id.reg_bld_a_negative:
            case R.id.reg_bld_b_negative:
            case R.id.reg_bld_o_negative:
            case R.id.reg_bld_ab_negative:
                selectedBldGrp.setBackgroundResource(R.drawable.blood_grp_not_selected);
                selectedBldGrp.setTextColor(getColor(R.color.textColorGrey));
                selectedBldGrp = findViewById(v.getId());
                selectedBldGrp.setBackgroundResource(R.drawable.blood_grp_selected);
                selectedBldGrp.setTextColor(Color.WHITE);
                bloodGroup = selectedBldGrp.getText().toString();
                break;

            case R.id.reg_sign_up_btn:

                verifyData();

                break;
        }
    }


    //    checking valid data or empty fields
    private void verifyData() {
        String name, phone, division, district, thana, age, password, emptyfield = "all ok";
        name = nameEditText.getText().toString();
        thana = thanaEditText.getText().toString();
        age = ageEditText.getText().toString();
        password = passwordEditText.getText().toString();
        division = divisionSpinner.getSelectedItem().toString();
        district = districtSpinner.getSelectedItem().toString();
        Intent i = getIntent();
        phone = i.getExtras().get("phone").toString();


//        checking empty Fields

        if (name.isEmpty()) {
            emptyfield = "name";
        } else if (thana.isEmpty()) {
            emptyfield = "thana";
        } else if (age.isEmpty()) {
            emptyfield = "age";
        } else if (password.isEmpty()) {
            emptyfield = "password";
        } else if (division.isEmpty()) {
            emptyfield = "division";
        } else if (district.isEmpty()) {
            emptyfield = "district";
        } else if (bloodGroup == null) {
            emptyfield = "bloodGroup";
        } else if (gender == null) {
            emptyfield = "gender";
        }


        if (emptyfield.equals("all ok")) {
            if (password.length() < 6) {
                Toast.makeText(this, "password must be of at least 6 characters", Toast.LENGTH_SHORT).show();
            } else {
                if (password.equals(confPasswordEditText.getText().toString())) {
//            retro operations

                    registerUser(name, phone, division, district, thana, age, password);


                } else {
                    Toast.makeText(this, "password doesn't match", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, "Enter " + emptyfield, Toast.LENGTH_SHORT).show();
        }


    }

//    database operations

    private void registerUser(String name, String phone, String division, String district, String thana, String age, String password) {

        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<UserDataModel> sendingData = retroInterface.registerRetroMethod(name, phone, gender, bloodGroup, division, district, thana, age, donorInfo, password);
        sendingData.enqueue(new Callback<UserDataModel>() {
            @Override
            public void onResponse(Call<UserDataModel> call, Response<UserDataModel> response) {
                if (response.body().getServerMsg().equals("Success")) {
                    Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();

//              going to login activity
                    Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegistrationActivity.this, response.body().getServerMsg(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UserDataModel> call, Throwable t) {
                Toast.makeText(RegistrationActivity.this, "failed to register! Check your connection and try again.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
