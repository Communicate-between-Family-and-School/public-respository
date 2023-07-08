package com.example.mySpecialConversion;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class CustomTextWatcher implements TextWatcher {
    private EditText editText;
    public CustomTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // 在文本变化时处理
        // 自动去除输入整数的前导零
        String inputText = s.toString();
        String trimmedText = inputText.replaceFirst("^0+(?!$)", "");
        if (!inputText.equals(trimmedText)) {
            editText.setText(trimmedText);
            editText.setSelection(trimmedText.length());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
