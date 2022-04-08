package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.mariuszgromada.math.mxparser.Expression;

public class MainActivity extends AppCompatActivity {
    private TextView textViewOperations;
    private TextView textViewResult;

    private String result = "";
    private String exp = "";
    private String lastNumber = "";
    private final String[] operations = {"+", "-", "/", "*"};
    private String lastOperation = "";
    private boolean isEqual = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewOperations = findViewById(R.id.textViewOperations);
        textViewResult = findViewById(R.id.textViewResult);
    }


    public void OnNumberBtnClick(View view) {
        Button btn = (Button) view;
        String btnText = btn.getText().toString();
        if ((exp.equals("0") && btnText.equals("0")) || (!isLastOperation(exp) && lastNumber.equals("0") && !lastNumber.contains(".")))
            return;

        if (isEqual)
            exp = result;

        if (exp.equals("0")) {
            exp = btnText;
        } else {
            exp += btnText;
        }

        isEqual = false;
        calculate(exp);
        changeLastNumber(exp);
    }


    public void onOperatorBtnClick(View view) {
        if (exp == null || exp.isEmpty())
            return;

        if (exp.lastIndexOf('.') == (exp.length() - 1))
            exp += "0";

        if (isLastOperation(exp)) {
            exp = exp.substring(0, exp.length() - 1);
        }
        if (isEqual)
            exp = result;

        String operation = ((Button) view).getText().toString();
        exp += operation;
        textViewOperations.setText(exp);
        textViewResult.setText("");
        result = "";
        changeLastOperation(exp);
        isEqual = false;
    }

    public void onDeleteLastSymbolBtnClick(View view) {
        if (exp != null && !exp.isEmpty()) {
            if (isEqual)
                exp = result;

            exp = exp.substring(0, exp.length() - 1);
            if (exp.length() == 0) {
                exp = "";
                textViewOperations.setText("");
            } else {
                calculate(exp);
                changeLastNumber(exp);
                changeLastOperation(exp);
            }
            isEqual = false;
        }
    }

    public void onEqualBtnClick(View view) {
        if (exp == null || exp.isEmpty() || isLastOperation(exp) || !hasOperations(exp))
            return;

        if (exp.lastIndexOf('.') == (exp.length() - 1)) {
            exp += "0";
            calculate(exp);
        }


        if (isEqual) {
            exp = result + lastOperation + lastNumber;
            calculate(exp);
        }

        textViewOperations.setText(result);
        textViewResult.setText("");
        isEqual = true;
    }

    public void onPointBtnClick(View view) {
        if (exp != null && !exp.isEmpty() && !isLastOperation(exp) && !lastNumber.contains(".")) {
            exp += ".";
            textViewOperations.setText(exp);
            isEqual = false;
            changeLastNumber(exp);
        }
    }

    public void onDeleteBtnClick(View view) {
        exp = "";
        lastNumber = "";
        lastOperation = "";
        textViewOperations.setText("");
        textViewResult.setText("");
        result = "";
        isEqual = false;
    }


    private void calculate(String exp) {
        if (exp.lastIndexOf(".") != exp.length() - 1 && hasOperations(exp) && !isLastOperation(exp)) {
            if (lastNumber.equals("0") && lastOperation.equals("/")) {
                Toast toast = Toast.makeText(this, "Can't divide by zero.", Toast.LENGTH_SHORT);
                toast.show();
                this.exp = exp.substring(0, exp.length() - 1);
                return;
            }
            Expression expression = new Expression(exp);
            String res = String.valueOf((expression.calculate()));
            textViewResult.setText(res);
            result = res;
        } else {
            textViewResult.setText("");
            result = "";
        }
        textViewOperations.setText(exp);
    }

    private void changeLastOperation(String str) {
        if (str != null && !str.isEmpty()) {
            String[] res = str.split("[^*|+|-|/]");
            lastOperation = res.length > 0 ? res[res.length - 1] : "";
        } else {
            lastOperation = "";
        }
        System.out.println(lastOperation);
    }

    private void changeLastNumber(String str) {
        if (str != null && !str.isEmpty()) {
            String[] res = str.split("[*|+|-|/]");
            lastNumber = res.length > 0 ? res[res.length - 1] : "";
        } else
            lastNumber = "";
    }

    private boolean isLastOperation(String str) {
        if (str != null && !str.isEmpty()) {
            String lastSymbol = str.substring(str.length() - 1);
            for (String symbol : operations) {
                if (symbol.equals(lastSymbol))
                    return true;
            }
        }
        return false;
    }

    private boolean hasOperations(String str) {
        if (str != null && !str.isEmpty()) {
            for (String symbol : operations) {
                if (str.contains(symbol))
                    return true;
            }
        }
        return false;
    }


}