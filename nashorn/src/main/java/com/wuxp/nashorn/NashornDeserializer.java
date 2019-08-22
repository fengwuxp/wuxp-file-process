package com.wuxp.nashorn;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

/**
 * nashorn engine javascript object to java object
 */
public interface NashornDeserializer<T> {


    /**
     * deserialize javascript object
     *
     * @param scriptObjectMirror
     * @return
     */
    T deserialize(ScriptObjectMirror scriptObjectMirror);

}
