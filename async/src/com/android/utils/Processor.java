package com.android.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

/**
 * Created by vicboma on 15/10/14.
 */
public class Processor {

    public static final String SYS_DEVICES_SYSTEM_CPU = "/sys/devices/system/cpu/";

    /**
     * Gets the number of cores available in this device, across all processors.
     * Requires: Ability to peruse the filesystem at "/sys/devices/system/cpu"
     *
     * @return The number of cores, or 1 if failed to get result
     */
    public static int deviceCore() {
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                if (Pattern.matches("cpu[0-9]+", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        File[] files = null;
        int result = 0;
        try {
            File dir = new File(SYS_DEVICES_SYSTEM_CPU);
            files = dir.listFiles(new CpuFilter());
            result = files.length;
        } catch (Exception e) {
            result = 1;
        } finally {
            return result;
        }
    }
}
