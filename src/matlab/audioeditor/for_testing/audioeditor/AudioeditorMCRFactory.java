/*
 * MATLAB Compiler: 6.0 (R2015a)
 * Date: Mon Sep 04 14:39:26 2017
 * Arguments: "-B" "macro_default" "-N" "-W" "java:audioeditor,Class1" "-T" "link:lib" 
 * "-d" "C:\\Users\\Livia\\Desktop\\audioeditor\\src\\matlab\\audioeditor\\for_testing" 
 * "class{Class1:C:\\Users\\Livia\\Desktop\\compiler\\audioplayer\\changeSpeed.m,C:\\Users\\Livia\\Desktop\\compiler\\audioplayer\\changeVolume.m,C:\\Users\\Livia\\Desktop\\compiler\\audioplayer\\cutSong.m,C:\\Users\\Livia\\Desktop\\compiler\\audioplayer\\deleteSong.m,C:\\Users\\Livia\\Desktop\\compiler\\audioplayer\\getSongHandle.m,C:\\Users\\Livia\\Desktop\\compiler\\audioplayer\\pauseSong.m,C:\\Users\\Livia\\Desktop\\compiler\\audioplayer\\playSong.m,C:\\Users\\Livia\\Desktop\\compiler\\audioplayer\\relocateSong.m,C:\\Users\\Livia\\Desktop\\compiler\\audioplayer\\resumeSong.m,C:\\Users\\Livia\\Desktop\\compiler\\audioplayer\\stopSong.m}" 
 */

package audioeditor;

import com.mathworks.toolbox.javabuilder.*;
import com.mathworks.toolbox.javabuilder.internal.*;

/**
 * <i>INTERNAL USE ONLY</i>
 */
public class AudioeditorMCRFactory
{
   
    
    /** Component's uuid */
    private static final String sComponentId = "audioeditor_1F1CFD572AA9C0E440C7BE0D827382F5";
    
    /** Component name */
    private static final String sComponentName = "audioeditor";
    
   
    /** Pointer to default component options */
    private static final MWComponentOptions sDefaultComponentOptions = 
        new MWComponentOptions(
            MWCtfExtractLocation.EXTRACT_TO_CACHE, 
            new MWCtfClassLoaderSource(AudioeditorMCRFactory.class)
        );
    
    
    private AudioeditorMCRFactory()
    {
        // Never called.
    }
    
    public static MWMCR newInstance(MWComponentOptions componentOptions) throws MWException
    {
        if (null == componentOptions.getCtfSource()) {
            componentOptions = new MWComponentOptions(componentOptions);
            componentOptions.setCtfSource(sDefaultComponentOptions.getCtfSource());
        }
        return MWMCR.newInstance(
            componentOptions, 
            AudioeditorMCRFactory.class, 
            sComponentName, 
            sComponentId,
            new int[]{8,5,0}
        );
    }
    
    public static MWMCR newInstance() throws MWException
    {
        return newInstance(sDefaultComponentOptions);
    }
}
