javac *.java
IF EXIST Chatbot.jar DEL /F Chatbot.jar
jar cfm App.jar manifest.txt *.class
echo off
cls
DEL /F *.class
java -jar App.jar
