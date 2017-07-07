package com.axellience.vuegwt;

import jodd.json.JsonParser;
import jodd.json.JsonSerializer;

/**
 * @author Adrien Baron
 */
public class SimpleTest
{
    public static void main(String[] argv)
    {
        JsonParser jsonParser = new JsonParser();
        jsonParser.looseMode(true);
        Object map = jsonParser.parse("HelloWorld");

        JsonSerializer serializer = new JsonSerializer();
        serializer.deep(true);
        System.out.println(serializer.serialize(map));
    }
}
