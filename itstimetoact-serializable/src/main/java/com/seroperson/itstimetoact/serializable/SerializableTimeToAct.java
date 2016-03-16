package com.seroperson.itstimetoact.serializable;

import com.seroperson.itstimetoact.TimeToAct;
import com.seroperson.itstimetoact.event.ActEvent;

import android.content.Context;

import java.io.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SerializableTimeToAct extends TimeToAct {

    private static final String STORAGE_NAME = "itstimetoact_storage";

    public SerializableTimeToAct(Context context) {
        super(context);
    }

    @Override
    protected Set<ActEvent> loadEventData(Context context) {
        File storage = getStorageFile(context);
        Set<ActEvent> result = new HashSet<ActEvent>();
        if(!storage.exists()) {
            return result;
        }
        ObjectInputStream objectInputStream = null;
        try {
            try {
                objectInputStream = new ObjectInputStream(new FileInputStream(storage));
                result = (Set<ActEvent>) objectInputStream.readObject();
            } finally {
                if(objectInputStream != null) {
                    objectInputStream.close();
                }
            }
        } catch(FileNotFoundException e) {
            // impossible
        } catch(ClassNotFoundException e) {
        } catch(IOException e) {
        }
        return result;
    }

    @Override
    protected boolean storeEventData(Collection<ActEvent> eventSet, Context context) {
        File storage = getStorageFile(context);
        ObjectOutputStream objectOutputStream = null;
        try {
            if(!storage.exists()) {
                storage.createNewFile();
            }
            try {
                objectOutputStream = new ObjectOutputStream(new FileOutputStream(storage));
                objectOutputStream.writeObject(eventSet);
                objectOutputStream.flush();
                return true;
            } finally {
                if(objectOutputStream != null) {
                    objectOutputStream.close();
                }
            }
        } catch(IOException e) {
        }
        return false;
    }

    private File getStorageFile(Context context) {
        return new File(context.getFilesDir().getPath().concat(File.separator).concat(STORAGE_NAME));
    }

}
