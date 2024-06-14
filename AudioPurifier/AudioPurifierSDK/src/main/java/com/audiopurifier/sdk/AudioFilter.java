package com.audiopurifier.sdk;

import android.app.Activity;
import android.util.Log;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class AudioFilter {
    private static final List<String> VALID_EXTENSIONS = Arrays.asList(
            "mp3", "wav", "aac", "flac", "ogg", "m4a", "wma", "aiff", "aif",
            "ac3", "opus", "amr", "pcm", "dts"
    );

    public static void applyFilter(Activity activity, String inputFile, int highPass, int lowPass, String fileName, CallBackMethods callBackMethods) {
        if (highPass <= 0) {
            throw new IllegalArgumentException("highPass value must be greater than 0");
        }
        if (lowPass <= 0) {
            throw new IllegalArgumentException("lowPass value must be greater than 0");
        }

        if (!hasValidExtension(fileName)) {
            throw new IllegalArgumentException("The file does not have a valid extension.");
        }

        File outputFile = new File(activity.getExternalFilesDir(null), fileName.trim());

        if (outputFile.exists()) {
            outputFile.delete();
        }

        String outputFilePath = outputFile.getAbsolutePath();
        String highPassStr = String.valueOf(highPass);
        String lowPassStr = String.valueOf(lowPass);
        String cmd = "-i \"" + inputFile + "\" -af \"highpass=f=" + highPassStr + ", lowpass=f=" + lowPassStr + "\" \"" + outputFilePath + "\"";

        Config.enableLogCallback(message -> Log.i(Config.TAG, message.getText()));
        FFmpeg.executeAsync(cmd, (executionId, returnCode) -> {
            if (returnCode == Config.RETURN_CODE_SUCCESS) {
                Log.i(Config.TAG, "Command execution completed successfully.");
                callBackMethods.success(outputFilePath);
            } else {
                Log.i(Config.TAG, String.format("Command execution failed with returnCode=%d.", returnCode));
                callBackMethods.failed(String.valueOf(returnCode));
            }
        });
    }

    private static boolean hasExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex > 0 && dotIndex < fileName.length() - 1;
    }

    private static boolean hasValidExtension(String fileName) {
        if (hasExtension(fileName)) {
            String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
            return VALID_EXTENSIONS.contains(extension);
        }
        return false;
    }
}