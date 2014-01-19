#!/usr/bin/ruby
# Author: Dejan Strbac (me@dejanstrbac.com)
#   Date: 18/01/2012
#   Note: on your local, run this script as ./solver.rb < input00.txt

class RibosomeExtractor
    START_CODON = 'ATG'
    STOP_CODONS = ['TAA', 'TAG', 'TGA']

    attr_accessor :codons, :dnas


    def initialize
        self.codons = {}
        self.dnas = []                                      # just makes more sense to support more DNAs

        STDIN.read.split("\n").each do |line|
            line = line.strip
            unless line.start_with?('//')                   # ignore comments
              line_words = line.split(' ')
              if line_words.size == 3                       # observing pattern here, codons are 3 words...
                 self.codons[line_words[0]] = line_words[1]
              else
                 self.dnas << line                          # ...while DNA is alone on the line
              end
            end
        end
    end


    def extract_proteins
      unless self.codons.empty? or self.dnas.empty?
        self.dnas.each do |dna|
          tr_dna, protein = dna.clone, nil
          while tr_dna && (pos = tr_dna.index(START_CODON))    # identified beginning of a protein
            protein, tr_dna = extract_protein_at(pos, tr_dna)
            STDOUT.puts protein
          end
        end
      end
    end


    def extract_protein_at(position, trans_dna)   #
        res_protein = ''
        begin
            run_cdn = trans_dna[position..position+2]    # extract the (first) codon, will be nil when out of range
            position += 3                                # move to the next codon
            if run_cdn && STOP_CODONS.include?(run_cdn)  # is it the end?
                run_cdn = nil
            else
                res_protein += self.codons[run_cdn]
            end
        end while !run_cdn.nil?
        return res_protein, trans_dna[position..-1]
    end

end

hello_facebook = RibosomeExtractor.new
hello_facebook.extract_proteins
