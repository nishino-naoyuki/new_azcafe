cd %1

javac -d %1/classes %1/*.java 2>%2/error.txt

cd %1/classes
java %3 > %2\result.txt