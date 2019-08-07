"C:\Program Files\Java\jdk1.8.0_172\bin\javac" *.java
IF EXIST Chatbot.jar DEL /F Chatbot.jar
"C:\Program Files\Java\jdk1.8.0_172\bin\jar" cfm App.jar manifest.txt *.class
echo off
cls
DEL /F *.class
java -jar App.jar
