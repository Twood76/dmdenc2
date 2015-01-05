package com.ricky.encounterassistant.ui.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ricky.encounterassistant.*;
import com.ricky.encounterassistant.models.Avatar;
import com.ricky.encounterassistant.models.Character;

/**
 * Created by Ricky on 10/14/2014.
 */
public class NewCharacterDialog extends DialogFragment {
    public static final String EXTRA_CHARACTER = "com.ricky.encounterassistant.character";
    private EditText nameEditText;
    private EditText healthEditText;
    private EditText initEditText;
    private EditText acEditText;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_new_character, null);

        nameEditText = (EditText) view.findViewById(R.id.new_character_nameEditText);
        healthEditText = (EditText) view.findViewById(R.id.new_character_healthEditText);
        initEditText = (EditText) view.findViewById(R.id.new_character_initEditText);
        acEditText = (EditText) view.findViewById(R.id.new_character_acEditText);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK)
                .setView(view)
                .setTitle(R.string.new_character)
                .setPositiveButton(R.string.create_button, null)
                .setNegativeButton(R.string.cancel_button, null)
                .create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (sendResult(Activity.RESULT_OK)) {
                            dialog.dismiss();
                        }
                    }
                });
            }
        });

        return dialog;
    }

    /*
     * Function that is called when the positive button of the New Character Dialog is pressed
     * sends a character generated by user input data to be added to the character list
     * Returns true if character was able to be created from the user input, false if not.
     */
    private boolean sendResult(int resultCode) {
        if (getTargetFragment() == null) return false;

        /*
         * Check if all fields of the dialog contains something, if not, then make a toast telling
         * the user to fill out all fields
         * If all fields were filled out, then create a new character object with all the data from
         * the fields and put it into an intent as an extra and return to target
         */
        if (nameEditText.getText().toString().equals("")|| acEditText.getText().toString().equals("") || healthEditText.getText().toString().equals("")
                || initEditText.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getActivity(), "Fill out all fields before submitting", Toast.LENGTH_LONG);
            toast.show();
            return false;
        }
        Character character = new Character(nameEditText.getText().toString(), Integer.parseInt(acEditText.getText().toString()),
                Integer.parseInt(healthEditText.getText().toString()), Integer.parseInt(healthEditText.getText().toString()),
                Integer.parseInt(initEditText.getText().toString()), new Avatar(getActivity(), "skeleton"), getActivity());
        Intent intent = new Intent();
        intent.putExtra(EXTRA_CHARACTER, character);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
        return true;
    }
}
