pipeline {
    agent {
        label 'ui-agent'
    }
    environment {
        DISPLAY = ':99'
        VIDEO_FILE_NAME = 'test.mp4'
    }
    tools {
        maven 'mvn3.8.3'
        jdk 'jdk11'
    }
    stages {
        stage('Tests') {
            steps {
                deleteDir()
                usexvfb()
                downloadChromeDriver()
                sh 'mvn clean test'
            }
        post {
            always {
                cleanUp()
                archiveArtifacts artifacts: VIDEO_FILE_NAME, followSymlinks: false
            }
        }
        }
    }
}

def usexvfb(){
    sh """
        sudo apt-get update && sudo apt-get install Xvfb ffmpeg -y
        Xvfb :99 -ac -screen 0 1280x1024x24 &
        ffmpeg -f x11grab -i :99 -c:v libx264 -preset ultrafast -tune zerolatency -crf 25 ./${VIDEO_FILE_NAME} &
    """
}

def downloadChromeDriver(){
    sh """
        #Download the ChromeDriver package
        wget -O /tmp/chrome-linux64.zip https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/116.0.5845.96/linux64/chromedriver-linux64.zip
        unzip /tmp/chrome-linux64.zip
        chmod +x chromedriver-linux64/chromedriver
    """
}

def cleanUp(){
    sh """
       killall Xvfb
       killall ffmpeg
    """
}