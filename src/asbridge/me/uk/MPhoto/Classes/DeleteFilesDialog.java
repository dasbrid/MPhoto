package asbridge.me.uk.MPhoto.Classes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import asbridge.me.uk.MPhoto.adapter.GridViewImageAdapter;
import asbridge.me.uk.MPhoto.helper.AppConstant;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by AsbridgeD on 30/11/2015.
 */
public class DeleteFilesDialog extends DialogFragment {

    ArrayList<CheckedFile> selectedFiles;
    ArrayList<CheckedFile> imageFiles;
    GridViewImageAdapter adapter;

    public DeleteFilesDialog() {
        super();
        this.selectedFiles = new ArrayList<CheckedFile>();
        this.imageFiles = null;
        this.adapter = null;
    }

    public DeleteFilesDialog(ArrayList<CheckedFile> selectedFiles, ArrayList<CheckedFile> imagefiles, GridViewImageAdapter adapter) {
        this();
        this.selectedFiles = selectedFiles;
        this.imageFiles = imagefiles;
        this.adapter = adapter;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to delete " + selectedFiles.size() + " selected pictures?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                File fileToDelete;
                for (int i = 0; i < selectedFiles.size(); i++) {
                    fileToDelete = selectedFiles.get(i).getFile();
                    // Only actually delete if deletion enabled
                    if (AppConstant.ALLOW_DELETE) {
                        // DON'T DELETE   fileToDelete.delete();
                    }
                    imageFiles.remove(selectedFiles.get(i));
                }
                adapter.clearSelection();
                adapter.notifyDataSetChanged();
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}


