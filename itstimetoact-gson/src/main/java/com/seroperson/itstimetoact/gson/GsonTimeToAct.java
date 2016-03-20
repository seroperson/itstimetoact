package com.seroperson.itstimetoact.gson;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import com.seroperson.itstimetoact.InFileTimeToAct;
import com.seroperson.itstimetoact.event.ActEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class GsonTimeToAct extends InFileTimeToAct {

    private final static Type defaultType = new TypeToken<Set<ActEvent>>() { }.getType();
    private final Gson gson = createGsonBuilder(new GsonBuilder()).create();

    public GsonTimeToAct(Context context) {
        super(context);
    }

    @Override
    protected Set<ActEvent> loadEventData(File storage) {
        FileReader reader = null;
        try {
            try {
                reader = new FileReader(storage);
                return gson.fromJson(reader, defaultType);
            } catch (FileNotFoundException e) {
                // impossible
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashSet<ActEvent>();
    }

    @Override
    protected boolean storeEventData(Collection<ActEvent> eventSet, File storage) {
        JsonWriter writer = null;
        try {
            try {
                writer = new JsonWriter(new FileWriter(storage));
                gson.toJson(eventSet, defaultType, writer);
                writer.flush();
                return true;
            } catch (FileNotFoundException e) {
                // impossible
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected GsonBuilder createGsonBuilder(GsonBuilder gsonBuilder) {
        return gsonBuilder;
    }

}
