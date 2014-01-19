#!/usr/bin/env python
'''
Factorial

Input: Take in a number
Output: The factorial of a number (n!). The product of all positive integers less than or equal to a number.

'''

import math

 # Recursive solution

def rec_fact(n):
    if n == 0:
        return 1
    else:
        return (n * rec_fact(n - 1))

# While loop iteration solution - O(n)

def loop_sol_1(n):
    if n == 0:
        n = 1
    num = n
    while num > 1:
        num -= 1
        n *= num
    return n

# For loop iteration solution - O(n)

def loop_sol_2(n):
    if n == 0:
        n = 1
    for num in range(1, n):
        n *= num
    return n

# For loop containing full conditional - O(n)
def loop_sol_3(x):
    for num in range(0,x+1):
        if num < 2:
            result = num
        else:
            result *= num
    return result

 
# List comprehension solution - O(n)
def functional(n):
    return reduce(lambda x, y: x * y, [1] + range(1, n + 1))

# Math library solution
std_lib = math.factorial

if __name__ == '__main__':
    # Test section
    implementations = [std_lib, rec_fact, loop_sol_1, loop_sol_2, loop_sol_3, functional]

    for impl in implementations:
        print "trying %s" % impl
        print "  f(0) == 1: %s" % (impl(0) == 1)
        print "  f(1) == 1: %s" % (impl(1) == 1)
        print "  f(2) == 2: %s" % (impl(2) == 2)
        print "  f(3) == 6: %s" % (impl(3) == 6)
