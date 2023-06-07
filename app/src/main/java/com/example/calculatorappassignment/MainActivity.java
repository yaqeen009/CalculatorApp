package com.example.calculatorappassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import java.util.Stack;
import android.widget.Button;
import android.widget.TextView;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView resultTV, inputTV;
    Button buttonC, buttonOpenBracket, buttonCloseBracket;
    Button buttonDivide, buttonMultiply, buttonAdd, buttonSubtract, buttonEqual;
    Button button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    Button buttonAC, buttonDot;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTV = findViewById(R.id.result_tv);
        inputTV = findViewById(R.id.input_tv);

        assignID(buttonC, R.id.c_button);
        assignID(button0, R.id.button_0);
        assignID(button1, R.id.button_1);
        assignID(button2, R.id.button_2);
        assignID(button3, R.id.button_3);
        assignID(button4, R.id.button_4);
        assignID(button5, R.id.button_5);
        assignID(button6, R.id.button_6);
        assignID(button7, R.id.button_7);
        assignID(button8, R.id.button_8);
        assignID(button9, R.id.button_9);
        assignID(buttonDivide, R.id.division);
        assignID(buttonMultiply, R.id.multiplication);
        assignID(buttonAdd, R.id.addition);
        assignID(buttonSubtract, R.id.subtraction);
        assignID(buttonEqual, R.id.equalTo);
        assignID(buttonOpenBracket, R.id.open_bracket);
        assignID(buttonCloseBracket, R.id.close_bracket);
        assignID(buttonAC, R.id.button_AC);
        assignID(buttonDot, R.id.button_dot);
    }

    void assignID(Button btn, int id){
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }
    String evaluateExpression(String expression) {
        try {
            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scope = context.initStandardObjects();
            Object result = context.evaluateString(scope, expression, "JavaScript", 1, null);

            if (result != null) {
                String finalResult = Context.toString(result);

                if (finalResult.endsWith(".0")) {
                    finalResult = finalResult.replace(".0", "");
                }

                return finalResult;
            }
        } catch (Exception e) {
            return "Err: Invalid expression";
        }

        return "Err: Invalid expression";
    }

    String evaluateParentheses(String data) {
        try {
            Stack<Integer> stack = new Stack<>();
            StringBuilder builder = new StringBuilder(data);
            for (int i = 0; i < builder.length(); i++) {
                char ch = builder.charAt(i);
                if (ch == '(') {
                    stack.push(i);
                } else if (ch == ')') {
                    if (stack.isEmpty()) {
                        return "Err: Invalid parentheses";
                    }
                    int openingIndex = stack.pop();
                    String expression = builder.substring(openingIndex + 1, i);
                    String result = evaluateExpression(expression);
                    if (result.startsWith("Err")) {
                        return result;
                    }
                    builder.replace(openingIndex, i + 1, result);
                    i = openingIndex + result.length();
                }
            }
            if (!stack.isEmpty()) {
                return "Err: Invalid parentheses";
            }
            return evaluateExpression(builder.toString());
        } catch (Exception e) {
            return "Err: Invalid expression";
        }
    }


    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        String buttonText = button.getText().toString();
        String dataToCalculate = inputTV.getText().toString();

        if (buttonText.equals("AC")){
            resultTV.setText("");
            inputTV.setText("");
            return;
        }
        if (buttonText.equals("=")){
            inputTV.setText(resultTV.getText());
            return;

        }
        if (buttonText.equals("C")){
            dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length()-1);
        }else if (buttonText.equals(")")) {
            if (dataToCalculate.contains("(")) {
                dataToCalculate += buttonText;
            }
        }  else {
            dataToCalculate = dataToCalculate + buttonText;
        }
        inputTV.setText(dataToCalculate);
        String finalResult = getResult(dataToCalculate);
        if (!finalResult.equals("Err")){
            resultTV.setText(finalResult);
        }
    }
    String getResult(String data){
        try {
            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable = context.initStandardObjects();
            String finalResult = context.evaluateString(scriptable, data, "JavaScript", 1, null).toString();
            if (finalResult.endsWith(".0")){
                finalResult = finalResult.replace(".0", "");
            }
            return finalResult;
        }catch(Exception e){
            return "Err";
        }
    }
}