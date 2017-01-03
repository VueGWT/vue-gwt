package com.axellience.vuegwt.client.jsnative;

import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

import java.util.Iterator;

/**
 * Source: https://github.com/ltearno/angular2-gwt/
 */
@JsType( isNative = true, namespace = JsPackage.GLOBAL, name = "Array" )
public class JsArray<T>
{
    @JsOverlay
    @SafeVarargs
    public static <T> JsArray<T> of( T... array )
    {
        JsArray<T> result = new JsArray<>();
        for( T item : array )
            result.push( item );
        return result;
    }

    @JsOverlay
    public static <T> JsArray<T> empty()
    {
        return new JsArray<>();
    }

    @JsMethod
    public native void push( T item );

    @JsMethod
    public native T pop();

    @JsMethod
    public native T shift();

    @JsMethod
    public native void unshift( T item );

    @JsProperty
    public native int getLength();

    @JsProperty
    public native void setLength(int length);

    @JsMethod
    public native JsArray<T> slice( int start, int end );

    @JsMethod
    public native String join();

    @JsMethod
    public native String join( String joiner );

    @JsMethod
    public native int indexOf( T item );

    @JsMethod
    public native void splice( int start, int length );

    @JsMethod
    public native JsArray<T> slice( int start );

    @JsMethod
    public native T find( JsPredicate<T> predicate );

    @JsMethod
    public native JsArray<T> filter( JsPredicate<T> predicate );

    @JsOverlay
    public final T get( int index )
    {
        return JsTools.get( this, index );
    }

    @JsOverlay
    public final void set( int index, T value )
    {
        JsTools.set( this, index, value );
    }

    @JsOverlay
    public final Iterable<T> iterate()
    {
        return () -> new Iterator<T>()
        {
            int index = 0;

            @Override
            public boolean hasNext()
            {
                return index < getLength();
            }

            @Override
            public T next()
            {
                T result = get( index );
                index++;
                return result;
            }

            @Override
            public void remove()
            {
                throw new RuntimeException();
            }
        };
    }
}
