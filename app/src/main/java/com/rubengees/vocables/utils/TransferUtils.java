package com.rubengees.vocables.utils;

import android.content.Context;
import android.util.Xml;

import com.rubengees.vocables.pojo.Meaning;
import com.rubengees.vocables.pojo.Unit;
import com.rubengees.vocables.pojo.Vocable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ruben Gees on 11.02.2015.
 */
public class TransferUtils {

    public static boolean isFileSupported(File file) {
        String filename = file.getName();
        return filename.endsWith(".csv") || filename.endsWith(".xml");
    }

    public static void export(List<Unit> units, File directory) throws IOException {
        XmlSerializer serializer = Xml.newSerializer();
        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(directory, "backup.xml")));

        serializer.setOutput(writer);
        writeXml(serializer, units);
    }

    private static void writeXml(XmlSerializer serializer, List<Unit> units) throws IOException {
        serializer.startDocument("UTF-16", true);
        serializer.startTag(null, "units");
        for (Unit unit : units) {
            serializer.startTag(null, "unit");
            serializer.startTag(null, "title");
            serializer.text(unit.getTitle());
            serializer.endTag(null, "title");
            serializer.startTag(null, "vocables");
            for (Vocable vocable : unit.getVocables()) {
                serializer.startTag(null, "vocable");

                serializer.startTag(null, "first_meaning");
                insertMeaning(serializer, vocable.getFirstMeaning());
                serializer.endTag(null, "first_meaning");

                serializer.startTag(null, "second_meaning");
                insertMeaning(serializer, vocable.getSecondMeaning());
                serializer.endTag(null, "second_meaning");
                serializer.endTag(null, "vocable");
            }
            serializer.endTag(null, "vocables");
            serializer.endTag(null, "unit");
        }
        serializer.endTag(null, "units");
        serializer.endDocument();
    }

    private static void insertMeaning(XmlSerializer serializer, Meaning meaning) throws IOException {
        for (String word : meaning.getMeanings()) {
            serializer.startTag(null, "value");
            serializer.text(word);
            serializer.endTag(null, "value");
        }
    }

    public static List<Unit> getList(Context context, File file) throws FormatException, IOException {
        if (file.getName().endsWith(".csv")) {
            return parseCsv(context, file);
        } else if (file.getName().endsWith(".xml")) {
            return parseXml(file);
        } else {
            return null;
        }
    }

    private static List<Unit> parseCsv(Context context, File file) throws FormatException, IOException {
        HashMap<String, Unit> unitMap = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        long creationTime = System.currentTimeMillis();
        int pos = 0;
        String line = reader.readLine();
        String unitTitle;

        while (line != null) {
            String[] split = line.split(",");

            if (split.length == 2) {
                unitTitle = "Default";
            } else if (split.length == 3) {
                unitTitle = split[2];
            } else {
                throw new FormatException(String.valueOf(pos));
            }

            String[] split1 = split[0].split(";");
            String[] split2 = split[1].split(";");

            if (split1.length >= 1 && split2.length >= 1) {
                Meaning first = new Meaning(new ArrayList<>(Arrays.asList(split1)));
                Meaning second = new Meaning(new ArrayList<>(Arrays.asList(split2)));
                Vocable vocable = new Vocable(first, second, null, creationTime);

                Unit unit;

                if (unitMap.containsKey(unitTitle)) {
                    unit = unitMap.get(unitTitle);
                } else {
                    unit = new Unit();

                    unit.setTitle(unitTitle);
                    unitMap.put(unitTitle, unit);
                }

                unit.add(vocable);
            } else {
                throw new FormatException(String.valueOf(pos));
            }

            line = reader.readLine();
            pos++;
        }

        return new ArrayList<>(unitMap.values());
    }

    private static List<Unit> parseXml(File file) throws FormatException, IOException {
        List<Unit> result = new ArrayList<>();
        XmlPullParser parser = null;
        long creationTime = System.currentTimeMillis();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            parser = factory.newPullParser();
            parser.setInput(new BufferedReader(new FileReader(file)));

            parser.nextTag();
            parser.require(XmlPullParser.START_TAG, null, "units");
            while (parser.nextTag() == XmlPullParser.START_TAG) {
                Unit unit = new Unit();

                parser.require(XmlPullParser.START_TAG, null, "unit");
                parser.nextTag();
                parser.require(XmlPullParser.START_TAG, null, "title");
                unit.setTitle(parser.nextText());
                parser.require(XmlPullParser.END_TAG, null, "title");
                parser.nextTag();
                unit.addAll(getVocablesFromXml(parser, creationTime));
                parser.require(XmlPullParser.END_TAG, null, "unit");

                result.add(unit);
            }
            parser.require(XmlPullParser.END_TAG, null, "units");
        } catch (XmlPullParserException e) {
            if (parser != null) {
                throw new FormatException(String.valueOf(parser.getLineNumber()));
            }
        }

        return result;
    }

    private static List<Vocable> getVocablesFromXml(XmlPullParser parser, long creationTime) throws IOException, XmlPullParserException {
        List<Vocable> result = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, null, "vocables");
        while (parser.nextTag() != XmlPullParser.END_TAG) {
            parser.require(XmlPullParser.START_TAG, null, "vocable");
            while (parser.nextTag() != XmlPullParser.END_TAG) {
                Meaning first;
                Meaning second;

                parser.require(XmlPullParser.START_TAG, null, "first_meaning");
                first = getMeaningFromXml(parser);
                parser.require(XmlPullParser.END_TAG, null, "first_meaning");
                parser.nextTag();
                parser.require(XmlPullParser.START_TAG, null, "second_meaning");
                second = getMeaningFromXml(parser);
                parser.require(XmlPullParser.END_TAG, null, "second_meaning");
                result.add(new Vocable(first, second, null, creationTime));
            }
        }
        parser.require(XmlPullParser.END_TAG, null, "vocables");
        parser.nextTag();

        return result;
    }

    private static Meaning getMeaningFromXml(XmlPullParser parser) throws IOException, XmlPullParserException {
        List<String> words = new ArrayList<>();

        parser.nextTag();
        do {
            parser.require(XmlPullParser.START_TAG, null, "value");
            words.add(parser.nextText());
            parser.require(XmlPullParser.END_TAG, null, "value");
        } while (parser.nextTag() != XmlPullParser.END_TAG);

        return new Meaning(words);
    }

    public static class FormatException extends Exception {
        public FormatException(String s) {
            super(s);
        }
    }

}
