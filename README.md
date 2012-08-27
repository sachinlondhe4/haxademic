# Haxademic
Haxademic is a multimedia platform, built in Java and [Processing](http://processing.org/). It's a starting point for interactive visuals, giving you a unified environment for both realtime and rendering modes. It loads several Java libraries and wraps them up to play nicely with each other, all within the context of Haxademic. It solves a number of problems faced by thread-unsafe inputs, and a number of hardware inputs like audio, Kinect, MIDI and OSC.

## Pre-Alpha State
While the code has been open-sourced, this library is not quite ready for general use, but I'm trying to get it there. There's plenty of interesting code within, and I fully endorse borrowing it however you see fit in the meantime. If you're interested in collaborating, please contact me via my [GitHub account](http://github.com/cacheflowe), or my [web site](http://cacheflowe.com/?page=contact). If you're interested in getting the project running, let me know and I can help for now.

## Features / Capabilities
* Inputs
	* Audio input and analysis (both realtime and step-through for rendering)
		* FFT analysis
		* Waveform/oscilloscope data
		* Audio beat detection
	* Step-through rendering with multiple audio files concurrently
	* MIDI input (both realtime and step-through for rendering)
		* Cached MIDI input to avoid thread-unsafe operations
	* OSC input
	* Kinect input
* 3D Tools (using Toxiclibs WETriangleMesh objects as the common format)
	* Convert SVG files to 2D meshes
	* Create a 2D mesh from text with a custom .ttf font
	* Simple extrusion of 2D meshes
	* Load and convert .obj files to WETriangleMesh
	* Mesh pool object to load and hold instance of any number of meshes
	* Base Camera type, with simple camera subclasses (needs work) 
	* Draw a mesh with incoming Audio data spread across the faces
	* Shatter a box or sphere with randomized Voronoi3D calculations (soon to work on any mesh)
	* Some basic shape-drawing code
	* Mesh smoothing
	* Mesh deform from audio input
	* Apply off-screen audio-reactive textures to a 3D mesh
* Image processing
	* PImage reversal
	* Multiple screenshot methods
* Utility objects
	* Math utilities
	* Easing floats (with 3D containers)
	* Elastic floats (with 3D containers)
	* Utilities to (re)set Applet properties like lighting, current matrix, drawing colors
	* OpenGL utility to set GL-specific properties
	* Debug utilities to report current actual frame rate, memory usage 
	* Timestamp generators
	* Eased color interpolation
* Output
	* Render to Quicktime or image sequence with minimal effort
	* High-quality rendering with the Sunflow renderer, for beautiful globally-illuminated, antialiased scenes 
	* Audio playback with cached audio clip pool
* General Environment
	* .properties file loader with overridable defaults
	* Directory searching for specific filetypes
	* Automatic screensaver disabling while running
	* True full-screen mode on OS X
	* Hides the window chrome on a non-fullscreen applet
* Apps
	* HaxVisual - A modular VJ system
	* KacheOut - A 2-player Kinect-based video game 
	* TimeLapse - Renders a .mov video from an image sequence in a directory

## Todo
* Comment the codebase and generate docs
* Create a unified keyboard/MIDI/OSC input system with improved midi/osc data handling
* Make the Eclipse project easily installable for anyone else
* Support Windows - mostly will require a different Kinect library & abstraction
* Clean up legacy code that's no longer used
* Lots more noted in the PAppletHax class comments

## Requirements / Compiling
IDE:

* Eclipse

Java & Processing libraries:

* Processing
* Krister.ESS
* OpenNI
* Toxiclibs
* p5sunflow
* OBJLoader
* themidibus
* oscP5
* fullscreen
* launchpad
* He_Mesh
* minim
* Geomerative

Use the following VM Arguments when running the Java Application

* -Xmx1024M
* -Xms1024M

General Use / Tips

* If you want to wipe your bin/ directory, you'll have to do a Project -> Clean... afterwards.
