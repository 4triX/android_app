package com.example.administrator.appone;

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

