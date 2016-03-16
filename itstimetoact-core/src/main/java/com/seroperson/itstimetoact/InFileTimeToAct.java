package com.seroperson.itstimetoact;

import android.content.Context;

import com.seroperson.itstimetoact.event.ActEvent;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class InFileTimeToAct extends TimeToAct {

    private static final String STORAGE_NAME = "itstimetoact_storage";

    public InFileTimeToAct(Context context) {
        super(context);
    }

    @Override
    protected Set<ActEvent> loadEventData(Context context) {
        File storage = getStorageFile(context);
        if(!storage.exists()/* || isEmpty(storage)*/) {
            return new HashSet<ActEvent>();
        }
        return loadEventData(storage);
    }

    protected Set<ActEvent> loadEventData(File storage) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected boolean storeEventData(Collection<ActEvent> eventSet, Context context) {
        File storage = getStorageFile(context);
        if(!storage.exists()) {
            try {
                storage.createNewFile();
            } catch (IOException e) {
                return false;
            }
        }
        return storeEventData(eventSet, storage);
    }

    protected boolean storeEventData(Collection<ActEvent> eventSet, File storage) {
        throw new UnsupportedOperationException();
    }

    protected String getFilename() {
        return STORAGE_NAME;
    }

    protected File getStorageFile(Context context) {
        return new File(context.getFilesDir().getPath().concat(File.separator).concat(getFilename()));
    }

}
