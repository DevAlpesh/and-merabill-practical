package com.practical.merabillpractical.Utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ValidationHelper {

    private static void validateForm(
            List<EditText> editTexts,
            MaterialButton button,
            Map<EditText, ValidationRule> validationRules
    ) {

        Runnable performValidate = () -> {
            boolean fieldsValid = true;
            for (EditText editText : editTexts) {
                ValidationRule validationRule = validationRules.get(editText);
                if (validationRule != null && !validationRule.isValid(editText.getText().toString())) {
                    fieldsValid = false;
                }
            }
            button.setEnabled(fieldsValid);
        };

        for (EditText editText : editTexts) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    performValidate.run();
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
        }
    }

    public static void validPaymentForm(EditText edtAmount, EditText edtType, EditText edtProvider, EditText edtTransactionRef, MaterialButton button) {

        boolean hasCashType = edtType.getText().toString().equalsIgnoreCase("cash");

        List<EditText> editTexts = Arrays.asList(edtAmount, edtType, edtProvider, edtTransactionRef);

        Map<EditText, ValidationRule> validationRules =
                Map.of(edtAmount, input -> !input.isEmpty(),
                        edtType, input -> !input.isEmpty(),
                        edtProvider, input -> hasCashType || !input.isEmpty(),
                        edtTransactionRef, input -> hasCashType || !input.isEmpty()
                );

        button.setEnabled(!edtAmount.getText().toString().isEmpty()
                && !edtType.getText().toString().isEmpty()
                && (hasCashType || !edtProvider.getText().toString().isEmpty())
                && (hasCashType || !edtTransactionRef.getText().toString().isEmpty())
        );

        validateForm(editTexts, button, validationRules);

    }

    public interface ValidationRule {
        boolean isValid(String input);
    }
}
