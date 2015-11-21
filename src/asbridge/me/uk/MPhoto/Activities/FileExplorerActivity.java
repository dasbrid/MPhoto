package asbridge.me.uk.MPhoto.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.*;
import asbridge.me.uk.MPhoto.R;
import asbridge.me.uk.MPhoto.settings.SettingsActivity;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by David on 20/11/2015.
 */
public class FileExplorerActivity extends Activity {
    private ArrayList<String> filenames = new ArrayList<String>();
    private ListView listView;

    private EditText folderView;

    private ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_explorer);

        TextView extFolder = (TextView) findViewById(R.id.extfolder);
        String secStore = Environment.getExternalStorageDirectory().toString();

        extFolder.setText("="+secStore);

        filenames.add(new String("choose a file"));


        listView = (ListView) findViewById(R.id.listView);
        folderView = (EditText) findViewById(R.id.folder);

         adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, filenames);

        // Assign adapter to ListView
        listView.setAdapter(adapter);

    }


    public void btnAlbumsClicked(View v) {
        Intent intent = new Intent(this, AlbumsActivity.class);
        this.startActivity(intent);
    }

    public void btnSettingsClicked(View v) {
        Intent intent = new Intent(this, SettingsActivity.class);
        this.startActivity(intent);
    }

    public void btnGoClicked(View v)
    {
        String f = folderView.getText().toString();
        if (f == null)
            return;
        Toast.makeText(this, f,Toast.LENGTH_SHORT).show();

        try {
            getFilesInFolder(new File(f));
            Toast.makeText(this, "found " + filenames.size() + " files", Toast.LENGTH_SHORT).show();

            adapter.notifyDataSetChanged();
        } catch (Exception ex) {
            Toast.makeText(this, "ex="+ex.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    // recursion method for getting FILES in a folder
    private void getFilesInFolder(File folder) {
        filenames.clear();
        ArrayList<String> files = new ArrayList<>();
        if (folder.isFile()) {
            Toast.makeText(this, folder.getName() + " is a file",Toast.LENGTH_SHORT).show();
            return;
        }
        File[] listFiles = folder.listFiles();
        if (listFiles == null) {
            Toast.makeText(this, folder.getName() + " is a file",Toast.LENGTH_SHORT).show();
            return;
        }
        for (File file : listFiles) {
            filenames.add(file.getAbsolutePath());
        }
    }

}