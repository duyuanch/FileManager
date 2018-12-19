package com.github.shellhub.filemanager.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.shellhub.filemanager.R;
import com.github.shellhub.filemanager.adapter.HomeAdapter;
import com.github.shellhub.filemanager.entity.FileRemoveEvent;
import com.github.shellhub.filemanager.entity.ScrollEvent;
import com.github.shellhub.filemanager.event.FileEntitiesEvent;
import com.github.shellhub.filemanager.event.RenameEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {

    @BindView(R.id.rv_main)
    RecyclerView rvMain;

    private HomeAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nav_home, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        setup();
        return view;
    }

    private void setup() {
        rvMain.setAdapter(adapter = new HomeAdapter());
        rvMain.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMain.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        rvMain.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                EventBus.getDefault().post(new ScrollEvent(dy));
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFileEntitiesEvent(FileEntitiesEvent event) {
        adapter.setFileEntities(event.getFileEntities());
        adapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRenameEvent(RenameEvent renameEvent) {
        adapter.getFileEntities().set(renameEvent.getPosition(), renameEvent.getFileEntity());
        adapter.notifyItemChanged(renameEvent.getPosition());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFileRemoveEvent(FileRemoveEvent fileRemoveEvent) {
        int removePosition = fileRemoveEvent.getPosition();
        adapter.getFileEntities().remove(removePosition);
        adapter.notifyItemRemoved(removePosition);
    }
}
