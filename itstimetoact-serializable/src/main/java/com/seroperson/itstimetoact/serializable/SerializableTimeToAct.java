package com.seroperson.itstimetoact.serializable;

import com.seroperson.itstimetoact.InFileTimeToAct;
import com.seroperson.itstimetoact.TimeToAct;
import com.seroperson.itstimetoact.event.ActEvent;

import android.content.Context;

import java.io.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * {@link TimeToAct} implementation that uses java.io serializables to serialize and deserialize events.
 * Be careful, because there is some limitations when you use this class and it is preferable to use gson
 * implementation (especially if you already use gson in your project or if you are using your own implementations of
 * {@link ActEvent}). The main pros of this implementation that it hasn't any external dependencies.
 * <a href="https://docs.oracle.com/javase/6/docs/platform/serialization/spec/serial-arch.html">Read more</a>.
 */
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
            } catch(FileNotFoundException e) {
                // impossible
                e.printStackTrace();
            } catch(ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                if(objectInputStream != null) {
                    objectInputStream.close();
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
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
                if(objectOutputStream != null) {
                    objectOutputStream.close();
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
