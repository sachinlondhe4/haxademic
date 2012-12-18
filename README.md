# Haxademic
Haxademic is a multimedia platform, built in Java and [Processing](http://processing.org/). It's a starting point for interactive visuals, giving you a unified environment for both realtime and rendering modes. It loads several Java libraries and wraps them up to play nicely with each other. It solves a number of problems faced by (potentially) thread-unsafe hardware inputs like audio, Kinect, MIDI and OSC. To view some projects created with the library, check out the [Haxademic Tumblr](http://haxademic.com/).

## Alpha State
While the code has been open-sourced, I haven't had time to write much (any) documentation, but I'm trying to get it there. You can see the example apps and sketches to get an idea of how to use various features, and the big hangup right now is that you'll have to find and install the 3rd-party libraries on your own. Even without fully installing everything, there's plenty of interesting code within, and I fully endorse borrowing it however you see fit in the meantime. If you're interested in collaborating, please contact me via my [GitHub account](http://github.com/cacheflowe), or my [web site](http://cacheflowe.com/?page=contact). If you'd like help getting the project running, let me know and I can personally assist for now.

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
	* Kinect input, with skeleton data via OpenNI
* 3D tools (using Toxiclibs WETriangleMesh objects as the common format)
	* Convert SVG files to 2D meshes
	* Simple 3D extrusion of 2D meshes
	* Load and convert .obj, .gif & .svg files to WETriangleMesh
	* Mesh pool object to load and hold instance of any number of meshes
	* Base Camera type, with simple camera subclasses (needs work) 
	* Draw a mesh with incoming Audio data spread across the faces
	* Shatter a box or sphere with randomized Voronoi3D calculations (soon to work on any mesh)
	* Some basic shape-drawing code
	* Mesh smoothing
	* Mesh deform from audio input
	* Apply off-screen audio-reactive textures to a 3D mesh
* Text tools
	* Create a 2D or extruded 3D mesh from text with a custom .ttf font
	* Draw 2D text with a custom .ttf font
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
	* Automatic system screensaver disabling while running
	* True full-screen mode on OS X
	* Toggles the window chrome on a non-fullscreen applet
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

* [Eclipse](http://www.eclipse.org/)

Java & Processing libraries:

* [Processing](http://processing.org/) (view the [Processing for Eclipse instructions](http://processing.org/learning/eclipse/))
* [Krister.ESS](http://www.tree-axis.com/Ess/)
* [simple-openni](http://code.google.com/p/simple-openni/)
* [toxiclibs](http://toxiclibs.org/)
* [p5sunflow](https://github.com/hryk/p5sunflow) original site is down :-/
* [OBJLoader](http://code.google.com/p/saitoobjloader/)
* [themidibus](https://github.com/sparks/themidibus)
* [oscP5](http://www.sojamo.de/libraries/oscP5/)
* [fullscreen](http://www.superduper.org/processing/fullscreen_api/)
* [launchpad](http://rngtng.github.com/launchpad/)
* [He_Mesh](http://hemesh.wblut.com/)
* [minim](http://code.compartmental.net/tools/minim/)
* [Geomerative](http://www.ricardmarxer.com/geomerative/)

Use the following VM Arguments when running the Java Application

* -d32 (when rendering to Quicktime movie or using a web cam)
* -Xmx1024M
* -Xms1024M

General Use / Tips

* If you want to wipe your bin/ directory, you'll have to do a Project -> Clean... afterwards.


## Licensing

The Haxademic codebase and apps are [MIT licensed](https://raw.github.com/cacheflowe/haxademic/master/LICENSE), so do what you want with these files. Feel free to let me know that you're using it for something cool.