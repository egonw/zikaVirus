# Zika Virus

Recent discussions on Twitter showed a need for drug leads and it was suggested that leads against the Dengue
virus may be tested against Zika too. A short list of papers was selected with Dengue leads and SMILES strings
were extracted. After adding a first few with createWDitems.groovy script it was noted that the compounds from
that paper were already in ChEMBL. Hence the createWDitemsFromChEMBL.groovy script.

## How to run
To run the two Groovy script you need to install Bioclipse 2.6.2 and the RDF feature. In both scripts you need
to adapt the first two lines, indicating the Q-entry of the paper and either the SMILES (createWDitems.groovy)
or the ChEMBL compound ID (createWDitemsFromChEMBL.groovy).

In the end, the script will create a short QuickStatement that you can enter at http://tools.wmflabs.org/wikidata-todo/quick_statements.php

## License
All code in this repository is licensed with the MIT.
