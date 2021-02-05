cd %1

javac -d %1/classes %1/*.java 2>%2/error.txt

cd %1/classes
java %3 > %2\%4

cd %1
java -jar C:\batch\checkstyle-8.40-all.jar -c C:\batch\azcafe.xml -o out.txt %1/*.java