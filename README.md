# BellChoir Project

The BellChoir project is a Java application that allows users to load and play songs using predefined musical notes. It provides functionality for loading songs from text files, playing them using audio playback, and handling various errors in song files.

## Features

- Load songs from text files specifying musical notes and lengths.
- Play loaded songs using audio playback with configurable AudioFormat.
- Handle errors such as missing files, unsupported notes, and note lengths.
- Ensure thread safety when playing songs concurrently.

## Usage

1. Clone or download the project repository to your local machine.
2. Open the project in your preferred IDE.
3. A ```build.xml``` file has been provided to allow easy use. When you are in the cloned directory, run ``` ant run ``` to run the default song, Mary Had a Little Lamb. To run you own song, include the argument ```-Dsong=<songfile.txt>``` so the whole command looks like ```ant run -Dsong=<songfile.txt>```

4. Example files of the songfile.txt can be found in the project-- use these to replicate for your own song!

## Documentation

- Javadoc comments are provided throughout the source code to explain classes, methods, and fields.
- Run the `javadoc` tool to generate HTML documentation from the source files. For example:
