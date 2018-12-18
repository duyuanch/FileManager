package com.github.shellhub.filemanager.view;

import com.github.shellhub.filemanager.entity.FileEntity;
import com.github.shellhub.filemanager.event.RenameEvent;

import java.util.List;

public interface MainView {
    void showFiles(List<FileEntity> fileEntities);

    void exit();

    void rename(RenameEvent renameEvent);
}
