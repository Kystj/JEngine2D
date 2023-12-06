/*
 Title: Audio
 Date: 2023-12-06
 Author: Kyle St John
 */
package engine.audio;

/** The Audio class is a LWJGL-based wrapper for managing audio playback in a game. It encapsulates
 *  loading, playing, pausing, stopping, and resource cleanup for audio files, utilizing OpenAL
 *  for sound rendering. */
public class Audio {

    /** Constructor that calls the init function and preforms full initialization uponcreation */
    public Audio(String filePath) {

    }

    /** Load audio file, create and attach buffers and preform the correct initialization of the Audio for
     * use in game. Returns the OpenAL buffer ID */
    private int init(String filePath) {
        return -1;
    }

    /** Play the audio */
    public void play() {

    }

    /** Pause the audio */
    public void pause() {

    }

    /** Stop the audio */
    public void stop() {

    }

    /** Preform cleanup as required */
    public void cleanup() {

    }
}