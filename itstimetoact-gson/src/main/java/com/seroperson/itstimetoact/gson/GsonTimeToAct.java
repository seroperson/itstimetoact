package com.seroperson.itstimetoact.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.seroperson.itstimetoact.InFileTimeToAct;
import com.seroperson.itstimetoact.TimeToAct;
import com.seroperson.itstimetoact.event.ActEvent;

import android.content.Context;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.seroperson.itstimetoact.Check.checkIsNull;

/**
 * {@link TimeToAct} implementations that uses gson to serialize/deserialize events to/from json format.
 * Override {@link GsonTimeToAct#createGsonBuilder(GsonBuilder)} to build your custom gson instance.
 * Also you must register each event class that you use via {@link RuntimeTypeAdapterFactory}.
 */
public class GsonTimeToAct extends InFileTimeToAct {

    private final static Type defaultType = new TypeToken<Set<ActEvent>>() {

    }.getType();
    private final Gson gson;

    public GsonTimeToAct(Context context) {
        super(context);
        GsonBuilder builder = createGsonBuilder(new GsonBuilder());
        if(checkIsNull(builder)) {
            throw new IllegalStateException("GsonTimeToAct#createGsonBuilder returned null");
        }
        gson = builder.create();
    }

    @Override
    protected Set<ActEvent> loadEventData(File storage) {
        FileReader reader = null;
        try {
            try {
                reader = new FileReader(storage);
                return gson.fromJson(reader, defaultType);
            } catch(FileNotFoundException e) {
                // impossible
                e.printStackTrace();
            } finally {
                if(reader != null) {
                    reader.close();
                }
            }
        } catch(IOException e) {
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
            } catch(FileNotFoundException e) {
                // impossible
                e.printStackTrace();
            } finally {
                if(writer != null) {
                    writer.close();
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * This builder will be used later to create gson instance.
     *
     * @return your custom gson builder. Must be not null.
     */
    protected GsonBuilder createGsonBuilder(GsonBuilder gsonBuilder) {
        return gsonBuilder;
    }

}
