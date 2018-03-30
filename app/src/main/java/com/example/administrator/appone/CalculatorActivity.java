package com.example.administrator.appone;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class CalculatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        etLoanAmount = (EditText) findViewById(R.id.loan_amount);
        etDownPayment = (EditText) findViewById(R.id.down_payment);
        etTerm = (EditText) findViewById(R.id.term);
        etAnnualInterestRate = (EditText) findViewById(R.id.annual_interest_rate);

        tvMonthlyPayment = (TextView) findViewById(R.id.monthly_repayment);
        tvTotalRepayment = (TextView) findViewById(R.id.total_repayment);
        tvTotalInterest = (TextView) findViewById(R.id.total_interest);
        tvAverageMonthlyInterest = (TextView) findViewById(R.id.average_monthly_interest);

        //get value from storage
        SharedPreferences sp = getSharedPreferences(PREFS_CALCULATIONS, Context.MODE_PRIVATE);
        if (sp.getBoolean(HAS_RECORD,false)) {
            etLoanAmount.setText(sp.getString(E_AMOUNT,""));
            etDownPayment.setText(sp.getString(E_DOWNPAYMENT,""));
            etAnnualInterestRate.setText(sp.getString(E_INTERESTRATE,""));
            etTerm.setText(sp.getString(E_TERM,""));
            calculate();
        }

    }

    private EditText etLoanAmount, etDownPayment, etTerm, etAnnualInterestRate;
    private TextView tvMonthlyPayment, tvTotalRepayment, tvTotalInterest, tvAverageMonthlyInterest;

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_calculate:
                Log.d("Check","Calculate Button clicked!!");

                calculate();
                break;
            case R.id.button_reset:
                Log.d("Check","Reset Button clicked!!");
                reset();
                break;
        }
    }

    public static final String PREFS_CALCULATIONS = "LoanCalculation";
    public static final String HAS_RECORD = "hasRecord";
    /*public static final String MONTHLY_REPAYMENT = "monthly repayment";
    public static final String TOTAL_REPAYMENT = "total repayment";get
    public static final String TOTAL_INTEREST = "total interest";
    public static final String MONTHLY_INTEREST = "monthly interest";*/
    public static final String E_AMOUNT = "amount";
    public static final String E_DOWNPAYMENT = "downpayment";
    public static final String E_INTERESTRATE = "interest rate";
    public static final String E_TERM = "term";

    private void calculate() {
        String amount = etLoanAmount.getText().toString();
        String downPayment = etDownPayment.getText().toString();
        String interestRate = etAnnualInterestRate.getText().toString();
        String term = etTerm.getText().toString();

        if (amount.isEmpty() || downPayment.isEmpty() ) {
            Log.d("Check", "empty value");
            return;
        }

        double loanAmount = Double.parseDouble(amount) - Double.parseDouble(downPayment);
        double interest = Double.parseDouble(interestRate) / 12 / 100;
        double noOfMonth = (Integer.parseInt(term) * 12);

        if (noOfMonth > 0) {
            double monthlyRepayment = loanAmount * (interest+(interest/(java.lang.Math.pow((1+interest),noOfMonth)-1)));
            double totalRepayment = monthlyRepayment * noOfMonth;
            double totalInterest = totalRepayment - loanAmount;
            double monthlyInterest = totalInterest / noOfMonth;

            DecimalFormat format1 = new DecimalFormat("#,###.00");

            tvMonthlyPayment.setText(format1.format(monthlyRepayment));
            tvTotalRepayment.setText(format1.format(totalRepayment));
            tvTotalInterest.setText(format1.format(totalInterest));
            tvAverageMonthlyInterest.setText(format1.format(monthlyInterest));

            //save in storage
            SharedPreferences sp = getSharedPreferences(PREFS_CALCULATIONS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            /*editor.putString(MONTHLY_REPAYMENT,format1.format(monthlyRepayment));
            editor.putString(TOTAL_REPAYMENT,format1.format(totalRepayment));
            editor.putString(TOTAL_INTEREST,format1.format(totalInterest));
            editor.putString(MONTHLY_INTEREST,format1.format(monthlyInterest));*/
            editor.putString(E_AMOUNT,etLoanAmount.getText().toString());
            editor.putString(E_DOWNPAYMENT,etDownPayment.getText().toString());
            editor.putString(E_INTERESTRATE,etAnnualInterestRate.getText().toString());
            editor.putString(E_TERM,etTerm.getText().toString());
            editor.putBoolean(HAS_RECORD,true);
            editor.apply();

        }
    }

    private void reset() {
        etLoanAmount.setText("");
        etDownPayment.setText("");
        etTerm.setText("");
        etAnnualInterestRate.setText("");

        tvMonthlyPayment.setText(R.string.default_result);
        tvTotalRepayment.setText(R.string.default_result);
        tvTotalInterest.setText(R.string.default_result);
        tvAverageMonthlyInterest.setText(R.string.default_result);
    }
}

