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

An example QuickStatement looks like:

    CREATE
    
    LAST	Den	"chemical compound"
    LAST	P31	Q11173	S248	Q22570477
    LAST	P233	"Cc1nc(sc1C(=O)\C=C\c2ccc(Cl)c(Cl)c2)n3nc(cc3c4ccccc4)c5ccccc5"	S248	Q22570477
    LAST	P274	"C28H19Cl2N3OS"
    LAST	P234	"InChI=1S/C28H19Cl2N3OS/c1-18-27(26(34)15-13-19-12-14-22(29)23(30)16-19)35-28(31-18)33-25(21-10-6-3-7-11-21)17-24(32-33)20-8-4-2-5-9-20/h2-17H,1H3/b15-13+"
    LAST	P235	"GDGXJFJBRMKYDL-FYWRMAATSA-N"
    LAST	P662	"5351382"
    LAST	P592	"CHEMBL476968"
    LAST	Len	"3-(3,4-dichlorophenyl)-1-(2-(3,5-diphenyl-1H-pyrazol-1-yl)-4-methylthiazol-5-yl)prop-2-en-1-one"


## License
All code in this repository is licensed with the MIT.
