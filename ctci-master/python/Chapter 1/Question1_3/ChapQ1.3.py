#Given two strings, write a method to decide if one is a permutation of the other.

# O(n^2)
def isPermutation(s1, s2):
    if len(s1)!=len(s2):
        return False
    else:
        for char in s1:
            if s2.find(char)==-1:
                return False
            else:
                s2.replace(char,"",1)
        return True

# big O complexity depends on python list sort complexity, which should be better than O(n^2)
def isPermutationSort(s1,s2):
    #sort both strings, check if they are equal
    if len(s1)!=len(s2): return False
    return sorted(s1) == sorted(s2)

#O(n)
#using a dict as a hash table to count occurences in s1, then comparing s2 with the dict
def isPermutationHash(s1,s2):  
    if len(s1) != len(s2):
        return False
    dic = {}
    for char in s1:
        dic[char] = dic.get(char, 0) + 1
    for char in s2:
        if dic.get(char,0) <= 0:
            return False
        else: dic[char] -= 1
    return True


    
#testing

#permutation
postest1 = ["abcdefgh","abcdefhg"]

#not permutation
negtest2 = ["abcdefgh","gfsdgsdffsd"]

#not permutation
negtest3 = ["abcdefgh","gfsdgsdf"]


#list of all functions to test
funclist = [isPermutation,isPermutationSort,isPermutationHash]

for func in funclist:
    print "Testing function " + str(func)    
    if func(postest1[0],postest1[1]):
        print "Test 1 passed"
    if not func(negtest2[0],negtest2[1]):
        print "Test 2 passed"
    if not func(negtest3[0],negtest3[1]):
        print "Test 3 passed"
                


