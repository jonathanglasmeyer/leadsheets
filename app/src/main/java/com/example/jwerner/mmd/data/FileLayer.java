package com.example.jwerner.mmd.data;

import android.os.Environment;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

/**
 * Created by jwerner on 2/10/15.
 */
@Singleton public class FileLayer {
    private final File mRootPath;

    @Inject public FileLayer() {
        mRootPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/sheets/");

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

    public ArrayList<Folder> getFolders() {
        File files[] = mRootPath.listFiles();
        final ArrayList<Folder> folders = new ArrayList<>();
        if (files != null) {
            for (File f : files) {
                String fName = f.getName();
                if (f.isDirectory() && !fName.startsWith(".")) { // exclude ".git" ...
                    final int size = getFilenamesForFolder(fName).size();
                    if (size > 0) folders.add(new Folder(fName, size));
                }
            }
        }
        return folders;
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

    public static class Folder {
        public String name;
        public int count;

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
