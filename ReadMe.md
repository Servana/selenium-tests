# Selenium tests on Ubuntu Server with Video recording

This Jenkins pipeline is designed to execute Selenium UI tests using Maven, JDK 11, and ChromeDriver on an Ubuntu Server environment. The pipeline leverages Xvfb (X Virtual Framebuffer) to create a virtual display for running tests and records the test execution as a video using FFmpeg. The primary goal is to provide a visual representation of the test run.

## Pipeline Overview

### 1. **Environment Variables**
   - `DISPLAY`: Specifies the X server display number (":99") for Xvfb.
   - `VIDEO_FILE_NAME`: Specifies the name of the video file generated during the test execution (default is 'test.mp4').

### 2. **Tools Configuration**
   - Maven version 3.8.3 is used (`maven 'mvn3.8.3'`).
   - JDK version 11 is used (`jdk 'jdk11'`).

## Custom Functions

### 1. **usexvfb()**
   - Installs FFmpeg and sets up Xvfb to create a virtual display for headless test execution.
   - Starts recording the virtual display using FFmpeg.

### 2. **downloadChromeDriver()**
   - Downloads the ChromeDriver package from a specific URL and extracts it into Jenkins workspace.

### 3. **cleanUp()**
   - Kills the Xvfb and FFmpeg processes to clean up resources post execution of tests.

## Xvfb (X Virtual Framebuffer)

[Xvfb](https://www.x.org/releases/X11R7.6/doc/man/man1/Xvfb.1.xhtml) is a virtual framebuffer X server. It allows you to run graphical applications without a physical display. In the provided Jenkins pipeline, Xvfb is used to create a virtual display, and the tests are executed on this virtual display.

### Options Used in `usexvfb()` Function:

- `Xvfb :99 -ac -screen 0 1280x1024x24 &`: Starts Xvfb with the following options:
  - `:99`: Specifies the display number as 99.
  - `-ac`: Disables access control for X server, allowing connections from any host.
  - `-screen 0 1280x1024x24`: Defines the screen parameters, specifying a screen with resolution 1280x1024 and 24-bit color.

## FFmpeg

[FFmpeg](https://www.ffmpeg.org/) is a powerful multimedia processing tool that can be used to encode, decode, and transcode multimedia files. In the provided Jenkins pipeline, FFmpeg is used to capture the virtual display created by Xvfb and save it as a video file.

### Options Used in `usexvfb()` Function:

- `sudo apt-get update && sudo apt-get install ffmpeg -y`: Updates the package list and installs the `ffmpeg` package. This can be baked into Jenkins agent AMI.

- `ffmpeg -f x11grab -i :99 -c:v libx264 -preset ultrafast -tune zerolatency -crf 25 ./${VIDEO_FILE_NAME} &`: Uses ffmpeg to record the virtual display:
  - `-f x11grab`: Specifies the input format as x11grab.
  - `-i :99`: Specifies the input source as the Xvfb display on `:99`.
  - `-c:v libx264`: Specifies the video codec as libx264.
  - `-preset ultrafast`: Sets the encoding preset to ultrafast for fast encoding.
  - `-tune zerolatency`: Optimizes the encoding for low-latency streaming.
  - `-crf 25`: Sets the Constant Rate Factor (CRF) for video quality (lower values result in higher quality).
  - `./${VIDEO_FILE_NAME} &`: Specifies the output file for the recorded video.

## Selenium WebDriver Configurations

### WebDriver and Display Settings

- `System.setProperty("webdriver.chrome.driver", "chromedriver-linux64/chromedriver");`
  - Sets the path for the ChromeDriver executable. Pipeline is currently downloading the linux chromedriver in workspace. It's recommended to use java project level chromedriver management.

- `System.setProperty("DISPLAY", ":99");`
  - Specifies the X server display number for the virtual display (Xvfb). Optional to specify here when it is specified as environmental variables in Jenkinsfile.

### ChromeOptions Configuration

- Creates a ChromeOptions instance for additional configurations.

- `options.setBinary("/usr/bin/google-chrome-stable");`
  - Sets the binary location for the Chrome browser. Change to the chrome install path based on the environment.


# How to run tests?

`mvn clean test`

Note: Adjust configurations based on your specific testing needs and the characteristics of your web application.

## Important Notes

- Ensure that the Jenkins agent running this pipeline has sufficient resources to handle the virtual display and video recording.

- Depending on the complexity of your UI tests, you may need to adjust the screen resolution and color depth parameters in the Xvfb options.

- Verify that the specified ChromeDriver version is compatible with the version of the Chrome browser used in your UI tests.