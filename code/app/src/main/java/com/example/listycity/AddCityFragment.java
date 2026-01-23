package com.example.listycity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {

    private static final String ARG_CITY = "city";
    private City cityToEdit = null;

    interface AddCityDialogListener {
        void addCity(City city);
        void editCity(City originalCity, City editedCity);
    }

    private AddCityDialogListener listener;

    // Empty constructor for adding new cities
    public AddCityFragment() {
    }

    // Static method to create fragment for editing
    public static AddCityFragment newInstance(City city) {
        AddCityFragment fragment = new AddCityFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CITY, city);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cityToEdit = (City) getArguments().getSerializable(ARG_CITY);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_add_city, null);

        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);

        // Check if we're editing or adding
        boolean isEditing = cityToEdit != null;
        String title = isEditing ? "Edit City" : getString(R.string.add_city_title);
        String positiveButton = isEditing ? "Update" : getString(R.string.add);

        // If editing, pre-fill the fields
        if (isEditing) {
            editCityName.setText(cityToEdit.getName());
            editProvinceName.setText(cityToEdit.getProvince());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        return builder
                .setView(view)
                .setTitle(title)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(positiveButton, (dialog, which) -> {
                    String cityName = editCityName.getText().toString();
                    String provinceName = editProvinceName.getText().toString();

                    if (isEditing) {
                        // Edit existing city
                        City editedCity = new City(cityName, provinceName);
                        listener.editCity(cityToEdit, editedCity);
                    } else {
                        // Add new city
                        listener.addCity(new City(cityName, provinceName));
                    }
                })
                .create();
    }
}
