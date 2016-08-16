package com.seroperson.itstimetoact;

import com.seroperson.itstimetoact.event.ActEvent;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Size;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * The base class for implementations that performs serialization/deserialization in/from file.
 * Like when you extending {@link TimeToAct}, you need to implement your serialization/deserialization
 * in two methods, but in this case it {@link InFileTimeToAct#loadEventData(File)} and
 * {@link InFileTimeToAct#storeEventData(Collection, File)} methods.
 */
public class InFileTimeToAct extends TimeToAct {

    private static final String STORAGE_NAME = "itstimetoact_storage";

    public InFileTimeToAct(@NonNull Context context) {
        super(context);
    }

    @Override
    @NonNull
    protected Set<ActEvent> loadEventData(@NonNull Context context) {
        File storage = getStorageFile(context);
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
    @NonNull
    protected Set<ActEvent> loadEventData(@NonNull File storage) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected boolean storeEventData(@NonNull Collection<ActEvent> eventSet, @NonNull Context context) {
        File storage = getStorageFile(context);
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
    protected boolean storeEventData(@NonNull Collection<ActEvent> eventSet, @NonNull File storage) {
        throw new UnsupportedOperationException();
    }

    /**
     * Defines name of the file which will be used to do your operations.
     *
     * @return name of the file. Must be not empty and not null.
     */
    @NonNull
    @Size(min = 1)
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
    @NonNull
    protected File getStorageFile(@NonNull Context context) {
        return new File(context.getFilesDir().getPath().concat(File.separator).concat(getFilename()));
    }

}