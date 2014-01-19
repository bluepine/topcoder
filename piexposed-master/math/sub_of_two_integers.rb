#!/usr/bin/ruby

=begin
Given a sorted array, output all triplets such that a-b=c.
=end

@nums = [ 1, 2, 3, 4, 5, 7, 9 ]

def sub_of_two_integers

  tuples = []

  #
  # The whole algorithm complexity in n^2. Note that a-b=c is equivalent
  # to a=b+c which is something we already solved in sum_of_two_integers.rb.
  #
  (0..@nums.size-1).each do |idx|

    target = @nums[idx]
    cnums = @nums.dup
    cnums.delete_at idx

    # We take the lowest and highest element of the list and do the sum in n:
    # 1- If the sum is higher than target then we remove the highest element
    #    and do the next iteration
    # 2- If the sum is lower than target then we remove the lowest element and
    #    and do the next iteration
    # 3- If the sums is on the target then we remove the lowest and the highest
    #    element
    
    while cnums.size > 1 do
      case cnums.first + cnums.last
      when target then
        tuples << [ cnums.first, cnums.last, target ]
        cnums.slice! 0
        cnums.slice! -1
      when 0..target-1 then cnums.slice! 0
      else cnums.slice! -1
      end
    end

  end
  tuples
end

