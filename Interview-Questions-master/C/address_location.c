/*

Given a variable, how can you find whether it was allocated 
from stack or from heap memory???

Note:  read about map file generated by gcc
*/

int is_stack(void* p) {
    int junk;
    return p > &junk;
}

int main(int argc, char** argv) {
    int a, b, c;
    int *d = malloc(1);
    int e;
    printf("%d %d %d %d %d\n", is_stack(&a), is_stack(&b), is_stack(&e), is_stack(d), is_stack(&e));
}
