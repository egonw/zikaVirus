# CHANGE THE NEXT TWO LINES
paperQ = "Q22713644"
smiles = "CC1=NC(=O)C2=C(N1)C=CC(=C2)CN(CC#C)C3=CC=C(C=C3)C(=O)NCC4=CC=C(C=C4)Cl"

mol = cdk.fromSMILES(smiles)
ui.open(mol)

inchiObj = inchi.generate(mol)
inchiShort = inchiObj.value.substring(6)
key = inchiObj.key // key = "GDGXJFJBRMKYDL-FYWRMAATSA-N"

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
    
    $item\tP31\tQ11173\tS248\t$paperQ
    $item\tDen\t\"chemical compound\"
    $item\tP233\t\"$smiles\"\tS248\t$paperQ
    $item\tP274\t\"$formula\"
    $item\tP234\t\"$inchiShort\"
    $item\tP235\t\"$key\"
    $pubchemLine
  """

  println "===================="
  println statement
  println "===================="
}

