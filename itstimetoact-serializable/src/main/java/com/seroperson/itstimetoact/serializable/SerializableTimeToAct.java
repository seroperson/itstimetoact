package com.seroperson.itstimetoact.serializable;

import com.seroperson.itstimetoact.InFileTimeToAct;
import com.seroperson.itstimetoact.event.ActEvent;

import android.content.Context;

import java.io.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SerializableTimeToAct extends InFileTimeToAct {

    public SerializableTimeToAct(Context context) {
        super(context);
    }

    @Override
    protected Set<ActEvent> loadEventData(File storage) {
        Set<ActEvent> result = new HashSet<ActEvent>();
        ObjectInputStream objectInputStream = null;
        try {
            try {
                objectInputStream = new ObjectInputStream(new FileInputStream(storage));
                result = (Set<ActEvent>) objectInputStream.readObject();
            } catch (FileNotFoundException e) {
                // impossible
            } catch (ClassNotFoundException e) {
            } finally {
                if (objectInputStream != null) {
                    objectInputStream.close();
                }
            }
        } catch (IOException e) {
        }
        return result;
    }

    @Override
    protected boolean storeEventData(Collection<ActEvent> eventSet, File storage) {
        ObjectOutputStream objectOutputStream = null;
        try {
            try {
                objectOutputStream = new ObjectOutputStream(new FileOutputStream(storage));
                objectOutputStream.writeObject(eventSet);
                objectOutputStream.flush();
                return true;
            } finally {
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
            }
        } catch (IOException e) {
        }
        return false;
    }

}
