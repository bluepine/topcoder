#include <stdio.h>

#define ARR_SIZE    6250
#define ZERO_CHAR   '0'

int main() {
    unsigned short A[ARR_SIZE], B[ARR_SIZE], C[ARR_SIZE+1],
    value_a = 0, value_b = 0, transfer = 0;
    
    int j = 0, k = 0, i = 0, n, q, pos, value;
    char a[100001], b[100001], cmd[32], bit;
    
    scanf("%d %d\n", &n, &q);
    
    gets(a);
    gets(b);
    
    while (n--) {
        value_a |= (a[n] - ZERO_CHAR) << k;
        value_b |= (b[n] - ZERO_CHAR) << k;
        k++;
        if (k == 16) {
            A[j] = value_a;
            B[j++] = value_b;
            value_a = value_b = k = 0;
        }
    }
    
    if (k) {
        A[j] = value_a;
        B[j] = value_b;
    }
    
    while(q--) {
        gets(cmd);
        if (cmd[4] == 'a') {
            sscanf(cmd, "set_a %d %c", &pos, &bit);
            j = pos >> 4;
            k = 1 << (pos % 16);
            A[j] = (bit - ZERO_CHAR) ? A[j] | k : A[j] & (~k);
        } else if (cmd[4] == 'b') {
            sscanf(cmd, "set_b %d %c", &pos, &bit);
            j = pos >> 4;
            k = 1 << (pos % 16);
            B[j] = (bit - ZERO_CHAR) ? B[j] | k : B[j] & (~k);
        } else {
            sscanf(cmd, "get_c %d", &pos);
            j = pos >> 4;
            k = pos % 16;
            
            transfer = 0;
            for (i = 0; i <= j; i++) {
                value = A[i] + B[i] + transfer;
                C[i] = value;
                transfer = value >> 16;
            }
            
            printf("%c", C[j] & (1 << k) ? '1' : '0');
        }
    }
    
    return 0;
}