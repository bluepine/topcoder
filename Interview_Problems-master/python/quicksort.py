#!/usr/bin/env python
'''
Quicksort

Input: list of numbers
Output: sorted list using quicksort 
'''

import random

# O(n^2) with list comprehension
def quick_sort(l):
    if len(l) <= 1:
        return []
    lesser = [x for x in l[1:] if x < l[0]]
    greater = [x for x in l[1:] if x > l[0]]
    return quick_sort(lesser) + [l[0]] + quick_sort(greater)

def find_pivot(l):
    pivot_index = random.randint(0,len(l)-1)
    return pivot_index

# O(n^2)
def quick_sort2(l, length=None):
    if len(l) <= 1:
        return l
    pivot_index = find_pivot(l)
    pivot = l[pivot_index]
    
    #Swap pivot to first item in list
    i = j = 1
    for num in l:
        if num < pivot:
            l[i],l[j]=l[j],l[i]
            i += 1
        j += 1
    l[0],l[i]=l[i],l[0]
    quick_sort2(l, i) 
    quicksort2(l, len(l)-1)
    return l # l will be changed as it goes through thus no = needed

# O(n^2)
def quick_sort3(l):
    left = right = []
    if len(l) <= 1:
        return l 
    else:
        pivot = random.randint(len(l))
        for num in l:
            if num > l[pivot]:
                left.append(l)
            else:
                right.append(l)
    return quick_sort3(left) + quick_sort3(right)


if __name__ == '__main__':
    #Test section
    l = [5,8,3,1,2,7,9,6]
    result = [1,2,3,5,6,7,8,9]

    implementations = [quick_sort, quick_sort2, quick_sort3, quick_sort4]

    for impl in implementations:
        print "trying %s" % impl
        print "%s == %s" % (impl,result)

