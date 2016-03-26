package com.seroperson.itstimetoact;

import com.seroperson.itstimetoact.event.ActEvent;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.seroperson.itstimetoact.Check.checkIsEmpty;
import static com.seroperson.itstimetoact.Check.checkIsNull;

/**
 * The base class for implementations that performs serialization/deserialization in/from file.
 * Like when you extending {@link TimeToAct}, you need to implement your serialization/deserialization
 * in two methods, but in this case it {@link InFileTimeToAct#loadEventData(File)} and
 * {@link InFileTimeToAct#storeEventData(Collection, File)} methods.
 */
public class InFileTimeToAct extends TimeToAct {

    private static final String STORAGE_NAME = "itstimetoact_storage";

    public InFileTimeToAct(Context context) {
        super(context);
    }

    @Override
    protected Set<ActEvent> loadEventData(Context context) {
        File storage = getNotNullStorageFile(context);
        if(!storage.exists()/* || isEmpty(storage)*/) {
            return new HashSet<ActEvent>();
        }
        return loadEventData(storage);
    }

    /**
     * Will be called when there is need for data deserialization.
     *
     * @param storage file with serialized objects to load
     *
     * @return the set of deserialized events.
     */
    protected Set<ActEvent> loadEventData(File storage) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected boolean storeEventData(Collection<ActEvent> eventSet, Context context) {
        File storage = getNotNullStorageFile(context);
        if(!storage.exists()) {
            try {
                storage.createNewFile();
            } catch(IOException e) {
                return false;
            }
        }
        return storeEventData(eventSet, storage);
    }

    /**
     * Will be called when there is need for data serialization.
     *
     * @param eventSet set of events for serialization.
     * @param storage  file in which you need to store.
     *
     * @return {@code true} if serialization finished successfully, {@code false} otherwise.
     */
    protected boolean storeEventData(Collection<ActEvent> eventSet, File storage) {
        throw new UnsupportedOperationException();
    }

    /**
     * Defines name of the file which will be used to do your operations.
     *
     * @return name of the file. Must be not empty and not null.
     */
    protected String getFilename() {
        return STORAGE_NAME;
    }

    /**
     * Defines the file which will be used to do your operations.
     *
     * @param context context to find your file.
     *
     * @return file for performing your operations. Must be not null.
     */
    protected File getStorageFile(Context context) {
        String storageName = getFilename();
        if(checkIsEmpty(storageName)) { // TODO more checks?
            throw new IllegalStateException("InFileTimeToAct#getFilename() returned empty or null string");
        }
        return new File(context.getFilesDir().getPath().concat(File.separator).concat(storageName));
    }

    private File getNotNullStorageFile(Context context) {
        File storage = getStorageFile(context);
        if(checkIsNull(storage)) {
            throw new IllegalStateException("InFileTimeToAct#getStorageFile(Context) returned null");
        }
        return storage;
    }

}
