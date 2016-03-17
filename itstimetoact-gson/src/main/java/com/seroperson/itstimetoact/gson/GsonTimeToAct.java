package com.seroperson.itstimetoact.gson;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.seroperson.itstimetoact.InFileTimeToAct;
import com.seroperson.itstimetoact.event.ActEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class GsonTimeToAct extends InFileTimeToAct {

    private final Gson gson = createGson(new GsonBuilder());

    public GsonTimeToAct(Context context) {
        super(context);
    }

    @Override
    protected Set<ActEvent> loadEventData(File storage) {
        JsonReader reader = null;
        try {
            try {
                reader = new JsonReader(new FileReader(storage));
                reader.beginObject();

                return gson.fromJson(reader, Class.forName(reader.nextName()));
            } catch (FileNotFoundException e) {
                // impossible
            } catch (ClassNotFoundException e) {
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        } catch (IOException e) {
        }
        return new HashSet<ActEvent>();
    }

    @Override
    protected boolean storeEventData(Collection<ActEvent> eventSet, File storage) {
        JsonWriter writer = null;
        try {
            try {
                writer = new JsonWriter(new FileWriter(storage));

                Class<?> type = eventSet.getClass();

                writer.beginObject();
                writer.name(type.getName());
                gson.toJson(eventSet, type, writer);
                writer.endObject();
                writer.flush();

                return true;
            } catch (FileNotFoundException e) {
                // impossible
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        } catch (IOException e) {
        }
        return false;
    }

    protected Gson createGson(GsonBuilder gsonBuilder) {
        return gsonBuilder.create();
    }

}
