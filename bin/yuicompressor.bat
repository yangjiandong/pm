echo please manual copy yuicompressor-2.4.2.jar from maven repository to here
java -jar yuicompressor-2.4.2.jar ..\src\main\webapp\js\showcase\shape-src.js -o ..\src\main\webapp\js\showcase\shape.js 
java -jar yuicompressor-2.4.2.jar ..\src\main\webapp\css\yui-src.css -o ..\src\main\webapp\css\yui.css 
java -jar yuicompressor-2.4.2.jar ..\src\main\webapp\css\style-src.css -o ..\src\main\webapp\css\style.css 

pause