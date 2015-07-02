package com.rubengees.vocables.utils;

import android.content.Context;
import android.util.Xml;

import com.rubengees.vocables.R;
import com.rubengees.vocables.pojo.MeaningList;
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

    public static final String TAG_UNITS = "units";
    public static final String TAG_UNIT = "unit";
    public static final String TAG_TITLE = "title";
    public static final String TAG_VOCABLES = "vocables";
    public static final String TAG_VOCABLE = "vocable";
    public static final String TAG_FIRST_MEANING = "first_meaning";
    public static final String TAG_SECOND_MEANING = "second_meaning";
    public static final String TAG_VALUE = "value";

    public static boolean isFileSupported(File file) {
        String filename = file.getName();
        return filename.endsWith(".csv") || filename.endsWith(".xml");
    }

    public static void export(List<Unit> units, File toExport) throws IOException {
        XmlSerializer serializer = Xml.newSerializer();
        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        BufferedWriter writer = new BufferedWriter(new FileWriter(toExport));

        serializer.setOutput(writer);
        writeXml(serializer, units);
    }

    private static void writeXml(XmlSerializer serializer, List<Unit> units) throws IOException {
        serializer.startDocument("UTF-16", true);
        serializer.startTag(null, TAG_UNITS);
        for (Unit unit : units) {
            serializer.startTag(null, TAG_UNIT);
            serializer.startTag(null, TAG_TITLE);
            serializer.text(unit.getTitle());
            serializer.endTag(null, TAG_TITLE);
            serializer.startTag(null, TAG_VOCABLES);
            for (Vocable vocable : unit.getVocables()) {
                serializer.startTag(null, TAG_VOCABLE);

                serializer.startTag(null, TAG_FIRST_MEANING);
                insertMeaning(serializer, vocable.getFirstMeaningList());
                serializer.endTag(null, TAG_FIRST_MEANING);

                serializer.startTag(null, TAG_SECOND_MEANING);
                insertMeaning(serializer, vocable.getSecondMeaningList());
                serializer.endTag(null, TAG_SECOND_MEANING);
                serializer.endTag(null, TAG_VOCABLE);
            }
            serializer.endTag(null, TAG_VOCABLES);
            serializer.endTag(null, TAG_UNIT);
        }
        serializer.endTag(null, TAG_UNITS);
        serializer.endDocument();
    }

    private static void insertMeaning(XmlSerializer serializer, MeaningList meaningList) throws IOException {
        for (String word : meaningList.getMeanings()) {
            serializer.startTag(null, TAG_VALUE);
            serializer.text(word);
            serializer.endTag(null, TAG_VALUE);
        }
    }

    public static List<Unit> getList(Context context, File file) throws FormatException, IOException {
        if (file.getName().endsWith(".csv")) {
            return parseCsv(context, file);
        } else if (file.getName().endsWith(".xml")) {
            return parseXml(context, file);
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
                unitTitle = context.getString(R.string.database_uni_title_default);
            } else if (split.length == 3) {
                unitTitle = split[2];
            } else {
                throw new FormatException(String.valueOf(pos));
            }

            String[] split1 = split[0].split(";");
            String[] split2 = split[1].split(";");

            if (split1.length >= 1 && split2.length >= 1) {
                MeaningList first = new MeaningList(new ArrayList<>(Arrays.asList(split1)));
                MeaningList second = new MeaningList(new ArrayList<>(Arrays.asList(split2)));
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
                throw new FormatException(context.getString(R.string.dialog_import_error_message) + " " + String.valueOf(pos));
            }

            line = reader.readLine();
            pos++;
        }

        return new ArrayList<>(unitMap.values());
    }

    private static List<Unit> parseXml(Context context, File file) throws FormatException, IOException {
        List<Unit> result = new ArrayList<>();
        XmlPullParser parser = null;
        long creationTime = System.currentTimeMillis();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            parser = factory.newPullParser();
            parser.setInput(new BufferedReader(new FileReader(file)));

            parser.nextTag();
            parser.require(XmlPullParser.START_TAG, null, TAG_UNITS);
            while (parser.nextTag() == XmlPullParser.START_TAG) {
                Unit unit = new Unit();

                parser.require(XmlPullParser.START_TAG, null, TAG_UNIT);
                parser.nextTag();
                parser.require(XmlPullParser.START_TAG, null, TAG_TITLE);
                unit.setTitle(parser.nextText());
                parser.require(XmlPullParser.END_TAG, null, TAG_TITLE);
                parser.nextTag();
                unit.addAll(getVocablesFromXml(parser, creationTime));
                parser.require(XmlPullParser.END_TAG, null, TAG_UNIT);

                result.add(unit);
            }
            parser.require(XmlPullParser.END_TAG, null, TAG_UNITS);
        } catch (XmlPullParserException e) {
            if (parser != null) {
                throw new FormatException(context.getString(R.string.dialog_import_error_message) + " " + String.valueOf(parser.getLineNumber()));
            }
        }

        return result;
    }

    private static List<Vocable> getVocablesFromXml(XmlPullParser parser, long creationTime) throws IOException, XmlPullParserException {
        List<Vocable> result = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, null, TAG_VOCABLES);
        while (parser.nextTag() != XmlPullParser.END_TAG) {
            parser.require(XmlPullParser.START_TAG, null, TAG_VOCABLE);
            while (parser.nextTag() != XmlPullParser.END_TAG) {
                MeaningList first;
                MeaningList second;

                parser.require(XmlPullParser.START_TAG, null, TAG_FIRST_MEANING);
                first = getMeaningFromXml(parser);
                parser.require(XmlPullParser.END_TAG, null, TAG_FIRST_MEANING);
                parser.nextTag();
                parser.require(XmlPullParser.START_TAG, null, TAG_SECOND_MEANING);
                second = getMeaningFromXml(parser);
                parser.require(XmlPullParser.END_TAG, null, TAG_SECOND_MEANING);
                result.add(new Vocable(first, second, null, creationTime));
            }
        }
        parser.require(XmlPullParser.END_TAG, null, TAG_VOCABLES);
        parser.nextTag();

        return result;
    }

    private static MeaningList getMeaningFromXml(XmlPullParser parser) throws IOException, XmlPullParserException {
        List<String> words = new ArrayList<>();

        parser.nextTag();
        do {
            parser.require(XmlPullParser.START_TAG, null, TAG_VALUE);
            words.add(parser.nextText());
            parser.require(XmlPullParser.END_TAG, null, TAG_VALUE);
        } while (parser.nextTag() != XmlPullParser.END_TAG);

        return new MeaningList(words);
    }

    public static class FormatException extends Exception {
        public FormatException(String s) {
            super(s);
        }
    }

}
