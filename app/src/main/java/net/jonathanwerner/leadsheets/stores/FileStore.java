package net.jonathanwerner.leadsheets.stores;

import android.os.Environment;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import net.jonathanwerner.leadsheets.events.AllSongsChanged;
import net.jonathanwerner.leadsheets.events.ChangeFolders;
import net.jonathanwerner.leadsheets.helpers.Strings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.greenrobot.event.EventBus;
import timber.log.Timber;

/**
 * Created by jwerner on 2/10/15.
 */
@Singleton
public class FileStore {
    final File mRootPath;
    private String mCurrentFile = "";

    @Inject
    public FileStore() {
        mRootPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/sheets/");

    }

    public File getRootPath() {
        return mRootPath;
    }

    public ArrayList<Folder> getFolders() {
        File files[] = mRootPath.listFiles();

        if (files == null) {
            mRootPath.mkdir();
            return new ArrayList<>();
        }

        Arrays.sort(files);

        final ArrayList<Folder> folders = new ArrayList<>();
        for (File f : files) {
            String fName = f.getName();
            if (f.isDirectory() && !fName.startsWith(".")) { // exclude ".git" ...
                final int size = getFilenamesForFolder(fName).size();
                folders.add(new Folder(fName, size));
            }
        }
        return folders;
    }

    public void renameSong(File song, String newName) {
        boolean b = song.renameTo(new File(song.getParentFile(), newName.trim() + ".txt"));
        Timber.d("renameSong: " + b);
    }

    public ArrayList<String> getFilenamesForFolder(String folder) {
        File fullPath = new File(mRootPath, folder);

        final ArrayList<String> filenames = new ArrayList<>();
        File files[] = fullPath.listFiles();
        if (files != null) {
            Arrays.sort(files);
            for (File f : files) {
                String fName = f.getName();
                if (fName.endsWith("txt")) {
                    filenames.add(fName.substring(0, fName.length() - 4));
                }
            }
        }
        return filenames;
    }

    public boolean removeFolder(String folderName) {
        final File folder = new File(mRootPath, folderName);

        final File archiveDir = new File(mRootPath, ".archive");
        final boolean mkdir = archiveDir.mkdir();
        final boolean worked = folder.renameTo(new File(archiveDir, folderName));
        if (!worked) Timber.d("removeFolder: didn't work..");
        emitFolderChange();
        return worked;
    }

    public void removeFile(File filePath) {
        final File archiveDir = new File(filePath.getParent(), ".archive");
        Timber.d("removeFile: " + archiveDir);
        final boolean mkdir = archiveDir.mkdir();
        Timber.d("removeFile: mkdir: " + mkdir);
        final boolean worked = filePath.renameTo(new File(archiveDir, filePath.getName()));
        if (!worked) Timber.d("removeFile: didn't work..");
        emitFolderChange();
    }

    public void restoreFile(File oldFilePath) {
        final File archiveDirFilePath = new File(new File(oldFilePath.getParent(), ".archive"), oldFilePath.getName());
        final boolean worked = archiveDirFilePath.renameTo(oldFilePath);
        Timber.d("restoreFile: " + worked);
    }

    private void emitFolderChange() {
        EventBus.getDefault().post(new ChangeFolders());
    }

    public String getCurrentFileWithoutExtension() {
        return Strings.capitalize(mCurrentFile.length() > 0 ? mCurrentFile.substring(0, mCurrentFile.length() - 4) : "");
    }

    public String getFileWithoutExtension(File filePath) {
        mCurrentFile = filePath.getName();
        return getCurrentFileWithoutExtension();
    }

    public String getContent(File filePath) {
        mCurrentFile = filePath.getName();
        String content = "";
        try {
            content = Files.toString(filePath, Charsets.UTF_8);
        } catch (IOException e) {
            Timber.v("getFNameContentTuples: " + filePath + " not found.");
        }
        return content;
    }

    public void writeContent(File filePath, String content) {

        try {
            Timber.d("writeContent: " + filePath);
            Files.write(content, filePath, Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        emitContentChange();
    }

    private void emitContentChange() {
        EventBus.getDefault().postSticky(new AllSongsChanged());
    }

    public void newFile(File filePath) {
        try {
            Files.write("", filePath, Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<String[]> getFNameContentTuples(String folder, Iterable<String> fnames) {
        ArrayList<String[]> fnameContentTuples = new ArrayList<>();

        for (String fname : fnames) {
            File file = new File(new File(mRootPath, folder), fname + ".txt");
            String content = "";
            try {
                content = Files.toString(file, Charsets.UTF_8);
            } catch (IOException e) {
                Timber.v("getFNameContentTuples: " + fname + " not found.");
            }
            fnameContentTuples.add(new String[]{fname, content});
        }
        return fnameContentTuples;
    }

    public void delete(final File file) {
        final boolean delete = file.delete();
        Timber.d("delete: " + file.toString());
        Timber.d("delete: " + delete);
    }

    public void newFolder(final String text) {
        final File folder = new File(getRootPath(), text);
        final boolean mkdir = folder.mkdir();
        if (mkdir) {
            emitFolderChange();
        }
    }

    public static class Folder {
        public final String name;
        public final int count;

        public Folder(String name, int count) {
            this.name = name;
            this.count = count;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
