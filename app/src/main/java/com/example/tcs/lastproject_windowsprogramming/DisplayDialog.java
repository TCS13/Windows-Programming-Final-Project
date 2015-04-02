package com.example.tcs.lastproject_windowsprogramming;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TextView;

/**
 * Created by TCS on 3/10/15.
 */
public class DisplayDialog extends DialogFragment{

    public static final String EXTRA_STRING_TITLE = "com.example.tcs.lastproject_windowsprogramming.string-title";
    public static final String EXTRA_STRING_MESSAGE = "com.example.tcs.lastproject_windowsprogramming.string-message";
    private String mTitle;
    private String mMessage;

    public static DisplayDialog newInstance(String title, String message) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_STRING_TITLE, title);
        args.putSerializable(EXTRA_STRING_MESSAGE, message);

        DisplayDialog fragment = new DisplayDialog();
        fragment.setArguments(args);

        return fragment;
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null)
            return;

        Intent i = new Intent();
        i.putExtra(EXTRA_STRING_TITLE, mTitle);
        i.putExtra(EXTRA_STRING_MESSAGE, mMessage);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        mTitle = (String)getArguments().getSerializable(EXTRA_STRING_TITLE);
        mMessage = (String)getArguments().getSerializable(EXTRA_STRING_MESSAGE);
        View v = getActivity().getLayoutInflater().inflate(R.layout.text_dialogs, null);
        TextView message = (TextView)v.findViewById(R.id.dialog_text);
        message.setText(mMessage);
        return new AlertDialog.Builder(getActivity()).setView(v).setTitle(mTitle).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                sendResult(Activity.RESULT_OK);
            }
        }).create();
    }
}