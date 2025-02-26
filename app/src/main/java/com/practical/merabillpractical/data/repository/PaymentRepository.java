package com.practical.merabillpractical.data.repository;

import android.content.Context;

import com.practical.merabillpractical.Utils.FileHelper;
import com.practical.merabillpractical.data.module.Payments;

import java.util.List;

public final class PaymentRepository {

    private final Context context;

    public PaymentRepository(Context context) {
        this.context = context;
    }

    public void savePayments(List<Payments> payments) {
        FileHelper.writeFile(payments, context);
    }

    public List<Payments> getPayments() {
        return FileHelper.getPayments(context);
    }

}
