#CSV2Lucene
A simple utility to index CSV contents and performe fulltext search on it.
It relies on [Apache Lucene](http://lucene.apache.org/core/) so you get its powerful query language

##Usage
The utility is built into a fatjar, so you just need to have **Java 7** installed
```
java -jar csv2lucene-1.1.2.jar <path-to-csv-file>

java -jar csv2lucene-1.1.2.jar -gui
```

You can pick a sample into the examples directory:
```
java -jar target/scala-2.10/csv2lucene-1.1.2.jar examples/Brands_Share_of_Voice_201507071516_comments.csv
Preparing to Index ...
Indexing examples/Brands_Share_of_Voice_201507071516_comments.csv ...
.........Done
               ___  _
              |__ \| |
  ___ _____   __ ) | |_   _  ___ ___ _ __   ___
 / __/ __\ \ / // /| | | | |/ __/ _ \ '_ \ / _ \
| (__\__ \\ V // /_| | |_| | (_|  __/ | | |  __/
 \___|___/ \_/|____|_|\__,_|\___\___|_| |_|\___|

Explore your CSV content
    Type help to get detailed information
    Press [tab] to auto-complete fields
    Press q to quit

Default field for query terms: content

search>
```

##Features
* Press [tab] to autocomplete of CSV's fields
* Full Lucene Query support, type `help`
* Index/Tokenize all CSV fields
* AccentInsensitive (são paulo and sao paulo matches)
* Parameter `-gui` to open a visual file selector

##Build
Use [sbt](http://www.scala-sbt.org/) to build:
```
sbt assembly
```
