// UPDATE THE NEXT TWO LINES
paperQ = "Q22570477"
chembl = "http://linkedchemistry.info/chembl/molecule/m366724"

sparql = """
SELECT DISTINCT ?smiles ?inchi ?inchikey {
  <$chembl> <http://purl.org/spar/cito/citesAsDataSource> ?paper ;
    <http://www.blueobelisk.org/chemistryblogs/inchi> ?inchi ;
    <http://www.blueobelisk.org/chemistryblogs/inchikey> ?inchikey ;
    <http://www.blueobelisk.org/chemistryblogs/smiles> ?smiles .
}
"""

if (bioclipse.isOnline()) {
  results = rdf.sparqlRemote(
    "http://rdf.farmbio.uu.se/chembl/sparql/", sparql
  )
  missing = results.rowCount == 0
} else {
  missing = true
}

smiles = results.get(1,"smiles")
inchiC = results.get(1,"inchi")
inchikeyC = results.get(1,"inchikey")

mol = cdk.fromSMILES(smiles)
ui.open(mol)

inchiObj = inchi.generate(mol)
inchiShort = inchiObj.value.substring(6)
key = inchiObj.key // key = "GDGXJFJBRMKYDL-FYWRMAATSA-N"

if (!inchiC.startsWith(inchiObj.value)) {
  println "Calculated InChI does not match the ChEMBL InChI"
  println "  calculated: " + inchiObj.value
  println "  ChEMBL    : " + inchiC
} else if (inchiC != inchiObj.value) {
  println "Calculated InChI differs from the ChEMBL InChI in stereochemistry (taking what ChEMBL gave)"
  inchiShort = inchiC
  key = inchikeyC
}

sparql = """
PREFIX wdt: <http://www.wikidata.org/prop/direct/>
SELECT ?compound WHERE {
  ?compound wdt:P235 "$key" .
}
"""

if (bioclipse.isOnline()) {
  results = rdf.sparqlRemote(
    "https://query.wikidata.org/sparql", sparql
  )
  missing = results.rowCount == 0
} else {
  missing = true
}

sparql = """
SELECT DISTINCT ?label {
  <$chembl> <http://www.w3.org/2000/01/rdf-schema#label> ?label .
}
"""

if (bioclipse.isOnline()) {
  labels = rdf.sparqlRemote(
    "http://rdf.farmbio.uu.se/chembl/sparql/", sparql
  )
  missinglabels = labels.rowCount == 0
} else {
  missinglabels = true
}

labelLine = ""
chemblIDLine = ""
if (!missinglabels) {
  labelLine = ""
  for (label in labels.getColumn("label")) {
    if (label.startsWith("CHEMBL")) {
      chemblIDLine = "$item\tP592\t\"$label\""
    } else {
      labelLine += "$item\tLen\t\"$label\"\n"
    }
  }
}
print "label: " + labelLine
println "ChEMBL ID: " + chemblIDLine

formula = cdk.molecularFormula(mol)

// Create the Wikidata QuickStatement, see https://tools.wmflabs.org/wikidata-todo/quick_statements.php

item = "LAST" // set to Qxxxx if you need to append info, e.g. item = "Q22579236"

pubchemLine = ""
if (bioclipse.isOnline()) {
  pcResults = pubchem.search(key)
  if (pcResults.size == 1) {
    cid = pcResults[0]
    pubchemLine = "$item\tP662\t\"$cid\""
  }
}

if (!missing) {
  println "===================="
  println "Already in Wikidata as " + results.get(1,"compound")
  println "===================="
} else {
  statement = """
    CREATE
    
    $item\tDen\t\"chemical compound\"
    $item\tP31\tQ11173\tS248\t$paperQ
    $item\tP233\t\"$smiles\"\tS248\t$paperQ
    $item\tP274\t\"$formula\"
    $item\tP234\t\"$inchiShort\"
    $item\tP235\t\"$key\"
    $pubchemLine
    $chemblIDLine
    $labelLine
  """

  println "===================="
  println statement
  println "===================="
}

