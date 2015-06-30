# csv_parser
CSV Parser for National Digital Library project. This uses OpenCSV, an open source library and produces XML files in format required for NDL metadata. Currently this is implemented for E-Library Spoken Tutorials only. We plan to extend this further to other CSV formats as well.
<h1>Requirements:</h1>
1. Use OpenCSV library and include it in used libraries.
http://sourceforge.net/projects/opencsv/
2. Use Apache Commons Lang 3.4 library
https://commons.apache.org/proper/commons-lang/download_lang.cgi
3. Prepare the field map similar to map.csv and provide its path in getMap() function.

