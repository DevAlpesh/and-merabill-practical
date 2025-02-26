package com.practical.merabillpractical.Utils;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.google.android.material.chip.Chip;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.practical.merabillpractical.R;
import com.practical.merabillpractical.data.module.Payments;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Currency;
import java.util.List;

public class Constants {

    public static final String DIALOG_VISIBLE = "dialog_visible";
    public static final String LAST_AMOUNT = "lastAmount";
    public static final String LAST_TYPE = "lastType";
    public static final String LAST_PROVIDER = "lastProvider";
    public static final String TRANSACTION_REF = "transactionRef";

    public static Chip baseChip(Context context) {
        Chip chip = new Chip(context);
        chip.setClickable(false);
        chip.setFocusable(false);
        chip.setCheckable(false);
        chip.setCheckedIcon(null);

        chip.setChipBackgroundColor(
                ContextCompat.getColorStateList(context, R.color.color_chip)
        );

        chip.setChipMinHeight(context.getResources().getDimension(R.dimen.chip_height));

        chip.setTextColor(
                ContextCompat.getColorStateList(context, R.color.black)
        );

        chip.setTextAppearance(R.style.Theme_TextView_TitleMedium);

        ShapeAppearanceModel shapeModel = chip.getShapeAppearanceModel()
                .toBuilder()
                .setAllCornerSizes(context.getResources().getDimension(R.dimen.chip_radius))
                .build();

        chip.setShapeAppearanceModel(shapeModel);

        return chip;
    }

    public static List<Payments> stringToPaymentList(String strPaymentJson) {
        Gson gson = new Gson();
        return gson.fromJson(strPaymentJson, new TypeToken<List<Payments>>() {
        }.getType());
    }

    public static String paymentDataToJson(List<Payments> data) {
        Gson gson = new Gson();
        return gson.toJson(data);
    }

    public static String formatAmount(double amount) {
        DecimalFormatSymbols ds = new DecimalFormatSymbols();
        ds.setCurrency(Currency.getInstance("INR"));
        DecimalFormat df = new DecimalFormat("₹ ####.00", ds);
        return df.format(amount);
    }
}
