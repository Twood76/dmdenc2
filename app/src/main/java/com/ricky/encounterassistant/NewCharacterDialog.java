package com.ricky.encounterassistant;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Ricky on 10/14/2014.
 */
public class NewCharacterDialog extends DialogFragment {
    public static final String EXTRA_CHARACTER = "com.ricky.encounterassistant.character";
    private EditText nameEditText;
    private EditText healthEditText;
    private EditText initEditText;
    private EditText acEditText;
    private EditText foEditText;
    private EditText reEditText;
    private EditText wiEditText;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_new_character, null);

        nameEditText = (EditText) view.findViewById(R.id.new_character_nameEditText);
        healthEditText = (EditText) view.findViewById(R.id.new_character_healthEditText);
        initEditText = (EditText) view.findViewById(R.id.new_character_initEditText);
        acEditText = (EditText) view.findViewById(R.id.new_character_acEditText);
        foEditText = (EditText) view.findViewById(R.id.new_character_foEditText);
        reEditText = (EditText) view.findViewById(R.id.new_character_reEditText);
        wiEditText = (EditText) view.findViewById(R.id.new_character_wiEditText);

        return new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK)
                .setView(view)
                .setTitle(R.string.new_character)
                .setPositiveButton(R.string.create_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .setNegativeButton(R.string.cancel_button, null)
                .create();
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null) return;

        Character character = new Character(nameEditText.getText().toString(), Integer.parseInt(acEditText.getText().toString()),
                Integer.parseInt(foEditText.getText().toString()), Integer.parseInt(reEditText.getText().toString()),
                Integer.parseInt(wiEditText.getText().toString()), Integer.parseInt(healthEditText.getText().toString()),
                Integer.parseInt(healthEditText.getText().toString()), Integer.parseInt(initEditText.getText().toString()),
                Character.Avatar.SKELETON, getActivity());
        Intent intent = new Intent();
        intent.putExtra(EXTRA_CHARACTER, character);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
