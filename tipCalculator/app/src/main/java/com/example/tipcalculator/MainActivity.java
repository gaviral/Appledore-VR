package com.example.tipcalculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import static com.example.tipcalculator.R.id.tipPercentageInput;

public class MainActivity extends AppCompatActivity {

    EditText totalBill, tipPercentage, numOfPeople;
    TextView totalToPay, totalTip, totalPerPerson;
    EditText selectedEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        totalBill = (EditText) findViewById(R.id.totalBillEntered); // total bill
        selectedEditText = totalBill;
        tipPercentage = (EditText) findViewById(tipPercentageInput); // tip percentage
        numOfPeople = (EditText) findViewById(R.id.numOfPeopleInput); // number of people (bill split)
        totalToPay = (TextView) findViewById(R.id.totalToPayResult);
        totalTip = (TextView) findViewById(R.id.totalTipResult);
        totalPerPerson = (TextView) findViewById(R.id.totalPerPersonResult);

        totalBill.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectedEditText = totalBill;
            }
        });
        tipPercentage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectedEditText = tipPercentage;
            }
        });
        numOfPeople.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectedEditText = numOfPeople;
            }
        });
    }


    public void onNumPadPressed(View v) {
        switch (v.getId()) {
            case (R.id.numPadButton_1):
                onNumberPadPressed(1);
                break;
            case (R.id.numPadButton_2):
                onNumberPadPressed(2);
                break;
            case (R.id.numPadButton_3):
                onNumberPadPressed(3);
                break;
            case (R.id.numPadButton_4):
                onNumberPadPressed(4);
                break;
            case (R.id.numPadButton_5):
                onNumberPadPressed(5);
                break;
            case (R.id.numPadButton_6):
                onNumberPadPressed(6);
                break;
            case (R.id.numPadButton_7):
                onNumberPadPressed(7);
                break;
            case (R.id.numPadButton_8):
                onNumberPadPressed(8);
                break;
            case (R.id.numPadButton_9):
                onNumberPadPressed(9);
                break;
            case (R.id.numPadButton_Reset):
                onNumberPadPressed(10);
                break;
            case (R.id.numPadButton_0):
                onNumberPadPressed(0);
                break;
            case (R.id.numPadButton_Del):
                onNumberPadPressed(11);
                break;
        }
        ;
        //onNumPadPress(1);
    }

    public void onNumberPadPressed(int numPressed) {
        int currentNum;
        String newNumber;
        try {
            currentNum = Integer.parseInt(selectedEditText.getText().toString());
        } catch (NumberFormatException ex) {
            currentNum = 0;
        }

        switch (numPressed) {
            case 10:
                selectedEditText.setText("0");
                updateTipReport();
                break;
            case 11:
                newNumber = String.format("%d", currentNum / 10); //backspace functionality
                selectedEditText.setText(newNumber);
                updateTipReport();
                break;
            default:
                System.out.println("+");
                newNumber = String.format("%d%d", currentNum, numPressed);
                selectedEditText.setText(newNumber);
                updateTipReport();
                break;
        }
    }


    public void onDecreaseTipPercentageClick(View view) {
        int currentTipPercentage;
        try {
            currentTipPercentage = Integer.parseInt(tipPercentage.getText().toString());
        } catch (NumberFormatException ex) {
            currentTipPercentage = 0;
        }

        currentTipPercentage--;

        String newTipPercentage = String.format("%d", currentTipPercentage);
        //TextView tipPercentageInput = (TextView) findViewById (tipPercentageInput);
        tipPercentage.setText(newTipPercentage);

        updateTipReport();
    }

    public void onIncreaseTipPercentageClick(View view) {
        int currentTipPercentage;
        try {
            currentTipPercentage = Integer.parseInt(tipPercentage.getText().toString());
        } catch (NumberFormatException ex) {
            currentTipPercentage = 0;
        }

        currentTipPercentage++;

        String newTipPercentage = String.format("%d", currentTipPercentage);
        //TextView tipPercentageInput = (TextView) findViewById (tipPercentageInput);
        tipPercentage.setText(newTipPercentage);

        updateTipReport();
    }

    public void onDecreaseNumberOfPeopleClick(View view) {
        int currentNumberOfPeople;
        try {
            currentNumberOfPeople = Integer.parseInt(numOfPeople.getText().toString());
        } catch (NumberFormatException ex) {
            currentNumberOfPeople = 0;
        }

        int newNumOfPeople = currentNumberOfPeople - 1;

        String newNumberOfPeople = String.format("%d", newNumOfPeople);
        //TextView numberOfPeopleInput = (TextView) findViewById (R.id.numOfPeopleInput);
        numOfPeople.setText(newNumberOfPeople);

        updateTipReport();
    }

    public void onIncreaseNumberOfPeopleClick(View view) {
        int currentNumberOfPeople;
        try {
            currentNumberOfPeople = Integer.parseInt(numOfPeople.getText().toString());
        } catch (NumberFormatException ex) {
            currentNumberOfPeople = 0;
        }

        int newNumOfPeople = currentNumberOfPeople + 1;

        String newNumberOfPeople = String.format("%d", newNumOfPeople);
        //TextView numberOfPeopleInput = (TextView) findViewById (R.id.numOfPeopleInput);
        numOfPeople.setText(newNumberOfPeople);

        updateTipReport();
    }

    public void updateTipReport() {
        double currentTotalBill, currentTipPercentage, currentNumberOfPeople;

        try {
            currentTotalBill = Integer.parseInt(totalBill.getText().toString());
        } catch (NumberFormatException ex) {
            currentTotalBill = 0;
        }

        try {
            currentTipPercentage = Integer.parseInt(tipPercentage.getText().toString());
        } catch (NumberFormatException ex) {
            currentTipPercentage = 0;
        }

        try {
            currentNumberOfPeople = Integer.parseInt(numOfPeople.getText().toString());
            ;
        } catch (NumberFormatException ex) {
            currentNumberOfPeople = 1;
        }


        double newTotalTipValue = currentTotalBill * (currentTipPercentage / 100);
        double newTotalBillValue = currentTotalBill + newTotalTipValue;
        double newTotalPerPersonValue = newTotalBillValue / currentNumberOfPeople;

        String newTotalToPay = String.format("$%.2f", newTotalBillValue);
        String newTotalTip = String.format("$%.2f", newTotalTipValue);
        String newTotalPerPerson = String.format("$%.2f", newTotalPerPersonValue);

        totalToPay.setText(newTotalToPay);
        totalTip.setText(newTotalTip);
        totalPerPerson.setText(newTotalPerPerson);
    }
}
