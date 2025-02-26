package com.practical.merabillpractical;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.practical.merabillpractical.Utils.Constants;
import com.practical.merabillpractical.Utils.ValidationHelper;
import com.practical.merabillpractical.data.module.PaymentType;
import com.practical.merabillpractical.data.module.Payments;
import com.practical.merabillpractical.data.repository.PaymentRepository;
import com.practical.merabillpractical.databinding.ActivityMainBinding;
import com.practical.merabillpractical.databinding.LayoutDialogBinding;
import com.practical.merabillpractical.presentation.PaymentViewModel;
import com.practical.merabillpractical.presentation.ViewModelFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    AtomicInteger selectedPaymentType = new AtomicInteger();
    private ActivityMainBinding binding;
    private PaymentViewModel viewModel;
    private String lastAmount;
    private String lastType;
    private String lastProvider;
    private String transactionRef;
    private AlertDialog alertDialog;
    private LayoutDialogBinding dialogBinding;
    private List<PaymentType> filterPayments;
    private boolean isDialogVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        initData();
        restoreAlertDialog(savedInstanceState);
        setListener();
        setContentView(binding.getRoot());
    }

    private void restoreAlertDialog(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            isDialogVisible = savedInstanceState.getBoolean(Constants.DIALOG_VISIBLE, false);
            lastAmount = savedInstanceState.getString(Constants.LAST_AMOUNT);
            lastType = savedInstanceState.getString(Constants.LAST_TYPE);
            lastProvider = savedInstanceState.getString(Constants.LAST_PROVIDER);
            transactionRef = savedInstanceState.getString(Constants.TRANSACTION_REF);
            if (isDialogVisible) {
                addPaymentDialog();
            }
        }
    }

    private void initData() {
        PaymentRepository repository = new PaymentRepository(MainActivity.this);
        viewModel = new ViewModelProvider(this, new ViewModelFactory(repository)).get(PaymentViewModel.class);
        updatePaymentsListUI();
        populatePayments();
        updatePrice();
    }

    private void updatePrice() {
        double totalAmount = 0;
        for (Payments payment : viewModel.getPayments()) {
            totalAmount += payment.getAmount();
        }

        String price = Constants.formatAmount(totalAmount);
        String strAmount = getString(R.string.txt_total_amount, price);
        SpannableString spannableString = new SpannableString(strAmount);
        int startIndex = strAmount.indexOf(price);
        int endIndex = startIndex + price.length();
        ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(Color.BLACK);
        spannableString.setSpan(foregroundSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.tvPrice.setText(spannableString);

        binding.btnSave.setEnabled(viewModel.isEnabledSaveButton());
    }

    private void setListener() {
        binding.btnSave.setOnClickListener(view -> {
            viewModel.savePayments(viewModel.getPayments());
            updatePaymentsListUI();
            populatePayments();
            updatePrice();
            Toast.makeText(MainActivity.this, getString(R.string.txt_saved_successfully), Toast.LENGTH_SHORT).show();
        });

        binding.tvPaymentLink.setOnClickListener(v -> {
            isDialogVisible = false;
            lastAmount = null;
            lastType = null;
            lastProvider = null;
            transactionRef = null;
            addPaymentDialog();
        });
    }

    private void addPaymentDialog() {

        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }

        isDialogVisible = true;

        filterPayments = viewModel.getDropDownData();

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
        dialogBinding = LayoutDialogBinding.inflate(getLayoutInflater());
        builder.setView(dialogBinding.getRoot());
        alertDialog = builder.create();

        if (lastAmount != null) dialogBinding.edtAmount.setText(lastAmount);
        updateInputs(lastType);
        if (lastProvider != null) dialogBinding.edtProvider.setText(lastProvider);
        if (transactionRef != null) dialogBinding.edtTransactionRef.setText(transactionRef);

        dialogBinding.btnCancel.setOnClickListener(v -> {
            isDialogVisible = false;
            alertDialog.dismiss();
        });

        dialogBinding.btnOk.setOnClickListener(v -> {
            isDialogVisible = false;
            if (dialogBinding != null) {
                String amount = Objects.requireNonNull(dialogBinding.edtAmount.getText()).toString();
                String provider = Objects.requireNonNull(dialogBinding.edtProvider.getText()).toString();
                String transactionRef = Objects.requireNonNull(dialogBinding.edtTransactionRef.getText()).toString();
                PaymentType paymentType = filterPayments.get(selectedPaymentType.get());
                viewModel.addNewPayments(new Payments(amount.isEmpty() ? 0 : Double.parseDouble(amount), paymentType, provider, transactionRef));

                updateInputs(paymentType.getType());
                updatePaymentsListUI();
                populatePayments();
                updatePrice();

                alertDialog.dismiss();
            }
        });

        dialogBinding.edtType.setOnClickListener(v -> {
            showDropDropDown(filterPayments, selectedPaymentType);
        });

        ValidationHelper.validPaymentForm(dialogBinding.edtAmount, dialogBinding.edtType, dialogBinding.edtProvider, dialogBinding.edtTransactionRef, dialogBinding.btnOk);

        alertDialog.show();
    }

    private void updateInputs(String paymentType) {
        if (paymentType == null) return;
        dialogBinding.edtType.setText(paymentType);
        boolean isCashOrNoType = paymentType.equalsIgnoreCase("cash") || paymentType.isEmpty();
        dialogBinding.tilProvider.setVisibility(isCashOrNoType?View.GONE:View.VISIBLE);
        dialogBinding.tilTransactionRef.setVisibility(isCashOrNoType?View.GONE:View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveDialogData();
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    private void saveDialogData() {
        if (dialogBinding != null) {
            lastAmount = Objects.requireNonNull(dialogBinding.edtAmount.getText()).toString();
            lastType = Objects.requireNonNull(dialogBinding.edtType.getText()).toString();
            lastProvider = Objects.requireNonNull(dialogBinding.edtProvider.getText()).toString();
            transactionRef = Objects.requireNonNull(dialogBinding.edtTransactionRef.getText()).toString();
        }
    }

    private void showDropDropDown(List<PaymentType> filterPayments, AtomicInteger selectedPaymentType) {
        ListPopupWindow lpw = new ListPopupWindow(MainActivity.this, null, androidx.appcompat.R.attr.listPopupWindowStyle);
        lpw.setAnchorView(dialogBinding.edtType);

        String[] values = new String[filterPayments.size()];
        for (int i = 0; i < filterPayments.size(); i++) {
            values[i] = filterPayments.get(i).getValue();
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, values);

        lpw.setAdapter(arrayAdapter);
        lpw.setOnItemClickListener((parent, lpwView, position, id) -> {
            selectedPaymentType.set(position);
            PaymentType paymentType = filterPayments.get(selectedPaymentType.get());

            if (paymentType.getType().equals("cash")) {
                dialogBinding.tilProvider.setVisibility(View.GONE);
                dialogBinding.tilTransactionRef.setVisibility(View.GONE);
            } else {
                dialogBinding.tilProvider.setVisibility(View.VISIBLE);
                dialogBinding.tilTransactionRef.setVisibility(View.VISIBLE);
            }

            dialogBinding.edtType.setText(paymentType.getValue());
            ValidationHelper.validPaymentForm(dialogBinding.edtAmount, dialogBinding.edtType, dialogBinding.edtProvider, dialogBinding.edtTransactionRef, dialogBinding.btnOk);
            lpw.dismiss();
        });
        lpw.show();
    }

    private void populatePayments() {
        binding.chipGroup.removeAllViews();
        for (int i = 0; i < viewModel.getPayments().size(); i++) {
            Payments data = viewModel.getPayments().get(i);
            Chip chip = Constants.baseChip(MainActivity.this);
            chip.setId(data.getId());
            String title = data.getPaymentType().getValue() + " = Rs. " + data.getAmount();
            chip.setText(title);
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(view -> {
                binding.chipGroup.removeView(view);
                viewModel.removePayment(data);
                updatePrice();
            });
            binding.chipGroup.addView(chip);
        }
    }

    private void updatePaymentsListUI() {
        boolean hasPayments = !viewModel.getPayments().isEmpty();
        binding.tvPrice.setVisibility(hasPayments ? View.VISIBLE : View.GONE);
        binding.tvPrice.setVisibility(hasPayments ? View.VISIBLE : View.GONE);
        binding.tvPayments.setVisibility(hasPayments ? View.VISIBLE : View.GONE);
        binding.chipGroup.setVisibility(hasPayments ? View.VISIBLE : View.GONE);
        binding.btnSave.setVisibility(hasPayments ? View.VISIBLE : View.GONE);
        binding.tvNoPaymentAddedYet.setVisibility(hasPayments ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Constants.DIALOG_VISIBLE, isDialogVisible);
        outState.putString(Constants.LAST_AMOUNT, lastAmount);
        outState.putString(Constants.LAST_TYPE, lastType);
        outState.putString(Constants.LAST_PROVIDER, lastProvider);
        outState.putString(Constants.TRANSACTION_REF, transactionRef);
    }
}