#Facebook Hiring Test

##Ribosome

Given a string representing DNA, return a list of strings each representing one of the proteins coded for by the DNA (Some details of DNA and protein are ignored here, you should ignore them too). DNA is represented as a string whose characters are all in the set [ACGT]. Proteins are represented as a string whose characters are all in the set [GAVLIMFWPSTCYNQDEKRH]. Three adjacent characters in DNA represent a codon. DNA is read one codon at a time and translated into protein. Reading starts with a start codon and stops at a stop codon.

There is one start codon, ATG, which also codes for the protein M (which means for the purposes of these problems that all proteins begin with M). ATG only acts as a start codon if it is not in between another start/stop codon pair. There are three stop codons, TAA, TAG, and TGA. These do not code for a character in the resulting protein, they simply terminate the protein.

For example, here is a short sequence of DNA:
    ACATGGTGCACCTGACTCTCATTTGAGATATAAAAAAACCATGAGATCGATGGCGCTACGCATAATATAAAAA

It is translated as follows:
    (junk) MVHLTLISTOP (junk) MRSMALRIISTOP(junk) ACCTCCAAC-ATG-GTG-CAC-CTG-ACT-CTC-ATT-TGA-GATATAAAAAAACC-ATG-AGA-TCG-ATG-GCG-CTA- CGC-ATA-ATA-TAA-AAAAGGCCA
So given the above protein as input your program should return:
    MVHLTLI
    MRSMALRII

