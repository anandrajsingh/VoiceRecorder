package android.example.voicerecorder.Fragments;

import android.content.Intent;
import android.example.voicerecorder.Adapters.RecordingsAdapter;
import android.example.voicerecorder.OnSelectListener;
import android.example.voicerecorder.R;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecordingsFragment extends Fragment implements OnSelectListener {

    private RecyclerView recyclerView;
    private List<File> fileList;
    private RecordingsAdapter recordingsAdapter;

    File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/VRecorder");

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recordings, container, false);

        displayFiles();
        return view;
    }

    private void displayFiles() {
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        fileList = new ArrayList<>();
        fileList.addAll(findFile(path));
        recordingsAdapter = new RecordingsAdapter(getContext(), fileList, this);
        recyclerView.setAdapter(recordingsAdapter);
    }

    public ArrayList<File> findFile(File file){
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();

        for(File singleFile : files){
            if(singleFile.getName().toLowerCase().endsWith(".amr")){
                arrayList.add(singleFile);
            }
        }

        return arrayList;
    }

    @Override
    public void OnSelected(File file) {
        Uri uri = FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".provider", file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "audio/x-wav");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        getContext().startActivity(intent);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser){
            displayFiles();
        }
    }
}
