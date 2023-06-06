package com.app.lsquared.utils;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class SVGColorChanger {

    private static final String SVG_PATH_ELEMENT = "path";
    private static final String SVG_FILL_ATTRIBUTE = "fill";
    private static final String SVG_NAMESPACE = null; // SVG namespace is not required

/*
    public static void changeSVGColor(Context context, String svgFileName, int newColor, OutputStream outputStream) throws IOException, XmlPullParserException {

        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open(svgFileName);

        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inputStream, null);

        XmlSerializer serializer = Xml.newSerializer();
        Writer writer = new OutputStreamWriter(outputStream);
        serializer.setOutput(writer);

        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals(SVG_PATH_ELEMENT)) {
                String fillColor = parser.getAttributeValue(SVG_NAMESPACE, SVG_FILL_ATTRIBUTE);
                if (fillColor != null) {
                    String modifiedFillColor = "#" + Integer.toHexString(newColor & 0x00FFFFFF);
                    // Set the modified color value
                    serializer.startTag(SVG_NAMESPACE, SVG_PATH_ELEMENT);
                    serializer.attribute(SVG_NAMESPACE, SVG_FILL_ATTRIBUTE, modifiedFillColor);
                    serializer.endTag(SVG_NAMESPACE, SVG_PATH_ELEMENT);
                } else {
                    // If there is no fill attribute, simply copy the original path
                    serializer.copyCurrentEventFrom(parser);
                }
            } else {
                // Copy other events as they are
                serializer.copyCurrentEventFrom(parser);
            }
        }
        serializer.flush();
        outputStream.close();
        inputStream.close();
    }
*/
 }




